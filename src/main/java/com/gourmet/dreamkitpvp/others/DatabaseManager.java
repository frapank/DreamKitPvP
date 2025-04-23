package com.gourmet.dreamkitpvp.others;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.sql.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseManager {

    String url = "jdbc:mysql://localhost:3306/users?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    String user = "root";
    String pass = "1231";


    public void initDatabase(){
        String query = "SELECT * FROM user";
        try (Connection connection = DriverManager.getConnection(url, user, pass)){
            Logger.info("Database connection established");
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

        } catch (SQLException e) {
            Logger.warning("Problem with the database: " + e);
        }
    }

    public boolean createUser(Player player){
        String query = "INSERT IGNORE INTO user (name) VALUE (?)";

        try (
                Connection connection = DriverManager.getConnection(url, user, pass);
                PreparedStatement stmt = connection.prepareStatement(query);
        ) {

            stmt.setString(1, player.getName());
            int result = stmt.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            Logger.warning("Database error adding user: " + e);
            return false;
        }
    }

    public int getFragmentByName(String playerName){
        String query = "SELECT fragments, name FROM user WHERE name = ? LIMIT 1";

        try (
                Connection connection = DriverManager.getConnection(url, user, pass);
                PreparedStatement stmt = connection.prepareStatement(query);
        ) {

           stmt.setString(1, playerName);

           ResultSet result = stmt.executeQuery();
           if (result.next()) {
               return result.getInt("fragments");
           }

           result.close();

        } catch (SQLException e) {
            Logger.warning("Database problem, can't find user: " + e);
        }

        return -1;
    }
}
