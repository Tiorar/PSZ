package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = Util.getConnection()) {
            UserDao userDAO = new UserDaoJDBCImpl(connection);
            UserService userService = new UserService(userDAO);
            userService.createUsersTable();

            User user1 = new User(null, "Никита", "Бабичев", (byte) 27);
            User user2 = new User(null, "Маккалей", "Галкин", (byte) 20);
            User user3 = new User(null, "Борис", "Ельцин", (byte) 99);
            User user4 = new User(null, "Егор", "Ракитин", (byte) 25);

            userService.saveUser(user1);
            userService.saveUser(user2);
            userService.saveUser(user3);
            userService.saveUser(user4);

            List <User> users = userService.getAllUsers();
            System.out.println("Список пользователей из базы данных:");
            for (User user : users) {
                System.out.println(user);
            }
            userService.cleanUsersTable();
            System.out.println("Таблица User очищена");
            userService.dropUsersTable();
            System.out.println("Таблица User удалена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}