package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PokerGame {
    private DeckInterface deck;
    private List<PlayerInterface> players;
    private List<Card> tableCards;

    public List<Card> getTableCards(){return tableCards;}

    public void newGame(DeckInterface deck, PlayerInterface player1, PlayerInterface... _players){
        this.deck = deck;
        tableCards = new ArrayList<Card>();
        players = new ArrayList<PlayerInterface>();
        players.add(player1);
        players.addAll(Arrays.asList(_players));
    }
    public void removePlayer(PlayerInterface player){
        players.remove(player);
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
    }
    //TODO: add checking for highCards where multiple players have 1 or more similar cards
}
