package org.artJava.protocol.config;

import java.io.File;

public class SimpleConfig implements Config {
    private final String masterIP;
    private final int masterPort;
    private final File dataDir;
    private final File confDir;
    private final int bindPort;
    private final String bindIP;

    SimpleConfig(String masterIP, int masterPort, File dataDir, File confDir, int bindPort, String bindIP) {
        this.masterIP = masterIP;
        this.masterPort = masterPort;
        this.dataDir = dataDir;
        this.confDir = confDir;
        this.bindPort = bindPort;
        this.bindIP = bindIP;
    }

    @Override
    public String getMasterIP() {
        return masterIP;
    }

    @Override
    public int getMasterPort() {
        return masterPort;
    }

    @Override
    public File getDataDir() {
        return dataDir;
    }

    @Override
    public File getConfDir() {
        return confDir;
    }

    @Override
    public int getBindPort() {
        return bindPort;
    }

    @Override
    public String getBindIP() {
        return bindIP;
    }
}
