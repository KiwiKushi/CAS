package org.dajicraft.chatOperations;

import java.util.ArrayList;
import java.util.Arrays;

import org.dajicraft.Server;
import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;

public class UserCommands {
	
	private String command;
	private ArrayList<String> arguements;
	private User user;
	
	public UserCommands(String command, User user) {
		try {
			this.command = command.split(" ")[0];
			this.arguements = new ArrayList<String>(Arrays.asList(
					this.command.split(" ")));
		} catch(IndexOutOfBoundsException e) {
			this.command = null;
			this.arguements = null;
		}
	}
	
	public void run() {
		if(command.equals("/rooms")) {
			for(Room currentRoom: Server.getAllRooms()) {
				user.alert((currentRoom.getName()+" - "+currentRoom.getRoomUsers().size()), user);
			}
		}
	}
	
}
