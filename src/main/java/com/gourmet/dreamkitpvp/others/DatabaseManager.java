package com.gourmet.dreamkitpvp.others;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.sql.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseManager {

    String url = "jdbc:mysql://localhost:3306/users?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    String user = "root";
    String pass = "1231";


    public void initDatabase(){
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Logger.info("Database connection established");
            String query = "SELECT * FROM user";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                int id = rs.getInt("id");
                String username = rs.getString("name");
                int fragments = rs.getInt("fragments");

                Logger.info("Utente: " + username + " (ID: " + id + ") - Frammenti: " + fragments);
            }

            rs.close();
            stmt.close();
            connection.close();

        } catch (SQLException e) {
            Logger.warning("Problem with the database: " + e);
        }
    }
}
