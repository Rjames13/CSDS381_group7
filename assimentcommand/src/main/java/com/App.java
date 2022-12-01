package com;

import java.sql.*;
import java.util.Scanner;

public class App {
	private static String table;
	private static int operation;


    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {
        System.out.println("Welcome to ClassBox Command Line Interface");
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter username");

        String userName = myObj.nextLine();  // Read user input
        System.out.println("Enter password");

        String password = myObj.nextLine();  // Read user input

        String connectionUrl =
                "jdbc:sqlserver://robin-amd.STUDENT.CWRU.EDU:1433;"
                        + "database=assignments;"
                        + "user=" + userName + ";"
                        + "password=" + password + ";"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;"
                        + "loginTimeout=30;";


        ResultSet resultSet = null;
        Connection connection;

        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(connectionUrl);
            connection.setAutoCommit(false);
            while (true) {
	            selectOperation(connection);
	            selectTable(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        catch (ClassNotFoundException e) {
        	e.printStackTrace();
        	System.exit(1);
        }


    }
    
    private static void selectTable(Connection connection) {
    	// Local variable
        int swValue;
        
    	// Display menu graphics
        System.out.println("=================================");
        System.out.println("|   Menu 2                      |");
        System.out.println("=================================");
        System.out.println("| Options:                      |");
        System.out.println("|        1. AssignmentType      |");
        System.out.println("|        2. Course              |");
        System.out.println("|        3. CourseAssignment    |");
        System.out.println("|        4. Grade               |");
        System.out.println("|        5. Student             |");
        System.out.println("|        6. Back                |");
        System.out.println("=================================");
        swValue = Keyin.inInt(" Select option: ");
        
        // Switch construct
        switch (swValue) {
            case 1:
                table = "AssignmentType";
                break;
            case 2:
                table = "Course";
                break;
            case 3:
                table = "CourseAssignment";
                break;
            case 4:
                table = "Grade";
                break;
            case 5:
                //table = "Student";
            	switch (operation) {
            		case 1: 
            			insertStudent(connection);
            			break;
            		case 2:
            			selectStudentByID(connection);
            			break;
            		case 3:
            			updateStudent(connection);
            		case 4:
            			deleteStudent(connection);
            	}
                break;
            case 6:
                System.out.println("Back selected");

                break;


            default:
                System.out.println("Invalid selection");
                selectTable(connection);
                break;

        }
    }

    private static void selectOperation(Connection connection) {
        // Local variable
        int swValue;

        // Display menu graphics
        System.out.println("=================================");
        System.out.println("|   Menu 1                      |");
        System.out.println("=================================");
        System.out.println("| Options:                      |");
        System.out.println("|        1. INSERT              |");
        System.out.println("|        2. SELECT              |");
        System.out.println("|        3. UPDATE              |");
        System.out.println("|        4. DELETE              |");
        System.out.println("|        5. Save & Exit         |");
        System.out.println("|        6. Exit without Saving |");
        System.out.println("=================================");
        swValue = Keyin.inInt(" Select option: ");

        if (swValue > 0 && swValue < 5) {
        	operation = swValue;
        }
        else if (swValue == 5) {
            System.out.println("Exit selected");
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.exit(0);
        }
        else if (swValue == 6) {
            System.out.println("Exit selected");
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(1);
            }

            System.exit(0);
        }
        else {
            System.out.println("Invalid selection");
            selectOperation(connection);
        }

    }
    
    private static void insertStudent(Connection connection) {
    	// Local variable
        String studentName;
        int studentYear;
        studentName = Keyin.inString(" Input student name: ");
        studentYear = Keyin.inInt(" Input student year: ");
        String query = "insert into Student(studentname, studentyear) values (?, ?);";
        try (var statement = connection.prepareStatement(query);) {
        	statement.setString(1, studentName);
        	statement.setInt(2, studentYear);
        	statement.execute();
        	statement.close();
        }
        catch (SQLException e) {
        	
        }
    }
    
    private static void selectStudentByID(Connection connection) {
    	int studentID;
    	studentID = Keyin.inInt(" Input student ID(-1 to see all student): ");
        if (studentID>0){
    	String query = "select * from student where student.studentID = ?";
    	try (var statement = connection.prepareStatement(query);) {
        	statement.setInt(1, studentID);
        	ResultSet set = statement.executeQuery();
            System.out.println("ID Name Year");
        	while (set.next()) {
        		String s1 = set.getString("studentID");
        		String s2 = set.getString("studentName");
        		String s3 = set.getString("studentYear");
        		System.out.println(s1 + " " + s2 + " " + s3);
        		// todo replace with string format
        	}
        	statement.close();
        }
        catch (SQLException e) {
        	
        }}
        else if (studentID<0){
            String query = "select * from student ";
            try (var statement = connection.prepareStatement(query);) {

                ResultSet set = statement.executeQuery();
                System.out.println("ID Name Year");
                while (set.next()) {
                    String s1 = set.getString("studentID");
                    String s2 = set.getString("studentName");
                    String s3 = set.getString("studentYear");
                    System.out.println(s1 + " " + s2 + " " + s3);
                    // todo replace with string format
                }
                statement.close();
            }
            catch (SQLException e) {

            }}

    }
    
    private static void updateStudent(Connection connection) {
    	int studentID;
    	String studentName;
        int studentYear;
        studentName = Keyin.inString(" Input new student name: ");
        studentYear = Keyin.inInt(" Input new student year: ");
    	studentID = Keyin.inInt(" Input student ID: ");
    	String query = "update student set studentname=?, studentyear=? where student.studentID=?";
    	try (var statement = connection.prepareStatement(query);) {
        	statement.setString(1, studentName);
        	statement.setInt(2, studentYear);
        	statement.setInt(3, studentID);
        	statement.execute();
        	statement.close();
        }
        catch (SQLException e) {
        	
        }
    }
    
    private static void deleteStudent(Connection connection) {
    	int studentID;
    	studentID = Keyin.inInt(" Input student ID: ");
    	String query = "delete from student where studentID = ?";
    	try (var statement = connection.prepareStatement(query);) {
        	statement.setInt(1, studentID);
        	statement.execute();
        	statement.close();
        }
        catch (SQLException e) {
        	
        }
    }
    
    
    private static void selectionmenu2() {
        // Local variable
        int swValue;

        // Display menu graphics
        System.out.println("============================");
        System.out.println("|   Menu 2    |");
        System.out.println("============================");
        System.out.println("| Options:                 |");
        System.out.println("|        1. Option 1       |");
        System.out.println("|        2. Option 2       |");
        System.out.println("|        3. Exit           |");
        System.out.println("============================");
        swValue = Keyin.inInt(" Select option: ");

        // Switch construct
        switch (swValue) {
            case 1:
                System.out.println("Option 1 selected");
                break;
            case 2:
                System.out.println("Option 2 selected");
                break;
            case 3:
                System.out.println("Exit selected");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid selection");
                break; // This break is not really necessary

        }


    }
    private static void selectionmenu3() {
        // Local variable
        int swValue;

        // Display menu graphics
        System.out.println("============================");
        System.out.println("|   Menu 3    |");
        System.out.println("============================");
        System.out.println("| Options:                 |");
        System.out.println("|        1. Option 1       |");
        System.out.println("|        2. Option 2       |");
        System.out.println("|        3. Exit           |");
        System.out.println("============================");
        swValue = Keyin.inInt(" Select option: ");

        // Switch construct
        switch (swValue) {
            case 1:
                System.out.println("Option 1 selected");
                break;
            case 2:
                System.out.println("Option 2 selected");
                break;
            case 3:
                System.out.println("Exit selected");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid selection");
                break; // This break is not really necessary

        }


    }


}

class Keyin {

