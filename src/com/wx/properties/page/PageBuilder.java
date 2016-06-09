package com.wx.properties.page;

import com.wx.crypto.Crypter;
import com.wx.crypto.CryptoException;

import java.io.*;

/**
 * // TODO: 09.06.16 Documentation here
 * Created on 15/01/2016
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class PageBuilder {

    private String encoding = "UTF-8";
    private boolean useXml;
    private ResourcePage defaults;

    private InputLink input;
    private OutputLink output;

    private Crypter crypter;

    public PageBuilder encrypted(Crypter crypter) {
        this.crypter = crypter;

        return this;
    }

    public PageBuilder useEncoding(String encoding) {
        this.encoding = encoding;

        return this;
    }

    public PageBuilder useXmlFormat() {
        this.useXml = true;

        return this;
    }

    public PageBuilder setXmlFormat(boolean useXml) {
        this.useXml = useXml;

        return this;
    }

    public PageBuilder defaultValues(ResourcePage defaults) {
        this.defaults = defaults;

        return this;
    }

    public PageBuilder fromFile(File file) {
        input = () -> new FileInputStream(file);
        output = () -> new FileOutputStream(file);

        return this;
    }

    public PageBuilder fromResource(String... packagePath) throws FileNotFoundException {
        StringBuilder propPathBuilder = new StringBuilder();
        for (String s : packagePath) {
            propPathBuilder.append("/").append(s);
        }
        final String propPath = propPathBuilder.toString();

        if (ResourcePage.class.getResource(propPath) == null) {
            throw new FileNotFoundException("Resource not found: " + propPath);
        }

        input = () -> ResourcePage.class.getResourceAsStream(propPath);
        output = null;

        return this;
    }

    public PageBuilder fromLinks(InputLink input, OutputLink output) {
        this.input = input;
        this.output = output;

        return this;
    }

    public ResourcePage get() {
        InputLink in = input;
        OutputLink out = output;

        if (crypter != null) {
            if (input != null) {
                in = () -> {
                    try {
                        return crypter.getInputStream(input.getInputStream(), false);
                    } catch (CryptoException e) {
                        throw new IOException(e);
                    }
                };
            }

            if (output != null) {
                out = () -> {
                    try {
                        return crypter.getOutputStream(output.getOutputStream(), true);
                    } catch (CryptoException e) {
                        throw new IOException(e);
                    }
                };
            }
        }
        return new ResourcePage(in, out, useXml, encoding, defaults);
    }

    public ResourcePage load() throws IOException {
        ResourcePage page = get();
        page.load();

        return page;
    }

    public ResourcePage create() throws IOException {
        ResourcePage page = get();
        page.forceSave();

        return page;
    }

    public ResourcePage loadOrCreate() throws IOException {
        try {
            return load();
        } catch (FileNotFoundException e) {
            return create();
        }
    }

}
