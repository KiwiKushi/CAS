package org.dajicraft.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;

public class RoomCB implements ActionListener {
	
	private JComboBox roomCB;
	private UserInterface ui;
	
	public RoomCB(JComboBox roomCB, UserInterface ui) {
		this.roomCB = roomCB;
		this.ui = ui;
	}
	
	public void updateRooms() {
		ArrayList<Room> roomsToApply = this.ui.getServer().getAllRooms();
		String names[] = new String[roomsToApply.size()-1];
		
		for(int currentNameIndex = 0; currentNameIndex != names.length; currentNameIndex++) {
			String name = names[currentNameIndex];
			names[currentNameIndex] = name;
		}
		
		
		this.roomCB = new JComboBox(names);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
	
}