    //*******************************
    //   support methods
    //*******************************
    //Method to display the user's prompt string
    public static void printPrompt(String prompt) {
        System.out.print(prompt + " ");
        System.out.flush();
    }

    //Method to make sure no data is available in the
    //input stream
    public static void inputFlush() {
        int dummy;
        int bAvail;

        try {
            while ((System.in.available()) != 0)
                dummy = System.in.read();
        } catch (java.io.IOException e) {
            System.out.println("Input error");
        }
    }

    //********************************
    //  data input methods for
    //string, int, char, and double
    //********************************
    public static String inString(String prompt) {
        inputFlush();
        printPrompt(prompt);
        return inString();
    }

    public static String inString() {
        int aChar;
        String s = "";
        boolean finished = false;

        while (!finished) {
            try {
                aChar = System.in.read();
                if (aChar < 0 || (char) aChar == '\n')
                    finished = true;
                else if ((char) aChar != '\r')
                    s = s + (char) aChar; // Enter into string
            } catch (java.io.IOException e) {
                System.out.println("Input error");
                finished = true;
            }
        }
        return s;
    }

    public static int inInt(String prompt) {
        while (true) {
            inputFlush();
            printPrompt(prompt);
            try {
                return Integer.valueOf(inString().trim()).intValue();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Not an integer");
            }
        }
    }

    public static char inChar(String prompt) {
        int aChar = 0;

        inputFlush();
        printPrompt(prompt);

        try {
            aChar = System.in.read();
        } catch (java.io.IOException e) {
            System.out.println("Input error");
        }
        inputFlush();
        return (char) aChar;
    }

    public static double inDouble(String prompt) {
        while (true) {
            inputFlush();
            printPrompt(prompt);
            try {
                return Double.valueOf(inString().trim()).doubleValue();
            } catch (NumberFormatException e) {
                System.out
                        .println("Invalid input. Not a floating point number");
            }
        }
    }
}
