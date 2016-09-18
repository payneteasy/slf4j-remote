package io.pne.logger.uploader.http;

import org.junit.Test;
import org.slf4j.event.Level;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class HttpLogUploaderTest {

    @Test
    public void integration() throws InterruptedException, IOException {

        TestHttpServer httpServer = new TestHttpServer();
        httpServer.start();

        File dir = new File("./target/remote-logging");
        dir.mkdirs();

        HttpLogUploader uploader = new HttpLogUploader(
                "http://localhost:9095/log/upload"
                , "123456"
                , "device-1"
                , dir
                , 512 * 1024 // 512kb
                , 1 * 1024 * 1024 // 1mb
                , 50
                , 10 * 1024 * 1024 // 10mb
        );
        for(int s = 0 ; s< 100; s++) {
            uploader.dumpCurrentSession();
            for(int i=0; i< 100; i++) {
                uploader.upload(Level.DEBUG, "sdk.HttpLogUploaderTest", "Hello Message " + UUID.randomUUID());
            }
            uploader.dumpCurrentSession();
        }

        httpServer.stop();

        Thread.sleep(5_000);
    }


    @Test
    public void cleanDir() {
        File dir = new File("./target/remote-logging");

        DirectoryCleaner cleaner = new DirectoryCleaner(".log", 100, 10 * 1024 * 1024, dir  );
        cleaner.cleanOldFiles();
    }
}
