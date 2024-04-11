package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jm.task.core.jdbc.util.Util;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public void createUsersTable() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "lastName VARCHAR(255) NOT NULL," +
                "age TINYINT" +
                ")";
        executeQuery(createTableQuery);
    }

    public void dropUsersTable() {
        String dropTableQuery = "DROP TABLE IF EXISTS users";
        executeQuery(dropTableQuery);
    }

    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        saveOrUpdate(user);
    }

    public void removeUserById(long id) {
        User user = getUserById(id);
        if (user != null) {
            delete(user);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            users = session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        String cleanTableQuery = "DELETE FROM users";
        executeQuery(cleanTableQuery);
    }

    private void executeQuery(String query) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createSQLQuery(query).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveOrUpdate(User user) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delete(User user) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User getUserById(long id) {
        User user = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}

