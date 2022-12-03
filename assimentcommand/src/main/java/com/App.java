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
                selectUseCase(connection);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }


    }

    private static void selectUseCase(Connection connection) {
        // Local variable
        int swValue;

        // Display menu graphics //TODO make look nice

        System.out.println("|   Menu 1                      |");

        System.out.println("| Options:                      |");
        System.out.println("|        1. Get Student Assignment            ");
        System.out.println("|        2. Change Grade for Task            ");
        System.out.println("|        3. Get Students Stats for Tasks             ");
        System.out.println("|        4. Add Student to System            ");
        System.out.println("|        5. Apply Late Penalty of overdue Tasks             ");
        System.out.println("|        6. Grade Partner Assignment");
        System.out.println("|        7. Drop Low Score         ");
        System.out.println("|        8. Update TA Course         ");
        System.out.println("|        9. Delete failed students           ");
        System.out.println("|        10. Drop all courses for a student           ");
        System.out.println("|        11. Save & Exit         |");
        System.out.println("|        12. Exit without Saving |");
        System.out.println("---------------------------------");
        swValue = InputTool.getasInt(" Select option: ");


        switch (swValue) {
            case 1:
                getStudentAssignment(connection);
                break;
            case 2:
                changeGrade(connection);
                break;
            case 3:
                studentStats(connection);
                break;
            case 4:
                addStudent(connection);
                break;
            case 5:
                latepenalty(connection);
                break;
            case 6:
                updatepartnerScore(connection);
                break;
            case 7:
                dropscore(connection);
                break;
            case 8:
            	updateTACourse(connection);
            	break;
            case 9:
            	removeFailed(connection);
            	break;
            case 10:
                dropAllClasses(connection);
            	break;
            case 11:
                System.out.println("Exit selected");
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                System.exit(0);
                break;
            case 12:
                System.out.println("Exit selected");
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(1);
                }

                System.exit(0);


            default:
                System.out.println("Invalid selection");
                selectUseCase(connection);
                break;

        }


    }
    
    private static void dropAllClasses(Connection connection) {
        int studentID = InputTool.getasInt("Student ID:");

        String callStoredProc = "{call dbo.dropAllClasses(?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);

            // 1 parameter to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, studentID);
             prepsStoredProc.execute();
             prepsStoredProc.close();
            System.out.println("All courses dropped");
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private static void updateTACourse(Connection connection) {
    	int id= InputTool.getasInt("TA ID:");
        int oldCourse= InputTool.getasInt("Old Course ID:");
        int newCourse= InputTool.getasInt("New Course ID:");


        String callStoredProc = "{call dbo.updateTACourse(?,?,?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 3 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, id);
            prepsStoredProc.setInt(2, oldCourse);
            prepsStoredProc.setInt(3, newCourse);
             prepsStoredProc.execute();
             prepsStoredProc.close();
            System.out.println("TA Course Updated");

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void removeFailed(Connection connection) {
        int lowScore= InputTool.getasInt("Failing Score:");
        String callStoredProc = "{call dbo.removeFails(?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 1 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, lowScore);

            prepsStoredProc.execute();
            prepsStoredProc.close();
            System.out.println("Removed failed students");

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void getStudentAssignment(Connection connection) {
       int id= InputTool.getasInt("Student ID:");
       String date= InputTool.getasString("Date to check by:");
       String callStoredProc = "{call dbo.getToDoForStudent(?,?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 4 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, id);
            prepsStoredProc.setString(2, date);

            ResultSet set = prepsStoredProc.executeQuery();
            System.out.println("ID Name Year");
            while (set.next()) {
                String s1 = set.getString("studentID");
                String s2 = set.getString("studentName");
                String s3 = set.getString("taskDescription");
                String s4 = set.getString("assName");
                String s5 = set.getString("assDescription");
                String s6 = set.getString("assDueDate");
                System.out.println(s1 + " " + s2 + " " + s3+" " +s4 + " " + s5 + " " + s6);
                // todo replace with string format
            }
            prepsStoredProc.close();

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private static void changeGrade(Connection connection) {
        int id= InputTool.getasInt("Student ID:");
        int task= InputTool.getasInt("Task ID:");
        double points= InputTool.getasDouble("New Grade:");


        String callStoredProc = "{call dbo.dropScore(?,?,?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 4 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, id);
            prepsStoredProc.setInt(2, task);
            prepsStoredProc.setDouble(3, points);
            prepsStoredProc.execute();
            prepsStoredProc.close();
            System.out.println("Score Updated");

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }




    private static void studentStats(Connection connection) {
        int id= InputTool.getasInt("Student ID:");



        String callStoredProc = "{call dbo.getGradeForStudent(?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 4 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, id);

            ResultSet set = prepsStoredProc.executeQuery();
            System.out.println("ID Name Year");
            while (set.next()) {
                String s1 = set.getString("studentID");
                String s2 = set.getString("studentName");
                String s3 = set.getString("Minimum");
                String s4 = set.getString("Unweighted Average");
                String s5 = set.getString("Maximum");

                System.out.println(s1 + " " + s2 + " " + s3+" " +s4 + " " + s5 );
                // todo replace with string format
            }

            prepsStoredProc.close();
            System.out.println("Score Updated");

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private static void addStudent(Connection connection) {
        String name= InputTool.getasString("Student Name:");
        int year= InputTool.getasInt("Year:");



        String callStoredProc = "{call dbo.InsertStudent(?,?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 4 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setString(1, name);
            prepsStoredProc.setInt(2, year);

            prepsStoredProc.execute();
            prepsStoredProc.close();
            System.out.println("Student Add");

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void latepenalty(Connection connection) {
        int id= InputTool.getasInt("Student ID:");
        int task= InputTool.getasInt("Task ID:");
        double points= InputTool.getasDouble("Late penalty in points:");


        String callStoredProc = "{call dbo.latePenalty(?,?,?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 4 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, id);
            prepsStoredProc.setInt(2, task);
            prepsStoredProc.setDouble(3, points);
            prepsStoredProc.execute();
            prepsStoredProc.close();
            System.out.println("Score Updated");

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void dropscore(Connection connection) {
        int id= InputTool.getasInt("Student ID:");
        int task= InputTool.getasInt("Task ID:");
        double points= InputTool.getasDouble("Scores will be dropped if under:");


        String callStoredProc = "{call dbo.lowScore(?,?,?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 4 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, id);
            prepsStoredProc.setInt(2, task);
            prepsStoredProc.setDouble(3, points);
            prepsStoredProc.execute();
            prepsStoredProc.close();
            System.out.println("Score Updated");

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private static void updatepartnerScore(Connection connection) {
        int id= InputTool.getasInt("First Student ID:");
        int id2= InputTool.getasInt("Second Student ID:");
        int task= InputTool.getasInt("Task ID:");
        double points= InputTool.getasDouble("Late penalty in points:");


        String callStoredProc = "{call dbo.updatePartnerScore(?,?,?,?)}";
        CallableStatement prepsStoredProc = null;
        try {
            prepsStoredProc = connection.prepareCall(callStoredProc);


            // 4 parameters to stored proc start with a parameter index of 1
            prepsStoredProc.setInt(1, id);
            prepsStoredProc.setInt(2, id);
            prepsStoredProc.setInt(3, task);
            prepsStoredProc.setDouble(4, points);
             prepsStoredProc.execute();
            prepsStoredProc.close();
            System.out.println("Score Updated");

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
    class InputTool {


        public static void prompt(String prompt) {
            System.out.print(prompt + " ");
            System.out.flush();
        }


        public static void flushInput() {
            int temp;
            int temp2;

            try {
                while ((System.in.available()) != 0)
                    temp = System.in.read();
            } catch (java.io.IOException e) {
                System.out.println("Input error");
            }
        }


        public static String getasString(String prompt) {
            flushInput();
            prompt(prompt);
            return getasString();
        }

        public static String getasString() {
            int x;
            String s = "";
            boolean finished = false;

            while (!finished) {
                try {
                    x = System.in.read();
                    if (x < 0 || (char) x == '\n')
                        finished = true;
                    else if ((char) x != '\r')
                        s = s + (char) x; // Enter into string
                } catch (java.io.IOException e) {
                    System.out.println("Input error");
                    finished = true;
                }
            }
            return s;
        }

        public static int getasInt(String prompt) {
            while (true) {
                flushInput();
                prompt(prompt);
                try {
                    return Integer.valueOf(getasString().trim()).intValue();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                }
            }
        }



        public static double getasDouble(String prompt) {
            while (true) {
                flushInput();
                prompt(prompt);
                try {
                    return Double.valueOf(getasString().trim()).doubleValue();
                } catch (NumberFormatException e) {
                    System.out
                            .println("Invalid input");
                }
            }
        }
    }










/*

    // old stuff that may still be useful
    
    private static void selectTable(Connection connection) {
    	// Local variable
        int swValue;
        
    	// Display menu graphics
        System.out.println("--------------------------------=");
        System.out.println("|   Menu 2                      |");
        System.out.println("|-------------------------------|");
        System.out.println("| Options:                      |");
        System.out.println("|        1. AssignmentType      |");
        System.out.println("|        2. Course              |");
        System.out.println("|        3. CourseAssignment    |");
        System.out.println("|        4. Grade               |");
        System.out.println("|        5. Student             |");
        System.out.println("|        6. Back                |");
        System.out.println("---------------------------------");
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

    }*/





