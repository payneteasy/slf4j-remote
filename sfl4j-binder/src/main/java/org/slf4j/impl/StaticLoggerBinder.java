package org.slf4j.impl;

import io.pne.logger.slf4j.RemoteLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

@SuppressWarnings("unused")
public class StaticLoggerBinder implements LoggerFactoryBinder {

    private static final StaticLoggerBinder INSTANCE = new StaticLoggerBinder();

    public static StaticLoggerBinder getSingleton() {
        return INSTANCE;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return new RemoteLoggerFactory();
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return RemoteLoggerFactory.class.getName();
    }
}
