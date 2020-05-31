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
import java.util.UUID;

import static org.junit.Assert.*;

public class UserMapperTest {

    private UserMapper userMapper;
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
        // 6. 使用动态代理
        this.userMapper = sqlSession.getMapper(UserMapper.class);


    }

    @Test
    public void login() {

        User user = userMapper.login("hj","123456");
        System.err.println(user);
    }

    @Test
    public void queryUserByTableName() {

        List<User> users = userMapper.queryUserByTableName("tb_user");
        users.forEach(System.err::println);
    }

    @Test
    public void queryUserById() {
    }

    @Test
    public void queryUserAll() {
    }

    @Test
    public void insertUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString().substring(0,16));
        user.setAge(20);
        user.setBirthday(new Date());
        user.setName("大神");
        user.setPassword("123456");
        user.setSex(2);
        user.setUserName("bigGod222");
        this.userMapper.insertUser(user);
        sqlSession.commit();
        System.out.println(user.getId());

    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUserById() {
    }
}