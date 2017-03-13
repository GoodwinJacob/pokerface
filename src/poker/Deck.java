package poker;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	//ArrayList of 52 cards
	ArrayList<Card> arrayOfCards = new ArrayList<Card>(52);
	private static String[] suits = { "hearts", "diamonds", "spades", "clubs"};
	private static String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "Jack", "Queen", "King"};
	
	/*
	 * @breif Initialize the deck and then shuffle it. 
	 */
	Deck(){
		initializeDeck();
		shuffleDeck();
		//printDeck(); //Uncomment to print deck to console
	}
	/*
	 * @brief Used to fill the a deck with cards.
	 * Cards are not shuffled.  
	 */
	public void initializeDeck(){
		for(int i = 0; i < suits.length; i++){
			for(int j = 0; j < ranks.length; j++){
				arrayOfCards.add(new Card(suits[i], ranks[j]));
			}
		}
	}
	public void shuffleDeck(){
		Collections.shuffle(arrayOfCards);
	}
	/*
	 * @brief Used to print the deck. Used for testing. 
	 */
	public void printDeck(){
		for(int i=0; i < arrayOfCards.size(); i++){
			System.out.println(arrayOfCards.get(i).getSuit() + "  " + arrayOfCards.get(i).getRank() );
		}
	}
}
