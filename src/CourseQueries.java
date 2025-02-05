
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mattr
 */
public class CourseQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement getAllCourses;
    private static PreparedStatement addCourse;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getCourseSeats;
    private static PreparedStatement dropCourse;
    private static ResultSet resultSet;
    
   public static ArrayList<CourseEntry> getAllCourses(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<CourseEntry> courseList = new ArrayList<CourseEntry>();
        try
        {
            getAllCourses = connection.prepareStatement("select * from app.course where semester = (?)");
            getAllCourses.setString(1,semester);
            resultSet = getAllCourses.executeQuery();
            
            while(resultSet.next())
            {
                CourseEntry nextEntry = new CourseEntry(resultSet.getString("semester"),resultSet.getString("courseCode"),resultSet.getString("description"),resultSet.getInt("seats"));
                courseList.add(nextEntry);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseList;
        
    }
   
   public static void addCourse(CourseEntry course)
    {
        connection = DBConnection.getConnection();
        try
        {
            String sem = course.getSemester();
            String cC = course.getCourseCode();
            String des = course.getDescription();
            int seat = course.getSeats();
            
            addCourse = connection.prepareStatement("insert into app.course (semester, courseCode, description, seats) values (?,?,?,?)");
            addCourse.setString(1,sem);
            addCourse.setString(2,cC);
            addCourse.setString(3,des);
            addCourse.setInt(4,seat);
            
            addCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> courseList = new ArrayList<String>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("select courseCode from app.course where semester = (?)");
            getAllCourseCodes.setString(1,semester);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next())
            {
                courseList.add(resultSet.getString("CourseCode"));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseList;
        
    }
    
    public static int getCourseSeats(String semester, String courseCode)
    {
        int courseSeats = 0;
        connection = DBConnection.getConnection();
        try
        {
            getCourseSeats = connection.prepareStatement("select seats from app.course where semester = ? and courseCode = ? ");
            getCourseSeats.setString(1,semester);
            getCourseSeats.setString(2,courseCode);
            resultSet = getCourseSeats.executeQuery();
            if (resultSet.next()){
                courseSeats = resultSet.getInt(1);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return courseSeats;
        
    }
    
    
    public static void dropCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropCourse = connection.prepareStatement("delete from app.course where semester = ? and courseCode = ? ");
            dropCourse.setString(1,semester);
            dropCourse.setString(2,courseCode);
            dropCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
}

