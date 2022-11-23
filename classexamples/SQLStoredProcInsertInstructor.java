// Demo for invoking a stored procedure within code
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.sql.ResultSet; // need for select queries to get result set
//import java.sql.Statement; // if writing a sql statement directly
import java.util.Scanner; 


public class SQLStoredProcInsertInstructor {

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {
        String connectionUrl = 
        "jdbc:sqlserver://localhost;"
                + "database=university;"
                + "user=dbuser;" // might need to change this to your database user such as "sa". not good to use "sa"
                + "password=ChangeToYourPasswordForUser-dbuser;"
                + "encrypt=true;"
                + "trustServerCertificate=true;"
                + "loginTimeout=30;";
        Scanner myObj = new Scanner(System.in);
        String inpDeptName, inpBuilding;
        float inpBudget;

        // Enter username and press Enter
       System.out.println("Enter dept_name then enter. "); 
       inpDeptName = myObj.nextLine();   
       System.out.println("Enter building then enter.");
       inpBuilding = myObj.nextLine();
       System.out.println("Enter budget as numeric (12,2) then enter. "); 
       inpBudget = myObj.nextFloat();
       
       myObj.close();

       System.out.println("dept_name: " + inpDeptName + "  building: " 
             + inpBuilding + "   budget: " + inpBudget);
        
        // 3 ? because 3 parameters to stored proc needed. Index for paramters start at 1.
        String callStoredProc = "{call dbo.insertDepartment(?,?,?)}"; 
   
        try (Connection connection = DriverManager.getConnection(connectionUrl);
                PreparedStatement prepsInsertInstructor = connection.prepareStatement(callStoredProc);) {
            // 3 parameters to stored proc start with a parameter index of 1
            prepsInsertInstructor.setString(1, inpDeptName); 
            prepsInsertInstructor.setString(2, inpBuilding);
            prepsInsertInstructor.setFloat(3, inpBudget);
            prepsInsertInstructor.execute();
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
