package com.wx.servercomm.http;

import com.wx.io.Accessor;
import com.wx.io.file.FileUtil;
import com.wx.util.log.LogHelper;
import com.wx.util.representables.string.StringRepr;
import javafx.scene.control.ProgressBar;

import java.io.*;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Wrapper of an {@link AbstractHttpRequest} that will automatically cache requests on disk.
 * <p>
 * This class needs access to a directory where it will store the cached results and configuration files.
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *          <p>
 *          Project: Worx File: CachedHttpGet.java (UTF-8) Date: May 12, 2013
 */
public class CachedHttpRequest extends AbstractHttpRequest {

    private static final Logger LOG = LogHelper.getLogger(CachedHttpRequest.class);

    private static final String CACHE_MAP_FILE = "cache_map";

    private final AbstractHttpRequest httpGet;
    private final File cacheDirectory;
    private final long validityPeriod;

    private final Properties cacheMap;
    private int responseCode;

    /**
     * Initializes a new disk cached {@link AbstractHttpRequest}.
     * <p>
     * <p>
     * The cached pages are saved within multiple files in a directory. It is advised to provide this class with its own
     * reserved directory.
     *
     * @param httpGet        Underlying requester
     * @param cacheDirectory Directory where cached results and configurations file will be stored
     * @param validityPeriod Period (in milliseconds) during which a buffered page is considered valid
     *
     * @throws IOException
     */
    public CachedHttpRequest(AbstractHttpRequest httpGet, File cacheDirectory, long validityPeriod) throws IOException {
        this(httpGet, cacheDirectory, validityPeriod, new Properties());
        loadCacheMap();
    }

    CachedHttpRequest(AbstractHttpRequest httpGet, File cacheDirectory, long validityPeriod, Properties cacheMap) {
        this.httpGet = httpGet;
        this.cacheDirectory = cacheDirectory;
        this.validityPeriod = validityPeriod;
        this.cacheMap = cacheMap;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public byte[] execute(String url) throws IOException {
        ServerResponse<String> entry = getCacheEntry(url);

        if (entry != null && entry.isValid(validityPeriod)) {
            File cacheFile = new File(cacheDirectory, entry.getData());

            if (cacheFile.isFile()) {

                LOG.finest("\t\tCACHE HIT " + url);
                return readCacheEntry(url, entry, cacheFile);
            }
        }

        return forceExecute(url, entry);
    }

    public void clearCache() {
        cacheMap.clear();

        File[] files = cacheDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }

    @Override
    public void interruptExecute() {
        httpGet.interruptExecute();
    }

    /**
     * Executes the request by ignoring (and updating) the buffer.
     *
     * @param url Url to execute
     *
     * @return Server's response
     *
     * @see #execute(String)
     */
    public byte[] forceExecute(String url) throws IOException {
        return forceExecute(url, getCacheEntry(url));
    }

    final byte[] forceExecute(String url, ServerResponse<String> entry) throws IOException {
        byte[] data = httpGet.execute(url);
        responseCode = httpGet.getResponseCode();

        String fileName;
        if (entry == null) {
            fileName = UUID.randomUUID().toString();
        } else {
            fileName = entry.getData();
        }

        entry = new ServerResponse<>(fileName, httpGet.getResponseCode());
        saveCacheEntry(url, entry, data);


        return data;
    }

    ServerResponse<String> getCacheEntry(String url) {
        String entry = (String) cacheMap.get(url);
        return entry == null ? null : ServerResponse.fromString(entry, new StringRepr());
    }

    void saveCacheEntry(String url, ServerResponse<String> entry, byte[] data) throws IOException {
        cacheMap.put(url, ServerResponse.toString(entry, new StringRepr()));
        saveCacheMap();

        File cacheFile = new File(cacheDirectory, entry.getData());
        writeFile(cacheFile, data);
    }

    byte[] readCacheEntry(String url, ServerResponse<String> entry, File cacheFile) throws IOException {
        responseCode = entry.getResponseCode();
        return readFile(cacheFile);
    }

    void removeCacheEntry(String url, ServerResponse<String> entry) {
        cacheMap.remove(url);
        if (entry != null) {
            File cacheFile = new File(cacheDirectory, entry.getData());
            if (cacheFile.isFile()) {
                cacheFile.delete();
            }
        }
    }

    byte[] readFile(File file) throws IOException {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Accessor accessor = new Accessor().setIn(file).setOut(bytes)) {
            accessor.pourInOut();

            return bytes.toByteArray();
        }
    }

    private void writeFile(File file, byte[] data) throws IOException {
        try (Accessor accessor = new Accessor().setOut(file, false)) {
            accessor.write(data);
        }
    }



    final void loadCacheMap() throws IOException {
        File cacheFile = new File(cacheDirectory, CACHE_MAP_FILE);

        if (cacheFile.exists()) {
            cacheMap.load(new FileInputStream(cacheFile));
        }
    }

    private void saveCacheMap() throws IOException {
        FileUtil.autoCreateDirectory(cacheDirectory);

        File cacheFile = new File(cacheDirectory, CACHE_MAP_FILE);
        cacheMap.store(new FileOutputStream(cacheFile), null);
    }


}
