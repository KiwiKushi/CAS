package org.dajicraft.chatOperations;

import java.util.ArrayList;
import java.util.Arrays;

import org.dajicraft.Server;
import org.dajicraft.ranks.Operator;
import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;

public class OperatorCommand {
	private static String command = "";
	public OperatorCommand(String command) {
		this.command = command;
	}
	
	public boolean run(User opUser) {
		try {
			if(command.substring(0, "/kick".length()).equals("/kick")) {
				String name = command.substring("/kick ".length(), command.length());
				for(User user: Server.getAllUsers()) {
					if(user.getName().equals(name) && user.getRoom().equals(opUser.getRoom())) {
						user.alert("You have been Kick!", user);
						Server.leave(user);
						Server.removeUser(user);
						System.out.println(user.getPrefix()+user.getName()+user.getSuffix()+" has been kicked!");
						Server.broadcast(user.getPrefix()+user.getName()+user.getSuffix()+" has been kicked!", user.getRoom());
						opUser.alert(user.getPrefix()+user.getName()+user.getSuffix()+" has successfully been kicked!\n", opUser);
						return true;
					}
				}
				System.out.println("No user named "+name);
				return false;
			} else if(command.substring(0, "/merge".length()).equals("/merge")) {
				ArrayList<String> arguements = new ArrayList<String>(Arrays.asList(command.split(" ")));
				for(Room currentRoom: Server.getAllRooms()) {
					if(currentRoom.getName().equals(arguements.get(1))) {
						currentRoom.setRoomUsers(opUser.getRoom().getRoomUsers());
						for(User currentUser: currentRoom.getRoomUsers()) {
							currentUser.setRoom(currentRoom);
						}
						opUser.alert(opUser.getRoom().getName()+" merged with "+currentRoom.getName(), opUser);
						String ip = opUser.getSocket().getInetAddress().toString();
						opUser.alert("Room merged! This will be logged in the server logs for security reasons", opUser);
						System.out.println(">>>>> "+opUser.getRoom().getName()+" has merged with "+currentRoom.getName()+
								" brought on "+opUser.getName()+": "+ip.substring(0, ip.length()-1)+" <<<<<");
						return false;
					}
				}
				opUser.alert("No such room", opUser);
			} else if(command.substring(0, "/getIp".length()).equals("/getIp")) {
				ArrayList<String> arguements = new ArrayList<String>(Arrays.asList(command.split(" ")));
				if(arguements.get(1).equals("-r")) {
					for(Room currentRoom: Server.getAllRooms()) {
						if(currentRoom.getName().equals(arguements.get(2)) && arguements.get(3).equals("-u")) {
							for(User currentUser: Server.getAllUsers()) {
								if(currentUser.getName().equals(arguements.get(4))) {
									String ip = currentUser.getSocket().getInetAddress().toString();
									ip = ip.substring(1, ip.length());
									opUser.alert((currentUser.getName()+"'s current address is "+ip), opUser);
								}
							}
							break;
						}
					}
				}
			} else if(command.substring(0, "/room".length()).equals("/room")) {
				for(Room currentRoom: Server.getAllRooms()) {
					opUser.alert((currentRoom.getName()+" - "+(currentRoom.getRoomUsers().size()+1)), opUser);
				}
			}
		} catch(StringIndexOutOfBoundsException e) {
			
		} catch(IndexOutOfBoundsException e) {
			
		}
		return true;
	}
}
