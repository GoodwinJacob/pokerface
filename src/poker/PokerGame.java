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
import org.json.JSONObject;

public class PokerGame {
    private DeckInterface deck;
    private List<PlayerInterface> players;
    private List<Card> tableCards;
    private int pot;
    private int buyin;
    private int turn; //0 for player 1 turn
    private int amountToCall;
    private boolean needThirdGameCycle = false;
    private int exec;

    public List<Card> getTableCards() {
        return tableCards;
    }

    private void getRankings() {
        for (PlayerInterface p : players) {
            HandEvaluator.checkRanking(p, tableCards);
        }
    }

    public void newGame(DeckInterface deck, int buyin, PlayerInterface player1, PlayerInterface... _players) {
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
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }
        setPot(total);
    }

    public void addPlayer(PlayerInterface p) {
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

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }


    }

    public void removePlayer(PlayerInterface player) {
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
        } else if (player1HighCard.rankInt() < player2HighCard.rankInt()) {
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

    public void dealHand() {
        for (PlayerInterface player : players) {
            player.getHand()[0] = deck.pop();
            player.getHand()[1] = deck.pop();
        }
        getRankings();
    }

    public void flop() {
        deck.pop();
        for (int i = 0; i < 3; i++) {
            tableCards.add(deck.pop());
        }
        getRankings();
    }

    //changed to turnRiver, just calling it twice now.
    public void turnRiver() {
        deck.pop();
        tableCards.add(deck.pop());
        getRankings();
    }

    //does the same thing as turn,
/*    public void river(){
        deck.pop();
        tableCards.add(deck.pop());
        getRankings();
    }*/
    public List<PlayerInterface> winner() {
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
        try {
            //connect to db
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/java", "root", "root");
            String sql = "";
            //get id and gold amount of winner
            int id = winner.getUserId();
            int userGold = winner.getGold();
            //set gold to push to DB
            userGold = userGold + getPot();
            //use prep statement to push new amount of gold to the DB
            sql = "UPDATE users SET gold = ? WHERE userid = ?";
            PreparedStatement prepStatement = conn.prepareStatement(sql);
            prepStatement.setInt(1, userGold);
            prepStatement.setInt(2, id);
            prepStatement.executeUpdate();
            //close connection to db
            conn.close();

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }

        return winnerList;
    }

    public void winnerGold(List<PlayerInterface> winnerList, int g) {
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
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }
    }

    public boolean gameCycle(String username, String action, int amount, boolean thridCycle){
        boolean result;
        int indexInPlayerList = findIndex(username);

        if(action.equals("fold")){
            result = false;
            List<PlayerInterface> winner = new ArrayList();
            if(indexInPlayerList == 0){
                winner.add(players.get(1));
            }
            else if(indexInPlayerList == 1){
                winner.add(players.get(0));
            }
            winnerGold(winner, pot);
            return result;
        }
        else if(action.equals("call")){
            result = true;
            //First time of this game cycle
            if(exec == 0){
                amountToCall = 0;
            }
            //Second time of game cycle and need to call
            if((exec == 1) && amountToCall > 0){
                //Find the amount of gold a player has
                int amountInPlayer = players.get(indexInPlayerList).getGold();
                //The amount makes the player all in
                if(amount >  amountInPlayer){
                    pot += amountInPlayer;
                    //The player is all in
                    players.get(indexInPlayerList).setGold(0);
                    //Reduce gold in DB here
                }
                //The player is not all in.
                else{
                    pot += amount;
                    //Subtract the amount needed to call
                    players.get(indexInPlayerList).setGold(amountInPlayer - amount);
                    //Reduce gold in DB here
                }
                amountToCall = 0;
            }
            //Second time of game cycle. This is the 2nd player checking
            else if((exec == 1) && (amountToCall == 0)){
                amountToCall = 0;
            }
            //Third time in game cycle. Needed if the second person bets
            else if((exec == 2) && (amountToCall > 0)){
                //Find the amount of gold a player has
                int amountInPlayer = players.get(indexInPlayerList).getGold();
                //The amount makes the player all in
                if(amount >  amountInPlayer){
                    pot += amountInPlayer;
                    //The player is all in
                    players.get(indexInPlayerList).setGold(0);
                    //Reduce gold in DB here
                }
                //The player is not all in.
                else{
                    pot += amount;
                    //Subtract the amount needed to call
                    players.get(indexInPlayerList).setGold(amountInPlayer - amount);
                    //Reduce gold in DB here
                }
            }
        }
        else if(action.equals("bet")){
            result = true;
            //If the first person bets update pot and amount to call
            if(exec == 0){
                //Find the amount of gold a player has
                int amountInPlayer = players.get(indexInPlayerList).getGold();
                //The amount makes the player all in
                if(amount >  amountInPlayer){
                    pot += amountInPlayer;
                    //The player is all in
                    players.get(indexInPlayerList).setGold(0);
                    //Reduce gold in DB here
                    amountToCall = amountInPlayer;
                }
                //The player is not all in.
                else{
                    pot += amount;
                    //Subtract the amount needed to call
                    players.get(indexInPlayerList).setGold(amountInPlayer - amount);
                    //Reduce gold in DB here
                    amountToCall = amount;
                }
            }
            //Second person bets
            else if(exec == 1){
                //Find the amount of gold a player has
                int amountInPlayer = players.get(indexInPlayerList).getGold();
                //The second person bet. We need to call gamecyle a third time
                needThirdGameCycle = true;
                //The amount makes the player all in
                if(amount >  amountInPlayer){
                    pot += amountInPlayer;
                    //The player is all in
                    players.get(indexInPlayerList).setGold(0);
                    //Reduce gold in DB here
                    amountToCall = amountInPlayer;
                }
                //The player is not all in. Add amount to call incase the first
                //person bet
                else{
                    pot += (amount + amountToCall);
                    //Subtract the amount needed to call
                    players.get(indexInPlayerList).setGold(amountInPlayer - amount);
                    //Reduce gold in DB here
                    amountToCall = amount;
                }
            }
            //Happens if the second person bets and the person tries to raise.
            //Forces a call
            else if(exec ==2){
                //Find the amount of gold a player has
                int amountInPlayer = players.get(indexInPlayerList).getGold();
                //The amount makes the player all in
                if(amountToCall >  amountInPlayer){
                    pot += amountInPlayer;
                    //The player is all in
                    players.get(indexInPlayerList).setGold(0);
                    //Reduce gold in DB here
                    amountToCall = 0;
                }
                //The player is not all in. Add amount to call incase the first
                //person bet
                else{
                    pot += amountToCall;
                    //Subtract the amount needed to call
                    players.get(indexInPlayerList).setGold(amountInPlayer - amountToCall);
                    //Reduce gold in DB here
                    amountToCall = 0;
                }
                needThirdGameCycle = false;
            }
        }
        else{
            result = true;
        }
        exec += 1;
        if(turn == 0){ turn = 1; }
        else if(turn == 1) { turn = 0; }


        return result;
    }
    private int findIndex(String username){
        int result;
        if(players.get(0).getUsername().equals(username)){
            result = 0;
        }
        else if(players.get(1).getUsername().equals(username)){
            result = 1;
        }
        //Should never happen
        else{
            result = -1;
        }
        return result;
    }

    public boolean needThirdGameCycle(){
        return needThirdGameCycle;
    }

    public String returnGameState(){
        JSONObject updatedState = new JSONObject();
        updatedState.put("pot", this.pot);
        updatedState.put("amountToCall", this.amountToCall);
        Integer i = 0;
        for(Card c : tableCards){
            String card = c.toString();
            updatedState.put(i.toString(), card);
            i++;
        }
        //updatedState.put("flop0", )
        return updatedState.toString();
    }
    public boolean myTurn(String username){
        if((players.get(0).getUsername()).equals(username)
                && turn == 0){
            return true;
        }
        else if((players.get(1).getUsername()).equals(username)
                && turn == 1){
            return true;
        }
        else{
            return false;
        }
    }
}
