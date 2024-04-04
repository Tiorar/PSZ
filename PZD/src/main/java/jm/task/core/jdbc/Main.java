package jm.task.core.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class User {
    private Long id;
    private String name;
    private String lastName;
    private Byte age;

    public User(Long id, String name, String lastName, Byte age) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}

interface UserDAO {
    void createTable() throws SQLException;
    void dropTable() throws SQLException;
    void clearTable() throws SQLException;
    void addUser(User user) throws SQLException;
    void deleteUser(Long id) throws SQLException;
    List<User> getAllUsers() throws SQLException;
}

class UserDAOImpl implements UserDAO {
    private Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "lastName VARCHAR(255) NOT NULL," +
                "age TINYINT" +
                ")");
        stmt.close();
    }

    @Override
    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS Users");
        stmt.close();
    }

    @Override
    public void clearTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("TRUNCATE TABLE Users");
        stmt.close();
    }

    @Override
    public void addUser(User user) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Users (name, lastName, age) VALUES (?, ?, ?)");
        pstmt.setString(1, user.getName());
        pstmt.setString(2, user.getLastName());
        pstmt.setInt(3, user.getAge());
        pstmt.executeUpdate();
        pstmt.close();
    }

    @Override
    public void deleteUser(Long id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Users WHERE id = ?");
        pstmt.setLong(1, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
        while (rs.next()) {
            User user = new User(rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("lastName"),
                    rs.getByte("age"));
            users.add(user);
        }
        rs.close();
        stmt.close();
        return users;
    }
}

class DBUtil {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String user = "root";
        String password = "password123";
        return DriverManager.getConnection(url, user, password);
    }
}

class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void createUserTable() throws SQLException {
        userDAO.createTable();
    }

    public void dropUserTable() throws SQLException {
        userDAO.dropTable();
    }

    public void clearUserTable() throws SQLException {
        userDAO.clearTable();
    }

    public void addUser(User user) throws SQLException {
        userDAO.addUser(user);
    }

    public void deleteUser(Long id) throws SQLException {
        userDAO.deleteUser(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }
}

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DBUtil.getConnection()) {
            UserDAO userDAO = new UserDAOImpl(connection);
            UserService userService = new UserService(userDAO);

            userService.createUserTable();

            List<User> users = userService.getAllUsers();
            for (User user : users) {
                System.out.println(user);
            }

            userService.clearUserTable();

            userService.dropUserTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
