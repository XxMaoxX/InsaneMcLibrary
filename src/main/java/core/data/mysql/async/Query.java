package core.data.mysql.async;



import core.data.Callback;
import core.data.mysql.MySQLAcces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Query implements Runnable {

    private PreparedStatement preparedStatement;
    private Callback<ResultSet, SQLException> callback;
    private Connection connection;

    public Query(PreparedStatement statement, MySQLAcces acces, Callback<ResultSet, SQLException> callback) {
        this.preparedStatement = statement;
        this.connection = acces.getConnection();
        this.callback = callback;
    }

    public Query(PreparedStatement statement, Connection connection, Callback<ResultSet, SQLException> callback) {
        this.preparedStatement = statement;
        this.connection = connection;
        this.callback = callback;
    }


    @Override
    public void run() {
        ResultSet resultSet = null;

        try {
            resultSet = preparedStatement.executeQuery();
            if(callback != null) {
                callback.call(resultSet, null);
            }
        } catch (SQLException e) {
            if(callback != null) {
                callback.call(null, e);
            }
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
