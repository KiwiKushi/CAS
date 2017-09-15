package org.dajicraft;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;
import org.dajicraft.log.Log;
import javax.swing.JFrame;
import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;
import org.dajicraft.room.Subroom;
import org.dajicraft.ui.UserInterface;
import org.dajicraft.Server;


public class Main {
	
	/*
	 * 		CoolAssStuff (CAS)
	 * Sorry for the inappropriate language, but this is the only absolute name
	 * this application has. So what is CAS? CAS is an encrypted chat room server
	 * that not only supports unauthenticated access to rooms, but encrypted pictures!
	 * So why did I make CAS? Before we answer this I need to make some stuff clear.
	 * I get bored and make stuff. Good, bad, I really don't care as long as it keeps
	 * me occupied. I also am a long time user of TOR chat rooms. I liked the idea of
	 * anonymous interactions in which everything could be discussed without fear of
	 * consequence. Plus I became a Richard Stallman fan boy. I also hated the owner
	 * of a TOR chat room service, that I commonly frequented. So I decided to see if
	 * I could make my own service. This service(CAS) would be open, customizable, and
	 * most importantly I had the ability to kick people out I didn't like. Pfft, no
	 * weebs allowed.
	 * 
	 * 		Things that make my life unbearable:
	 * 1.) I didn't use polymorphism on the user systems
	 * 2.) Sending pictures used to work, and remarkably fast, might I add?
	 * However, now it doens't work since the addition of encryption. The error
	 * could just be a client issue; Although, I only have one compatible client.
	 * 3.) I didn't comment every single thing I did! I attempted to go back sometime
	 * ago and fix this, but I only added in a few.
	 * 4.) The server class is disgusting.
	 * 5.) I don't have time to do large rewrites to fix most of these issues.
	 * I am also afraid that it will break the server
	 */
	
	
	private static ServerPriliminary serverPrilim = new ServerPriliminary();
	private static Server server;
	public static ServerPriliminary getServerPrilim() {
		return serverPrilim;
	}

	public static void setServerPrilim(ServerPriliminary serverPrilim) {
		Main.serverPrilim = serverPrilim;
	}

	public static Server getServer() {
		return server;
	}

	public static void setServer(Server server) {
		Main.server = server;
	}

	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		Main.log = log;
	}

	public static UserInterface getUi() {
		return ui;
	}

	public static void setUi(UserInterface ui) {
		Main.ui = ui;
	}

	private static Log log = new Log(">>", "hh::mm::ss ");
	private static UserInterface ui = new UserInterface("Chatroom Server", server, log);
	
	public static void main(String[] args) {
		ui.loadAllComponenets();
		start();
	}
	
	public static void start() {
		server.listenForUsers();
	}
	
	public static void stop() {
		for(Room room: server.getAllRooms()) server.broadcast("The server is shutting down. You will be disconnected", room);
		for(User user: server.getAllUsers()) {
			try {
				user.getSocket().close();
			} catch (IOException e) {
				user.setSocket(null);
			}
		}
	}
	
	public static void restart() {
		stop();
		/*
		 * This does not work!
		 * So I wrote it to crash the application
		 */
		System.exit(0);
		
		start();
		
	}
	
}