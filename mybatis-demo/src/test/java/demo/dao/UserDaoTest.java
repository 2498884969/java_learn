package demo.dao;

import demo.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTest {

    private UserDao userDao;

    private SqlSession sqlSession;

    @Before
    public void setUp() throws Exception {

        // 1. 指定全局配置文件
        String resource = "mybatis-config.xml";
        // 2. 读取配置文件
        InputStream inputStream = Resources.getResourceAsStream(resource);
        // 3. 构建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 4. 获取sqlSession
        sqlSession = sqlSessionFactory.openSession();
        // 5. 获取userDao
//        this.userDao = new UserDaoImpl(sqlSession);

        // 6. 使用动态代理
        this.userDao = sqlSession.getMapper(UserDao.class);
    }

    @Test
    public void queryUserById() {
        System.err.println(userDao.queryUserById("1"));
    }

    @Test
    public void queryUserAll() {

        List<User> users = userDao.queryUserAll();
        users.forEach(
                System.err::println
        );

    }

    @Test
    public void insertUser() {
        User user = new User();
        user.setId("3");
        user.setAge(16);
        user.setBirthday(new Date("1990/09/02"));
        user.setName("大鹏");
        user.setPassword("123456");
        user.setSex(1);
        user.setUserName("evan");
        this.userDao.insertUser(user);
        this.sqlSession.commit();

    }

    @Test
    public void updateUser() {

        User user = new User();
        user.setBirthday(new Date());
        user.setName("静鹏");
        user.setPassword("654321");
        user.setSex(1);
        user.setUserName("evanjin1");
        user.setId("1");
        this.userDao.updateUser(user);
        this.sqlSession.commit();

    }

    @Test
    public void deleteUser() {
        userDao.deleteUser("3");
        sqlSession.commit();
    }
}