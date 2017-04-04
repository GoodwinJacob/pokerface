package poker;

public class Card {
	//attributes of the cards
	private String suit;
	private String rank;

	//simple constructor for class
	Card(String suit, String rank){
		this.suit = suit;
		this.rank = rank;
	}
	//returns the int for rank as a string
	public static String rankString(int newRank){
		return ranks[newRank];
	}

	//getter for suit
	public String getSuit(){
		return suit;
	}
	//getter for rank
	public String getRank(){
		return rank;
	}
	public String printCard(){return suit + " " + rank;}
}
