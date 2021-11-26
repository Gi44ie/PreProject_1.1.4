package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Session session;
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            String sql = "create table if not exists users(id bigint not null auto_increment, " +
                    "name varchar(45) not null, lastname varchar(45) not null, age tinyint not null, " +
                    "primary key(id))";

            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            session.close();
        } catch(Exception e) {
            if(transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    @Override
    public void dropUsersTable() {
        try {
            session = Util.getSessionFactory().openSession();
            session.createSQLQuery("drop table users").executeUpdate();
            session.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        User user = new User(name, lastName, age);
        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
        } catch(Exception ex) {
            if(transaction != null) {
                transaction.rollback();
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        User user = new User();
        user.setId(id);
        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
            session.close();
        } catch(Exception ex) {
            if(transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list;
        session = Util.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        list = (List<User>) session.createSQLQuery("select * from users").addEntity(User.class).list();
        tr.commit();
        session.close();
        if(list.size() > 0) {
            return list;
        }
        return null;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try {
            session = Util.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
            session.close();
        } catch(Exception ex) {
            if(transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }
}
