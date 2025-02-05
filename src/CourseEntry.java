

/**
 *
 * @author mattr
 */
public class CourseEntry {
    private String semester;
    private String courseCode;
    private String description;
    private int seats;
    public CourseEntry(String semester, String courseCode, String description, int seats) {
        this.semester = semester;
        this.courseCode = courseCode;
        this.description = description;
        this.seats = seats;
    }

    public String getSemester() {
        return semester;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getDescription() {
        return description;
    }

    public int getSeats() {
        return seats;
    }
    
}
