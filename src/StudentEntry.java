
/**
 *
 * @author mattr
 */
public class StudentEntry {
    private String studentID;
    private String firstName;
    private String lastName;

    public StudentEntry(String studentID, String firstName, String lastName) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public String toString(){
        return String.format("%s, %s", lastName,firstName);
    }
        
    
}
