package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.sql.*;

public class PokerGame {
    private DeckInterface deck;
    private List<PlayerInterface> players;
    private List<Card> tableCards;
    private int pot;
    private int buyin;

    public List<Card> getTableCards(){return tableCards;}

    public void newGame(DeckInterface deck, int buyin, PlayerInterface player1, PlayerInterface... _players){
        this.deck = deck;
        this.buyin = buyin;
        int total = 0;
        tableCards = new ArrayList<Card>();
        players = new ArrayList<PlayerInterface>();
        players.add(player1);
        players.addAll(Arrays.asList(_players));
        try {
            //connection to database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/java", "root", "root");
            String sql = "";
            for (PlayerInterface p : players) {
                int id = p.getUserId();
                int userGold = p.getGold();
                userGold = userGold - buyin;
                sql = "UPDATE users SET gold = ? WHERE userid = ?";
                PreparedStatement prepStatement = conn.prepareStatement(sql);
                prepStatement.setInt(1, userGold);
                prepStatement.setInt(2, id);
                prepStatement.executeUpdate();
                //increase pot
                total = total + buyin;
            }
            conn.close();
        }
        catch(Exception e){
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }
        setPot(total);
    }
    public void addPlayer(PlayerInterface p){
        //get gold from player to increase the pot
        try {
            //connect to db
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/java", "root", "root");
            String sql = "";
            players.add(p);
            p.enterGame();
            int id = p.getUserId();
            int userGold = p.getGold();
            userGold = userGold - buyin;
            //use prep statement
            sql = "UPDATE users SET gold = ? WHERE userid = ?";
            PreparedStatement prepStatement = conn.prepareStatement(sql);
            prepStatement.setInt(1, userGold);
            prepStatement.setInt(2, id);
            prepStatement.executeUpdate();
            //close connection to db
            conn.close();

        }
        catch(Exception e){
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }


    }
    public void removePlayer(PlayerInterface player){
        players.remove(player);
        player.exitGame();
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public void dealHand(){
        for(PlayerInterface player : players){
            player.getHand()[0] = deck.pop();
            player.getHand()[1] = deck.pop();
        }
        //TODO integrate hand evaluator
        //getRankings();
    }
    public void flop(){
        deck.pop();
        for(int i = 0; i < 3; i++) {
            tableCards.add(deck.pop());
        }
        //getRankings()
    }
    public void turn() {
        deck.pop();
        tableCards.add(deck.pop());
        //getRankings();
    }
    public void river(){
        deck.pop();
        tableCards.add(deck.pop());
        //getRankings();
    }
    public List<PlayerInterface> winner(){
        //getRankings();
        //TODO: implement winner selector using hand evaluator
        //TODO: on declaration of winner, select the winning player and push the pot to his
    }
    //TODO: add checking for highCards where multiple players have 1 or more similar cards
}
