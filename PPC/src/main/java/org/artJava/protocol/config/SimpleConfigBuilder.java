package org.artJava.protocol.config;

import java.io.File;

public class SimpleConfigBuilder {
    private String masterIP;
    private int masterPort;
    private File dataDir;
    private File confDir;
    private int bindPort;
    private String bindIP;

    public SimpleConfigBuilder setMasterIP(String masterIP) {
        this.masterIP = masterIP;
        return this;
    }

    public SimpleConfigBuilder setMasterPort(int masterPort) {
        this.masterPort = masterPort;
        return this;
    }

    public SimpleConfigBuilder setDataDir(File dataDir) {
        this.dataDir = dataDir;
        return this;
    }

    public SimpleConfigBuilder setConfDir(File confDir) {
        this.confDir = confDir;
        return this;
    }

    public SimpleConfigBuilder setBindPort(int bindPort) {
        this.bindPort = bindPort;
        return this;
    }

    public SimpleConfigBuilder setBindIP(String bindIP) {
        this.bindIP = bindIP;
        return this;
    }

    public SimpleConfig createSimpleConfig() {
        return new SimpleConfig(masterIP, masterPort, dataDir, confDir, bindPort, bindIP);
    }
    
}