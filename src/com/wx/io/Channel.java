package com.wx.io;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;

/**
 * Created on 23/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class Channel implements AutoCloseable {


    public static String getPublicIpAddress() throws IOException {
        // TODO: 2/26/16 Place this somewhere better?
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) e.nextElement();

            Enumeration e2 = ni.getInetAddresses();

            while (e2.hasMoreElements()) {
                InetAddress ip = (InetAddress) e2.nextElement();
                if (!ip.isLinkLocalAddress() && !ip.isLoopbackAddress()) {
                    return ip.getHostAddress();
                }
            }
        }

        throw new IOException("Public IP address not found"); //*/
    }

    private final DataInputStream in;
    private final DataOutputStream out;

    public Channel(Socket socket) throws IOException {
        this(socket.getInputStream(), socket.getOutputStream());
    }

    public Channel(InputStream in, OutputStream out) {
        this.in = new DataInputStream(in);
        this.out = new DataOutputStream(out);
    }

    public DataOutputStream out() {
        return out;
    }

    public DataInputStream in() {
        return in;
    }

    public Channel secureChannel(Crypter crypter) throws CryptoException {
        return new Channel(crypter.getInputStream(in, false), crypter.getOutputStream(out, true));
    }

    public byte[] readSizedBytes(int sizeThreshold) throws IOException {
        int size = in.readInt();
        if (size > sizeThreshold) {
            throw new IOException("Received size too big: " + size);
        }
        if (size < 0) {
            return null;
        }


        byte[] key = new byte[size];
        in.readFully(key);

        return key;
    }


    public <E extends Enum<E>> E readEnum(Class<E> eClass) throws IOException {
        E[] values = eClass.getEnumConstants();
        int code = read();

        if (code < 0 || code >= values.length) {
            throw new IOException("Invalid response code received: " + code);
        }

        return values[code];
    }

    public String readSizedString(int sizeThreshold) throws IOException {
        byte[] bytes = readSizedBytes(sizeThreshold);
        if (bytes == null) {
            return null;
        }

        return new String(bytes, "UTF-8");
    }

    //<editor-fold desc="Delegate in">
    public int read(byte[] b) throws IOException {
        return in.read(b);
    }

    public int readUnsignedByte() throws IOException {
        return in.readUnsignedByte();
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public double readDouble() throws IOException {
        return in.readDouble();
    }

    public void readFully(byte[] b) throws IOException {
        in.readFully(b);
    }

    public byte readByte() throws IOException {
        return in.readByte();
    }

    public int readUnsignedShort() throws IOException {
        return in.readUnsignedShort();
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        in.readFully(b, off, len);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    public String readUTF() throws IOException {
        return in.readUTF();
    }

    public char readChar() throws IOException {
        return in.readChar();
    }

    public static String readUTF(DataInput in) throws IOException {
        return DataInputStream.readUTF(in);
    }

    public long skip(long n) throws IOException {
        return in.skip(n);
    }

    public float readFloat() throws IOException {
        return in.readFloat();
    }

    public int read() throws IOException {
        return in.read();
    }

    public void reset() throws IOException {
        in.reset();
    }

    public boolean readBoolean() throws IOException {
        return in.readBoolean();
    }

    public int skipBytes(int n) throws IOException {
        return in.skipBytes(n);
    }

    public short readShort() throws IOException {
        return in.readShort();
    }

    public long readLong() throws IOException {
        return in.readLong();
    }

    public boolean markSupported() {
        return in.markSupported();
    }
    //</editor-fold>


    public void writeSizedBytes(byte[] key) throws IOException {
        if (key == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(key.length);
            out.write(key);
        }
    }

    public <E extends Enum<E>> void writeEnum(E code) throws IOException {
        write(code.ordinal());
    }

    public void writeSizedString(String value) throws IOException {
        byte[] bytes = value == null ? null : value.getBytes("UTF-8");
        writeSizedBytes(bytes);
    }

    //<editor-fold desc="Delegate out">
    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    public void write(byte[] b) throws IOException {
        out.write(b);
    }

    public void writeDouble(double v) throws IOException {
        out.writeDouble(v);
    }

    public void writeChars(String s) throws IOException {
        out.writeChars(s);
    }

    public void writeChar(int v) throws IOException {
        out.writeChar(v);
    }

    public void writeShort(int v) throws IOException {
        out.writeShort(v);
    }

    public int size() {
        return out.size();
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void writeByte(int v) throws IOException {
        out.writeByte(v);
    }

    public void writeInt(int v) throws IOException {
        out.writeInt(v);
    }

    public void writeFloat(float v) throws IOException {
        out.writeFloat(v);
    }

    public void writeBoolean(boolean v) throws IOException {
        out.writeBoolean(v);
    }

    public void writeBytes(String s) throws IOException {
        out.writeBytes(s);
    }

    public void writeLong(long v) throws IOException {
        out.writeLong(v);
    }

    public void writeUTF(String str) throws IOException {
        out.writeUTF(str);
    }
    //</editor-fold>

    public void closeInput() throws IOException {
        in.close();
    }

    public void closeOutput() throws IOException {
        out.close();
    }

    public void close() throws IOException {
        closeInput();
        closeOutput();
    }
}
