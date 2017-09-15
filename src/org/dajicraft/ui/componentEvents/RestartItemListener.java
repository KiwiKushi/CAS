package org.dajicraft.ui.componentEvents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.dajicraft.Main;
import org.dajicraft.ui.UserInterface;

public class RestartItemListener implements ActionListener {
	
	private UserInterface ui;
	
	public RestartItemListener(UserInterface ui) {
		this.ui = ui;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Main.restart();
	}
	
}