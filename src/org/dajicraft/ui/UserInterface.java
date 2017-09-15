package org.dajicraft.ui;

import java.awt.MenuBar;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.dajicraft.Main;
import org.dajicraft.Server;
import org.dajicraft.log.Log;
import org.jsfml.window.Mouse;
import org.dajicraft.ui.UserCB;
import org.dajicraft.ui.componentEvents.DataInputListener;
import org.dajicraft.ui.componentEvents.InfoItemListener;
import org.dajicraft.ui.componentEvents.InfoRefreshItemListener;
import org.dajicraft.ui.componentEvents.RestartItemListener;
import org.dajicraft.ui.componentEvents.ServerReportDumpListener;
import org.dajicraft.ui.componentEvents.ServerSSListener;
import org.dajicraft.ui.componentEvents.StopItemListener;

public class UserInterface {
	
	private JFrame window = new JFrame();
	private Server server;
	private Log log;
	private UserInterfaceListener uil;
	
	
	// Components Sidekicks
	private GUILog GLog;
	private UserCB ucb;
	private RoomCB rcb;
	private DataInputListener dil;
	private ServerSSListener ssl;
	
	
	
	// Components
	private JTextPane uiLog;
	private JComboBox roomsCB;
	private JComboBox usersCB;
	private JTextField dataInput;
	
	// Menu Bar
	private JMenuBar menuBar;
		// Server Menu
	private JMenu serverMenu = new JMenu("Server");
			// Server Menu Items
	private JMenuItem stopItem = new JMenuItem("Stop");
	private JMenuItem restartItem = new JMenuItem("Restart");
	private JMenuItem serverReportDump = new JMenuItem("Export Server Report");
	private JMenuItem infoItem = new JMenuItem("Info Page");
	private JMenuItem infoRefreshItem = new JMenuItem("Refresh Info Page");
	
	
	
	public DataInputListener getDil() {
		return dil;
	}

	public void setDil(DataInputListener dil) {
		this.dil = dil;
	}

	public JTextField getDataInput() {
		return dataInput;
	}

	public void setDataInput(JTextField dataInput) {
		this.dataInput = dataInput;
	}

	
	public UserInterface(String windowName, Server server, Log log) {
		this.window.setTitle(windowName);
		this.server = server;
		this.log = log;
		this.uil = new UserInterfaceListener(this.log, this.server);
	}
	
	public void setupWindow() {
		this.window.setVisible(true);
		this.window.setSize(600, 600);
		this.window.setResizable(true);
		this.window.addWindowListener(uil);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setLayout(null);
	}
	
	public void loadAllComponenets() {
		
		this.uiLog = new JTextPane();
		this.window.add(this.uiLog);
		this.uiLog.setVisible(true);
		this.uiLog.setBounds(200, 10, 400, 400);
		this.uiLog.setEditable(false);
		this.GLog = new GUILog(this);
		
		this.usersCB = new JComboBox();
		this.roomsCB = new JComboBox();
		this.window.add(roomsCB);
		this.roomsCB.setVisible(true);
		this.roomsCB.setBounds(10, 40, 100, 20);
		this.rcb = new RoomCB(this.roomsCB, this);
		
		this.window.add(this.usersCB);
		this.usersCB.setVisible(true);
		this.usersCB.setBounds(10, 10, 100, 20);
		this.ucb = new UserCB(this.usersCB, this);
		this.usersCB.addActionListener(this.ucb);
		
		this.dataInput = new JTextField();
		this.window.add(this.dataInput);
		this.dataInput.setVisible(true);
		this.dataInput.setBounds(200, 410, 400, 20);
		this.dil = new DataInputListener(this);
		this.dataInput.addKeyListener(dil);
		
		this.menuBar = new JMenuBar();
		this.stopItem.addActionListener(new StopItemListener(this));
		this.restartItem.addActionListener(new RestartItemListener(this));
		this.serverReportDump.addActionListener(new ServerReportDumpListener(this));
		this.infoItem.addActionListener(new InfoItemListener(this));
		this.infoRefreshItem.addActionListener(new InfoRefreshItemListener(this));
		this.serverMenu.add(this.stopItem);
		this.serverMenu.add(restartItem);
		this.serverMenu.add(serverReportDump);
		this.serverMenu.add(infoItem);
		this.serverMenu.add(infoRefreshItem);
		this.menuBar.add(this.serverMenu);
		this.window.setJMenuBar(this.menuBar);
		
	}
	
	public JMenuItem getRestartItem() {
		return restartItem;
	}

	public void setRestartItem(JMenuItem restartItem) {
		this.restartItem = restartItem;
	}

	public JMenuItem getInfoItem() {
		return infoItem;
	}

	public void setInfoItem(JMenuItem infoItem) {
		this.infoItem = infoItem;
	}

	public JMenuItem getInfoRefreshItem() {
		return infoRefreshItem;
	}

	public void setInfoRefreshItem(JMenuItem infoRefreshItem) {
		this.infoRefreshItem = infoRefreshItem;
	}

	public RoomCB getRcb() {
		return rcb;
	}

	public void setRcb(RoomCB rcb) {
		this.rcb = rcb;
	}

	public ServerSSListener getSsl() {
		return ssl;
	}

	public void setSsl(ServerSSListener ssl) {
		this.ssl = ssl;
	}

	public JComboBox getRoomsCB() {
		return roomsCB;
	}

	public void setRoomsCB(JComboBox roomsCB) {
		this.roomsCB = roomsCB;
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public JMenu getServerMenu() {
		return serverMenu;
	}

	public void setServerMenu(JMenu serverMenu) {
		this.serverMenu = serverMenu;
	}

	public JMenuItem getStopItem() {
		return stopItem;
	}

	public void setStopItem(JMenuItem stopItem) {
		this.stopItem = stopItem;
	}

	public JMenuItem getServerReportDump() {
		return serverReportDump;
	}

	public void setServerReportDump(JMenuItem serverReportDump) {
		this.serverReportDump = serverReportDump;
	}

	public UserCB getUcb() {
		return ucb;
	}

	public void setUcb(UserCB ucb) {
		this.ucb = ucb;
	}

	public JFrame getWindow() {
		return window;
	}

	public void setWindow(JFrame window) {
		this.window = window;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public UserInterfaceListener getUil() {
		return uil;
	}

	public void setUil(UserInterfaceListener uil) {
		this.uil = uil;
	}

	public GUILog getGLog() {
		return GLog;
	}

	public void setGLog(GUILog GLog) {
		this.GLog = GLog;
	}

	public JTextPane getUiLog() {
		return uiLog;
	}

	public void setUiLog(JTextPane uiLog) {
		this.uiLog = uiLog;
	}

	public JComboBox getUsersCB() {
		return usersCB;
	}

	public void setUsersCB(JComboBox usersCB) {
		this.usersCB = usersCB;
	}
	
}