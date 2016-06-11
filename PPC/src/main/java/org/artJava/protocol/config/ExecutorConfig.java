package org.artJava.protocol.config;

import java.io.File;

public interface ExecutorConfig {

    String getMasterIP();

    int getMasterPort();

    File getDataDir();

    File getConfDir();
}
