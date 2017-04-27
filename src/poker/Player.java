package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
import java.util.List;

public class Player implements PlayerInterface{
    private Card[] hand = new Card[2];
    private List<Card> rankingList = null;
    private HandRank handRank = null;
    private Card highCard = null;
    private int userId;
    private int gold;
    private int handsPlayed;
    private int handsWon;
    private String username;
    Boolean inGame = false;

    public Player(){

    }
    public Player(String name, int id, int gold, int handsWon, int handsPlayed){
        this.username = name;
        this.userId = id;
        this.gold = gold;
        this.handsWon = handsWon;
        this.handsPlayed = handsPlayed;
    }
    //getter for highCard
    public Card getHighCard(){ return highCard; }
    //setter for highCard
    public void setHighCard(Card highCard){ this.highCard=highCard; }
    //getter and setter for handRank
    public HandRank getHandRank(){return handRank;}
    public void setHandRank(HandRank handRank){this.handRank = handRank;}
    //getter for hand
    public Card[] getHand(){ return hand; }
    //setter for hand
    public void setHand(Card[] hand){ this.hand=hand; }
    //getter and setter for userId
    public int getUserId(){return userId;}
    public void setUserId(int userId){this.userId = userId;}
    //getter and setter for gold
    public int getGold(){return gold;}
    public void setGold(int gold){this.gold = gold;}
    //getter and setter for handsPlayed
    public int getHandsPlayed(){return handsPlayed;}
    public void setHandsPlayed(int handsPlayed){this.handsPlayed = handsPlayed;}
    //getter and setter for handsWon
    public int getHandsWon(){return handsWon;}
    public void setHandsWon(int handsWon){this.handsWon = handsWon;}
    //getter and setter for username
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    //boolean for whether in the game or not
    public void enterGame(){this.inGame = true;}
    public void exitGame(){this.inGame = false;}
    //getter and setter for ranking list
    public List<Card> getRankingList(){ return rankingList; }
    public void setRankingList(List<Card> rankingList){this.rankingList=rankingList;}
}
