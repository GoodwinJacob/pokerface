package poker_client;

public class Card {
	//attributes of the cards
	private Suit suit;
	private Rank rank;

	//simple constructor for class
	Card(Suit suit, Rank rank){
		this.suit = suit;
		this.rank = rank;
	}


	//getter for suit
	public Suit getSuit(){
		return suit;
	}
	//getter for rank
	public Rank getRank(){
		return rank;
	}
	//returns the int for rank as a string
	public Integer rankInt(){
		return rank.ordinal();
	}
	//override equals and hashCode
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (!(obj instanceof Card)) {
			return false;
		} else {
			Card card2 = (Card) obj;
			return rank.equals(card2.getRank()) && suit.equals(card2.getSuit());
		}
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(String.valueOf(rank.ordinal())
				+ String.valueOf(suit.ordinal()));
	}


	public String printCard(){return suit.toString() + " " + rank.toString();}
}
