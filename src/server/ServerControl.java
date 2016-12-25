package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.text.BadLocationException;

public class ServerControl implements ActionListener{

	public static Thread runServer;
	public static Server server;
	
	@Override
	public void actionPerformed(ActionEvent source) {
		
		if(source.getSource() == ServerGUI.connect)
		{
			if(!ServerGUI.isRunning)
			{
				String host = ServerGUI.hostField.getText();
				int port = Integer.parseInt(ServerGUI.portField.getText());
				int maxUsers = Integer.parseInt(ServerGUI.maxUserField.getText());
				ServerGUI.isRunning = true;
				ServerGUI.connect.setText("Deconnect");
				ServerGUI.hostField.setEnabled(false);
				ServerGUI.portField.setEnabled(false);
				ServerGUI.maxUserField.setEnabled(false);
				
				server = new Server(port, maxUsers, host);
				
				runServer = new Thread(new Runnable() {
					
					@Override
					public void run() {
						server.serverRun();	
					}
				});
				runServer.start();
			}
			else
			{
				ServerGUI.isRunning = false;
				ServerGUI.connect.setText("Connect");
				ServerGUI.hostField.setEnabled(true);
				ServerGUI.portField.setEnabled(true);
				ServerGUI.maxUserField.setEnabled(true);
				try {
					server.connexion.close();
				} catch (IOException e) {
					
				}
				server = null;
				runServer = null;
			}
			
		}
		else if (source.getSource() == ServerGUI.block) {
			
			String selectedPseudo = ServerGUI.userList.getSelectedValue();
			int index = ServerGUI.userList.getSelectedIndex();
			int i = server.blockClient(selectedPseudo);
				try {
					if(i==1) 
					{
						ServerGUI.write("Client "+selectedPseudo+" removed successfuly", "log");
						ServerGUI.userList.remove(index);
						notifyAll();
					}
					else ServerGUI.write("Client "+selectedPseudo+" client not removed!", "error");
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			
		}
		
		
	}
	
}
