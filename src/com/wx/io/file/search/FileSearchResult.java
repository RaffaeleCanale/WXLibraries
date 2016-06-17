package com.wx.io.file.search;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created on 16/01/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class FileSearchResult {

    private final Set<File> exactMatch;
    private final Set<File> potentialResults;

    FileSearchResult(Set<File> exactMatch, Set<File> potentialResults) {
        this.exactMatch = exactMatch == null ? Collections.EMPTY_SET : exactMatch;
        this.potentialResults = potentialResults == null ? Collections.EMPTY_SET : potentialResults;
    }


    public boolean hasResult() {
        return !exactMatch.isEmpty() || !potentialResults.isEmpty();
    }

    public boolean hasExactMatch() {
        return !exactMatch.isEmpty();
    }

    public Set<File> getExactMatch() {
        return exactMatch;
    }

    public Set<File> getPotentialResults() {
        return potentialResults;
    }

    @Override
    public String toString() {
        return "Exact match: " + exactMatch + "\nPotential results: " + potentialResults;
    }

    public static class Builder {
        private Set<File> exactMatch = new HashSet<>();
        private Set<File> potentialResults = new HashSet<>();

        public void setExactMatch(File result) {
            exactMatch.add(result);
        }

        public void addPotentialResult(File result) {
            potentialResults.add(result);
        }

        public FileSearchResult build() {
            return new FileSearchResult(exactMatch, potentialResults);
        }
    }
}
