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
    public int getUserId();
    public void setUserId(int userId);
    public int getGold();
    public void setGold(int gold);
    public int getHandsPlayed();
    public void setHandsPlayed(int handsPlayed);
    public int getHandsWon();
    public void setHandsWon(int handsWon);
    public String getUsername();
    public void setUsername(String username);
    public void enterGame();
    public void exitGame();

    //add databaseFunctions later
}
