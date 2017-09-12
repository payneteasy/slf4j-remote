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

h3. initialise the uploader
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
        .setCurrentUploader(new CompositorLogUploader(new AndroidLogUploader(), httpUploader));
```

h3. Log a message
```java
        LogUploaderConfig.getInstance()
                .getCurrentUploader()
                .upload(priority, name, message);
```

h3. How for format a message with slf4j-api
```java
private void formatAndLog(Level priority, String format, Object... argArray) {
        //noinspection ConstantConditions
        FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
        _log(priority, ft.getMessage(), ft.getThrowable());
}

   private void _log(Level priority, String message, Throwable throwable) {
        if (throwable != null) {
            message += '\n' + getStackTraceString(throwable);
        }

        LogUploaderConfig.getInstance()
                .getCurrentUploader()
                .upload(priority, name, message);
    }
    
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    private void log(Level priority, String message, Throwable throwable) {
        //noinspection ConstantConditions
        _log(priority, message, throwable);
    }
    
```
