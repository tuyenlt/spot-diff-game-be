package imggame.engine;

import imggame.models.User;

public class GameState {
	private int pointLeftToGuess;
	private Player player1;
	private Player player2;
	private int MAX_TIME_PER_GUESS = 30;
	private int MIN_POINT_PER_GUESS = 10;
	private int TIME_FACTOR = 10;
    private ImageSet imageSet;


	public GameState(Player p1, Player p2 , ImageSet imageSet){
        this.imageSet = imageSet;
		this.player1 = p1;
		this.player2 = p2;
		this.pointLeftToGuess = imageSet.getTotalDifferences();
        this.player1.isTurn = true;
	}

	public void resetTimer(){
		this.player1.timer = MAX_TIME_PER_GUESS;
		this.player2.timer = MAX_TIME_PER_GUESS;
	}

	public void switchTurn(){
		this.player1.isTurn = !this.player1.isTurn;
		this.player2.isTurn = !this.player2.isTurn;
	}

	public Player getCurrentPlayer(){
		if(this.player1.isTurn) return this.player1;
		else return this.player2;
	}

	public Player getWaitingPlayer(){
		if(this.player1.isTurn) return this.player2;
		else return this.player1;
	}

	public void  decreaseTimer(){
		if(this.player1.isTurn) this.player1.timer--;
		else this.player2.timer--;
	}

	public void calculateScore(){
		Player currentPlayer = this.getCurrentPlayer();
		int score = MIN_POINT_PER_GUESS + (currentPlayer.timer / MAX_TIME_PER_GUESS) * TIME_FACTOR;
		currentPlayer.score += score;
		this.pointLeftToGuess--;
	}

	public boolean isGameOver(){
		return this.pointLeftToGuess <= 0;
	}

	public User getWinner(){
		if(this.player1.score > this.player2.score) return this.player1.info;
		else if(this.player2.score > this.player1.score) return this.player2.info;
		else return null;
	}


    public User getLoser(){
        if(this.player1.score < this.player2.score) return this.player1.info;
        else if(this.player2.score < this.player1.score) return this.player2.info;
        else return null;
    }

    public void guessPoint(int x, int y) {
        if (this.imageSet.checkGuess(x, y)) {
            this.calculateScore();
        }
    }
	
} 
