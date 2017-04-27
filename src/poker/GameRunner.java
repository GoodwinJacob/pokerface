package poker;

/**
 * Created by jacobgoodwin on 4/26/17.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class GameRunner {
    public static void main(String[] args) {
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
        //TODO: create socket
        Lobby lobby = new Lobby();


        for(;;) {
            //TODO:put in while loop, validate with boolean (active serverSocket)
            PokerGame game = lobby.game;
                //TODO:create players here
            PlayerInterface p1 = new Player();
            PlayerInterface p2 = new Player();
            DeckInterface deck = new Deck();
            // The Deck and Player used in the GameTexasHoldem are interfaces..
            // The newGame signature: public void newGame(DeckInterface deck, PlayerInterfer player1, PlayerInterface... _players).
            // At least one player is mandatory to start the game, but you can add as many players as you want (Java Varargs).
            game.newGame(deck, 50, p1, p2);
            game.dealHand();
            //TODO:gameCycle(String action, String username, int amount); x2
            game.flop();
            //TODO:gameCycle(); x2
            game.turnRiver();
            //TODO:gameCycle(); x2
            game.turnRiver();
            //TODO:gameCycle(); x2
            //get winner
            List<PlayerInterface> winnerList = game.winner();
            int s = winnerList.size();
            int g = game.getPot();
            // Getting the result
            if (winnerList.size() > 1) {
                // DRAW GAME
                //divide pot amongst winners
                g = (g / s);
                game.winnerGold(winnerList, g);
                game.setPot(0);
            } else if (winnerList.contains(p1)) {
                // p1 WINNER
                // WINNER HAND: p1.getHandRank();
                game.winnerGold(winnerList, g);
            } else if (winnerList.contains(p2)) {
                // p2 WINNER
                // WINNER HAND: p2.getHandRank();
                game.winnerGold(winnerList, g);
            }

        }
    }
}
