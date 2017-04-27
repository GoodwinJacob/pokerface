package poker;

/**
 * Created by jacobgoodwin on 4/13/17.
 */

import java.sql.*;

public class DatabaseDriver {
    static final String JDBC_Driver = "com.mysql.jdbc.Driver";
    //TODO: add the url when working
    static final String DB_URL = "URL goes here";
    //credentials
    static final String USERNAME = "root";
    static final String PASSWORD = "password";
    public void ConnectDB() {
        Connection conn = null;
        Statement stmt = null;
        //register driver
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Creating statement...");
        //connect
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        //query
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT idplayer, playername, gold, handsplayed, handswon, FROM players";
        ResultSet rs = stmt.executeQuery(sql);
        //extract data
        while(rs.next()){
            //Retrieve by column name
            int idplayer  = rs.getInt("id");
            String playername = rs.getInt("playername");
            int gold = rs.getString("gold");
            int handsplayed = rs.getString("handsplayed");
            int handswon = rs.getString("handswon");
        //clean up environment
    }
}
