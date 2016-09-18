package io.pne.logger.uploader.http;

import io.pne.logger.InternalLogger;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class DirectoryCleaner {

    private static final InternalLogger LOG = new InternalLogger(DirectoryCleaner.class);

    private final File       dir;
    private final int        filesCount;
    private final long       directorySize;
    private final String     suffix;
    private final FileFilter fileFilter;

    public DirectoryCleaner(String aSuffix, int aFilesCount, long aDirectorySize, File aDir) {
        filesCount    = aFilesCount;
        directorySize = aDirectorySize;
        dir           = aDir;
        suffix = aSuffix;

        fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(suffix);
            }
        };
    }

    public void cleanOldFiles() {
        checkFilesCount(dir);
        checkDirectorySize(dir);
    }

    public File[] getTargetFiles() {
        return getSortedFiles(dir);
    }

    private void checkDirectorySize(File dir) {
        File[] sortedFiles = getSortedFiles(dir);

        long sum = 0;
        for (int i = sortedFiles.length - 1; i >= 0; --i) {
            File file = sortedFiles[i];
            sum += file.length();

            if(sum > directorySize) {
                deleteFile(file);
            }
        }
    }

    private void checkFilesCount(File dir) {
        File[] sortedFiles = getSortedFiles(dir);

        int deletesCount = sortedFiles.length - filesCount;
        for(int i=0; i< deletesCount && i < sortedFiles.length ; i++) {
            File file = sortedFiles[i];
            deleteFile(file);
        }
    }

    private void deleteFile(File file) {
        LOG.info("Deleting old file {}", file.getName());
        if (!file.delete()) {
            LOG.error("Could not delete file {}", file.getAbsolutePath());
        }
    }

    private File[] getSortedFiles(File dir) {
        File[] files = dir.listFiles(fileFilter);

        if (files == null || files.length == 0) {
            // no files
            return new File[]{};
        }

        Arrays.sort(files);
        return files;
    }

}
