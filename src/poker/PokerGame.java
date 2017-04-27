package poker;

/**
 * Created by jacobgoodwin on 4/3/17.
 */
import java.sql.PreparedStatement;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.sql.*;

public class PokerGame {
    private DeckInterface deck;
    private List<PlayerInterface> players;
    private List<Card> tableCards;
    private int pot;
    private int buyin;

    public List<Card> getTableCards(){return tableCards;}

    private void getRankings() {
        for (PlayerInterface p : players) {
            HandEvaluator.checkRanking(p, tableCards);
        }
    }

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

    public Integer sumRankingList(PlayerInterface player) {
        Integer sum = 0;
        for (Card card : player.getRankingList()) {
            sum += card.rankInt();
        }
        return sum;
    }
    public Card getSecondHighCard(PlayerInterface player, Card card) {
        if (player.getHand()[0].equals(card)) {
            return player.getHand()[1];
        }
        return player.getHand()[0];
    }

    private PlayerInterface checkHighSequence(PlayerInterface player1, PlayerInterface player2) {
        Integer player1Rank = sumRankingList(player1);
        Integer player2Rank = sumRankingList(player2);
        if (player1Rank > player2Rank) {
            return player1;
        } else if (player1Rank < player2Rank) {
            return player2;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private PlayerInterface checkHighCardWinner(PlayerInterface player1, PlayerInterface player2) {
        PlayerInterface winner = compareHighCard(player1, player1.getHighCard(),
                player2, player2.getHighCard());
        if (winner == null) {
            Card player1Card = HandEvaluator.getHighCard(player1,
                    Collections.EMPTY_LIST);
            Card player2Card = HandEvaluator.getHighCard(player2,
                    Collections.EMPTY_LIST);
            winner = compareHighCard(player1, player1Card, player2, player2Card);
            if (winner != null) {
                player1.setHighCard(player1Card);
                player2.setHighCard(player2Card);
            } else if (winner == null) {
                player1Card = getSecondHighCard(player1, player1Card);
                player2Card = getSecondHighCard(player2, player2Card);
                winner = compareHighCard(player1, player1Card, player2,
                        player2Card);
                if (winner != null) {
                    player1.setHighCard(player1Card);
                    player2.setHighCard(player2Card);
                }
            }
        }
        return winner;
    }

    private PlayerInterface compareHighCard(PlayerInterface player1, Card player1HighCard,
                                    PlayerInterface player2, Card player2HighCard) {
        if (player1HighCard.rankInt() > player2HighCard.rankInt()) {
            return player1;
        }
        else if (player1HighCard.rankInt() < player2HighCard.rankInt()) {
            return player2;
        }
        return null;
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
        getRankings();
    }
    public void flop(){
        deck.pop();
        for(int i = 0; i < 3; i++) {
            tableCards.add(deck.pop());
        }
        getRankings();
    }
    public void turn() {
        deck.pop();
        tableCards.add(deck.pop());
        getRankings();
    }
    public void river(){
        deck.pop();
        tableCards.add(deck.pop());
        getRankings();
    }
    public List<PlayerInterface> winner(){
        getRankings();
        List<PlayerInterface> winnerList = new ArrayList<PlayerInterface>();
        PlayerInterface winner = players.get(0);
        Integer winnerRank = HandEvaluator.getRankingToInt(winner);
        winnerList.add(winner);
        for (int i = 1; i < players.size(); i++) {
            PlayerInterface player = players.get(i);
            Integer playerRank = HandEvaluator.getRankingToInt(player);
            //Draw game
            if (winnerRank == playerRank) {
                PlayerInterface highHandPlayer = checkHighSequence(winner, player);
                //Draw checkHighSequence
                if (highHandPlayer == null) {
                    highHandPlayer = checkHighCardWinner(winner, player);
                }
                //Not draw in checkHighSequence or checkHighCardWinner
                if (highHandPlayer != null && !winner.equals(highHandPlayer)) {
                    winner = highHandPlayer;
                    winnerList.clear();
                    winnerList.add(winner);
                } else if (highHandPlayer == null) {
                    //Draw in checkHighSequence and checkHighCardWinner
                    winnerList.add(winner);
                }
            } else if (winnerRank < playerRank) {
                winner = player;
                winnerList.clear();
                winnerList.add(winner);
            }
            winnerRank = HandEvaluator.getRankingToInt(winner);
        }

        return winnerList;
        //TODO: on declaration of winner, select the winning player and push the pot to his
    }
}
