
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author mattr
 */
public class ScheduleQueries {
    private static Connection connection;
    private static ArrayList<String> faculty = new ArrayList<String>();
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement upgradeSchedule;
    private static PreparedStatement UpgradeWaitlistScheduleEntry;
    private static ResultSet resultSet;
    
    
    
    
    
    
    public static void addScheduleEntry(ScheduleEntry schedule)
    {
        connection = DBConnection.getConnection();
        try
        {
            String semester = schedule.getSemester();
            String courseCode = schedule.getCourseCode();
            String studentID = schedule.getStudentID();
            String status = schedule.getStatus();
            Timestamp timestamp = schedule.getTimestamp();
            
            addScheduleEntry = connection.prepareStatement("insert into app.schedule (semester, courseCode, studentID, status, timestamp) values (?,?,?,?,?)");
            addScheduleEntry.setString(1, semester);
            addScheduleEntry.setString(2, courseCode);
            addScheduleEntry.setString(3, studentID);
            addScheduleEntry.setString(4, status);
            addScheduleEntry.setTimestamp(5, timestamp);
            addScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static int getScheduledStudentCount(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int count = 0;
        try
        {
            getScheduledStudentCount = connection.prepareStatement("select count(studentID) from app.schedule where semester = ? and courseCode = ?");
            getScheduledStudentCount.setString(1,semester);
            getScheduledStudentCount.setString(2,courseCode);
            resultSet = getScheduledStudentCount.executeQuery();
            if (resultSet.next()){
                count = resultSet.getInt(1);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return count;
        
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> scheduleList = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduleByStudent = connection.prepareStatement("select * from app.schedule where studentID = (?) and semester = (?)");
            getScheduleByStudent.setString(1,studentID);
            getScheduleByStudent.setString(2,semester);
            resultSet = getScheduleByStudent.executeQuery();
            
            while(resultSet.next())
            {
                ScheduleEntry nextSchedule = new ScheduleEntry(resultSet.getString("semester"),resultSet.getString("courseCode"),resultSet.getString("studentID"),resultSet.getString("status"),resultSet.getTimestamp("timestamp"));
                scheduleList.add(nextSchedule);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return scheduleList;
        
    }
    
    
    public static ArrayList<ScheduleEntry> getScheduledStudentsByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> scheduleList = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduledStudentCount = connection.prepareStatement("select * from app.schedule where semester = ? and courseCode = ? and status = 's'");
            getScheduledStudentCount.setString(1,semester);
            getScheduledStudentCount.setString(2,courseCode);
            resultSet = getScheduledStudentCount.executeQuery();
            while (resultSet.next()){
                ScheduleEntry nextSchedule = new ScheduleEntry(resultSet.getString("semester"),resultSet.getString("courseCode"),resultSet.getString("studentID"),resultSet.getString("status"),resultSet.getTimestamp("timestamp"));
                scheduleList.add(nextSchedule);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return scheduleList;
        
    }
    
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> scheduleList = new ArrayList<ScheduleEntry>();
        try
        {
            getScheduledStudentCount = connection.prepareStatement("select * from app.schedule where semester = ? and courseCode = ? and status = 'w'");
            getScheduledStudentCount.setString(1,semester);
            getScheduledStudentCount.setString(2,courseCode);
            resultSet = getScheduledStudentCount.executeQuery();
            while (resultSet.next()){
                ScheduleEntry nextSchedule = new ScheduleEntry(resultSet.getString("semester"),resultSet.getString("courseCode"),resultSet.getString("studentID"),resultSet.getString("status"),resultSet.getTimestamp("timestamp"));
                scheduleList.add(nextSchedule);
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return scheduleList;
        
    }
    
    public static void dropScheduleByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = ? and courseCode = ?");
            dropScheduleByCourse.setString(1,semester);
            dropScheduleByCourse.setString(2,courseCode);
            dropScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            dropStudentScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = ? and studentID = ? and courseCode = ?");
            dropStudentScheduleByCourse.setString(1,semester);
            dropStudentScheduleByCourse.setString(2,studentID);
            dropStudentScheduleByCourse.setString(3,courseCode);
            dropStudentScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        
    }
    
    public static String UpgradeWaitlistScheduleEntry(String semester, String courseCode){
        String studentID = "";
        connection = DBConnection.getConnection();
        try
        {
            UpgradeWaitlistScheduleEntry = connection.prepareStatement("select * from app.schedule where semester = ? and courseCode = ? and status = 'w' order by timestamp asc");
            UpgradeWaitlistScheduleEntry.setString(1,semester);
            UpgradeWaitlistScheduleEntry.setString(2,courseCode);
            resultSet = UpgradeWaitlistScheduleEntry.executeQuery();
            if (resultSet.next()){
                studentID = resultSet.getString("studentID");
            }
            if(studentID.isEmpty()==false){
                upgradeSchedule = connection.prepareStatement("update app.schedule set status = 's' where semester = ? and courseCode = ? and studentID = ?");
                upgradeSchedule.setString(1,semester);
                upgradeSchedule.setString(2,courseCode);
                upgradeSchedule.setString(3,studentID);
                upgradeSchedule.executeUpdate();
            }
        
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return studentID;
    }
    
}
