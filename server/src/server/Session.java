
package server;

public class Session {

	private Storage storage = Storage.getInstance();

	public int start(String nick) {
		return storage.startSession(nick); 
	}

	public boolean end(int id) {
		return storage.endSession(id);
	}

}

