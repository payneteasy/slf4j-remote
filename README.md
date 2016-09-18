# slf4j-remote

[![Build Status](https://travis-ci.org/payneteasy/slf4j-remote.svg?branch=master)](https://travis-ci.org/payneteasy/slf4j-remote)
[![Release](https://jitpack.io/v/payneteasy/slf4j-remote.svg)](https://jitpack.io/#payneteasy/slf4j-remote)

Remote logger for android for slf4j.

It firstly saves log lines to a memory buffer, then to a file and then sends to a http server.

Features
--------
* lightweight 25 kb
* send logs via http

Maven
-----
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
...
<dependency>
    <groupId>com.github.payneteasy.slf4j-remote</groupId>
    <artifactId>android</artifactId>
    <version>1.0-1</version>
</dependency>
``` 

Gradle
------
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
...
dependencies {
    compile 'com.github.payneteasy.slf4j-remote:android:1.0-1'
}
```

How to use
----------

```java
HttpLogUploader httpUploader = new HttpLogUploader(
          "https://your-server.com/log-upload"     // url
        , "D4B321C8-2471-4B32-9F1E-5378EACA4DC0"   // token
        , Settings.Secure.getString(aContext.getContentResolver(), Settings.Secure.ANDROID_ID)
        , aContext.getDir("logs", Context.MODE_PRIVATE)
        ,  50 * 1024        // buffer size = 50  KB
        , 200 * 1024        // file size   = 200 KB
        , 100               // files count = 100
        ,  20 * 1024 * 1024 // dir size    = 20 MB
);

LogUploaderConfig.getInstance()
        .setCurrentUploader(new CompositorLogUploader(new AndroidLogUploader(), UPLOADER));
```


