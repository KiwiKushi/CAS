package org.kushi.alertBoxes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public abstract class StringInputBox implements ActionListener {
	
	private String displayText;
	private int size;
	
	// Thread
	private Thread displayBoxThread;
	
	// JFrame
	private JFrame displayBox;
	
	// JSwing Components
	private JLabel label = new JLabel();
	private JTextField dataInput;
	private JButton button;
	
	public StringInputBox(String displayText, int size) {
		this.displayText = displayText;
		this.size = size;
		
		this.button = new JButton("Enter");
		this.button.addActionListener(this);
		this.prompt();
	}
	
	public void prompt() {
		this.displayBoxThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Window Setup
				displayBox = new JFrame(displayText);
				displayBox.setVisible(true);
				displayBox.setSize(360, 200);
				displayBox.setLayout(null);
				displayBox.setResizable(false);
				displayBox.setAlwaysOnTop(true);
				displayBox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				// Component Setup
				label = new JLabel(displayText);
				label.setBounds(30, displayBox.getHeight()/2, displayText.length()*100, 20);
				displayBox.add(label);
				
				dataInput = new JTextField();
				dataInput.setBounds(displayBox.getWidth()/2, displayBox.getHeight()/2, size, 20);
				displayBox.add(dataInput);
				
				button.setBounds(displayBox.getWidth()/2+size, displayBox.getHeight()/2, 80, 20);
				displayBox.add(button);
			}
			
		});
		displayBoxThread.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.click(this.dataInput.getText());
	}
	
	public abstract void click(String data);
	
}