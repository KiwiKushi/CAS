package org.dajicraft.ui.componentEvents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.dajicraft.Main;
import org.dajicraft.ui.UserInterface;

public class InfoRefreshItemListener implements ActionListener {
	
	private UserInterface ui;
	
	public InfoRefreshItemListener(UserInterface ui) {
		this.ui = ui;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		File infoPage = new File("src\\org\\dajicraft\\info.html");
		if(infoPage.exists()) {
			String logMsg = infoPage.delete() ? "info.html has been deleted." : "info.html could not be deleted. This may be because"
					+ "\nyou are not running this program with the correct privileges.";
			this.ui.getGLog().log(logMsg);
			this.ui.getLog().log(logMsg);
			return;
		}
		this.ui.getLog().log("info.html does not exist, in its directory.");
	}
	
}