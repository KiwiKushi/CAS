package org.dajicraft.room;

public class Subroom {
	
	private String name;
	private int maxUser;
	
	public Subroom(String name, int maxUsers) {
		this.name = name;
		this.maxUser = maxUsers;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMaxUsers() {
		return maxUser;
	}
	
}