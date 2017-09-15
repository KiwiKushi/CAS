package org.dajicraft.chatOperations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.dajicraft.Server;
import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;
import org.dajicraft.ui.UserInterface;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ControlPanelCommand {
	
	private Server server;
	
	public ControlPanelCommand(Server server) {
		this.server = server;
	}
	
	public boolean commandDispatch(String data) {
		try {
			
			JSONParser parser = new JSONParser();
			
			try {
				if(((JSONObject) parser.parse(data)).get("data").toString().substring(0, 1).equals("/")) {
					
					// Add commands for the server panel
					
				} else {
					for(Room room: this.server.getAllRooms()) this.server.broadcast(data, room);
					this.server.getLog().log("Alert Issued: "+data);
					this.server.getUi().getGLog().log("Alert Issued: "+data);
				}
			} catch (ParseException e) {
				server.getLog().log("Error formatting server command panel input.");
			}
			
			
			
		} catch(StringIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
	
	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
	
}