package com.gourmet.dreamkitpvp.others.managers;

import com.gourmet.dreamkitpvp.DreamKitPvP;
import com.gourmet.dreamkitpvp.others.Logger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DatabaseManager {

    final String url = "jdbc:mysql://localhost:3306/kitpvp?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    final String user = "root";
    final String pass = DreamKitPvP.getConfigManager().getDatabasePassword();
    @Getter private final HashMap<Player, PlayerData> playersData = new HashMap<>();
    @Getter private Boolean isOnline = false;

    public void setupDatabase() {

        if (pass == null) return;

        final String baseUrl = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
        final String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS kitpvp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

        final String createPlayerTableQuery = "CREATE TABLE IF NOT EXISTS players (" + "name VARCHAR(255) NOT NULL PRIMARY KEY, " + "fragments INT DEFAULT 0" + ")";

        final String createStatisticsTableQuery = "CREATE TABLE IF NOT EXISTS statistics ("
                + "name VARCHAR(255) NOT NULL, " + "kills INT DEFAULT 0, "
                + "deaths INT DEFAULT 0, " + "kd DECIMAL(5,2) DEFAULT 0.00, "
                + "bounty INT DEFAULT 0, " + "streak INT DEFAULT 0, "
                + "bastard INT DEFAULT 0, " + "PRIMARY KEY(name), "
                + "FOREIGN KEY(name) REFERENCES players(name) ON DELETE CASCADE" + ")";

        try (Connection connection = DriverManager.getConnection(baseUrl, user, pass); Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createDatabaseQuery);
            Logger.info("Database 'kitpvp' created/checked successfully");
        } catch (SQLException e) {
            Logger.warning("Errore nella creazione del database: " + e.getMessage());
            isOnline = false;
        }

        try (Connection connection = DriverManager.getConnection(url, user, pass); Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createPlayerTableQuery);
            stmt.executeUpdate(createStatisticsTableQuery);
            Logger.info("Tables 'players' and 'statistics' created/checked successfully");
        } catch (SQLException e) {
            Logger.warning("Errore nella creazione delle tabelle: " + e.getMessage());
            isOnline = false;
        }
    }

    public void initDatabase() {

        if (pass == null) return;


        final String query = "SELECT p.name, p.fragments, s.kills, s.deaths, s.kd, s.bounty, s.bastard " + "FROM players p LEFT JOIN statistics s ON p.name = s.name";

        try (Connection connection = DriverManager.getConnection(url, user, pass); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Logger.info("Database connection established");
            isOnline = true;
        } catch (SQLException e) {
            Logger.warning("Problem with the database: " + e.getMessage());
            isOnline = false;
        }
    }


    public void createUser(Player player) {
        final String insertPlayer = "INSERT IGNORE INTO players (name) VALUES (?)";
        final String insertStats = "INSERT IGNORE INTO statistics (name) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            try (PreparedStatement stmt1 = connection.prepareStatement(insertPlayer)) {
                stmt1.setString(1, player.getName());
                stmt1.executeUpdate();
            }
            try (PreparedStatement stmt2 = connection.prepareStatement(insertStats)) {
                stmt2.setString(1, player.getName());
                stmt2.executeUpdate();
            }
            Logger.info("User and initial statistics created for " + player.getName());
        } catch (SQLException e) {
            Logger.warning("Database error adding user and stats: " + e.getMessage());
        }
    }

    // Fragments
    public void updateFragments(String playerName, int newFragments) {
        final String query = "UPDATE players SET fragments = ? WHERE name = ?";
        try (Connection connection = DriverManager.getConnection(url, user, pass); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, newFragments);
            stmt.setString(2, playerName);
            int rows = stmt.executeUpdate();
            if (rows > 0) Logger.info("Fragments updated for " + playerName);
            else Logger.warning("No player found with name: " + playerName);
        } catch (SQLException e) {
            Logger.warning("Database error updating fragments: " + e.getMessage());
        }
    }

    // Statistics
    public void updateStatistics(String playerName, int kills, int deaths, double kd, int bounty, int streak, int bastard) {
        final String query = "INSERT INTO statistics (name, kills, deaths, kd, bounty, streak, bastard) VALUES (?, ?, ?, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE kills = ?, deaths = ?, kd = ?, bounty = ?, streak = ?, bastard = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            stmt.setInt(2, kills);
            stmt.setInt(3, deaths);
            stmt.setDouble(4, kd);
            stmt.setInt(5, bounty);
            stmt.setInt(6, streak);
            stmt.setInt(7, bastard);

            stmt.setInt(8, kills);
            stmt.setInt(9, deaths);
            stmt.setDouble(10, kd);
            stmt.setInt(11, bounty);
            stmt.setInt(12, streak);
            stmt.setInt(13, bastard);
            stmt.executeUpdate();

        } catch (SQLException e) {
            Logger.warning("Database error updating statistics: " + e.getMessage());
        }
    }

    // Getter
    public PlayerStats getStatistics(String playerName) {
        final String query = "SELECT kills, deaths, kd, bounty, streak, bastard FROM statistics WHERE name = ? LIMIT 1";
        try (Connection connection = DriverManager.getConnection(url, user, pass); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new PlayerStats(playerName, rs.getInt("kills"), rs.getInt("deaths"), rs.getDouble("kd"), rs.getInt("bounty"), rs.getInt("streak"), rs.getInt("bastard"));
                }
            }
        } catch (SQLException e) {
            Logger.warning("Database problem fetching statistics: " + e.getMessage());
        }
        return null;
    }

    public int getFragmentByName(String playerName) {
        final String query = "SELECT fragments FROM players WHERE name = ? LIMIT 1";
        try (Connection connection = DriverManager.getConnection(url, user, pass); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) return result.getInt("fragments");
            }
        } catch (SQLException e) {
            Logger.warning("Database problem, can't find user: " + e.getMessage());
        }
        return -1;
    }

    public void decreaseInfamyForAll(int amount) {

        String sql = "UPDATE statistics " + "SET bastard = GREATEST(bastard - ?, 0)";

        try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amount);
        } catch (SQLException e) {
            Logger.warning("Errore decrementando infamia: " + e.getMessage());
        }
    }

    public static class PlayerStats {
        public final String name;
        public int kills;
        public int deaths;
        public double kd;
        public int bounty;
        public int streak;
        public int bastard;

        public PlayerStats(String name, int kills, int deaths, double kd, int bounty, int streak, int bastard) {
            this.name = name;
            this.kills = kills;
            this.deaths = deaths;
            this.kd = kd;
            this.bounty = bounty;
            this.streak = streak;
            this.bastard = bastard;
        }
    }

    public static class PlayerData {
        public final String name;
        public int fragments;
        public PlayerStats stats;

        public PlayerData(Player player, DatabaseManager databaseManager) {
            this.name = player.getName();
            this.fragments = databaseManager.getFragmentByName(name);
            this.stats = databaseManager.getStatistics(name);
        }
    }
}

