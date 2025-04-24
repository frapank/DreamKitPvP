package com.gourmet.dreamkitpvp.others;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.sql.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DatabaseManager {

    @Getter
    Boolean isOnline = false;

    String url = "jdbc:mysql://localhost:3306/users?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    String user = "root";
    String pass = "1231";

    public void initDatabase() {
        final String query = "SELECT * FROM user";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            Logger.info("Database connection established");
            isOnline = true;

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("name");
                int fragments = rs.getInt("fragments");

                Logger.info(String.format("Utente: %s (ID: %d) - Frammenti: %d", username, id, fragments));
            }

        } catch (SQLException e) {
            Logger.warning("Problem with the database: " + e.getMessage());
            isOnline = false;
        }
    }

    public void createUser(Player player) {
        final String query = "INSERT IGNORE INTO user (name) VALUE (?)";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, player.getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            Logger.warning("Database error adding user: " + e.getMessage());
        }
    }

    public int getFragmentByName(String playerName) {
        final String query = "SELECT fragments FROM user WHERE name = ? LIMIT 1";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, playerName);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return result.getInt("fragments");
                }
            }

        } catch (SQLException e) {
            Logger.warning("Database problem, can't find user: " + e.getMessage());
        }

        return -1;
    }
}
