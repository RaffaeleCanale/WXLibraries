package com.wx.io.file.search;

import com.wx.io.file.FileUtil;
import com.wx.util.Format;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created on 18/03/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class FileSearcher {


    //<editor-fold desc="Helper methods">
    public static FileFilter fileExtensionIn(String... extensions) {
        return f -> {
            String foundExtension = FileUtil.fileExtension(f.getName());
            for (String extension : extensions) {
                if (extension.equals(foundExtension)) {
                    return true;
                }
            }

            return false;
        };
    }

    public static FileFilter nameIs(String exactMatch, boolean ignoreCase) {
        return exactMatch(File::getName, exactMatch, ignoreCase);
    }

    public static FileFilter nameContains(String subString, boolean ignoreCase) {
        return substringMatch(File::getName, subString, ignoreCase);
    }

    public static FileFilter nameMatches(String regex, boolean ignoreCase) {
        return regexMatch(File::getName, regex, ignoreCase);
    }

    public static FileFilter sizeIsBiggerThan(long size) {
        return f -> f.length() > size;
    }

    public static FileFilter sizeIsSmallerThan(long size) {
        return f -> f.length() < size;
    }

    public static FileFilter substringMatch(Function<File, String> fileAttr, String substring, boolean ignoreCase) {
        return stringFilter(fileAttr, substring, ignoreCase, String::contains);
    }

    public static FileFilter regexMatch(Function<File, String> fileAttr, String match, boolean ignoreCase) {
        return stringFilter(fileAttr, match, ignoreCase, String::matches);
    }


    public static FileFilter exactMatch(Function<File, String> fileAttr, String match, boolean ignoreCase) {
        return stringFilter(fileAttr, match, ignoreCase, String::equals);
    }

    public static FileFilter and(FileFilter... filters) {
        if (filters.length == 1) {
            return filters[0];
        }
        return f -> {
            for (FileFilter filter : filters) {
                if (!filter.accept(f)) {
                    return false;
                }
            }
            return true;
        };
    }

    public static FileFilter or(FileFilter... filters) {
        if (filters.length == 1) {
            return filters[0];
        }
        return f -> {
            for (FileFilter filter : filters) {
                if (filter.accept(f)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static FileFilter not(FileFilter filter) {
        return f -> !filter.accept(f);
    }

    private static FileFilter stringFilter(Function<File, String> fileAttr, String match,
                                           boolean ignoreCase,
                                           BiFunction<String, String, Boolean> comparator) {
        String search = ignoreCase ? match.toLowerCase() : match;
        return f -> {
            String value = ignoreCase ? fileAttr.apply(f).toLowerCase() : fileAttr.apply(f);
            return comparator.apply(value, search);
        };
    }
    //</editor-fold>

    private FileFilter preFilter;
    private FileFilter dirFilter;
    private int depthLimit = Integer.MAX_VALUE;
    private int resultLimit = Integer.MAX_VALUE;
    private final File directory;

    public FileSearcher(File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory");
        }
        this.directory = directory;
    }

    public File getDirectory() {
        return directory;
    }

    public FileSearcher setBrowseFilter(FileFilter browseFilter) {
        Objects.requireNonNull(browseFilter);
        this.dirFilter = or(not(File::isDirectory), browseFilter);

        return this;
    }

    public FileSearcher preFilter(FileFilter preFilter) {
        this.preFilter = preFilter;

        return this;
    }

    //    public FileSearcher setGeneralFilter(FileFilter fileFilter) {
//        this.generalFilter = fileFilter;
//
//        return this;
//    }

    public FileSearcher setDepthLimit(int depthLimit) {
        this.depthLimit = depthLimit;

        return this;
    }

    public FileSearcher setResultLimit(int resultLimit) {
        this.resultLimit = resultLimit;

        return this;
    }

    public List<File> searchFile(FileFilter filter) {
        return search(joinFilters(File::isFile, filter));
    }

    public List<File> searchDirectory(FileFilter filter) {
        return search(joinFilters(File::isDirectory, filter));
    }

    public List<File> search(FileFilter filter) {
        List<File> result = new LinkedList<>();
        search(filter, result::add);

        return result;
    }

    public FileSearchResult extendedSearchByName(String name, boolean ignoreCase) {
        return extendedSearch(nameIs(name, ignoreCase), nameContains(name, ignoreCase));
    }

    public FileSearchResult extendedSearch(FileFilter exactMatch, FileFilter approxMatch) {
        return extendedSearch(exactMatch, approxMatch, true);
    }

    public FileSearchResult extendedSearch(FileFilter exactMatch, FileFilter approxMatch, boolean ignoreApproxMatchCount) {
        final FileSearchResult.Builder result = new FileSearchResult.Builder();

        FileFilter filter = f -> {
            if (exactMatch.accept(f)) {
                result.setExactMatch(f);
                return true;

            } else if (approxMatch.accept(f)) {
                result.addPotentialResult(f);
                if (!ignoreApproxMatchCount) {
                    return true;
                }
            }
            return false;
        };

        search(filter, null);

        return result.build();
    }

    public int search(FileFilter filter, FileConsumer resultConsumer) {
        filter = joinFilters(preFilter, filter);
        if (filter == null) {
            filter = f -> true;
        }
        return forEach(directory, 0, filter, resultConsumer);
    }

    private FileFilter joinFilters(FileFilter f1, FileFilter f2) {
        if (f1 == null) {
            return f2;
        } else if (f2 == null) {
            return f1;
        } else {
            return and(f1, f2);
        }
    }

    private int forEach(File dir, int depth, FileFilter fileFilter, FileConsumer resultConsumer) {
        if (depth >= depthLimit) {
            return 0;
        }

        int resultCount = 0;
        final File[] files = dir.listFiles(dirFilter);

        for (File file : files) {
            if (fileFilter.accept(file)) {
                resultCount++;
                if (resultConsumer != null) {
                    resultConsumer.found(file);
                }

                if (resultCount >= resultLimit) {
                    return resultCount;
                }
            }

            if (file.isDirectory()) {
                resultCount += forEach(file, depth + 1, fileFilter, resultConsumer);
                if (resultCount >= resultLimit) {
                    return resultCount;
                }
            }
        }

        return resultCount;
    }


}
