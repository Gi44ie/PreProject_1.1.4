package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Session session;
    private Transaction transaction;

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        transaction = null;

        session = Util.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        String sql = "create table if not exists users(id bigint not null auto_increment, " +
                "name varchar(45) not null, lastname varchar(45) not null, age tinyint not null, " +
                "primary key(id))";

        session.createSQLQuery(sql).executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        session = Util.getSessionFactory().openSession();
        session.createSQLQuery("drop table users").executeUpdate();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User sUser = new User(name, lastName, age);
        session = Util.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.save(sUser);
        transaction.commit();
        session.close();
    }

    @Override
    public void removeUserById(long id) {
        transaction = null;
        User rUser = new User();
        rUser.setId(id);
        session = Util.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.delete(rUser);
        transaction.commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list;
        session = Util.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        list = (List<User>) session.createSQLQuery("select * from users").addEntity(User.class).list();
        tr.commit();
        session.close();
        if (list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        transaction = null;
        session = Util.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.createQuery("delete from User").executeUpdate();
        transaction.commit();
        session.close();
    }
}
