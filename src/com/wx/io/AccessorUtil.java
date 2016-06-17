/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.io;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;
import com.wx.io.zip.Unzipper;
import com.wx.io.zip.Zipper;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author Raffaele
 */
public class AccessorUtil {




	/**
	 * Reads a {@code String} in the given {@code Accessor} that has been encoded in UTF-8 and with its length. The
	 * encoding is as following:<br><br> {@code <length><string>}<br><br> Where {@code <length>} is the length of {@code
	 * <string>}. Empty or {@code null} lines can also be read.<br><br> Note that carriage returns do not effect the
	 * encoding.
	 *
	 * @param accessor        {@code Accessor} to read
	 * @param bytesCountLimit Maximum accepted length to read
	 *
	 * @return The read {@code String}
	 *
	 * @throws IOException
	 */
	public static String readWithLength(DataInput accessor,
										int bytesCountLimit) throws IOException {
		assert accessor != null;

		int length = accessor.readInt();

		if (length == -1) {
			return null;

		} else if (length == 0) {
			return "";

		} else if (length > 0 && length <= bytesCountLimit) {
			byte[] bytes = new byte[length];
			accessor.readFully(bytes);
			return new String(bytes, "UTF-8");

		}

		throw new IOException("Out of bounds string length: " + length);
	}

	/**
	 * Sends a {@code String} in the given {@code Accessor} with its length. The encoding is as following:<br><br>
	 * {@code <length><string>}<br><br> Where {@code <length>} is the length of {@code <string>}. Empty {@code null}
	 * lines can also be written.<br><br> Note that carriage returns do not effect the encoding.
	 *
	 * @param accessor {@code Accessor} to write
	 * @param line     {@code String} to send
	 *
	 * @throws IOException
	 */
	public static void sendWithLength(DataOutput accessor, String line)
			throws IOException {
		Objects.requireNonNull(accessor);

		if (line == null) {
			accessor.writeInt(-1);

		} else if (line.isEmpty()) {
			accessor.writeInt(0);

		} else {
			byte[] bytes = line.getBytes(StandardCharsets.UTF_8);

			accessor.writeInt(bytes.length);
			accessor.write(bytes, 0, bytes.length);
		}
	}

	/**
	 * Reads a list of {@code String} that has been encoded with its length in the given {@code Accessor}.<br> Empty and
	 * {@code null} list can also be read.
	 *
	 * @param accessor          {@code Accessor} to read
	 * @param listSizeLimit     Maximum accepted number of elements in the list to read
	 * @param stringLengthLimit Maximum accepted length of {@code String} to read
	 *
	 * @return The read list
	 *
	 * @throws IOException
	 */
	public static List<String> readListWithLength(Accessor accessor,
												  int listSizeLimit, int stringLengthLimit) throws IOException {
		List<String> result = new LinkedList<>();

		int size = accessor.readInt();

		if (size == -1) {
			return null;

		} else if (size >= 0 && size <= listSizeLimit) {
			for (int i = 0; i < size; i++) {
				result.add(readWithLength(accessor, stringLengthLimit));
			}
			return result;

		}
		throw new IOException("Out of bounds list size: " + size);
	}

	/**
	 * Sends a list of {@code String} with the number of elements of the list and the size of each {@code String}.<br>
	 * Empty and {@code null} lists can also be sent.
	 *
	 * @param accessor Accessor to write
	 * @param list     List to send
	 *
	 * @throws IOException
	 * @see #sendWithLength(DataOutput, String)
	 */
	public static void sendWithLength(Accessor accessor, List<String> list)
			throws IOException {
		if (list == null) {
			accessor.writeInt(-1);
		} else if (list.isEmpty()) {
			accessor.writeInt(0);
		} else {
			accessor.writeInt(list.size());
			for (String s : list) {
				sendWithLength(accessor, s);
			}
		}
	}

	/**
	 * Simply zips the given files into one archive.
	 *
	 * @param zipFile Archive to create
	 * @param files   Files to zip
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 *
	 * @deprecated Use '{@code new Zypper(files).zipTo(zipFile)}' instead, see {@link Zipper}
	 */
	public static void zip(File zipFile, File... files)
			throws IOException {
		try {
            zip(zipFile, Deflater.DEFAULT_COMPRESSION, ZipOutputStream.DEFLATED, null, null, files);
		} catch (CryptoException ex) {
		}
	}

