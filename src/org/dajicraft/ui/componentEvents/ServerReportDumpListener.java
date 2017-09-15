package org.dajicraft.ui.componentEvents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import javax.swing.JFileChooser;

import org.dajicraft.ui.UserInterface;

public class ServerReportDumpListener implements ActionListener {
	
	private UserInterface ui;
	private Thread exportThread;
	
	public ServerReportDumpListener(UserInterface ui) {
		this.ui = ui;
		this.exportThread = new Thread(new Runnable() {

			@Override
			public void run() {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Pick Location For Export");
				fileChooser.showOpenDialog(null);
				fileChooser.setVisible(true);
				File file = fileChooser.getSelectedFile();
				
				
				try {
					if(!file.exists()) {
						try {
							file.createNewFile();
						} catch (IOException e) {
							ui.getGLog().log("Error creating report file.");
							ui.getLog().log("Error creating report file.");
						}
					}
					
					try {
						BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
						Date date = new Date();
						
						try {
							outputStream.write("Server Report for "+date+"\n");
							outputStream.write("There were "+ui.getServer().getUsers().size()+" users chatting\n");
							outputStream.flush();
						} catch (IOException e) {
							ui.getGLog().log("I/O Error on file dump.");
							ui.getLog().log("I/O Error on file dump.");
						}
						
					} catch (FileNotFoundException e) {
						ui.getGLog().log("Error exporting report.");
						ui.getLog().log("Error exporting report.");
					}
				} catch(NullPointerException e) {
					ui.getLog().log("Stopping file search.");
					exportThread.interrupt();
					ui.getLog().log("File search halted..");
				}
				
				
			}	
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		exportThread.run();
	}
}