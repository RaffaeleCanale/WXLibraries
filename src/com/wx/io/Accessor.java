package com.wx.io;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * This class allows to easily perform common IO actions. An accessor object wraps an {@code InputStream} and/or an
 * {@code OutputStream} and provides many utility functions to read/write different types of data.
 * <p>
 * <b>Note:</b> For input/output containing text, prefer the usage of a {@link TextAccessor} as it handles better text
 * encodings issues.
 *
 * @author Raffaele Canale
 * @version 2.1 // First review the 28.12.12
 */
public class Accessor implements DataOutput, DataInput, Closeable {

    public static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * Pours <b>all</b> the content of the input stream into the output stream.
     *
     * @param in          Input stream to read and empty
     * @param out         Output stream to fill
     * @param buffer_size Buffer size for reading/writing
     *
     * @throws IOException
     */
    private static void pour(InputStream in, OutputStream out, int buffer_size)
            throws IOException {
        byte[] buffer = new byte[buffer_size];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        out.flush();
    }

    /**
     * Pours the content of the input stream into the output stream until the input stream is empty <b>or</b> the given
     * limit has been reached.
     *
     * @param in          Input stream to read
     * @param out         Output stream to fill
     * @param buffer_size Buffer size for reading/writing
     * @param limit       Maximum number of bytes to copy
     *
     * @throws IOException
     */
    private static void pour(InputStream in, OutputStream out, int buffer_size,
                             long limit) throws IOException {
        byte[] buffer = new byte[buffer_size];
        int bytesReadCount;
        long bytesToRead = limit;
        int step = (int) Math.min(buffer_size, bytesToRead);
        while (bytesToRead > 0
                && (bytesReadCount = in.read(buffer, 0, step)) != -1) {
            out.write(buffer, 0, bytesReadCount);

            bytesToRead -= bytesReadCount;
            step = (int) Math.min(buffer_size, bytesToRead);
        }
        out.flush();
    }

    private DataOutputStream out;
    private DataInputStream in;

    /**
     * Initialize a new accessor. Use the methods {@link #setIn(InputStream)} and/or {@link #setOut(OutputStream)} to
     * initialize this accessor's streams.
     */
    public Accessor() {
    }

    /**
     * Sets this accessor {@code InputStream}. If an {@code InputStream} was already assigned to this accessor, an
     * {@code IllegalArgumentException} is thrown. To reassign the {@code InputStream}, use {@link #close()} first.
     * <p>
     * <b>Note:</b> The {@code InputStream} will be wrapped in a {@link BufferedInputStream} for better performance.
     *
     * @param source Source InputStream
     *
     * @return this (for chained calls)
     */
    public Accessor setIn(InputStream source) {
        if (in != null) {
            throw new IllegalArgumentException("Already has an input");
        }
        in = new DataInputStream(new BufferedInputStream(Objects.requireNonNull(source)));
        return this;
    }

    /**
     * Opens an {@code InputStream} on the given file.
     *
     * @param file File to open
     *
     * @return this (for chained calls)
     *
     * @throws FileNotFoundException
     */
    public Accessor setIn(File file) throws FileNotFoundException {
        return setIn(new FileInputStream(file));
    }

