package org.dajicraft.ui.componentEvents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.dajicraft.Main;
import org.dajicraft.ui.UserInterface;

public class InfoItemListener implements ActionListener {
	
	private UserInterface ui;
	
	public InfoItemListener(UserInterface ui) {
		this.ui = ui;
	}
	
	public void createNewInfoFile(File file) {
		
		if(!file.exists()) {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				ui.getLog().log("Error creating new info page.");
			}
		}
		
		try {
			FileWriter fWriter = new FileWriter(file);
			String infoPageSource = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n<HTML>\r\n<HEAD>\r\n\t<META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=utf-8\">\r\n\t<TITLE></TITLE>\r\n\t<META NAME=\"GENERATOR\" CONTENT=\"LibreOffice 4.1.6.2 (Linux)\">\r\n\t<META NAME=\"CREATED\" CONTENT=\"0;0\">\r\n\t<META NAME=\"CHANGED\" CONTENT=\"0;0\">\r\n\t<STYLE TYPE=\"text/css\">\r\n\t<!--\r\n\t\t@page { size: 8.5in 11in; margin: 1in }\r\n\t\tP { margin-bottom: 0.08in }\r\n\t-->\r\n\t</STYLE>\r\n</HEAD>\r\n<BODY LANG=\"en-US\" BGCOLOR=\"#fffca8\" DIR=\"LTR\">\r\n<P ALIGN=CENTER STYLE=\"margin-bottom: 0in; line-height: 200%\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=7><B>%name%\r\nInfo Page</B></FONT></FONT></P>\r\n<P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>\tWelcome\r\nto %name%, version %version%. %name% is a chat room server\r\napplication that tries to extend on the idea of anonymity, via\r\npseudonyms and nonpersistent accounts.</FONT></FONT></P>\r\n<P STYLE=\"margin-bottom: 0in\"><BR>\r\n</P>\r\n<P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=6><B>Version\r\nNotes</B></FONT></FONT></P>\r\n<P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><B>Version\r\n0.3</B></FONT></FONT></P>\r\n<UL>\r\n\t<LI><P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>Addition\r\n\tof a GUI</FONT></FONT></P>\r\n\t<LI><P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>Additional\r\n\tBug Fixes</FONT></FONT></P>\r\n</UL>\r\n<P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><B>Version\r\n0.2 (Unreleased)</B></FONT></FONT></P>\r\n<P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>Hierarchy\r\nSystem Introduced</FONT></FONT></P>\r\n<UL>\r\n\t<LI><P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>Additional\r\n\tBug Fixes</FONT></FONT></P>\r\n\t<LI><P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>First\r\n\tClient made.</FONT></FONT></P>\r\n</UL>\r\n<P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><B>Version\r\n0 .1</B></FONT></FONT></P>\r\n<UL>\r\n\t<LI><P STYLE=\"margin-bottom: 0in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>First\r\n\trelease</FONT></FONT></P>\r\n</UL>\r\n<P STYLE=\"margin-bottom: 0in; line-height: 150%\"><BR>\r\n</P>\r\n</BODY>\r\n</HTML>\r\n";
			infoPageSource = infoPageSource.replaceAll("%version%", String.valueOf(Main.getServerPrilim().getVersion()));
			infoPageSource = infoPageSource.replaceAll("%name%", Main.getServerPrilim().getName());
			infoPageSource = infoPageSource.replaceAll("%port%", String.valueOf(Main.getServerPrilim().getPort()));
			
			fWriter.write(infoPageSource);
			fWriter.flush();
			
		} catch (IOException e) {
			this.ui.getLog().log("Error creating writer for info page.");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		File infoPage = new File("src\\org\\dajicraft\\info.html");
		if(!infoPage.exists())
			this.createNewInfoFile(infoPage);
			try {
				java.awt.Desktop.getDesktop().browse(infoPage.toURI());
			} catch (IOException e1) {
			}
	}

}
