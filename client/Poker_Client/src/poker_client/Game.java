/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker_client;

import java.util.List;

/**
 *
 * @author 
 */
public class Game {
    public List<String> users;
    public int pot;

    Game() {

    }
   
    
    public List<String> getUsers(){
        return users;
    }

    public boolean removeUser(String username) {
        if(users.isEmpty() || !users.contains(username)){
            return false;
        }
        else{
            users.remove(username);
            return true;
        }
    }

    public boolean addUser(String username) {
        if(users == null || users.size() == 9 || users.contains(username)){
           return false;
        }
        else{
            users.add(username);
            return true;
        }
    }
    
}