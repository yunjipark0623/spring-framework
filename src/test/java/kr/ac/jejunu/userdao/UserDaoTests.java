package kr.ac.jejunu.userdao;

import org.junit.Test;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UserDaoTests {
    @Test
    public void testGet() throws SQLException, ClassNotFoundException {//throw는 예외처리가 발생했을 때 그 책임을 전가하는 것이다(던져버림으로써) 그래도 try catch(예외 블랙홀 - 예외를 흔적도 없이 먹어치움)보다는 낫다, SQLException은 데이터 소스에 액세스 하는 동안 발생하는 오류 및 경고에 대한 정보 제공
        Long id = 1l;
        String name = "허윤호";
        String password = "1234";
//        UserDao userDao = new UserDao();//UserDao의 객체 userDao생성
        //UserDao userDao = new UserDao(new JejuConnectionMaker());//client입장에서는 UserDao가 자신한테 dependency를 던져서 받긴 했지만 자기도 dependency를 가지고 싶지 않음 -> 자기도 던지고 싶음 -> DaoFactory를 만들어서 dependency만을 관리하는 공장을 만들자!!
        DaoFactory daofactory = new DaoFactory();//이 factory를 생산하는 것이 factory pattern
        UserDao userDao = daofactory.getUserDao();
        User user = userDao.get(id);
        assertThat(user.getId(), is(id));//is는 파라미터값이 같은지 확인한다. user.getId()한 값과 id값이 같은지 확인하는 것이다.
        assertThat(user.getName(), is(name));
        assertThat(user.getPassword(), is(password));
    }

    @Test
    public void testAdd() throws SQLException, ClassNotFoundException {
        String name = "헐크";
        String password = "1111";
        User user = new User();//User의 객체 user생
        user.setName(name);//헐크를 name으로 다시 set한다.
        user.setPassword(password);
//        UserDao userDao = new UserDao(new JejuConnectionMaker());
        DaoFactory daofactory = new DaoFactory();
        UserDao userDao = daofactory.getUserDao();
        Long id = userDao.add(user);
        User resultUser = userDao.get(id);
        assertThat(resultUser.getId(), is(id));
        assertThat(resultUser.getName(), is(name));
        assertThat(resultUser.getPassword(), is(password));
    }

    @Test
    public void testHallaGet() throws SQLException, ClassNotFoundException {//throw는 예외처리가 발생했을 때 그 책임을 전가하는 것이다(던져버림으로써) 그래도 try catch(예외 블랙홀 - 예외를 흔적도 없이 먹어치움)보다는 낫다, SQLException은 데이터 소스에 액세스 하는 동안 발생하는 오류 및 경고에 대한 정보 제공
        Long id = 1l;
        String name = "허윤호";
        String password = "1234";
        //UserDao userDao = new UserDao();//UserDao의 객체 userDao생성
        //UserDao userDao = new UserDao(new HallaConnectionMaker());
        DaoFactory daofactory = new DaoFactory();
        UserDao userDao = daofactory.getUserDao();
        User user = userDao.get(id);
        assertThat(user.getId(), is(id));//is는 파라미터값이 같은지 확인한다. user.getId()한 값과 id값이 같은지 확인하는 것이다.
        assertThat(user.getName(), is(name));
        assertThat(user.getPassword(), is(password));
    }
}
