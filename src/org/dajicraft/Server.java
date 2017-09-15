package org.dajicraft;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Parameter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.dajicraft.ranks.User;
import org.dajicraft.room.Room;
import org.dajicraft.ui.UserInterface;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kushi.cryptography.Crypter;
import org.dajicraft.chatOperations.OperatorCommand;
import org.dajicraft.chatOperations.RoomOperatorCommands;
import org.dajicraft.chatOperations.UserCommands;
import org.dajicraft.log.Log;
import org.dajicraft.ranks.Operator;

public class Server {
	
	private static Log log = new Log(">> ", "[HH:mm:ss] ");
	private static UserInterface ui;
	private static ArrayList<User> users = new ArrayList<User>();
	private static ArrayList<Room> rooms = new ArrayList<Room>();
	private static ServerPriliminary serverPrilim;
	private static boolean listening = true;
	private static ServerSocket serverSock;
	private static int port;
	
	private static String code = "Kushi";
	
	public static void setGUI(UserInterface userInterface) {
		ui = userInterface;
	}
	
	public static ArrayList<User> getAllUsers() {
		return users;
	}
	
	public static void removeUser(User user) {
		users.remove(user);
	}
	
	public static void addRoom(Room room) {
		rooms.add(room);
	}
	
	public static void removeRoom(Room room) {
		rooms.remove(room);
	}
	
	public static ArrayList<Room> getAllRooms() {
		return rooms;
	}
	
	
	
	
	public static ServerPriliminary getServerPrilim() {
		return serverPrilim;
	}

	public static void setServerPrilim(ServerPriliminary serverPrilim) {
		Server.serverPrilim = serverPrilim;
	}

	public void shutdown() {
		for(Room room: this.rooms) this.broadcast("The server is shutting down. You will be disconnected", room);
		for(User user: this.users) {
			try {
				user.getSocket().close();
			} catch (IOException e) {
				user.setSocket(null);
			}
		}
		System.exit(0);
	}
	
	public static boolean broadcast(String msg, Room room) {
		for(User ruser: users) {
			if(room.getName().equals(ruser.getRoom().getName())) {
				ruser.alert(msg, ruser);
			}
		}
		return false;
	}
	
	
	private static String createNewName() {
		Random rand = new Random();
		String name = "Guest"+rand.nextInt(9999 - 1) + 1;
		for(User currentUser: users) {
			if(currentUser.getName().equals(name)) {
				return createNewName();
			}
		}
		return name;
	}
	
	
	public static Log getLog() {
		return log;
	}

	public static void setLog(Log log) {
		Server.log = log;
	}

	public static UserInterface getUi() {
		return ui;
	}

	public static void setUi(UserInterface ui) {
		Server.ui = ui;
	}

	public static ArrayList<User> getUsers() {
		return users;
	}

	public static void setUsers(ArrayList<User> users) {
		Server.users = users;
	}

	public static ArrayList<Room> getRooms() {
		return rooms;
	}

	public static void setRooms(ArrayList<Room> rooms) {
		Server.rooms = rooms;
	}

	public static String getCode() {
		return code;
	}

	public static void setCode(String code) {
		Server.code = code;
	}
	
	
	// Server Joining Connection Handling
	
	public static void listenForUsers() {
		port = 100;
		try {
			serverSock = new ServerSocket(port);
			log.log("Server Bound To "+port);
			ui.getGLog().log("Server Bound To "+port);
			listening = true;
		} catch (IOException e1) {
			try {
				log.log("Could not rebind port.");
				serverSock.close();
				log.log("Port rebinded.");
				serverSock = new ServerSocket(port);
			} catch (IOException e) {
				log.log("Could not close the ServerSocket.");
			}
		}
		
		while(listening) {
			try {
				sessionHandler(serverSock.accept());
			} catch (IOException e) {
				log.log("Dropped Connection on Entry!");
				ui.getGLog().log("Dropped Connection on Entry!");
			}
		}
	}
	
	
	public static void sentAlertFromServer(User user, String data) {
		
		try {
			user.getBufferedWriter().write(Server.getLog().format(data));
			user.getBufferedWriter().flush();
		} catch (IOException e) {
			Server.getLog().log(Server.getLog().format("I/O with "+user.getName()+" in "+user.getRoom().getName()));
		}
		
		Server.getUi().getGLog().log(Server.getLog().format(data));
		Server.getLog().log(Server.getLog().format(data));
	}
	

	
	
	
	public static boolean isListening() {
		return listening;
	}

