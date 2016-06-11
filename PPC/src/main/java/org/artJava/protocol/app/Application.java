package org.artJava.protocol.app;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.artJava.protocol.config.SimpleConfig;
import org.artJava.protocol.config.SimpleConfigBuilder;
import org.artJava.protocol.http.service.NodeMasterHttpService;

public class Application {

	private static SimpleConfig readConfig(CommandLine commandLine) throws IOException {
        String bindIP = commandLine.getOptionValue("bind-ip", "0.0.0.0");
        int bindPort = Integer.parseInt(commandLine.getOptionValue("bind-port", "51888"));
        String masterIP = commandLine.getOptionValue("master-ip", "localhost");
        int masterPort = Integer.parseInt(commandLine.getOptionValue("master-port", "51888"));
        return new SimpleConfigBuilder()
                .setMasterIP(masterIP)
                .setMasterPort(masterPort)
                .setBindPort(bindPort)
                .setBindIP(bindIP)
                .createSimpleConfig();
    }

    public static void main(String[] args) {
        try {
            Options options = new Options();
            options.addOption("m", "master", false, "run as a node master");
            options.addOption("b", "bind-ip", true, "specify an IP to bind");
            options.addOption("p", "bind-port", true, "specify a port to bind");
            options.addOption(null, "master-ip", true, "specify a master IP to connect");
            options.addOption(null, "master-port", true, "specify a master port to connect");
            options.addOption("e", "executor", false, "run as a node executor");
            options.addOption("h", "help", false, "get help");
            
            CommandLine commandLine = new GnuParser().parse(options, args);
            boolean isMaster = commandLine.hasOption("master");
            boolean isExecutor = commandLine.hasOption("executor");
            if (commandLine.hasOption("help")) {
                new HelpFormatter().printHelp("mlp-dc", options);
            } else {
                if (!(isMaster ^ isExecutor)) {
                    System.out.println("Please type -h to show help contents. ");
                } else {
                    SimpleConfig config = readConfig(commandLine);
                    if (isMaster) {
                        final NodeMasterHttpService service = NodeMasterHttpService.getInstance();
                        service.start(config);
                        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                service.stop();
                            }
                        }, "Application - Shutdown Hook"));
                    } else {
                        final NodeExecutor executor = new NodeExecutor(config);
                        executor.start();
                        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                executor.stop();
                            }
                        }, "Application - Shutdown Hook"));
                    }
                }
            }
        } catch (Throwable e) {
            System.out.println("Fatal error found: ");
            e.printStackTrace();
            System.exit(1);
        }}
}
