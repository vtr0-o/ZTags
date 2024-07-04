package net.ztags;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabase {
    private Connection connection;
    private String host, database, username, password;
    private int port;

    public MySQLDatabase(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database +
                "?useSSL=false&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&serverTimezone=UTC&characterEncoding=UTF-8";
        connection = DriverManager.getConnection(url, username, password);

        createTagsTable();
        createUserDataTable();
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTagsTable() {
        String createTagsTableSQL = "CREATE TABLE IF NOT EXISTS tags ("
                + "tagid VARCHAR(255) PRIMARY KEY,"
                + "name VARCHAR(255) NOT NULL,"
                + "prefix VARCHAR(255),"
                + "suffix VARCHAR(255),"
                + "weight INT"
                + ")";

        try (PreparedStatement statement = connection.prepareStatement(createTagsTableSQL)) {
            statement.executeUpdate();
            Bukkit.getLogger().info("Table 'tags' created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUserDataTable() {
        String createUserDataTableSQL = "CREATE TABLE IF NOT EXISTS user_data ("
                + "uuid VARCHAR(36) PRIMARY KEY,"
                + "tagid VARCHAR(255),"
                + "FOREIGN KEY (tagid) REFERENCES tags(tagid) ON DELETE SET NULL ON UPDATE CASCADE"
                + ")";

        try (PreparedStatement statement = connection.prepareStatement(createUserDataTableSQL)) {
            statement.executeUpdate();
            Bukkit.getLogger().info("Table 'user_data' created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addOrUpdateUserData(String uuid, String tagid) {
        String addOrUpdateUserDataSQL = "INSERT INTO user_data (uuid, tagid) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE tagid = VALUES(tagid)";

        try (PreparedStatement statement = connection.prepareStatement(addOrUpdateUserDataSQL)) {
            statement.setString(1, uuid);
            statement.setString(2, tagid);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTagForUser(String uuid) {
        String getTagForUserSQL = "SELECT tagid FROM user_data WHERE uuid = ?";

        try (PreparedStatement statement = connection.prepareStatement(getTagForUserSQL)) {
            statement.setString(1, uuid);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("tagid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Method to add or update a tag
    public void addOrUpdateTag(Tag tag) {
        String addOrUpdateTagSQL = "INSERT INTO tags (tagid, name, prefix, suffix, weight) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name = VALUES(name), prefix = VALUES(prefix), suffix = VALUES(suffix), weight = VALUES(weight)";

        try (PreparedStatement statement = connection.prepareStatement(addOrUpdateTagSQL)) {
            statement.setString(1, tag.getID());
            statement.setString(2, tag.getName());
            statement.setString(3, tag.getPrefix());
            statement.setString(4, tag.getSuffix());
            statement.setInt(5, tag.getWeight());

            statement.executeUpdate();
            Bukkit.getLogger().info("Tag added/updated successfully: TagID = " + tag.getID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTag(String tagid) {
        String deleteTagSQL = "DELETE FROM tags WHERE tagid = ?";

        try (PreparedStatement statement = connection.prepareStatement(deleteTagSQL)) {
            statement.setString(1, tagid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a tag
    public Tag getTag(String tagid) {
        String getTagSQL = "SELECT * FROM tags WHERE tagid = ?";

        try (PreparedStatement statement = connection.prepareStatement(getTagSQL)) {
            statement.setString(1, tagid);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Tag(
                            resultSet.getString("tagid"),
                            resultSet.getString("name"),
                            resultSet.getString("prefix"),
                            resultSet.getString("suffix"),
                            resultSet.getInt("weight")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String getAllTagsSQL = "SELECT * FROM tags";

        try (PreparedStatement statement = connection.prepareStatement(getAllTagsSQL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tags.add(new Tag(
                            resultSet.getString("tagid"),
                            resultSet.getString("name"),
                            resultSet.getString("prefix"),
                            resultSet.getString("suffix"),
                            resultSet.getInt("weight")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }

}