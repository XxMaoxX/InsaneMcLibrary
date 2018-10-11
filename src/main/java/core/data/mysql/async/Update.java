package core.data.mysql.async;


import core.data.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update implements Runnable {

    private PreparedStatement preparedStatement;
    private Callback<Integer, SQLException> callback;
    private Connection connection;

    public Update(PreparedStatement statement, Connection connection, Callback<Integer, SQLException> callback) {
        this.preparedStatement = statement;
        this.connection = connection;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            if (callback != null) {
                callback.call(preparedStatement.executeUpdate(), null);
            }
        } catch (SQLException e) {
            if (callback != null) {
                callback.call(null, e);
            }
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
