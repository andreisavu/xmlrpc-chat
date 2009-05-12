
package server;

import java.util.*;

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

	public Map<String,String> toMap() {
		Map<String, String> ret = new HashMap<String,String>();
		ret.put("id", "" + id);
		ret.put("msg", message);
		ret.put("name", user);
		return ret;
	}

	public void fromMap(Map<String,String> m) {
		id = Integer.parseInt(m.get("id"));
		message = m.get("msg");
		user = m.get("name");
	}

}

