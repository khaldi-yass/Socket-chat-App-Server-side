package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *  L'interface du serveur, rien de speciale ici
 */
public class ServerGUI extends JFrame{


	private static final long serialVersionUID = 1L;
	public static DefaultListModel<String> model = new DefaultListModel<>();
	public static JList<String> userList;
	public static JButton connect;
	public static JButton block;
	public static JTextField hostField;
	public static JTextField portField;
	public static JTextField maxUserField;
	public static JTextPane log;
	public static StyledDocument logDoc;
	public static SimpleAttributeSet style;
	public static boolean isRunning=false;
	
	public ServerGUI() {
		setTitle("Server");
		setSize(525,355);
		setMinimumSize(new Dimension(525,355));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
	}
	public static void main(String[] args) {
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {}
		
		ServerGUI fen = new ServerGUI();
		JPanel container = new JPanel();
		container.setLayout(null);
		fen.setContentPane(container);
		
		ScrollPane sp = new ScrollPane();
		JPanel connectionPanel = new JPanel();
		JLabel hostLabel = new JLabel("Host:");
		JLabel portLabel = new JLabel("Port:");
		JLabel maxUserLabel = new JLabel("Max Users:");
		hostField = new JTextField("localhost");
		portField = new JTextField("1500");
		maxUserField = new JTextField("10");
		connect = new JButton("Connect");
		userList = new JList<>(model);
		block = new JButton("Block user");
		log = new JTextPane();
		logDoc = log.getStyledDocument();
		style = new SimpleAttributeSet();
		
		//---------------------------------------------------------
		connectionPanel.setLayout(null);
		connectionPanel.setBounds(275, 20, 220, 210);
		connectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Connection Area"));
		hostLabel.setBounds(20, 42, 50, 15);
		portLabel.setBounds(20, 78, 50, 15);
		maxUserLabel.setBounds(20, 111, 50, 15);
		hostField.setBounds(100, 42, 100, 20);
		portField.setBounds(100, 78, 100, 20);
		maxUserField.setBounds(100, 111, 100, 20);
		connect.setBounds(100, 155, 100, 25);
		userList.setBounds(20, 20, 225, 175);
		userList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		block.setBounds(88, 205, 100, 25);
		sp.setBounds(20, 240, 475, 60);
		log.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		log.setEditable(false);
		//--------------------------------------------------------
		
		
		container.add(userList);
		container.add(block);
		sp.add(log);
		container.add(sp);
		container.add(connectionPanel);
		connectionPanel.add(hostLabel);
		connectionPanel.add(portLabel);
		connectionPanel.add(maxUserLabel);
		connectionPanel.add(hostField);
		connectionPanel.add(portField);
		connectionPanel.add(maxUserField);
		connectionPanel.add(connect);
		
		//----------------------------------------------------------
		ServerControl svc = new ServerControl();
		connect.addActionListener(svc);
		block.addActionListener(svc);
		
		//----------------------------------------------------------
		fen.setVisible(true);
	}
	
	/**
	 * 
	 * @param text : le texte a ecrire
	 * @param type : affiche le texte sous differents syples selon le type(log - error)
	 * @throws BadLocationException : une exception generee par la methode insertString
	 */
	public static void write(String text, String type) throws BadLocationException
	{
		SimpleDateFormat time = new SimpleDateFormat("d-M-Y HH:mm:ss");
		if(type.equals("log"))
		{
			StyleConstants.setForeground(ServerGUI.style, Color.BLUE);
			StyleConstants.setBold(ServerGUI.style, true);
			logDoc.insertString(logDoc.getLength(), time.format(new Date())+": "+text+"\n", ServerGUI.style);
		}
		else if(type.equals("error"))
		{
			StyleConstants.setForeground(ServerGUI.style, Color.red);
			StyleConstants.setBold(ServerGUI.style, true);
			logDoc.insertString(logDoc.getLength(), time.format(new Date())+": "+text+"\n", ServerGUI.style);
		}
	}
}
