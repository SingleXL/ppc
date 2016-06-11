package org.artJava.protocol.http.service;

import com.cloud.dc.config.MasterHttpServiceConfig;
import com.cloud.dc.master.NodeMaster;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 6/2/15.
 */
public class NodeMasterHttpService {
    private static final NodeMasterHttpService INSTANCE = new NodeMasterHttpService();

    public static final String BASE_URI = "https://0.0.0.0/v1/";
    public static final int PORT = 443;
    private static final String RESOURCE_PKG = "com.cloud.dc.http.resource";
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeMasterHttpService.class);

    private HttpServer server;
    private ResourceConfig rc;
    private NodeMaster master;

    private NodeMasterHttpService() {
        server = null;
        master = null;
        rc = new ResourceConfig().packages(RESOURCE_PKG);
    }

    public static NodeMasterHttpService getInstance() {
        return INSTANCE;
    }

    public void start(MasterHttpServiceConfig config) throws IOException {
        LOGGER.info("Starting http service... ");
        master = new NodeMaster(config);
        master.start();
        server = GrizzlyHttpServerFactory.createHttpServer(
                UriBuilder.fromUri(BASE_URI).port(PORT).build(),
                rc,
                true,
                makeSSL());
        LOGGER.info("Http service started. ");
    }

    private SSLEngineConfigurator makeSSL() throws IOException {
        SSLContextConfigurator sslConf = new SSLContextConfigurator();
        sslConf.setKeyStoreBytes(read("ssl/mlpdcs_ks.jks"));
        sslConf.setKeyStorePass("password");
        sslConf.setTrustStoreBytes(read("ssl/mlpdcs_ts.jks"));
        sslConf.setTrustStorePass("password");
        return new SSLEngineConfigurator(sslConf, false, false, false);
    }

    private byte[] read(String resource) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream in = NodeMasterHttpService.class.getClassLoader().getResourceAsStream(resource)) {
            int ch;
            while ((ch = in.read()) != -1) {
                out.write(ch);
            }
            out.flush();
            return out.toByteArray();
        }
    }

    public NodeMaster getMaster() {
        if (master != null) {
            return master;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void stop() {
        LOGGER.info("Stopping http service... ");
        if (server != null) {
            server.shutdown();
        }
        if (master != null) {
            master.stop();
            master = null;
        }
        LOGGER.info("Http service stopped. ");
    }
}