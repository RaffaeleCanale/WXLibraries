package com.wx.servercomm.http;

import com.wx.util.log.LogHelper;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created on 17/11/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class SafeCachedHttpRequest extends CachedHttpRequest {

    private static final Logger LOG = LogHelper.getLogger(SafeCachedHttpRequest.class);

    public SafeCachedHttpRequest(AbstractHttpRequest httpGet, File cacheDirectory, long validityPeriod) {
        super(httpGet, cacheDirectory, validityPeriod, new Properties());

        try {
            loadCacheMap();
        } catch (IOException e) {
            logException("Failed to load cache", e);
            clearCache();
        }
    }

    @Override
    ServerResponse<String> getCacheEntry(String url) {
        try {
            return super.getCacheEntry(url);
        } catch (ClassCastException e) {
            logException("Failed to read cache entry", e);
            removeCacheEntry(url, null);
            return null;
        }
    }

    @Override
    void saveCacheEntry(String url, ServerResponse<String> entry, byte[] data) {
        try {
            super.saveCacheEntry(url, entry, data);
        } catch (IOException | ClassCastException e) {
            logException("Failed to save cache", e);
            removeCacheEntry(url, entry);
        }
    }

    @Override
    byte[] readCacheEntry(String url, ServerResponse<String> entry, File cacheFile) throws IOException {
        try {
            return super.readCacheEntry(url, entry, cacheFile);
        } catch (IOException e) {
            logException("Failed to read from cache", e);
            removeCacheEntry(url, entry);
            return forceExecute(url, null);
        }
    }

    private void logException(String message, Exception e) {
        LOG.warning(message + ": [" + e.getClass().getSimpleName() + "] " + e.getMessage());
    }
}
