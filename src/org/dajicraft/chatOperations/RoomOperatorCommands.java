package org.dajicraft.chatOperations;

import java.util.ArrayList;
import java.util.Arrays;

import org.dajicraft.Server;
import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;

public class RoomOperatorCommands {
	private static String command;
	private static User targetRoomOperator;
	
	public RoomOperatorCommands(String command) {
		this.command = command;
	}
	
	
	public boolean run(User user) {
		/*
		 * Need to revamp this method so the
		 * user can get more information from
		 * errors
		 */
		
		try {
			if(command.substring(0, "/makeOp".length()).equals("/makeOp")) {
				ArrayList<String> arguements = new ArrayList<String>(Arrays.asList(command.substring("/makeOp".length(), command.length()).split(" ")));
				User newRoomOp = user.stringToUser(arguements.get(1));
				if(newRoomOp == null || !newRoomOp.getRoom().equals(user.getRoom())) {
					user.alert("No such user in "+user.getRoom().getName(), user);
					return false;
				}
				user.setRoomOperator(false);
				user.setPrefix("");
				newRoomOp.setRoomOperator(true);
				newRoomOp.setPrefix("["+newRoomOp.getRoom().getDisplay()+" Operator] ");
				newRoomOp.broadcast(user.getName()+" has made "+newRoomOp.getName()+" Room Operator", user);
				newRoomOp.alert("You are now Room Operator", newRoomOp);
				System.out.println(user.getName()+" has given leadership of "+newRoomOp.getRoom().getName()+" to "+newRoomOp.getName());
			} else if(command.substring(0, "/seventyNineProtons".length()).equals("/seventyNineProtons")) {
				user.alert("You have just created a gold!", user);
				System.out.println(user.getName()+" just created gold!");
			}
		} catch(StringIndexOutOfBoundsException e) {
			
		}
		
		
		return false;
	}
}