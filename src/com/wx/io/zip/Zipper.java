package com.wx.io.zip;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.io.Accessor;
import com.wx.util.pair.Pair;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created on 16/10/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Zipper {


    private int compressionLevel = Deflater.DEFAULT_COMPRESSION;
    private int method = ZipOutputStream.DEFLATED;
    private Consumer<File> fileConsumer;

    private OutputStream output;
    private List<File> input;

    public Zipper setOutput(OutputStream output) {
        this.output = output;

        return this;
    }

    public Zipper setOutput(File zipFile) throws FileNotFoundException {
        return setOutput(new FileOutputStream(zipFile));
    }

    public Zipper setInput(List<File> input) {
        this.input = input;

        return this;
    }

    public Zipper setInput(File... files) {
        return setInput(Arrays.asList(files));
    }

    public Zipper setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;

        return this;
    }

    public Zipper setMethod(int method) {
        this.method = method;

        return this;
    }

    public Zipper setFileConsumer(Consumer<File> fileConsumer) {
        this.fileConsumer = fileConsumer;

        return this;
    }

    public void zip() throws IOException {
        try {
            zipEncrypted(null);
        } catch (CryptoException e) {
            // Shouldn't happen
        }
    }

    public void zipEncrypted(Crypter crypter) throws IOException, CryptoException {
        Objects.requireNonNull(input, "Input not set");
        Objects.requireNonNull(output, "Output not set");
//        if (input == null || input.isEmpty()) {
//            return;
//        }

        Deque<Pair<File, URI>> queue = new ArrayDeque<>();
        for (File file : input) {
            URI base = file.getAbsoluteFile().getParentFile().toURI();
            if (!file.exists()) {
                throw new FileNotFoundException(file.getAbsolutePath()
                        + " not found");
            }
            queue.add(new Pair<>(file, base));
        }

        try (ZipOutputStream zout = initZipOutputStream(output, crypter);
             Accessor zipAccessor = new Accessor().setOut(zout)) {

            zout.setMethod(method);
            zout.setLevel(compressionLevel);

            while (!queue.isEmpty()) {
                Pair<File, URI> pair = queue.pop();
                File file = pair.get1();
                URI base = pair.get2();

                String name = base.relativize(file.toURI()).getPath();

                zout.putNextEntry(new ZipEntry(name));
                if (file.isDirectory()) {
                    for (File child : file.listFiles()) {
                        queue.add(new Pair<>(child, base));
                    }

                } else {
                    try (Accessor fileCopy = new Accessor().setIn(file)) {
                        fileCopy.pourInto(zipAccessor);
                    }
                    if (fileConsumer != null) {
                        fileConsumer.accept(file);
                    }
                    zout.closeEntry();
                }
            }

        }
    }



    private ZipOutputStream initZipOutputStream(OutputStream out, Crypter crypter) throws CryptoException, FileNotFoundException {
        if (crypter != null) {
            return new ZipOutputStream(crypter.getOutputStream(out, true));
        } else {
            return new ZipOutputStream(out);
        }
    }

}