	public static void setListening(boolean listening) {
		Server.listening = listening;
	}

	public static ServerSocket getServerSock() {
		return serverSock;
	}

	public static void setServerSock(ServerSocket serverSock) {
		Server.serverSock = serverSock;
	}

	public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Server.port = port;
	}

	public static void leave(User user) {
		user.setSessoin(false);
		try {
			user.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		users.remove(user);
		ui.getUcb().updateUsers();
		user.getRoom().removeUser(user);
		if(user.isRoomOperator()) {
			try {
				User newRoomOperator = user.getRoom().getRoomUsers().get(0);
				newRoomOperator.setRoomOperator(true);
				newRoomOperator.setPrefix("["+newRoomOperator.getRoom().getDisplay()+" Operator] ");
				
				ui.getGLog().log((newRoomOperator.getName()+" has been made Operator of the Room"));
				broadcast(newRoomOperator.getName()+" has been made Operator of the Room", user.getRoom());
				
			} catch(IndexOutOfBoundsException e) {
				System.out.println(user.getRoom().getName()+" has been deleted");
			}
		}
		
		user.getRoom().removeUser(user);
		rooms.remove(user.getRoom());
		
		log.log(user.getPrefix()+user.getName()+user.getSuffix()+" in room: "+user.getRoom().getName()+"/"+user.getRoom().getDisplay()+" has left.");
		broadcast(user.getPrefix()+user.getName()+user.getSuffix()+" in room: "+user.getRoom().getName()+"/"+user.getRoom().getDisplay()+" has left.", user.getRoom());
		ui.getGLog().log(user.getPrefix()+user.getName()+user.getSuffix()+" in room: "+user.getRoom().getName()+"/"+user.getRoom().getDisplay()+" has left.");
	}
	
	public static void destroyOperator(Operator op) {
		op.setSessoin(false);
		try {
			op.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Operator "+op.getName()+" has been destroyed");
	}
	
	
	
	public static boolean sessionHandler(Socket sock) {
		log.log("User Has Connected Running Through Setup");
		ui.getGLog().log("User Has Connected Running Through Setup");
		BufferedWriter out = null;
		

		
		
		
		

		
		
		Thread userThread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				String ip = sock.getInetAddress().toString();
				ip = ip.substring(1, ip.length());
				
				BufferedReader in = null;
				BufferedWriter out = null;
				try {
					String username = null;
					String roomName = null;
					String opCode = null;
					String timeStamp = new SimpleDateFormat(
							"HH:mm::ss").format(new Date());
					
					
					
					boolean isRoomOperator = false;
					/*
					 * in and out are the input and output
					 * of this temp account
					 */
					
					
					in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
					/*
					 * reading the client info needs to be changed with a time out and
					 * a full info read instead of this trash that is only for developement
					 */
					
					Crypter crypter = new Crypter(1024);
					
					JSONObject cryptoIntroPacket = new JSONObject();
					cryptoIntroPacket.put("messageType", "CIP");
					cryptoIntroPacket.put("privateKey", new String(Base64.getEncoder().encode(crypter.getPrivateKey().getEncoded())));
					cryptoIntroPacket.put("publicKey", new String(Base64.getEncoder().encode(crypter.getPublicKey().getEncoded())));
					
					out.write(cryptoIntroPacket.toJSONString()+"\n");
					out.flush();
					
					String line;
					sock.setSoTimeout(4000);
					
					
					try {
						JSONParser parser = new JSONParser();
						try {
							
							line = in.readLine();
							
							JSONObject introductionPacket = (JSONObject) parser.parse(new String(crypter.decrypt(Base64.getDecoder().decode(new String(line.getBytes(), "UTF-8"))), "UTF-8"));
							/*
							 * Yay, we are not still screwing around here!
							 * Our almighty savior JSON has come!
							 * Thou no longer hath use dreaded, "from scratch," parsers!
							 */
							
							if(!introductionPacket.containsKey("Username") && !introductionPacket.containsKey("Room") && !introductionPacket.containsKey("OAC") && !introductionPacket.containsKey("TimeStamp")) {
								log.log("User tried to join with malformatted introduction packet.");
								ui.getGLog().log("User tried to join with malformatted introduction packet.");
								return;
							}
							
							username = (String) introductionPacket.get("Username").toString();
							roomName = (String) introductionPacket.get("Room").toString();
							opCode = (String) introductionPacket.get("OAC").toString();
							timeStamp = (String) introductionPacket.get("TimeStamp").toString();
							
							
						} catch (ParseException e) {
							log.log("User tried to join with malformatted introduction packet.");
							ui.getGLog().log("User tried to join with malformatted introduction packet.");
							return;
						}
						
					} catch(SocketException e) {
						log.log("Connection dropped.");
						ui.getGLog().log("Connection dropped.");
						
						return;
					} catch(SocketTimeoutException e) {
						log.log("Connection dropped.");
						ui.getGLog().log("Connection dropped.");
						return;
					}
					
					if(username.contains(" ")) {
						try {
							username = username.split(" ")[0];
						} catch(IndexOutOfBoundsException e) {
							out.write("There was an error with your name.");
							out.flush();
							username = createNewName();
							log.log((ip+" has tried to join with a malformed name."));
						}
					}
					
					Room room = new Room(roomName, new ArrayList<String>());
					
					boolean roomExist = false;
					for(Room currentRoom: rooms) {
						if(currentRoom.getName().equals(room.getName())) {
							room = currentRoom;
							roomExist = true;
						}
					}
					
					
					if(!roomExist) {
						isRoomOperator = true;
						rooms.add(room);
					}
					
					/* 
					 * Checks for network side cloning needs to be removed
					 */
					for(User currentUser: users) {
						if(currentUser.getName().equals(username) && roomName.equals(currentUser.getRoom().getName())) {
							username = createNewName();
							out.write("Someone already has that name you are now "+username+"\n");
							out.flush();
						}
					}
					
					sock.setSoTimeout(Integer.MAX_VALUE);
					
					// Checking if the System Operator Code is right
					if(opCode.length() == code.length()) {
						if(opCode.substring(0, code.length()).equals(code)) {
							
							
							//  This person is a SysOp
							Operator sysOp = new Operator(username, room, sock,timeStamp, in, out, true);
							// But also a user
							User opUser = new User(username, room, sock,timeStamp, in, out, true);
							// This adds it into the server user list
							users.add(opUser);
							sysOp.setPrefix("[System Operator] ");
							
							ui.getUcb().updateUsers();
							/*
							 * System Operators Session
							 */
							while(sysOp.getSession()) {
								try {
									log.log("[System Operator] "+username+" has joined "+room.getDisplay()+"/"+room.getName());
									ui.getGLog().log("[System Operator] "+username+" has joined "+room.getDisplay()+"/"+room.getName());
									opUser.broadcast(opUser.getPrefix()+opUser.getName()+opUser.getSuffix()+" has joined "+room.getDisplay(), opUser);
									String msg;
									while(true) {
										String messagePacket = sysOp.getBufferedReader().readLine();
										String rawMsg = "";
										JSONObject messagePacketJSONObject;
										JSONParser msgParser = new JSONParser();
										try {
											messagePacketJSONObject = (JSONObject) msgParser.parse(messagePacket);
											
											if(messagePacketJSONObject.containsKey("data") &&
													messagePacketJSONObject.containsKey("messageType") &&
													messagePacketJSONObject.containsKey("timeStamp")) {
												
												
												
												// This is for normal text messages
												if(messagePacketJSONObject.get("messageType").equals("text")) {
													rawMsg = messagePacketJSONObject.get("data").toString();
													/*
													 * Checking if it's in command format
													 */
													
													if(rawMsg.substring(0, 1).equals("/")) {
														// Runs command execution
														new OperatorCommand(rawMsg).run(opUser);
													} else {
														/*
														 * Formats message and sends to all connected users
														 */
														
														msg = "["+new SimpleDateFormat("mm:ss").format(new Date())+"] "+sysOp.getPrefix()+sysOp.getName()+sysOp.getSuffix()+
																" >> "+rawMsg;
														sysOp.broadcast(messagePacket, opUser);
														ui.getGLog().log(msg);
														msg = "";
													
													}
												} else if(messagePacketJSONObject.get("messageType").equals("image")) {
													
													/*
													 * What?! How do you know about this?!
													 * This was supposed to be top secret!
													 */
													
													byte[] imageByteArray = Base64.getDecoder().decode(messagePacketJSONObject.get("data").toString());
													BufferedImage bufferedImage;
													InputStream inputStream = new ByteArrayInputStream(imageByteArray);
													bufferedImage = ImageIO.read(inputStream);
													System.out.println("Hello");
													
												}
											}
												
											
										} catch (ParseException e) {
											ui.getGLog().log(log.format(sysOp.getName()+" in "+sysOp.getRoom().getName()+" tried to send a malformatted message packet."));
											log.log(log.format(sysOp.getName()+" in "+sysOp.getRoom().getName()+" tried to send a malformatted message packet."));
											continue;
										}
									}
								} catch (IOException e) {
									destroyOperator(sysOp);
									leave(opUser);
								} catch(StringIndexOutOfBoundsException e) {
									// Just here so the 
								}
							}
						}
					} else {
						// Creating the user
						User newUser = new User(username, room, sock,timeStamp, in, out, true);
						newUser.setCrypter(crypter);
						// Adding into the server user list
						users.add(newUser);
						newUser.getRoom().addUser(newUser);
						if(isRoomOperator) {
							newUser.setRoomOperator(true);
							newUser.setPrefix("["+newUser.getRoom().getDisplay()+" Operator] ");
						}
						
						ui.getUcb().updateUsers();
						while(newUser.getSession()) {
							try {
								ui.getGLog().log(username+" has joined "+room.getDisplay()+"/"+room.getName());
								log.log(username+" has joined "+room.getDisplay()+"/"+room.getName());
								newUser.broadcast(newUser.getPrefix()+newUser.getName()+newUser.getSuffix()+" has joined "+room.getDisplay(), newUser);
								String msgPacket;
								String rawMsg;
								String msg;
								
								
								
								while(true) {
									msgPacket = new String(newUser.getCrypter().decrypt(Base64.getDecoder().decode(newUser.getBufferedReader().readLine().getBytes())), "UTF-8");
									
									JSONParser msgPacketParser = new JSONParser();
									try {
										JSONObject messagePacketJSONObject = (JSONObject) msgPacketParser.parse(msgPacket);
										System.out.println(msgPacket);
										if(messagePacketJSONObject.containsKey("data") &&
												messagePacketJSONObject.containsKey("messageType")) {
											// This is for normal text messages
											if(messagePacketJSONObject.get("messageType").equals("text")) {
												rawMsg = messagePacketJSONObject.get("data").toString();
												
												/*
												 * Checking if it's in command format
												 */
												
												if(rawMsg.substring(0, 1).equals("/")) {
													// Runs command execution
													new UserCommands(rawMsg, newUser).run();
												} else {
													/*
													 * Formats message and sends to all connected users
													 */
													
													newUser.broadcast(messagePacketJSONObject.get("data").toString(), newUser);
													ui.getGLog().log(log.format(messagePacketJSONObject.get("data").toString()));
													msg = "";
												
												}
											} else if(messagePacketJSONObject.get("messageType").equals("image")) {
												
												/*
												 * What?! How do you know about this?!
												 * This was supposed to be top secret!
												 */
												byte[] imageByteArray = Base64.getDecoder().decode(messagePacketJSONObject.get("data").toString());
												BufferedImage bufferedImage;
												InputStream inputStream = new ByteArrayInputStream(imageByteArray);
												bufferedImage = ImageIO.read(inputStream);
												
												if(bufferedImage.getHeight() <= 1080  || bufferedImage.getWidth() <= 1920) {
													
													JSONObject imageMessagePacket = new JSONObject();
													imageMessagePacket.put("messageType", "image");
													imageMessagePacket.put("data", messagePacketJSONObject.get("data").toString());
													imageMessagePacket.put("format", messagePacketJSONObject.get("format"));
													imageMessagePacket.put("name", newUser.getPrefix()+newUser.getName()+newUser.getSuffix());
													
													for(User user: users) {
														if(user.getRoom().equals(newUser.getRoom()) && !user.equals(newUser)) {
															user.getBufferedWriter().write(new String(Base64.getEncoder().encode(newUser.getCrypter().encrypt(imageMessagePacket.toJSONString())), "UTF-8")+"\n");
															user.getBufferedWriter().flush();
															user.alert(user.getName()+" has sent a picture. Use the image menu to open it.", user);
														}
													}
													newUser.alert("Your image has been sent.", newUser);
												}
											}											
										}
									} catch (ParseException e) {
										ui.getGLog().log(log.format(newUser.getName()+" in "+newUser.getRoom().getName()+" tried to send a malformatted message packet."));
										log.log(log.format(newUser.getName()+" in "+newUser.getRoom().getName()+" tried to send a malformatted message packet."));
										continue;
									}
									
									
									
								}
							} catch (IOException e) {
								/*
								 * Runs everything needed to stop
								 * the connection
								 */
								leave(newUser);
							} catch(NullPointerException e) {
								leave(newUser);
								return;
							}
						}
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		// Starts the Clients handling in a Thread
		userThread.start();
		return true;
	}
}