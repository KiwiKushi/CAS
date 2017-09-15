package org.dajicraft.ui.componentEvents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.dajicraft.chatOperations.ControlPanelCommand;
import org.dajicraft.ui.UserInterface;
import org.jsfml.window.Keyboard.Key;

public class DataInputListener implements KeyListener {
	
	private UserInterface ui;
	private ControlPanelCommand cpc;
	
	public DataInputListener(UserInterface ui) {
		this.ui = ui;
		this.cpc = new ControlPanelCommand(ui.getServer());
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 10) {
			String dIText = this.ui.getDataInput().getText();
			
			this.cpc.commandDispatch(dIText);
			this.ui.getDataInput().setText(null);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}


}