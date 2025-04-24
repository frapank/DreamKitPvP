package com.gourmet.dreamkitpvp.others.managers;

import com.gourmet.dreamkitpvp.others.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.sql.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DatabaseManager {

    @Getter
    Boolean isOnline = false;

    final String url = "jdbc:mysql://localhost:3306/kitpvp?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    final String user = "root";
    final String pass = "1231";

    public void setupDatabase() {
        final String baseUrl = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
        final String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS kitpvp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        final String createTableQuery =
                "CREATE TABLE IF NOT EXISTS fragments_db (" +
                        "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(255) NOT NULL UNIQUE, " +
                        "fragments INT DEFAULT 0" +
                        ")";

        try (Connection connection = DriverManager.getConnection(baseUrl, user, pass);
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(createDatabaseQuery);
            Logger.info("Database 'kitpvp' created/checked successfully");

        } catch (SQLException e) {
            throw new RuntimeException("Errore nella creazione del database: " + e.getMessage(), e);
        }

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(createTableQuery);
            Logger.info("Table 'fragments_db' created/checked successfully");

        } catch (SQLException e) {
            throw new RuntimeException("Errore nella creazione della tabella: " + e.getMessage(), e);
        }
    }


    public void initDatabase() {
        final String query = "SELECT * FROM fragments_db";

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
        final String query = "INSERT IGNORE INTO fragments_db (name) VALUE (?)";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, player.getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            Logger.warning("Database error adding user: " + e.getMessage());
        }
    }

    public int getFragmentByName(String playerName) {
        final String query = "SELECT fragments FROM fragments_db WHERE name = ? LIMIT 1";

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
