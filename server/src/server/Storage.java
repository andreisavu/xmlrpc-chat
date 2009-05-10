/**
 * Simple in memory database for chat server
 *
 * Multi-threading safe
 */

package server;

import java.util.*;

public class Storage {

	private static Storage instance = null;

	public static Storage getInstance() {
		if(instance == null) {
			instance = new Storage();
		}
		return instance;
	}

	private int generateMessageId() {
		return 0;
	}

	public Storage() {
	}

	public boolean putMessage(int id, String msg) {
		return false;
	}

	public List<Message> getMessages() {
		return getMessages(0);
	}

	public List<Message> getMessages(int minId) {
		return new LinkedList<Message>();
	}

	public int startSession(String name) {
		return 0;
	}

	public boolean endSession(int id) {
		return false;
	}

	public boolean validSessionId(int id) {
		return false;
	}

	public List<String> getUsers() {
		return new LinkedList<String>();
	}

}

