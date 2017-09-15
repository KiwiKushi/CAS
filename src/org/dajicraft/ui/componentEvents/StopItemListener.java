package org.dajicraft.ui.componentEvents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.dajicraft.ui.UserInterface;

public class StopItemListener implements ActionListener {
	
	private UserInterface ui;
	
	public StopItemListener(UserInterface ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.ui.getGLog().log("Shutting Down...");
		this.ui.getLog().log("Shutting Down...");
		try {
			Thread.sleep(2000);
			this.ui.getServer().shutdown();
		} catch (InterruptedException e1) {
			this.ui.getLog().log("A nonchat related error has occurred, could not pause thread.");
		}
	}

}
