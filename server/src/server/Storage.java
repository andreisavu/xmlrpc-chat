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


	private int sessionIdCounter = 0;
	private int messageIdCounter = 0;

	private List<Message> messages = new LinkedList<Message>();
	private Map<Integer, String> sessions = new HashMap<Integer, String>();

	private synchronized int generateMessageId() {
		return ++messageIdCounter;
	}

	private synchronized int generateSessionId() {
		return ++sessionIdCounter;
	}

	public boolean putMessage(int id, String msg) {
		return false;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public List<Message> getMessages(int minId) {
		List<Message> ret = new LinkedList<Message>();
		for(Message m : messages) {
			if(m.getId() >= minId) {
				ret.add(m);
			}
		}
		return ret;
	}

	public int startSession(String name) {
		return generateSessionId();
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

