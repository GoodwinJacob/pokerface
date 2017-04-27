package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
import java.util.List;

public class Player implements PlayerInterface{
    private Card[] hand = new Card[2];
    private List<Card> rankingList = null;
    private Card highCard = null;
    private int userId;
    private int gold;
    private int handsPlayed;
    private int handsWon;
    private String username;
    Boolean inGame = false;


    //getter for highCard
    public Card getHighCard(){ return highCard; }
    //setter for highCard
    public void setHighCard(Card highCard){ this.highCard=highCard; }
    //getter for hand
    public Card[] getHand(){ return hand; }
    //setter for hand
    public void setHand(Card[] hand){ this.hand=hand; }
    public int getUserId(){return userId;}
    public void setUserId(int userId){this.userId = userId;}
    public int getGold(){return gold;}
    public void setGold(int gold){this.gold = gold;}
    public int getHandsPlayed(){return handsPlayed;}
    public void setHandsPlayed(int handsPlayed){this.handsPlayed = handsPlayed;}
    public int getHandsWon(){return handsWon;}
    public void setHandsWon(int handsWon){this.handsWon = handsWon;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public void enterGame(){this.inGame = true;}
    public void exitGame(){this.inGame = false;}
    public List<Card> getRankingList(){ return rankingList; }
    public void setRankingList(List<Card> rankingList){this.rankingList=rankingList;}
}
