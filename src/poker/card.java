package poker;

public class Card {
	private String suit;
	private String rank;

	
	Card(String suit, String rank){
		this.suit = suit;
		this.rank = rank;
	}
	
	public String getSuit(){
		return suit;
	}
	public String getRank(){
		return rank;
	}
	/*
	 * Used for testing. Remove later
	 */
	public String printCard(){
		return suit + " " + rank;
	}
}
