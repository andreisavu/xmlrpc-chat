/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.Map;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;

/**
 *
 * @author Alexandru Popescu
 */
public class ClientThread extends Thread {

	private ChatForm ui;
	private boolean exitFlag = false;
	private Chat chat;
	private Logger log = Logger.getLogger(ClientThread.class.getName());

	ClientThread(Chat chat) {
		this.chat = chat;
		this.ui = ChatApplication.getApplication().getChatForm();
	}

	@Override
	public void run() {
		int lastId = -1;
		while (!shouldExit()) {
			try {
				lastId = setMessages(lastId);
				setUsers();
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				break;
			} catch (XmlRpcException ex) {
				log.info("something is fucked");
			}
		}
	}

	public synchronized void exit() {
		exitFlag = true;
	}

	private int setMessages(int lastId) throws XmlRpcException {
		Map<String, String>[] messages = chat.getMessage(lastId+1);

		String[] lines = new String[messages.length];
		int count = 0, aux = 0;
		for (Map<String, String> m : messages) {
			lines[count++] = m.get("name") + ": " + m.get("msg");
			aux = Integer.parseInt(m.get("id"));
			if(aux > lastId) {
				lastId = aux;
			}
		}
		ui.setMessages(lines);
		return lastId;
	}

	private void setUsers() throws XmlRpcException {
		String[] users = chat.getUsers();
		ui.setUsers(users);
	}

	private synchronized boolean shouldExit() {
		return exitFlag;
	}
}
