/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.json.*;
import java.io.IOException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ClientNetworking implements Runnable {
    
    Socket clientSocket;
    //private DataInputStream  inputStream;
    private BufferedReader inputStream;
    private DataOutputStream outputStream;
    private String host;
    private int port;
    
    public ClientNetworking(){
        
    }
    @Override
    public void run() {
        
        setup();
        System.out.println("Hello from a thread!");
    }
    
    public void setup() {
        host = "127.0.0.1";
        port = 6789;
        try{
            clientSocket = new Socket(host, port);
            clientSocket.setKeepAlive(true);
            login("User111","pass222");
        }
        catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public void login(String user, String pass) throws IOException{
        //JsonObject loginString = Json.createObjectBuilder().build();
        //loginString.put(JsonString("username"), "user");
        JsonObject loginString = Json.createObjectBuilder()
                .add("action","login")
                .add("username", user)
                .add("password", pass)
                .build();
        
        outputStream = new DataOutputStream(clientSocket.getOutputStream()); 
        System.out.print("Sending ");
        System.out.print(loginString);
        
        outputStream.writeBytes(loginString.toString() + '\n');
    }
    public void checkOrCall(String username, int amount)throws IOException{
        JsonObject call = Json.createObjectBuilder()
                .add("action", "call")
                .add("username", username)
                .add("amount", amount)
                .build();
        
        outputStream = new DataOutputStream(clientSocket.getOutputStream()); 
        System.out.print("Sending ");
        System.out.print(call);
        outputStream.writeBytes(call.toString() + '\n');
    }
    public void bet(String username, int amount) throws IOException{
        JsonObject newBet = Json.createObjectBuilder()
                .add("action", "bet")
                .add("username", username)
                .add("amount", amount)
                .build();
        outputStream = new DataOutputStream(clientSocket.getOutputStream()); 
        System.out.print("Sending ");
        System.out.print(newBet);
        outputStream.writeBytes(newBet.toString() + '\n');
    }
    public void fold(String username) throws IOException{
        JsonObject fold = Json.createObjectBuilder()
                .add("action", "fold")
                .add("username", username)
                .build();
        outputStream = new DataOutputStream(clientSocket.getOutputStream()); 
        System.out.print("Sending ");
        System.out.print(fold);
        outputStream.writeBytes(fold.toString() + '\n');
    }
    public void sendJSONObject(JsonObject jobj){
        
    }
    
    public void recieveMessageFromServer(){
        try{
            BufferedReader inFromServer = new BufferedReader(new
                InputStreamReader(clientSocket.getInputStream())); 
           
            JSONObject response = new JSONObject(inFromServer.readLine());
            String action = response.getString("action");
            
            //Deal with login response
            if(action.equals("login") &&
              (response.getString("response")).equals("success")){
                    //Login sucess full. Hide the login form and setup local user
                    System.out.println("Recieved: " + response.toString());
            }
            else if(action.equals("login") &&   
                (response.getString("response")).equals("failed")){
                    System.out.println("Recieved: " + response.toString());
                    //Do something on wrong login
                    //Tell user wrong password
            }
            
            //Deal with call response
            if(action.equals("call") &&
               (response.getString("response")).equals("success")){
                    
            }
        }
        catch(IOException e){
            System.out.println("Exception in recievemessageFromServer");
        }
    }
}
