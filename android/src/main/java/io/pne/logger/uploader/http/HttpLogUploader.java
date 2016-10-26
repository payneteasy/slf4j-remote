package io.pne.logger.uploader.http;

import io.pne.logger.InternalLogger;
import io.pne.logger.uploader.ILogUploader;
import org.slf4j.event.Level;

import java.io.File;
import java.net.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import static io.pne.logger.uploader.http.CurrentFile.READY_SUFFIX;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class HttpLogUploader implements ILogUploader {

    private static final InternalLogger LOG = new InternalLogger(HttpLogUploader.class);

    private final ExecutorService     executor;
    private final MemoryBuffer        buffer;
    private final HttpUploaderService uploaderService;
    private final MessageFormatter    messageFormatter = new MessageFormatter();
    private final CurrentFile         currentFile;

    public HttpLogUploader(String aUploadUrl, String aAccessToken, String aDeviceId, File aDir
            , int aMemoryBufferSize, long aMaxFileSize, int aFilesCount, long aDirectorySize
    ) {
        this(aUploadUrl, aAccessToken, aDeviceId, aDir, aMemoryBufferSize, aMaxFileSize, aFilesCount, aDirectorySize, null);
    }

    public HttpLogUploader(String aUploadUrl, String aAccessToken, String aDeviceId, File aDir
            , int aMemoryBufferSize, long aMaxFileSize, int aFilesCount, long aDirectorySize
            , Proxy aProxy
    ) {

        if(!aDir.exists() || !aDir.isDirectory()) {
            LOG.error("Directory {} does not exist", aDir.getAbsolutePath());
        }

        currentFile      = new CurrentFile(aDir, aMaxFileSize);
        uploaderService  = new HttpUploaderService(aUploadUrl, aAccessToken, aDeviceId, new DirectoryCleaner(READY_SUFFIX, aFilesCount, aDirectorySize, aDir), aProxy);
        buffer           = new MemoryBuffer(aMemoryBufferSize);

        executor         = newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "http-log-uploader");
            }
        });
    }

    @Override
    public void upload(Level aLevel, String aTag, String aMessage) {
        final byte[] message = messageFormatter.createMessage(aLevel, aTag, aMessage);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(buffer.hasSpace(message.length)) {
                    buffer.append(message);
                } else {
                    buffer.clearToFile(currentFile);
                    buffer.append(message);

                    if(currentFile.isLarge()) {
                        currentFile.closeAndCreateNew();
                        uploaderService.uploadFiles();
                    }
                }
            }
        });
    }

    /**
     * Save file to a disk and send to a server
     */
    public void dumpCurrentSession() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                buffer.clearToFile(currentFile);
                currentFile.closeAndCreateNew();
                uploaderService.uploadFiles();
            }
        });
    }

}
