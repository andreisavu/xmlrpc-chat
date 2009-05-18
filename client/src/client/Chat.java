/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author Admin
 */
public class Chat {

	private Session session;
	private XmlRpcClient client;
	private Logger log = Logger.getLogger(Chat.class.getName());

	public Chat(Session s, XmlRpcClient rpc) {
		this.session = s;
		this.client = rpc;
	}

	Map<String, String>[] getMessage(int lastId) throws XmlRpcException {
		Object[] params = new Object[]{session.getId(), lastId};
		try {
			Object[] objs = (Object[]) client.execute("Chat.get", params);

			Map<String, String> ret[] = new HashMap[objs.length];
			int count = 0;
			for (Object o : objs) {
				ret[count++] = (HashMap<String, String>) o;
			}
			return ret;
		} catch (XmlRpcException ex) {
			log.severe(ex.getMessage());
			throw ex;
		}
	}

	String[] getUsers() throws XmlRpcException {
		Object[] params = new Object[]{session.getId()};
		try {
			Object[] objs = (Object[]) client.execute("Chat.getUsers", params);

			String[] strs = new String[objs.length];
			int count = 0;
			for (Object o : objs) {
				strs[count++] = (String) o;
			}
			return strs;
		} catch (XmlRpcException ex) {
			log.severe(ex.getMessage());
			throw ex;
		}
	}

	boolean send(String text) throws Exception {
		Object[] params = new Object[]{session.getId(), text};
		try {
			return (Boolean) client.execute("Chat.post", params);
		} catch (XmlRpcException ex) {
			log.severe(ex.getMessage());
			throw ex;
		}
	}
}
