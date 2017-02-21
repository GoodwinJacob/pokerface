package poker;

public class Card {
	//attributes of the cards
	private short suit;
	private short rank;
	//static arrays for suits and ranks to initialize the cards for the deck
	private static String[] suits = { "hearts", "diamonds", "spades", "clubs"};
	private static String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "Jack", "Queen", "King"};
	//simple constructor for class
	Card(short suit, short rank){
		this.suit = suit;
		this.rank = rank;
	}
	//returns the int for rank as a string
	public static String rankString(int newRank){
		return ranks[newRank];
	}
	//getter for suit
	public short getSuit(){
		return suit;
	}
	//getter for rank
	public short getRank(){
		return rank;
	}
}
