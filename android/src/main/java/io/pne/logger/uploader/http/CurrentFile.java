package io.pne.logger.uploader.http;

import io.pne.logger.InternalLogger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;

public class CurrentFile {

    public static final String CURRENT_SUFFIX = "-current";
    public static final String READY_SUFFIX   = ".log";

    private static final InternalLogger LOG = new InternalLogger(CurrentFile.class);

    private final File       directory;
    private final Random     random;
    private final long       maxFileSize;
    private final FileFilter filter;
    private       File       activeFile;

    public CurrentFile(File aDir, long aMaxFileSize) {
        directory   = aDir;
        random      = new Random();
        activeFile  = createNewActiveFile();
        maxFileSize = aMaxFileSize;

        filter      = new FileFilter() {
            @Override
            public boolean accept(File aFile) {
                return aFile.getName().endsWith(CURRENT_SUFFIX);
            }
        };
    }

    public boolean isLarge() {
        return activeFile.length() > maxFileSize;
    }

    public void closeAndCreateNew() {
        if(activeFile.exists()) {
            renameFile(activeFile, directory);
        }
        renameAllCurrentFiles();
        activeFile = createNewActiveFile();
    }

    public void write(byte[] aBuffer, int aOffset, int aLength) {
        try {
            FileOutputStream out = new FileOutputStream(activeFile, true);
            //noinspection TryFinallyCanBeTryWithResources
            try {
                out.write(aBuffer, aOffset, aLength);
            } finally {
                out.close();
            }
        } catch (Exception e) {
            LOG.error("Could not write to file {}", activeFile.getAbsolutePath());
        }
    }

    private File createNewActiveFile() {
        return new File(directory, currentTimeMillis() + "-" + abs(random.nextInt(99999)) + CURRENT_SUFFIX);
    }

    private static void renameFile(File file, File aDirectory) {
        File readyFile = new File(aDirectory, file.getName() + READY_SUFFIX);
        if (!file.renameTo(readyFile)) {
            LOG.error("Could not rename file {} to {}", file.getAbsolutePath(), readyFile.getAbsolutePath());
            throw new IllegalStateException();
        }
    }

    private void renameAllCurrentFiles() {
        File[] files = directory.listFiles(filter);

        if(files == null) {
            return;
        }

        for (File file : files) {
            renameFile(file, directory);
        }
    }

}
