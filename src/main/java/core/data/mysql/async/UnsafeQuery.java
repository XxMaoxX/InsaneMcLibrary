package core.data.mysql.async;

import core.data.Callback;
import core.data.mysql.MySQLAcces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnsafeQuery implements Runnable {

    private String preparedStatement;
    private MySQLAcces acces;
    private Callback<ResultSet, SQLException> callback;
    private Connection connection = acces.getConnection();

    public UnsafeQuery(String statement, MySQLAcces acces, Callback<ResultSet, SQLException> callback) {
        this.preparedStatement = statement;
        this.acces = acces;
        this.callback = callback;
    }


    @Override
    public void run() {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(preparedStatement);
            resultSet = statement.executeQuery();
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
                    statement.close();
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
