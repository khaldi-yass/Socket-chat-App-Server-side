package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.text.BadLocationException;

import message.Message;

public class Server {

	public ArrayList<ServerThread> userList;
	static int maxUsers;
	public int port;
	static String host;
	Socket client;
	public ServerSocket connexion;
	
	/**
	 * Initialise le serveur avec les champs:
	 * @param port : numero de port
	 * @param max : nombre max d'utilisateurs
	 */
	public Server(int port, int max) {
		this(port, max, "localhost");
	}
	
	/**
	 * Initialise le serveur avec les champs:
	 * @param po : numero de port
	 * @param max : nombre max d'utils
	 * @param ho : nom du host
	 */
	public Server(int po, int max, String ho) {
		port=po;
		maxUsers=max;
		host=ho;
		userList = new ArrayList<ServerThread>();
	}
	
	
	public void serverRun()
	{
		
		try {
			// creons une nouvelle connexion serverSocket
			connexion = new ServerSocket(port,maxUsers,InetAddress.getByName(host));
			//utiliser annuler le TimeOut de l'adress apres deconnexion
			connexion.setReuseAddress(true);
			//ecrire un message 
			ServerGUI.write("Server connected: \nPort: "+port+"\nMaximum users: "+maxUsers, "log");
			
			//boucle infinie qui ecoute
			while (true) {			
				client = null;
				// verifions si le nombre max est atteints
				if(userList.size() < maxUsers)
				{
					ServerGUI.write("Waiting...", "log");
					//methode accept attend un client
					client = connexion.accept();
					//si un client se connecte on l'ajoute dans la liste et l'envoie dans un thread pour se connecter avec
					ServerGUI.write("New client connected: "+client.getInetAddress().getHostName(), "log");
					ServerThread process = new ServerThread(client,this);
					userList.add(process);
					process.start();
					Thread.sleep(1000);
					ServerGUI.model.addElement(process.pseudo);
				}
				else
				{
					//si le serveur est plein il attend un notify des autres threads connectes
					ServerGUI.write("full room! Next attempt in 5 secs", "error");
					Thread.sleep(5000);
				}
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		// le serveur se ferme en fermant la fenetre ou cliquant sur deconnecter
		
	}
	
	/**
	 * Methode qui sert a enlever un client selon so id
	 * @param id : id du client a enlever
	 * @return : un entier(1 success ; -1 failure)
	 */
	public synchronized int removeClient(int id)
	{
		for (Iterator<ServerThread> iterator = userList.iterator(); iterator.hasNext();) {
			ServerThread client = iterator.next();
			
			if(client.id == id)
			{
				iterator.remove();
				return 1;
			}
		}
		return -1;
	}
	
	/**
	 * Methode qui sert a enlever un client selon so pseudo
	 * @param pseudo : pseudo du client a enlever
	 * @return : un entier(1 success ; -1 failure)
	 */
	public synchronized int blockClient(String pseudo)
	{
		for (Iterator<ServerThread> iterator = userList.iterator(); iterator.hasNext();) {
			ServerThread client = iterator.next();
			
			if(client.pseudo.equals(pseudo))
			{
				iterator.remove();
				ServerGUI.model.removeElementAt(ServerGUI.userList.getSelectedIndex());
				try {
					client.client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return 1;
			}
		}
		return -1;
	}
	
	/**
	 * Envoie a tous les clients
	 * @param msg : message a envoyer
	 * @param exc : celui qui envoie ne recevra pas le message encore une fois
	 */
	public synchronized void sendToAll(Message msg, int exc)
	{
		try
		{
			for(ServerThread client : userList)
			{
				if(client.id != exc)
				{
					client.output.writeObject(msg);
					client.output.flush();
				}
			}
	    } catch(IOException ioException){
	        ioException.printStackTrace();
	    }
	}
	
}
