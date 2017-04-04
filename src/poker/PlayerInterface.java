package poker;

/**
 * Created by jacobgoodwin on 3/28/17.
 */
import java.util.List;

public interface PlayerInterface {
    public Card[] getHand();
    public void setHand(Card[] hand);
    public List<Card> getRankingList;
    public void setRankingList;
    public Card getHighCard();
    public void setHighCard(Card highCard);

    //add databaseFunctions later
}
