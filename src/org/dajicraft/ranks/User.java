package org.dajicraft.ranks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;
import org.dajicraft.room.Subroom;
import org.json.simple.JSONObject;
import org.kushi.cryptography.Crypter;
import org.dajicraft.Server;
import org.dajicraft.chatOperations.ChatFunction;

public class User implements ChatFunction {
	
	private String username;
	private Room room;
	private String prefix = "";
	private String suffix = "";
	private Socket sock;
	private String lastInteraction;
	private BufferedReader in;
	private BufferedWriter out;
	private boolean session;
	private ArrayList<String> permissions = new ArrayList<String>();
	private boolean isRoomOperator = false;
	private String lastMsg = "";
	private Subroom currentSubroom;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private Crypter crypter;
	
	
	public User(String username, Room room, Socket sock,
			String lastInteraction, BufferedReader in,
			BufferedWriter out, boolean session) {
		this.username = username;
		this.room = room;
		this.sock = sock;
		this.lastInteraction = lastInteraction;
		currentSubroom = new Subroom(room.getName(), Integer.MAX_VALUE);
		try {
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.session = session;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Socket getSock() {
		return sock;
	}

	public void setSock(Socket sock) {
		this.sock = sock;
	}

	public String getLastInteraction() {
		return lastInteraction;
	}

	public void setLastInteraction(String lastInteraction) {
		this.lastInteraction = lastInteraction;
	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

	public BufferedWriter getOut() {
		return out;
	}

	public void setOut(BufferedWriter out) {
		this.out = out;
	}

	public ArrayList<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(ArrayList<String> permissions) {
		this.permissions = permissions;
	}

	public Subroom getCurrentSubroom() {
		return currentSubroom;
	}

	public void setCurrentSubroom(Subroom currentSubroom) {
		this.currentSubroom = currentSubroom;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public void setSession(boolean session) {
		this.session = session;
	}

	public void setSocket(Socket sock) {
		this.sock = sock;
	}
	
	public User stringToUser(String username) {
		for(User currentUser: Server.getAllUsers()) {
			if(currentUser.getName().equals(username)) return currentUser;
		}
		return null;
	}
	
	public Subroom getSubroom() {
		return currentSubroom;
	}
	
	public boolean isRoomOperator() {
		return isRoomOperator;
	}

	
	public String getName() {
		return username;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public Socket getSocket() {
		return sock;
	}
	
	public BufferedReader getBufferedReader() {
		return in;
	}
	
	
	public boolean getSession() {
		return session;
	}
	
	
	public BufferedWriter getBufferedWriter() {
		return out;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public void setSessoin(boolean state) {
		session = state;
	}
	
	public void setRoomOperator(boolean isRoomOperator) {
		this.isRoomOperator = isRoomOperator;
	}
	
	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}
	
	public String getLastMsg() {
		return lastMsg;
	}
	
	
	
	@Override
	public boolean broadcast(String msg, User user) {
		
		JSONObject msgPacket = new JSONObject();
		msgPacket.put("messageType", "text");
		msgPacket.put("data", msg);
		msgPacket.put("name", user.getPrefix()+user.getName()+user.getSuffix());
		
		msg = msgPacket.toJSONString();
		
		for(User ruser: new Server().getAllUsers()) {
			if(ruser.getRoom().getName().equals(user.getRoom().getName()) && 
					ruser.getSubroom().getName().equals(user.getSubroom().getName())) {
				try {
					BufferedWriter rout = ruser.getBufferedWriter();
					String encodeEncryptedMessage = new String(Base64.getEncoder().encode(ruser.getCrypter().encrypt(msg)),"UTF-8");
					rout.write(encodeEncryptedMessage+"\n");
					rout.flush();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	@Override
	public String formatMsg(String prefix, String username,
			String suffix, String msg) {
		return prefix+username+suffix+msg;
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void setSuffix(String suffix) {
		for(String permission: permissions) {
			if(permission == "set Prefix") this.suffix = suffix;
		}
	}


	@Override
	public void setName(String newName) {
		username = newName;
	}


	@Override
	public void setRoom(String roomName) {
		// TODO Auto-generated method stub
		
	}


	public String getSuffix() {
		return suffix;
	}


	public String getPrefix() {
		return prefix;
	}
	

	
	public void alert(String msg, User user) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(user.getSocket().getOutputStream()));
			JSONObject alertPacket = new JSONObject();
			alertPacket.put("messageType", "alert");
			alertPacket.put("data", msg);
			out.write(new String(Base64.getEncoder().encode((this.crypter.encrypt(alertPacket.toJSONString()))), "UTF-8")+"\n");
			out.flush();
		} catch (IOException e) {
			Server.leave(user);
		}
		
	}
	

	public Crypter getCrypter() {
		return crypter;
	}

	public void setCrypter(Crypter crypter) {
		this.crypter = crypter;
	}

	public boolean message(String msg, User sender, User recipient) {
		BufferedWriter out = recipient.getBufferedWriter();
		try {
			out.write(sender.getPrefix()+sender.getName()+sender.getSuffix()+" >> "+msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
}