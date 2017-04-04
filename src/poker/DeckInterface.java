package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
public interface DeckInterface {
    public Card pop();
    public void printDeck();
    public void shuffleDeck();
    public void initializeDeck();

}
