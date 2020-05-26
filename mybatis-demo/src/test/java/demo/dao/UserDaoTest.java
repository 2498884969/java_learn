package demo.dao;

import demo.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTest {

    public UserDao userDao;
    public SqlSession sqlSession;

    @org.junit.Before
    public void setUp() throws Exception {

        // mybatis-config.xml
        String resource = "mybatis-config.xml";
        // 读取配置文件
        InputStream is = Resources.getResourceAsStream(resource);
        // 构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        // 获取sqlSession
        sqlSession = sqlSessionFactory.openSession();
//        this.userDao = new UserDaoImpl(sqlSession);
        // 通过动态代理的方式获取dao实现
        this.userDao = sqlSession.getMapper(UserDao.class);

    }

    @org.junit.Test
    public void queryUserById() {
        System.out.println(this.userDao.queryUserById("1"));
    }

    @org.junit.Test
    public void queryUserAll() {
        List<User> users = userDao.queryUserAll();
        users.forEach(System.out::println);
    }

    @org.junit.Test
    public void insertUser() {

        User user = new User();
        user.setId("a");
        user.setAge(16);
        user.setBirthday(new Date("1990/09/02"));
        user.setName("大鹏");
        user.setPassword("123456");
        user.setSex(1);
        user.setUser_name("evan");
        this.userDao.insertUser(user);
        this.sqlSession.commit();

    }

    @org.junit.Test
    public void updateUser() {

        User user = new User();
        user.setBirthday(new Date());
        user.setName("静鹏");
        user.setPassword("654321");
        user.setSex(1);
        user.setUser_name("evanjin");
        user.setId("1");
        this.userDao.updateUser(user);
        this.sqlSession.commit();

    }

    @org.junit.Test
    public void deleteUser() {
        this.userDao.deleteUser("a");
        this.sqlSession.commit();
    }
}