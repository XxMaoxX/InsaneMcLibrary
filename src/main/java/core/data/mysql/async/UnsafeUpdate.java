package core.data.mysql.async;



import core.data.Callback;
import core.data.mysql.MySQLAcces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UnsafeUpdate implements Runnable{

    private String statement;
    private MySQLAcces acces;
    private Callback<Integer, SQLException> callback;
    private Connection connection = acces.getConnection();

    public UnsafeUpdate(String statement, MySQLAcces acces, Callback<Integer, SQLException> callback) {
        this.statement = statement;
        this.acces = acces;
        this.callback = callback;
    }

    @Override
    public void run() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(statement);

            if(callback != null) {
                callback.call(preparedStatement.executeUpdate(), null);
            }
        } catch (SQLException e) {
            if(callback != null) {
                callback.call(null, e);
            }
        } finally {
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
