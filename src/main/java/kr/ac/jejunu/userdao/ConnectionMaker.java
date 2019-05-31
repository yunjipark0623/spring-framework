package kr.ac.jejunu.userdao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface ConnectionMaker {
    Connection getConnection() throws SQLException, ClassNotFoundException;
    //url, user, password등의 정보가 들어가 있으니까 private로 만들어 준다. 다른 db를 사용하려면 이 getConnection부분이 달라져야 한다. 매번 달라져야 하기때문에 무엇이 나올지 모른다. -> 추상화 한다. -> private를 public abstract로 바꾼다.
//        Class.forName("com.mysql.cj.jdbc.Driver");
//        return DriverManager.getConnection("jdbc:mysql://172.18.102.128/jeju?serverTimezone=UTC",
//                "portal", "portaljejunu");
//    }

    //이 connectionMaker를 만듬으로써 HallaUserDao, JejuUserDao따로 만들었던걸 이거 하나로 같이 쓸 수 있음 -> HallaUserDao, JejuUserDao필요 없어짐 -> 삭제
    //이 connectionMaker도 jeju, halla에서 필요하니까 추상화할 필요가 있는데 메소드 레이어에서는 abstract였다면 class레이어에서는 interface이다.
}