package org.dajicraft.ui;

import java.util.ArrayList;

import javax.swing.JTextPane;

public class GUILog {
	
	private UserInterface ui;
	private ArrayList<String> log = new ArrayList();
	
	public GUILog(UserInterface ui) {
		this.ui = ui;
	}
	
	public UserInterface getUi() {
		return ui;
	}


	public void setUi(UserInterface ui) {
		this.ui = ui;
	}


	public ArrayList<String> getLog() {
		return log;
	}

	public void setLog(ArrayList<String> log) {
		this.log = log;
	}
	
	public void log(String msg) {
		this.log.add(msg);
		this.ui.getUiLog().setText(null);
		String compLog = "";
		for(String log: this.log) {
			compLog = compLog.concat("\n"+log);
		}
		this.ui.getUiLog().setText(compLog);
	}

}