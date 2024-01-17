package com.test.Scanning_Report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.Test;

public class Dataset {

    @Test
    public void testDB() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver loaded");

        String url = "jdbc:mysql://apollo2.humanbrain.in:3306/HBA_V2";
        String username = "root";
        String password = "Health#123";
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("MYSQL database connected");

        // Call the method to execute and print the queries
        try {
        	executeAndPrintQuery(connection, "Scanned Images", "SELECT SUM(totalImages) FROM `slidebatch`;");
            executeAndPrintQuery(connection, "Failed Images", "SELECT SUM(totalImages) FROM `slidebatch`WHERE process_status=5;");
            executeAndPrintQuery(connection, "Image QC pending", "SELECT SUM(totalImages) FROM `slidebatch`WHERE process_status=6;");
            executeAndPrintQuery(connection, "Image QC completed", "SELECT SUM(totalImages) FROM `slidebatch` WHERE process_status >6 AND process_status <=11;");
            executeAndPrintQuery(connection, "Image QC Failed", "SELECT COUNT(*) FROM `huron_slideinfo` WHERE isQC=0;");
            executeAndPrintQuery(connection, "Image QC Passed", "SELECT COUNT(*) FROM `huron_slideinfo` WHERE isQC=1;");
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the connection
            connection.close();
        }
    }

    private void executeAndPrintQuery(Connection connection, String message, String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        // Process the result
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            System.out.println("Count of " + message + ": " + count);
        }

        // Close the result set and statement
        resultSet.close();
        statement.close();
    }
}
