package com.wx.io;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utility method that regroup all the common stream operations into one class. <br> An Accessor can contain up to 2
 * streams (1 in, 1 out).
 *
 * @author Raffaele Canale
 * @version 1.1 // Reviewed the 28.12.12
 */
public class TextAccessor implements Closeable {

    public static final int DEFAULT_BUFFER_SIZE = 1024;
    private BufferedWriter mOut;
    private BufferedReader mIn;

    private static void pour(BufferedReader in, BufferedWriter out)
            throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            out.write(line);
            out.newLine();
        }
        out.flush();
    }

    private static void pour(BufferedReader in, BufferedWriter out,
                             long limit) throws IOException {
        int count = 0;
        String line;
        while ((line = in.readLine()) != null && count < limit) {
            out.write(line);
            out.newLine();
            count++;
        }
        out.flush();
    }

    /**
     * Sets this Accessor InputStream
     *
     * @param source Source InputStream
     *
     * @return this
     */
    public TextAccessor setIn(InputStream source) {
        if (source == null) {
            throw new NullPointerException();
        }
        mIn = new BufferedReader(new InputStreamReader(source, Charset.forName("UTF-8")));
        return this;
    }

    //<editor-fold defaultstate="collapsed" desc="setIn overload">

    /**
     * Opens an InputStream on the given file.
     *
     * @param file File to open
     *
     * @return this
     *
     * @throws FileNotFoundException
     */
    public TextAccessor setIn(File file) throws FileNotFoundException {
        return setIn(new FileInputStream(file));
    }

    /**
     * Opens a {@code Crypter} input stream.
     *
     * @param in          Source input stream
     * @param crypter     {@code Crypter} used for encryption/decryption
     * @param encryptMode {@code true} to set the {@code Crypter} in encryption mode
     *
     * @return this
     *
     * @throws CryptoException
     */
    public TextAccessor setInCrypter(InputStream in, Crypter crypter,
                                 boolean encryptMode) throws CryptoException {
        return setIn(crypter.getInputStream(in, encryptMode));
    }

    /**
     * Opens a {@code Crypter} input stream on the given file.
     *
     * @param file        File to open
     * @param crypter     {@code Crypter} used for encryption/decryption
     * @param encryptMode {@code true} to set the {@code Crypter} in encryption mode
     *
     * @return this
     *
     * @throws FileNotFoundException
     * @throws CryptoException
     */
    public TextAccessor setInCrypter(File file, Crypter crypter,
                                 boolean encryptMode) throws FileNotFoundException, CryptoException {
        return setInCrypter(new FileInputStream(file), crypter, encryptMode);
    }

    /**
     * Sets this Accessor OutputStream
     *
     * @param os OutputStream to set
     *
     * @return this
     */
    public TextAccessor setOut(OutputStream os) {
        if (os == null) {
            throw new NullPointerException();
        }
        mOut = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
        return this;
    }

    //<editor-fold defaultstate="collapsed" desc="setOut overload">    

    /**
     * Sets this Accessor OutputStream to the given file
     *
     * @param file   File
     * @param append <code>true</code> to append at the end of the file
     *
     * @return this
     *
     * @throws FileNotFoundException
     */
    public TextAccessor setOut(File file, boolean append)
            throws FileNotFoundException {
        return setOut(new FileOutputStream(file, append));
    }

    /**
     * Sets a CipherOutputStream to the given file
     *
     * @param file        File
     * @param append      <code>true</code> to append at the end of the file
     * @param crypter     {@code Crypter} to use for encryption/decryption
     * @param encryptMode {@code true} to set the {@code Crypter} in encryption mode
     *
     * @return this
     *
     * @throws FileNotFoundException
     * @throws CryptoException
     */
    public TextAccessor setOutCrypter(File file, boolean append, Crypter crypter,
                                  boolean encryptMode) throws FileNotFoundException, CryptoException {
        return setOutCrypter(new FileOutputStream(file, append), crypter,
                encryptMode);
    }

    /**
     * Sets an CipherOutputStream to this Accessor OutputStream
     *
     * @param os          Target outputStream
     * @param crypter     {@code Crypter} used for encryption/decryption
     * @param encryptMode {@code true} to set the {@code Crypter} in encryption mode
     *
     * @return this
     *
     * @throws CryptoException
     */
    public TextAccessor setOutCrypter(OutputStream os, Crypter crypter,
                                  boolean encryptMode) throws CryptoException {
        return setOut(crypter.getOutputStream(os, encryptMode));
    }

    /**
     * Pours all the content of this Accessor input stream into this Accessor output stream.
     *
     * @throws IOException
     */
    public void pourInOut() throws IOException {
        checkInput();
        checkOutput();

        pour(mIn, mOut);
    }

    /**
     * Pours all the content of this Accessor input stream into the output stream of the given Accessor.
     *
     * @param destination Accessor where to pour
     *
     * @throws IOException
     */
    public void pourInto(TextAccessor destination) throws IOException {
        checkInput();
        destination.checkOutput();

        pour(mIn, destination.mOut);
    }

    /**
     * Pours all the content of this Accessor input stream into the output stream of the given Accessor.
     *
     * @param limit       Maximum number of bytes to copy.
     * @param destination Accessor where to pour
     *
     * @throws IOException
     */
    public void pourInto(long limit, TextAccessor destination) throws IOException {
        checkInput();
        destination.checkOutput();

        pour(mIn, destination.mOut, limit);
    }


    /**
     * Reads all the input stream as a text.
     *
     * @return A {@code String} containing all the text in the input stream
     *
     * @throws IOException
     */
    public String readText() throws IOException {
        checkInput();

        String result = "";
        boolean firstLine = true;
        String line;
        while ((line = mIn.readLine()) != null) {
            if (firstLine) {
                result = line;
                firstLine = false;
            } else {
                result += "\n" + line;
            }
        }
        return result;
    }

    /**
     * Reads all {@code String} lines contained in the input stream.
     *
     * @param lines Collection where the read lines will be added
     *
     * @throws IOException
     */
    public void read(Collection<String> lines) throws IOException {
        read(lines, Function.identity());
    }

    /**
     * Reads {@code String} lines contained in the input stream until the end or the limit number of lines is reached.
     *
     * @param lines Collection where the read lines will be added
     * @param limit Maximum number of lines to read
     *
     * @throws IOException
     */
    public void read(Collection<String> lines, int limit)
            throws IOException {
        read(lines, Function.identity(), limit);
    }

    /**
     * Reads all the input and casts into any type where each element is encoded in one line.
     *
     * @param <T>    Type of the data to read
     * @param data   Collection where read results will be added
     * @param caster Function used to decode lines
     *
     * @throws IOException
     */
    public <T> void read(Collection<T> data, Function<String, T> caster)
            throws IOException {
        checkInput();
        if (data == null || caster == null) {
            throw new NullPointerException();
        }

        String line;
        while ((line = mIn.readLine()) != null) {
            data.add(caster.apply(line));
        }
    }

    /**
     * Reads all until the end or the maximum number of lines is reached and casts into any type where each element is
     * encoded in one line.
     *
     * @param <T>    Type of the data to read
     * @param data   Collection where read results will be added
     * @param caster Function used to decode lines
     * @param limit  Maximum number of lines (elements) to read
     *
     * @throws IOException
     */
    public <T> void read(Collection<T> data, Function<String, T> caster,
                         int limit) throws IOException {
        checkInput();
        if (data == null || caster == null) {
            throw new NullPointerException();
        }

        String line;
        int count = 0;
        while (count <= limit && (line = mIn.readLine()) != null) {
            data.add(caster.apply(line));
            count++;
        }
    }


    /**
     * Write the given lines into the output stream, all separated by {@code \n}.
     *
     * @param lines Lines to write
     *
     * @throws IOException
     */
    public void write(String... lines) throws IOException {
        write(Arrays.asList(lines), Function.identity());
    }

    /**
     * Write the given lines into the output stream, all separated by {@code \n}.
     *
     * @param lines Lines to write
     *
     * @throws IOException
     */
    public void write(Collection<String> lines) throws IOException {
        write(lines, Function.identity());
    }

    /**
     * Write a list of any type that is into the output stream.
     *
     * @param <T>    Type of the elements to write
     * @param data   Collection containing the elements to write
     * @param caster Function used to cast T into a string (preferably a single line)
     *
     * @throws IOException
     */
    public <T> void write(Collection<T> data, Function<T, String> caster)
            throws IOException {
        checkOutput();
        if (data == null || caster == null) {
            throw new NullPointerException();
        }
        for (T value : data) {
            mOut.write(caster.apply(value));
            mOut.newLine();
        }
        mOut.flush();
    }

    //<editor-fold defaultstate="collapsed" desc="in delegate">

    public void reset() throws IOException {
        mIn.reset();
    }

    public Stream<String> lines() {
        return mIn.lines();
    }

    public boolean markSupported() {
        return mIn.markSupported();
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        return mIn.read(cbuf, off, len);
    }

    public boolean ready() throws IOException {
        return mIn.ready();
    }

    public void mark(int readAheadLimit) throws IOException {
        mIn.mark(readAheadLimit);
    }

    public long skip(long n) throws IOException {
        return mIn.skip(n);
    }


    public String readLine() throws IOException {
        return mIn.readLine();
    }


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="out delegate">

    public void write(char[] cbuf, int off, int len) throws IOException {
        mOut.write(cbuf, off, len);
    }

    public void write(String s, int off, int len) throws IOException {
        mOut.write(s, off, len);
    }

    public void newLine() throws IOException {
        mOut.newLine();
    }

    public void flush() throws IOException {
        mOut.flush();
    }

    //</editor-fold>

    /**
     * Closes the input and output stream if any.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        closeOutput();
        closeInput();
    }

    /**
     * Closes the input stream if any.
     *
     * @throws IOException
     * @see InputStream#close()
     */
    public void closeInput() throws IOException {
        if (mIn != null) {
            mIn.close();
        }
    }

    /**
     * Closes the output stream if any.
     * <p>
     *
     * @throws IOException
     * @see OutputStream#close()
     */
    public void closeOutput() throws IOException {
        if (mOut != null) {
            mOut.close();
        }
    }

    private void checkInput() throws NullPointerException {
        if (mIn == null) {
            throw new NullPointerException("in stream has not been "
                    + "initialized");
        }
    }

    private void checkOutput() throws NullPointerException {
        if (mOut == null) {
            throw new NullPointerException("out stream has not been "
                    + "initialized");
        }
    }
}
