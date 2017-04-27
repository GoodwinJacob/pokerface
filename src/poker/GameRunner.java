package poker;

/**
 * Created by jacobgoodwin on 4/26/17.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class GameRunner {
    public static void main(String[] args){
        //create lobby
        //populate from lobby
        //get players from lobby
        //create game
        //game.newGame()
        //deal
        //flop
        //turn
        //river
        //get winner
        //if more than one winner
        //else ifs for all potential players
        PokerGame game = new PokerGame();
        //TODO:create players here
        PlayerInterface p1 = new Player();
        PlayerInterface p2 = new Player();
        DeckInterface deck = new Deck();
        // The Deck and Player used in the GameTexasHoldem are interfaces..
        // The newGame signature: public void newGame(IDeck deck, IPlayer player1, IPlayer... _players).
        // At least one player is mandatory to start the game, but you can add as many players as you want (Java Varargs).
        game.newGame(deck, 50, p1, p2);
        game.dealHand();
        //TODO:gameCycle();
        game.flop();
        //TODO:gameCycle();
        game.turnRiver();
        //TODO:gameCycle();
        game.turnRiver();
        //get winner
        List<PlayerInterface> winnerList = game.winner();

        // Getting the result
        if(winnerList.size() > 1){
            // DRAW GAME
            int s = winnerList.size();
            int g = game.getPot();
            //divide pot amongst winners
            g = (g / s);
            try {
                //connection to database
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/java", "root", "root");
                String sql = "";
                for (PlayerInterface p : winnerList) {
                    int id = p.getUserId();
                    int userGold = p.getGold();
                    userGold = userGold + g;
                    sql = "UPDATE users SET gold = ? WHERE userid = ?";
                    PreparedStatement prepStatement = conn.prepareStatement(sql);
                    prepStatement.setInt(1, userGold);
                    prepStatement.setInt(2, id);
                    prepStatement.executeUpdate();
                    //increase pot
                }
                conn.close();
            }
            catch(Exception e){
                System.err.println("Got an exception! ");
                e.printStackTrace();
            }
            game.setPot(0);
        }

        else if(winnerList.contains(p1)) {
            // p1 WINNER
            // WINNER HAND: p1.getHandRank();
        }
        else if(winnerList.contains(p2)) {
            // p2 WINNER
            // WINNER HAND: p2.getHandRank();
        }

        // The table cards
        game.getTableCards();

        // To get the player actual ranking
        p1.getHandRank();
        p2.getHandRank();
        // IPlayer.getRankingEnum() can be changed in deal, call flop, bet river and bet turn.

    }
}
