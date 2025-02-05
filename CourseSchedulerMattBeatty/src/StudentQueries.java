
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mattr
 */
public class StudentQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement getAllStudents;
    private static PreparedStatement addStudent;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    
    public static ArrayList<StudentEntry> getAllStudents()
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> studentList = new ArrayList<StudentEntry>();
        try
        {
            getAllStudents = connection.prepareStatement("select * from app.student order by studentID");
            resultSet = getAllStudents.executeQuery();
            
            while(resultSet.next())
            {
                StudentEntry nextEntry = new StudentEntry(resultSet.getString("studentID"),resultSet.getString("firstName"),resultSet.getString("lastName"));
                studentList.add(nextEntry);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return studentList;
        
    }
    
    public static void addStudent(StudentEntry student)
    {
        connection = DBConnection.getConnection();
        try
        {
            String id = student.getStudentID();
            String first = student.getFirstName();
            String last = student.getLastName();
            
            addStudent = connection.prepareStatement("insert into app.student (studentID, firstName, lastName) values (?,?,?)");
            addStudent.setString(1,id);
            addStudent.setString(2,first);
            addStudent.setString(3,last);
            addStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    
    
    public static StudentEntry getStudent(String studentID)
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> studentList = new ArrayList<StudentEntry>();
        try
        {
            getStudent = connection.prepareStatement("select * from app.student where studentID = ?");
            getStudent.setString(1,studentID);
            resultSet = getStudent.executeQuery();
            
            while(resultSet.next())
            {
                StudentEntry nextEntry = new StudentEntry(resultSet.getString("studentID"),resultSet.getString("firstName"),resultSet.getString("lastName"));
                studentList.add(nextEntry);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return studentList.get(0);
        
    }
    
    
    
    
    
    public static void dropStudent(String studentID)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropStudent = connection.prepareStatement("delete from app.student where studentID = ?");
            dropStudent.setString(1,studentID);
            dropStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    
    
    
}
