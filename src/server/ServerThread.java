package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.text.BadLocationException;

import message.Message;

public class ServerThread extends Thread{
	
	Server server;
	public Socket client;
	SimpleDateFormat time;
	public ObjectOutputStream output;
	public ObjectInputStream input;
	Message message;
	private static AtomicInteger nextId = new AtomicInteger();
	public int id;
	public String pseudo;
	
	public ServerThread(Socket client, Server server) {
		this.client=client;
		this.server=server;
		time=new SimpleDateFormat("d-M-Y HH:mm:ss");
		this.id=nextId.incrementAndGet();
	}
	
	@Override
	public void run() {
		super.run();
		
		try 
		{
			input = new ObjectInputStream(client.getInputStream());
			output = new ObjectOutputStream(client.getOutputStream());
			output.flush();
			
			//sending id to client
			output.writeObject(id);
			output.flush();
			
			//receiving pseudo from client
			pseudo = (String) input.readObject();
			
			//sending success message to client
			Message succ = new Message(pseudo, Message.TEXT_TYPE, "You connected successfully\nPseudo: "+pseudo);
			output.writeObject(succ);
			output.flush();
			do
			{
				message = (Message) input.readObject();
				ServerControl.server.sendToAll(message, id);
				
			}while(!client.isClosed());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			try {
				ServerGUI.write("Data received in unknown format\n Error: "+e.getMessage(), "error");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		} finally {
			try 
			{
				client.close();
				output.close();
				input.close();
				int rm = server.removeClient(id);
					try {
						
						if(rm == -1)
						{
							ServerGUI.write("Client not removed from array!", "error");
						}
						else {
							ServerGUI.write("Client: "+pseudo+" disconnected!", "error");
						}
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
