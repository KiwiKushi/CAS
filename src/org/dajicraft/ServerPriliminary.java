package org.dajicraft;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;

import javax.swing.JFrame;

import org.dajicraft.log.Log;
import org.dajicraft.ranks.User;
import org.dajicraft.room.Subroom;
import org.dajicraft.ui.UserInterface;
import org.dajicraft.Server;


public class ServerPriliminary {
	
	private Log log = new Log(">> ", "[HH:mm:ss] ");
	private Server server = new Server();
	private UserInterface ui;
	private int port = 100;
	private float version = 0.3f;
	private String name = "Chatroom";

	public ServerPriliminary() {	
		
		/*
		 * Setting up the user interface. This is
		 * done before the server comes online to
		 * provide the user with a speedy prompt
		 */
		
		ui = new UserInterface("Chatroom Server Admin Panel", server, log);
		ui.setupWindow();
		ui.loadAllComponenets();
		
		server.setGUI(ui);
		
		/*
		 * Catching connections and running the
		 * all the things to set up that persons account
		 * or drop the connecting
		 */
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public UserInterface getUi() {
		return ui;
	}

	public void setUi(UserInterface ui) {
		this.ui = ui;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}
}