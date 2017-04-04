package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
import java.util.List;

public class PLayer implements PlayerInterface{
    private Card[] hand = new Card[2];
    private List<Card> rankingList = null;
    private Card highCard = null;
    //getter for highCard
    public Card getHighCard(){ return highCard; }
    //setter for highCard
    public void setHighCard(Card highCard){ this.highCard=highCard; }
    //getter for hand
    public Card[] getHand(){ return hand; }
    //setter for hand
    public void setHand(Card[] hand){ this.hand=hand; }
    public List<Card> getRankingList(){ return rankingList; }
    public void setRankingList(List<Card> rankingList){this.rankingList=rankingList;}
}
