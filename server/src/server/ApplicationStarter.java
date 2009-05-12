/**
 * Chat Server Entry Point Class
 *
 *
 */

package server;

import java.util.logging.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.xmlrpc.webserver.ServletWebServer;
import org.apache.xmlrpc.webserver.XmlRpcServlet;

public class ApplicationStarter {

	private static Logger log = Logger.getLogger("chat");

	private static Properties conf = new Properties();
	private static int listenPort = 8080;
	
	public static void main(String[] args) {
		log.info("Starting chat server ...");
	
		loadConfiguration();
		startXmlRpcServer();
	}

	private static void loadConfiguration() {
		log.info("Loading application configuration ...");
		try {
			conf.load(new FileInputStream("server.properties"));
		} catch (IOException ex) {
			log.severe(ex.toString());
			System.err.println("Configuration file not found.");
			System.exit(0);
		}
	}

	private static void startXmlRpcServer() {

		listenPort = Integer.parseInt(conf.getProperty("server.port", "8080"));
		log.info("Starting chat server on port " + listenPort + " ...");

		try {
			XmlRpcServlet servlet = new XmlRpcServlet();
			ServletWebServer webServer = new ServletWebServer(servlet, listenPort);
			webServer.start();
		} catch (Exception ex) {
			log.severe(ex.toString());
		}
	}

}

