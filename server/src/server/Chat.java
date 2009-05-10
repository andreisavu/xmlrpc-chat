
package server;

import java.util.*;

public class Chat {

	private Storage storage = Storage.getInstance();

	public boolean post(int id, String message) {
		return storage.putMessage(id, message);
	}

	public List<Message> get(int id, int lastId) {
		if(!storage.validSessionId(id)) {
			return new LinkedList<Message>();
		}
		return storage.getMessages(lastId);
	}

	public List<String> getUsers(int id) {
		if(!storage.validSessionId(id)) {
			return new LinkedList<String>();
		}
		return storage.getUsers();
	}

}

