package poker;
/**
 * Created by jacobgoodwin on 4/26/17.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;


public class Lobby {
    private List<PlayerInterface> users;
     PokerGame game;
    NetworkingForServer netManager;

    public Lobby(){
        game = new PokerGame();
        netManager = new NetworkingForServer();
    }

    //public

    /* Network for server */
    /*
     *
     * @author User
     */
    public class NetworkingForServer implements Runnable {

        ConcurrentMap socketList = new ConcurrentHashMap(); //Key: Username <String>  Value: Socket <Socket>
        //Welcome socket
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private ExecutorService threadPool;
        private int poolSize = 20;


        public NetworkingForServer() {
            try {
                int port = 6789;
                serverSocket = new ServerSocket(6789);
                //Setup thread pool
                threadPool = Executors.newFixedThreadPool(poolSize);
                System.out.println("Server socket running on " + port);
                run();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (true) {

                    System.out.println("in run() networkingForServer ");
                    threadPool.execute(new networkThread(serverSocket.accept()));
                }
            } catch (IOException e) {

            }
        }
    }

    /**
     * @author User
     */
    public class networkThread implements Runnable {
        String userName;
        Socket clientSocket;

        public networkThread(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.clientSocket.setKeepAlive(true);

        }

        public void run() {
            try {
                while (true) {
                    BufferedReader messageFromClient = new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));
                    String message = messageFromClient.readLine();
                    processMessage(message);
                    //System.out.println("Debug message: " + message);
                }
            } catch (IOException e) {
                System.out.println("IOException");
                //e.printStackTrace();
            }
        }

        public void processMessage(String message) {
            JSONObject temp = new JSONObject(message);
            System.out.println(temp);
            String action = temp.getString("action");
            String userDoingAction = temp.getString("username");
            System.out.println(action);
            if (action.equals("login")) {
                handleLogin(message);
            }
            if (action.equals("call") ||
                    action.equals("bet") ||
                    action.equals("fold")) {
                //Update server side game state
                handleAction(message);
            }

        }

        private void sendMessageToClient(JSONObject message) {
            try {
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                outToClient.writeBytes(message.toString() + '\n');
            } catch (IOException e) {
                System.out.println("Error sending message to client");
            }
        }

        public void handleLogin(String message) {
            JSONObject temp = new JSONObject(message);
            String username = temp.getString("username");
            String pass = temp.getString("password");
            String query = "SELECT * from users where username = ? and password = ?";
            try {
                //connection to database
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/java", "root", "root");

                PreparedStatement prepStatement = conn.prepareStatement(query);
                prepStatement.setString(1, username);
                prepStatement.setString(2, pass);

                ResultSet rs = prepStatement.executeQuery(query);

                JSONObject messageToSend = new JSONObject();
                messageToSend.put("action", "login");

                //User/pass word is wrong
                if (!rs.next()) {

                    messageToSend.put("response", "failed");
                    sendMessageToClient(messageToSend);
                } else {

                    Player newPlayer = new Player();
                    newPlayer.setUsername(rs.getString("username"));
                    newPlayer.setGold(rs.getInt("gold"));
                    System.out.println("Creating " + rs.getString("username") + " with " + rs.getInt("gold") + " gold");

                    messageToSend.put("response", "success");
                    messageToSend.put("username", rs.getString("username"));
                    messageToSend.put("gold", rs.getInt("gold"));
                    sendMessageToClient(messageToSend);
                    //Add username and socket to socketmapp
                    netManager.socketList.put(rs.getString("username"), clientSocket);
                }

                conn.close();
            } catch (Exception e) {
                System.err.println("Got an exception! ");
                e.printStackTrace();
            }
        }

        public void handleAction(String message) {
            JSONObject temp = new JSONObject(message);
            String username = temp.getString("username");
            String action = temp.getString("action");
            Integer amount = new Integer(temp.getString("amount"));
            System.out.println(username + " " + action + " " + amount);
            if (game.myTurn(username)) {
                game.gameCycle(username, action, amount, game.needThirdGameCycle());
            }
        }

    }

}
