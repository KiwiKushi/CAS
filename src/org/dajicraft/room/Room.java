package org.dajicraft.room;

import java.util.ArrayList;

import org.dajicraft.ranks.User;

public class Room {
	private String name;
	private String displayName;
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<String> traits = new ArrayList<String>();
	private boolean isDefinite;
	private boolean isRoomDefinite = false;
	
	
	public Room(String name, ArrayList<String> traits) {
		this.name = name;
		this.displayName = name;
		this.traits = traits;
	}
	
	public String getDisplay() {
		return displayName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRoomUsers(ArrayList<User> users) {
		this.users = users;
	}
	
	public void setDisplayName(String name) {
		this.name = name;
	}
	
	public ArrayList<User> getRoomUsers() {
		return users;
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
	
	public void addUser(User user) {
		users.add(user);
	}
}