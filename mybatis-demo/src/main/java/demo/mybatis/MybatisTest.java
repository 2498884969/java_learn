package demo.mybatis;

import demo.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class MybatisTest {
    public static void main(String[] args) throws Exception {
        // 1. 指定全局配置文件
        String resource = "mybatis-config.xml";
        // 2. 读取配置文件
        InputStream inputStream = Resources.getResourceAsStream(resource);
        // 3. 构建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 4. 获取sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            // 5. 操作CRUD，第一个参数：指定statement，规则：命名空间+“.”+statementId
            // 6. 第二个参数：指定传入sql的参数：这里是用户id
            User user = sqlSession.selectOne("MyMapper.selectUser", 1);
            System.err.println(user);
        } finally {
            sqlSession.close();
        }
    }
}

