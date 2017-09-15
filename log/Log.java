package org.dajicraft.log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	private String logMsgIdentifier = null;
	private SimpleDateFormat dateFormat = null;
	private String dateLogPrefix = null;
	
	public Log(String logMsgIdentifier, String dateLogPrefix) {
		this.logMsgIdentifier = logMsgIdentifier;
		this.dateLogPrefix = dateLogPrefix;
	}
	
	public String format(String logMsg) {
		dateFormat = new SimpleDateFormat(dateLogPrefix);
		String outputMsg = dateFormat.format(new Date())+logMsgIdentifier;
		dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		String[] dates = dateFormat.format(new Date()).toString().split(":");
		String month;
		switch(Integer.valueOf(dates[1])) {
		case 1:
			month = "January";
			break;
		case 2:
			month = "Febuary";
			break;
		case 3:
			month = "March";
			break;
		case 4:
			month = "April";
			break;
		case 5:
			month = "May";
			break;
		case 6:
			month = "June";
			break;
		case 7:
			month = "July";
			break;
		case 8:
			month = "August";
			break;
		case 9:
			month = "September";
			break;
		case 10:
			month = "October";
			break;
		case 11:
			month = "November";
			break;
		case 12:
			month = "December";
			break;
		default:
			month = "Unknown Month";
			break;
		}
		logMsg = logMsg.replaceAll("t:month_name", month);
		logMsg = logMsg.replaceAll("<br>", "\n");
		logMsg = logMsg.replaceAll("<tab>", "\t");
		logMsg = logMsg.replaceAll("t:year", dates[0]);
		logMsg = logMsg.replaceAll("t:month", dates[1]);
		logMsg = logMsg.replaceAll("t:day", dates[2]);
		logMsg = logMsg.replaceAll("t:hour", dates[3]);
		logMsg = logMsg.replaceAll("t:minute", dates[4]);
		logMsg = logMsg.replaceAll("t:second", dates[5]);
		outputMsg += logMsg;
		return outputMsg;
	}
	
	public void log(String logMsg) {
		try {
			PrintStream out = System.out;
			System.setOut(out);
			System.out.println(this.format(logMsg));
		} catch(NullPointerException e) {
			System.out.println("There was an error creating the config folder/file."+
		"\nThis could be because you have placed the server in an unaccessible folder.");
		}
	}
}