package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck implements DeckInterface{
        //ArrayList of 52 cards
        ArrayList<Card> arrayOfCards = new ArrayList<Card>;

        private Random random = new Random();

        /*
         * @brief Initialize the deck and then shuffle it.
         */
        Deck(){
            this(new Random());
            //printDeck(); //Uncomment to print deck to console
        }
        Deck(Random random) {
            this.random = random;
            initializeDeck();
            shuffleDeck();
        }

        /*
         * @brief Used to fill the a deck with cards.
         * Cards are not shuffled.
         */
        public void initializeDeck(){
            for(Suit suit : Suit.values()){
                for(Rank rank : Rank.values()){
                    arrayOfCards.add(new Card(suit, rank));
                }
            }
        }
        public void shuffleDeck(){
            Collections.shuffle(arrayOfCards);
        }
        public Card pop() {
            return arrayOfCards.remove(random.nextInt(arrayOfCards.size()))
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
}
