package kr.ac.jejunu.userdao;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {//추상화된 함수를 가지고 있으니까 class도 추상화클래스가 되어야 한다.
    //private final ConnectionMaker connectionMaker = new JejuConnectionMaker();//new라는 거 자체가 dependency임 의존할 수 밖에 없음. 이 UserDao입장에서는 dependency를 가지고 싶지 않음 -> 어떻게 해야 할까? -> userDao를 호출하는 userdao한테 너가 dependency를 결정해서 나한테 넘겨줘 하는 거임 -> UserDaoTests에서 호출
//    private final ConnectionMaker connectionMaker;
    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User get(Long id) throws SQLException, ClassNotFoundException {//User의 객체와 같은 데이터타입을 return해줘야 함, 함수명은 get이고 파라미터로 long데이터 타입의 id를 받음
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement("select * from userinfo where id = ?");
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
            }
        }   finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //어떤 이유인지는 모르겠지만 exception이 발생해서 서버가 죽는다고 하자
//        Connection connection = dataSource.getConnection();
//
//        PreparedStatement preparedStatement =
//                connection.prepareStatement("select * from userinfo where id = ?");//userinfo테이블에서 id를 다 가지고 온다.
//        preparedStatement.setLong(1, id);
//
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//        resultSet.next();
//        User user = new User();
//        user.setId(resultSet.getLong("id"));
//        user.setName(resultSet.getString("name"));
//        user.setPassword(resultSet.getString("password"));
//
//        resultSet.close();//자원해제
//        preparedStatement.close();
//        connection.close();

        return user;
    }

    public Long add(User user) throws SQLException, ClassNotFoundException {//long데이터 타입을 return하고 add라는 함수명을 가지고 User의 객체인 user를 파라미터로 받는다.
        Connection connection = null;
        PreparedStatement preparedStatement = null;//userinfo테이블에 name, password의 값이 들어오면 add한다.
        Long id = null;
        try {
            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement("insert into userinfo(name, password) values(?, ?)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

            preparedStatement.executeUpdate();

            id = getLastInsertId(connection);
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return id;
    }

    public void update(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;//userinfo테이블에 name, password의 값이 들어오면 add한다.

        try {
            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement("update userinfo set name = ?, password = ? where id = ?");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setLong(3, user.getId());

            preparedStatement.executeUpdate();
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void delete(Long id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;//userinfo테이블에 name, password의 값이 들어오면 add한다.

        try {
            connection = dataSource.getConnection();

            preparedStatement = connection.prepareStatement("delete from userinfo where id = ? ");
            preparedStatement.setLong(1, id );

            preparedStatement.executeUpdate();
        } finally {
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Long getLastInsertId(Connection connection) throws SQLException {
        ResultSet resultSet = null;
        Long id = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select last_insert_id()");//가장 최근에 삽입된 id를 가지고 온다.
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            id = resultSet.getLong(1);
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }

//    private Connection getConnection() throws SQLException, ClassNotFoundException {
        //url, user, password등의 정보가 들어가 있으니까 private로 만들어 준다. 다른 db를 사용하려면 이 getConnection부분이 달라져야 한다. 매번 달라져야 하기때문에 무엇이 나올지 모른다. -> 추상화 한다. -> private를 public abstract로 바꾼다.
//        return connectionMaker.getConnection();
//    }
    //jejuproductUserdao, hallaproductUserdao를 생성한다고 할 때(새로운 product클래스를 생성한다고 할 때) 똑같은 getConnection을 사용하니까 이것을 delicate해서 밖으로 빼버리자!! -> 하나의 클래스에서는 abstract를 했는데 다른 두개의 클래스에서 필요할 때에는 delicate해서 하나의 클래스로 뽑아냄
}
