package imggame.engine;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import imggame.models.User;

public class Player {
	public User info;
	public int score;
	public ObjectInputStream is;
	public ObjectOutputStream os;
	public boolean isTurn;
	public int timer;

	public Player(User user, ObjectInputStream is, ObjectOutputStream os){
		this.info = user;
		this.is = is;
		this.os = os;
		this.isTurn = false;
		this.score = 0;
	}
}

