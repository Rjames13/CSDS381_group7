package com;
import java.sql.*;
import java.util.Scanner;

public class App {
    String connectionUrl;
    Connection connection;



    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {
        System.out.println("Welcome to ClassBox Command Line Inteface");
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter username");

        String userName = myObj.nextLine();  // Read user input
        System.out.println("Enter password");

        String password = myObj.nextLine();  // Read user input

        String connectionUrl =
                "jdbc:sqlserver://robin-amd.STUDENT.CWRU.EDU:1433;"
                        + "database=university;"
                        + "user="+userName+";"
                        + "password="+password+";"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;"
                        + "loginTimeout=30;";

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();) {

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT top 100 ID, Name, Dept_Name from Instructor";
            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
