package org.example.h2Example;

import org.example.util.H2JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class H2CreateExample {
    private static final String createTableSQL = "create table users(id int  primary key, name varchar(20), email varchar(20),  country varchar(20),  password varchar(20));";

    public static void main(String[] args) {
        var h2CreateExample = new H2CreateExample();
        h2CreateExample.createTable();

        try (Connection connection = H2JDBCUtils.getConnection()) {
            var statement = connection.createStatement();
            statement.execute(createTableSQL);
        } catch (SQLException ex){
            H2JDBCUtils.printSQLException(ex);
        }
    }



    private void createTable() {
        System.out.println(createTableSQL);

        try (Connection connection = H2JDBCUtils.getConnection()) {
            var statement = connection.createStatement();
            statement.execute(createTableSQL);
        } catch (SQLException ex){
            H2JDBCUtils.printSQLException(ex);
        }
    }
}
