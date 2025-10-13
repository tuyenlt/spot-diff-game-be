
package imggame.models;

public class User {
	private String id;
	private String username;
	private String email;
	private String password;
	private int score;
	private int elo;

	public User(String id, String username, String email, String password, int score, int elo) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.score = score;
		this.elo = elo;
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.score = 0;
		this.elo = 1000;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public int getScore() {
		return score;
	}

	public int getElo() {
		return elo;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setElo(int elo) {
		this.elo = elo;
	}

}
