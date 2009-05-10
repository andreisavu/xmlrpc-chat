
package server;

public class Message {

	private int id;

	private String message;

	private String user;

	public Message(int id, String user, String message) {
		this.id = id;
		this.message = message;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return  message;
	}

	public String getUser() {
		return user;
	}

}

