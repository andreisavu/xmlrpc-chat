
package server;

import java.util.*;
import java.util.logging.Logger;

public class Chat {

	private Storage storage = Storage.getInstance();
	private Logger log = Logger.getLogger("chat");

	public boolean post(int id, String message) {
		log.info("Message: " + message + " SID: " + id);
		return storage.putMessage(id, message);
	}

	public List<Map<String,String>> get(int id, int lastId) {
		if(!storage.validSessionId(id)) {
			return new LinkedList<Map<String,String>>();
		}
		List<Map<String,String>> ret = new LinkedList<Map<String,String>>();
		for(Message m : storage.getMessages(lastId)) {
			ret.add(m.toMap());
		}
		return ret;
	}

	public List<String> getUsers(int id) {
		if(!storage.validSessionId(id)) {
			return new LinkedList<String>();
		}
		return storage.getUsers();
	}
}

