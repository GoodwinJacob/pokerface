/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker_client;

import static java.util.Arrays.binarySearch;

/**
 *
 * @author Nic
 */
public class User {
    private String username;
    private String password;
    private int seatNumber;
    private int numberOfChips;
    public int numberGames = 0;


    
    User(String name,String pword, int seatNum){
        this.username = name;
        this.password = pword;
        this.seatNumber = seatNum;
        //garray = new int[5];
    }
    
    /*
    public boolean addGame(){
        boolean added;
        added = false;
        if(getNumberGames() < maxNumberGames){
            game = new Game();
            if(game.addUser(username)){
                added = true;
                this.numberGames = this.numberGames+1;
                int ngames = this.numberGames - 1;
                garray[ngames] = game.getId();
            }
        }
        else if(binarySearch(garray,-1) >= 0){
            game = new Game();
            if(game.addUser(username)){
                added = true;
                garray[binarySearch(garray,-1)] = game.getId();
                this.numberGames = this.numberGames+1;
            }
        }
        return added;
    }
    
    public boolean removeGame(int gameId){
        boolean removed = false;
        int i;
        i = binarySearch(garray,gameId);
        if(this.numberGames > 0 && i >= 0){
            if(game.removeUser(username)){
                garray[i] = -1;
                this.numberGames = this.numberGames - 1; //mark index of array as available for new game
            }
        }
        return removed;
    }*/
    
    public int getSeatNumber(){
        return this.seatNumber;
    }
    
    public int getNumberGames(){
        return this.numberGames;
    }
    
    public int getNumberOfChips(){
        return this.numberOfChips;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public String getPassword(){
        return this.password;
    }
    /*
    public void setUsername(String name) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.username = name;
    }

    private void setPassword(String pword) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.password = pword;
    }*/
}
