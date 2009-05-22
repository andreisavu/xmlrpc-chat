
package server;

import java.util.logging.Logger;

public class Session {

	private Storage storage = Storage.getInstance();
	private Logger log = Logger.getLogger("chat");

	public int start(String nick) {
		log.info("Starting new session for " + nick);
        return storage.startSession(nick);
	}

	public boolean end(int id) {
		log.info("Closing session for id " + id);
		return storage.endSession(id);
	}

}