    /**
     *
     * @param zipFile
     * @param fileConsumer
     * @param files
     * @throws IOException
     *
     * @Deprecated Use {@link Zipper} instead
     */
	public static void zip(File zipFile, Consumer<File> fileConsumer, File... files)
			throws IOException {
		try {
            zip(zipFile, Deflater.DEFAULT_COMPRESSION, ZipOutputStream.DEFLATED, null, fileConsumer, files);
        } catch (CryptoException ex) {
		}
	}

    /**
     *
     * @param zipfile
     * @param crypter
     * @param files
     * @throws IOException
     * @throws CryptoException
     *
     * @Deprecated Use {@link Zipper} instead
     */
	public static void zip(File zipfile, Crypter crypter, File... files)
			throws IOException, CryptoException {
		zip(zipfile, Deflater.DEFAULT_COMPRESSION, ZipOutputStream.DEFLATED,
				crypter, files);
	}

    /**
     *
     * @param zipfile
     * @param compressionLevel
     * @param method
     * @param crypter
     * @param files
     * @throws IOException
     * @throws CryptoException
     *
     * @Deprecated Use {@link Zipper} instead
     */
	public static void zip(File zipfile, int compressionLevel, int method,
						   Crypter crypter, File... files) throws IOException, CryptoException {
		zip(zipfile, compressionLevel, method, crypter, null, files);
	}

    /**
     *
     * @param zipfile
     * @param compressionLevel
     * @param method
     * @param crypter
     * @param fileConsumer
     * @param files
     * @throws IOException
     * @throws CryptoException
     *
     * @Deprecated Use {@link Zipper} instead
     */
	public static void zip(File zipfile, int compressionLevel, int method,
						   Crypter crypter, Consumer<File> fileConsumer, File... files) throws IOException, CryptoException {

        new Zipper().setInput(files).setOutput(zipfile)
                .setCompressionLevel(compressionLevel)
                .setMethod(method)
                .setFileConsumer(fileConsumer)
                .zipEncrypted(crypter);
    }

	@Deprecated
	private static void zipDir(File directory, File zipfile) throws IOException {
		URI base = directory.toURI();
		Deque<File> queue = new LinkedList<>();
		queue.push(directory);

		try (OutputStream out = new FileOutputStream(zipfile);
			 ZipOutputStream zout = new ZipOutputStream(out);
			 Accessor zipAccessor = new Accessor().setOut(zout)) {

			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File child : directory.listFiles()) {
					String name = base.relativize(child.toURI()).getPath();

					if (child.isDirectory()) {
						queue.push(child);
						name = name.endsWith(File.separator) ? name : name + File.separator;
						zout.putNextEntry(new ZipEntry(name));
					} else {
						zout.putNextEntry(new ZipEntry(name));
						try (Accessor fileCopy = new Accessor().setIn(child)) {
							fileCopy.pourInto(zipAccessor);
						}
						zout.closeEntry();
					}
				}
			}
		}
	}

	public static void unzip(File zipfile, File toDirectory) throws FileNotFoundException, IOException {
		try {
			unzip(zipfile, toDirectory, null, null);
		} catch (CryptoException ex) {
			throw new IOException(ex);
		}
	}

	public static void unzip(File zipfile, File toDirectory, Consumer<File> fileConsumer) throws FileNotFoundException, IOException {
		try {
			unzip(zipfile, toDirectory, null, fileConsumer);
		} catch (CryptoException ex) {
			throw new IOException(ex);
		}
	}

	public static void unzip(File zipfile, File toDirectory, Crypter crypter)
			throws IOException, CryptoException {
		unzip(zipfile, toDirectory, crypter, null);
	}

	public static void unzip(File zipfile, File toDirectory, Crypter crypter, Consumer<File> fileConsumer)
			throws IOException, CryptoException {

        new Unzipper().setInput(zipfile).setOutput(toDirectory)
                .setCrypter(crypter)
                .setFileConsumer(fileConsumer)
                .unzip();
    }



	@Deprecated
	private static void unzipDir(File zipfile, File toDirectory) throws IOException {
		ZipFile zfile = new ZipFile(zipfile);

		Enumeration<? extends ZipEntry> entries = zfile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File file = new File(toDirectory, entry.getName());
			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				file.getParentFile().mkdirs();

				try (Accessor accessor = new Accessor()
						.setIn(zfile.getInputStream(entry))
						.setOut(file, false)) {

					accessor.pourInOut();
				}
			}
		}
	}

	public static boolean createParent(String path) {
		return createParent(new File(path));
	}

	public static boolean createParent(File file) {

		File parent = file.getParentFile();
		if (parent == null) {

			return false;
		}
		return parent.exists() || parent.mkdirs();
	}


}