    /**
     * Opens a {@code Crypter} input stream.
     *
     * @param in          Source input stream
     * @param crypter     {@code Crypter} used for encryption/decryption
     * @param encryptMode {@code true} to set the {@code Crypter} in encryption mode
     *
     * @return this (for chained calls)
     *
     * @throws CryptoException
     * @see Crypter
     */
    public Accessor setInCrypter(InputStream in, Crypter crypter,
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
     * @return this (for chained calls)
     *
     * @throws FileNotFoundException
     * @throws CryptoException
     * @see Crypter
     */
    public Accessor setInCrypter(File file, Crypter crypter,
                                 boolean encryptMode) throws FileNotFoundException, CryptoException {
        return setInCrypter(new FileInputStream(file), crypter, encryptMode);
    }

    /**
     * Sets this accessor {@code OutputStream}. If an {@code OutputStream} was already assigned to this accessor, an
     * {@code IllegalArgumentException} is thrown. To reassign the {@code OutputStream}, use {@link #close()} first.
     * <p>
     * <b>Note:</b> The {@code OutputStream} will be wrapped in a {@link BufferedOutputStream} for better performance.
     *
     * @param os OutputStream to set
     *
     * @return this (for chained calls)
     */
    public Accessor setOut(OutputStream os) {
        if (out != null) {
            throw new IllegalArgumentException("Already has an output");
        }
        out = new DataOutputStream(new BufferedOutputStream(Objects.requireNonNull(os)));
        return this;
    }


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
    public Accessor setOut(File file, boolean append)
            throws FileNotFoundException {
        return setOut(new FileOutputStream(file, append));
    }

    /**
     * Sets this Accessor OutputStream to the given file
     *
     * @param file   File
     *
     * @return this
     *
     * @throws FileNotFoundException
     */
    public Accessor setOut(File file)
            throws FileNotFoundException {
        return setOut(new FileOutputStream(file));
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
    public Accessor setOutCrypter(File file, boolean append, Crypter crypter,
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
    public Accessor setOutCrypter(OutputStream os, Crypter crypter,
                                  boolean encryptMode) throws CryptoException {
        return setOut(crypter.getOutputStream(os, encryptMode));
    }

    /**
     * Pours all the content of this Accessor input stream into this accessor output stream.
     *
     * @throws IOException
     */
    public void pourInOut() throws IOException {
        pourInOut(DEFAULT_BUFFER_SIZE);
    }

    /**
     * Pours all the content of this Accessor input stream into this Accessor output stream.
     *
     * @param buffer_size Size of the read/write buffer
     *
     * @throws IOException
     */
    public void pourInOut(int buffer_size) throws IOException {
        checkInput();
        checkOutput();

        pour(in, out, buffer_size);
    }

    /**
     * Pours all the content of this Accessor input stream into the output stream of the given Accessor.
     *
     * @param destination Accessor where to pour
     *
     * @throws IOException
     */
    public void pourInto(Accessor destination) throws IOException {
        checkInput();
        destination.checkOutput();

        pour(in, destination.out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Pours all the content of this Accessor input stream into the output stream of the given Accessor.
     *
     * @param destination Accessor where to pour
     * @param buffer_size Size of the read/write buffer
     *
     * @throws IOException
     */
    public void pourInto(Accessor destination, int buffer_size) throws IOException {
        checkInput();
        destination.checkOutput();

        pour(in, destination.out, buffer_size);
    }

    /**
     * Pours all the content of this Accessor input stream into the output stream of the given Accessor.
     *
     * @param limit       Maximum number of bytes to copy.
     * @param destination Accessor where to pour
     *
     * @throws IOException
     */
    public void pourInto(long limit, Accessor destination) throws IOException {
        checkInput();
        destination.checkOutput();

        pour(in, destination.out, DEFAULT_BUFFER_SIZE, limit);
    }

    /**
     * Reads all the {@link Serializable} objects contained in the file and adds them in the given collection.
     *
     * @param data Collection where will results be added
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readSerializable(Collection<Serializable> data) throws IOException,
            ClassNotFoundException {
        checkInput();
        if (data == null) {
            throw new NullPointerException();
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            Object o;
            while ((o = ois.readObject()) != null) {
                data.add((Serializable) o);
            }
        } catch (EOFException eof) {
        }
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
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

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
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

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        int count = 0;
        while (count <= limit && (line = reader.readLine()) != null) {
            data.add(caster.apply(line));
            count++;
        }
    }

    /**
     * Writes the given collection to the output stream using an {@link ObjectOutputStream}.
     *
     * @param data Collection of {@link Serializable} to write
     *
     * @throws IOException
     */
    public void writeSerializable(Collection<Serializable> data) throws IOException {
        checkOutput();
        if (data == null) {
            throw new NullPointerException();
        }
        ObjectOutputStream oos = new ObjectOutputStream(out);

        for (Object o : data) {
            oos.writeObject(o);
        }

        oos.flush();
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
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        for (T value : data) {
            writer.write(caster.apply(value));
            writer.newLine();
        }
        writer.flush();
    }

    //<editor-fold defaultstate="collapsed" desc="in delegate">    
    @Override
    public final int skipBytes(int n) throws IOException {
        return in.skipBytes(n);
    }

    @Override
    public final int readUnsignedShort() throws IOException {
        return in.readUnsignedShort();
    }

    @Override
    public final int readUnsignedByte() throws IOException {
        return in.readUnsignedByte();
    }

    @Override
    public final String readUTF() throws IOException {
        return in.readUTF();
    }

    @Override
    public final short readShort() throws IOException {
        return in.readShort();
    }

    @Override
    public final long readLong() throws IOException {
        return in.readLong();
    }

    @Override
    public final int readInt() throws IOException {
        return in.readInt();
    }

    @Override
    public final void readFully(byte[] b, int off, int len) throws IOException {
        in.readFully(b, off, len);
    }

    @Override
    public final void readFully(byte[] b) throws IOException {
        in.readFully(b);
    }

    @Override
    public final float readFloat() throws IOException {
        return in.readFloat();
    }

    @Override
    public final double readDouble() throws IOException {
        return in.readDouble();
    }

    @Override
    public final char readChar() throws IOException {
        return in.readChar();
    }

    public final void readChars(char[] buffer) throws IOException {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = in.readChar();
        }
    }

    @Override
    public final byte readByte() throws IOException {
        return in.readByte();
    }

    @Override
    public final boolean readBoolean() throws IOException {
        return in.readBoolean();
    }

    public final int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public final int read(byte[] b) throws IOException {
        return in.read(b);
    }

    @Override
    @Deprecated
    public String readLine() throws IOException {
        StringBuilder result = new StringBuilder();


        try {
            boolean eolFound = false;
            while (!eolFound) {
                byte nextChar = readByte();
                if (nextChar == 10) { // \n code
                    eolFound = true;
                } else {
                    result.append((char) nextChar);
                }
            }

        } catch (EOFException ex) {
            if (result.length() == 0) {
                return null;
            }
        }

        return result.toString();
//        return new BufferedReader(new InputStreamReader(in, "UTF-8")).readLine();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="out delegate">
    @Override
    public final void writeUTF(String str) throws IOException {
        out.writeUTF(str);
    }

    @Override
    public final void writeShort(int v) throws IOException {
        out.writeShort(v);
    }

    @Override
    public final void writeLong(long v) throws IOException {
        out.writeLong(v);
    }

    @Override
    public final void writeInt(int v) throws IOException {
        out.writeInt(v);
    }

    @Override
    public final void writeFloat(float v) throws IOException {
        out.writeFloat(v);
    }

    @Override
    public final void writeDouble(double v) throws IOException {
        out.writeDouble(v);
    }

    @Override
    public final void writeChars(String s) throws IOException {
        out.writeChars(s);
    }

    @Override
    public final void writeChar(int v) throws IOException {
        out.writeChar(v);
    }

    @Override
    public final void writeBytes(String s) throws IOException {
        out.writeBytes(s);
    }

    @Override
    public final void writeByte(int v) throws IOException {
        out.writeByte(v);
    }

    @Override
    public final void writeBoolean(boolean v) throws IOException {
        out.writeBoolean(v);
    }

    @Override
    public void write(byte[] b) throws IOException {
        out.write(b, 0, b.length);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    @Override
    public synchronized void write(int b) throws IOException {
        out.write(b);
    }

    public final int size() {
        return out.size();
    }

    public void flush() throws IOException {
        out.flush();
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
        if (in != null) {
            in.close();
            in = null;
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
        if (out != null) {
            out.close();
            out = null;
        }
    }

    private void checkInput() throws NullPointerException {
        Objects.requireNonNull(in, "in stream has not been initialized");
    }

    private void checkOutput() throws NullPointerException {
        Objects.requireNonNull(out, "out stream has not been initialized");
    }
}
