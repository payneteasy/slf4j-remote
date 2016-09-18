package io.pne.logger.uploader.http;

import io.pne.logger.InternalLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class HttpUploaderService {

    private static final InternalLogger LOG = new InternalLogger(HttpUploaderService.class);

    private final Executor         executor;
    private final DirectoryCleaner directoryCleaner;
    private final String           uploadUrl;
    private final String           token;
    private final String           deviceId;

    public HttpUploaderService(String aUploadUrl, String aToken, String aDeviceId, DirectoryCleaner aDirectoryCleaner) {
        directoryCleaner = aDirectoryCleaner;
        uploadUrl        = aUploadUrl + "/" + aDeviceId;
        token            = aToken;
        deviceId         = aDeviceId;

        executor         = newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "http-log-uploader-service");
            }
        });
    }

    public void uploadFiles() {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                directoryCleaner.cleanOldFiles();

                try {
                    File[] files = directoryCleaner.getTargetFiles();
                    LOG.info("Uploading {} files to {}", files.length, uploadUrl);

                    for (File file : files) {
                        uploadFile(file);
                    }

                } catch (Exception e) {
                    LOG.error("Could not upload files to " + uploadUrl, e);
                }

            }
        });
    }

    private void uploadFile(File aFile) throws IOException {
        LOG.info("Uploading file {} to {}", aFile.getName(), uploadUrl);

        HttpURLConnection con = (HttpURLConnection) new URL(uploadUrl).openConnection();
        con.setReadTimeout    (30 * 1000);
        con.setConnectTimeout (30 * 1000);

        con.setRequestMethod  ("POST");

        con.setRequestProperty("Content-Length"     , "" + aFile.length());
        con.setRequestProperty("Content-Type"       , "application/octet-stream");
        con.setRequestProperty("Content-Disposition", "attachment; filename=\""+aFile.getName()+"\"");
        con.setRequestProperty("Authorization"      , "Bearer " + token);
        con.setRequestProperty("Device-id"          , deviceId);

        con.setDoOutput(true);

        copyStream(aFile, con.getOutputStream());

        int responseCode = con.getResponseCode();
        if( responseCode == 200) {
            if(!aFile.delete()) {
                LOG.error("Could not delete file {}", aFile.getAbsolutePath());
            }
        } else {
            LOG.error("Could not upload file {} to {}, code={}, message={}", aFile.getName(), uploadUrl, responseCode, con.getResponseMessage());
        }
    }

    private void copyStream(File aFile, OutputStream aOut) throws IOException {
        try {
            FileInputStream in = new FileInputStream(aFile);
            byte[] buf = new byte[4096];
            int size;
            while ( ( size = in.read(buf)) >= 0) {
                aOut.write(buf, 0, size);
            }
        } finally {
            aOut.close();
        }
    }
}
