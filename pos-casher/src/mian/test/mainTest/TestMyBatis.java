package mainTest;

import com.dianba.pos.menu.mapper.UserMapper;
import com.dianba.pos.menu.po.User;
import com.dianba.pos.menu.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2017/4/25 0025.
 */
public class TestMyBatis {

    public static void main(String[] args) {
     ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springmvc.xml");
      UserMapper itemsMapper = (UserMapper) applicationContext.getBean("UserMapper");

       User user= itemsMapper.selectByPrimaryKey(3232L);
       System.out.print(user.getAge());
    }
}
