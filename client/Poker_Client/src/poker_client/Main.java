/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker_client;

/**
 *
 * @author User
 */

public class Main {
    
    public static void main(String args[]) {
       
        User testUser = new User("testUser", "pass", -1);
        
        PokerTable table = new PokerTable(testUser);
      
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                table.setVisible(true);
            }
        });
    }
}
