package message;

import java.io.Serializable;

public class Message implements Serializable{
	
	public static final int TEXT_TYPE = 1;
	public static final int IMAGE_TYPE = 2;
	public static final int AUDIO_TYPE = 3;
	public static final int WIZZ_TYPE = 4;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sourceClientpseudo;
	int type;
	Object cont;
	
	public Message(String pseudo, int type, Object cont) {	
		this.sourceClientpseudo = pseudo;
		this.type = type;
		this.cont = cont;
	}
	
	public String show()
	{
		if(this.type == TEXT_TYPE)
		{
			return (String) cont;
		}
		else if(this.type == IMAGE_TYPE)
		{
			return (String) cont;
		}
		else if(this.type == AUDIO_TYPE)
		{
			return "Audio message";
		}
		else if(this.type == WIZZ_TYPE)
		{
			return "Wizz message";
		}
		return "Message";
	}

	public String getSourceClientPseudo()
	{
		return this.sourceClientpseudo;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	public Object getCont()
	{
		return this.cont;
	}
	
	
}

class Wizz
{
	public Wizz() {
		
	}
}
