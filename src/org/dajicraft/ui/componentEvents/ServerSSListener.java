package org.dajicraft.ui.componentEvents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.dajicraft.Main;
import org.dajicraft.Server;
import org.dajicraft.ui.UserInterface;

public class ServerSSListener implements ActionListener {
	
	private UserInterface ui;
	private Server server;
	
	public ServerSSListener(UserInterface ui) {
		this.ui = ui;
		this.server = ui.getServer();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals("Start Server")) {
		}
	}

}