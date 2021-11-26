package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Statement statement;
    private PreparedStatement psSave;
    private PreparedStatement psRemove;

    public UserDaoJDBCImpl() {
        try {
            statement = Util.getConnection().createStatement();
            psSave = Util.getConnection().prepareStatement("INSERT INTO users( name, lastName, age) " +
                    "VALUES (?, ?, ?)");
            psRemove = Util.getConnection().prepareStatement("DELETE FROM users WHERE id = ?");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createUsersTable() {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users(id INT NOT NULL AUTO_INCREMENT ," +
                    "name VARCHAR(55) NOT NULL, lastName VARCHAR(55) NOT NULL, age INT NOT NULL, PRIMARY KEY(id))");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try {
            statement.executeUpdate("drop table users");
        } catch(SQLException ex) {
//            ignore
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            psSave.setString(1, name);
            psSave.setString(2, lastName);
            psSave.setByte(3, age);
            psSave.executeUpdate();
            System.out.println("User с именем " + name + " добавлен в таблицу");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try {
            psRemove.setLong(1, id);
            psRemove.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM users";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getLong(1));
                user.setName(rs.getString(2));
                user.setLastName(rs.getString(3));
                user.setAge(rs.getByte(4));
                list.add(user);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        try {
            statement.executeUpdate("TRUNCATE users");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
