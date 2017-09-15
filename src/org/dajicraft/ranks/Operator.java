package org.dajicraft.ranks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;
import org.kushi.cryptography.Crypter;
import org.dajicraft.Server;
import org.dajicraft.chatOperations.ChatFunction;

public class Operator implements ChatFunction {
	
	private String username;
	private String prefix = "";
	private String suffix = "";
	private Room room;
	private Socket sock;
	private String lastInteraction;
	private BufferedReader in;
	private BufferedWriter out;
	private boolean session;
	private Crypter crypter;
	
	
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


	private ArrayList<String> permissions = new ArrayList<String>();
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public Operator(String username, Room room, Socket sock,
			String lastInteraction, BufferedReader in,
			BufferedWriter out, boolean session) {
		this.username = username;
		this.room = room;
		this.sock = sock;
		this.lastInteraction = lastInteraction;
		try {
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.session = session;
	}

	
	public String getSuffix() {
		return suffix;
	}
	
	
	public String getPrefix() {
		return prefix;
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

	public void setSessoin(boolean state) {
		session = state;
	}
	
	
	public boolean universalBroadcast(String msg, User user) {
		for(User ruser: new Server().getAllUsers()) {
			try {
				BufferedWriter rout = ruser.getBufferedWriter();
				rout.write(msg+"\n");
				rout.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}
		return false;
	}
	
	private void alert(String msg, User opUser) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(opUser.getSocket().getOutputStream()));
			out.write(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public boolean broadcast(String msg, User user) {
		for(User ruser: new Server().getAllUsers()) {
			if(ruser.getRoom().getName().equals(user.getRoom().getName())) {
				try {
					BufferedWriter rout = ruser.getBufferedWriter();
					rout.write(msg+"\n");
					rout.flush();
				} catch (IOException e) {
					Server.leave(ruser);
					return false;
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

	public void setRoom(Room roomName) {
		room = roomName;
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


	@Override
	public void setRoom(String roomName) {
		// TODO Auto-generated method stub
		
	}
}