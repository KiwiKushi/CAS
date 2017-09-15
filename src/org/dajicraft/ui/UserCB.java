package org.dajicraft.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import org.dajicraft.ranks.User;

public class UserCB implements ActionListener {
	
	private JComboBox userCB;
	private UserInterface ui;
	
	public UserCB(JComboBox userCB, UserInterface ui) {
		this.userCB = userCB;
		this.ui = ui;
	}
	
	public void updateUsers() {
		ArrayList<User> usersToApply = this.ui.getServer().getAllUsers();
		String names[] = new String[0];
		if(usersToApply.size() != 0) {
			names = new String[usersToApply.size()-1];
			
			for(int currentNameIndex = 0; currentNameIndex != names.length; currentNameIndex++) {
				String name = usersToApply.get(currentNameIndex+1).getPrefix()+usersToApply.get(currentNameIndex+1).getName()+
						usersToApply.get(currentNameIndex+1).getSuffix();
				names[currentNameIndex] = name;
			}
		}
		
		this.userCB = new JComboBox(names);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}