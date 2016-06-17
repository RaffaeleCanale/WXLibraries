package com.wx.io.zip;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.io.Accessor;
import com.wx.io.file.FileUtil;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created on 16/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Unzipper {

    private Crypter crypter;
    private Consumer<File> fileConsumer;

    private InputStream input;
    private File output;

    public Unzipper setInput(InputStream input) {
        this.input = input;

        return this;
    }

    public Unzipper setInput(File zipFile) throws FileNotFoundException {
        return setInput(new BufferedInputStream(new FileInputStream(zipFile)));
    }

    public Unzipper setOutput(File toDirectory) {
        this.output = toDirectory;

        return this;
    }

    public Unzipper setCrypter(Crypter crypter) {
        this.crypter = crypter;

        return this;
    }

    public Unzipper setFileConsumer(Consumer<File> fileConsumer) {
        this.fileConsumer = fileConsumer;

        return this;
    }

    public void unzip()
            throws IOException, CryptoException {

        try (ZipInputStream zis = initZipInputStream(input);
             Accessor zipAccessor = new Accessor().setIn(zis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File file = new File(output, entry.getName());
                if (entry.isDirectory()) {
                    FileUtil.autoCreateDirectory(file);
                } else {
//                    file.getParentFile().mkdirs();
                    FileUtil.autoCreateDirectory(file.getParentFile());

                    if (fileConsumer != null) {
                        fileConsumer.accept(file);
                    }
                    try (Accessor accessor = new Accessor().setOut(file, false)) {
                        zipAccessor.pourInto(accessor);
                    }
                }
                zis.closeEntry();
            }
        }
    }

    public InputStream zipEntryStream(String fileInZip) throws IOException, CryptoException {
        ZipInputStream zis = initZipInputStream(input);

        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (entry.getName().equals(fileInZip)) {
                return zis;
            }
        }

        return null;
    }

    public List<String> getZipEntryNames() throws CryptoException, IOException {
        List<String> result = new LinkedList<>();

        try (ZipInputStream zis = initZipInputStream(input)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                result.add(entry.getName());
                zis.closeEntry();
            }
        }

        return result;
    }

    public boolean unzipSingleElement(String elementName) throws CryptoException, IOException {
        try (ZipInputStream zis = initZipInputStream(input);
             Accessor zipAccessor = new Accessor().setIn(zis)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals(elementName)) {
                    File file = new File(output, entry.getName());
                    if (entry.isDirectory()) {
                        file.mkdirs();
                    } else {
                        file.getParentFile().mkdirs();

                        try (Accessor accessor = new Accessor().setOut(file, false)) {
                            zipAccessor.pourInto(accessor);
                        }
                    }
                    zis.closeEntry();
                    return true;
                }

            }
        }

        return false;
    }

    private ZipInputStream initZipInputStream(InputStream in) throws FileNotFoundException, CryptoException {
        if (crypter != null) {
            return new ZipInputStream(crypter.getInputStream(in, false));
        } else {
            return new ZipInputStream(in);
        }
    }
}
