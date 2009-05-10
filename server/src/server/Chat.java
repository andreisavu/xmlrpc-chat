
package server;

public class Chat {

	public boolean post(int id, String message) {
		return true;
	}

	public List<Message> get(int id, int last_id) {
		return new LinkedList<Message>();
	}

	public List<String> getUsers(int id) {
		return new LinkedList<String>();
	}

}

