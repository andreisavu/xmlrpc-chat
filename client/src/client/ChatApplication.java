/*
 * ClientApp.java
 */
package client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class ChatApplication extends SingleFrameApplication {

	private ChatForm chatForm;
	private LoginForm loginForm;
	private String name,  server;
	private XmlRpcClient rpc;
	private Logger log = Logger.getLogger(ChatApplication.class.getName());
	private Session session;
	private Chat chat;
	private ClientThread monitor;

	public ChatForm getChatForm() {
		return chatForm;
	}

	public LoginForm getLoginForm() {
		return loginForm;
	}

	/**
	 * At startup create and show the main frame of the application.
	 */
	@Override
	protected void startup() {
		loginForm = new LoginForm();
		chatForm = new ChatForm();
		show(loginForm);
	}

	/**
	 * This method is to initialize the specified window by injecting resources.
	 * Windows shown in our application come fully initialized from the GUI
	 * builder, so this additional configuration is not needed.
	 */
	@Override
	protected void configureWindow(java.awt.Window root) {
	}

	/**
	 * A convenient static getter for the application instance.
	 * @return the instance of ClientApp
	 */
	public static ChatApplication getApplication() {
		return Application.getInstance(ChatApplication.class);
	}

	/**
	 * Main method launching the application.
	 */
	public static void main(String[] args) {
		launch(ChatApplication.class, args);
	}

	/**
	 * Show the chat main window and start the message fetch thread
	 * 
	 */
	public void startChat(String name, String server) throws Exception {
		this.name = name;
		this.server = server;

		try {
			initRpcClient(server);
			initSession(name);
			initChat();
			initMonitor();
		} catch (Exception ex) {
			throw ex;
		}

		loginForm.setVisible(false);
		show(chatForm);
	}

	public void sendMessage(String text) throws Exception {
		chat.send(text);
	}

	public void close() {
		try {
			monitor.exit();
			monitor.join();
		} catch (InterruptedException ex) {
		}
		session.end();
	}

	private void initChat() {
		chat = new Chat(session, rpc);
	}

	private void initMonitor() {
		monitor = new ClientThread(chat);
		monitor.start();
	}

	private void initRpcClient(String server) throws MalformedURLException {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL(server));
		} catch (MalformedURLException ex) {
			log.severe(ex.toString());
			throw ex;
		}
		rpc = new XmlRpcClient();
		rpc.setConfig(config);
	}

	private void initSession(String name) throws Exception {
		session = new Session(rpc);
		if (session.start(name) == 0) {
			throw new Exception("Chose another name.");
		}
	}
}
