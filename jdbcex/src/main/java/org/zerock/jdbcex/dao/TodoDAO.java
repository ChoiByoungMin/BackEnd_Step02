package org.zerock.jdbcex.dao;

import lombok.Cleanup;
import org.zerock.jdbcex.domain.TodoVO;

import java.sql.*;

public class TodoDAO {

    public String getTime(){
        String now = null;
        
        // try~with~Resources
        // try()안에서 생성한 객체는 try이 종료될 때, 자동으로 close가 호출되므로
        // finally구문을 사용하지 않아도 된다. 코드를 간결하게 하려는 목적
        try(Connection connection = ConnectionUtil.INSTANCE.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select now()");
        ResultSet resultSet = preparedStatement.executeQuery();
        ){
            resultSet.next();

            now = resultSet.getString(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return now;
    }

    public String getTime2() throws Exception {

        /*
        *  lombok에서 제공해주는 @Cleanup 을 사용하면
        * 메서드가 종료될 때 자동으로 Close()를 호출해준다.
        * */
        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement("select now()");
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        String now = resultSet.getString(1);

        return now;
    }

    public void insert(TodoVO vo) throws Exception {
        String sql = "insert into tbl_todo (title, dueDate, finished) values (?,?,?)";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());

        preparedStatement.executeUpdate();
    }
}
