package poker;

public class card {
	private short suit;
	private short rank;
	private static String[] suits = { "hearts", "diamonds", "spades", "clubs"};
	private static String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "Jack", "Queen", "King"};

	
	
	public short getSuit(){
		return suit;
	}
	public short getRank(){
		return rank;
	}
}
