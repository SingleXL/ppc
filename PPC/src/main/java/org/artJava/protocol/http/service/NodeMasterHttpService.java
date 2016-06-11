package org.artJava.protocol.http.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.UriBuilder;

import org.artJava.protocol.app.NodeMaster;
import org.artJava.protocol.config.SimpleConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 6/2/15.
 */
public class NodeMasterHttpService {
	private static final NodeMasterHttpService INSTANCE = new NodeMasterHttpService();

	public static final String BASE_URI = "https://0.0.0.0/v1/";
	public static final int PORT = 8443;
	private static final String RESOURCE_PKG = "org.artJava.protocol.http.resource";
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

	public void start(SimpleConfig config) throws Exception {
		LOGGER.info("Starting http service... ");
		master = new NodeMaster(config.getBindIP(), config.getBindPort());
		master.start();
		server = GrizzlyHttpServerFactory.createHttpServer(UriBuilder.fromUri(BASE_URI).port(PORT).build(), rc, false, null);
		LOGGER.info("Http service started. ");
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