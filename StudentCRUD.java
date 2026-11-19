import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentCRUD {

    // 1. CREATE
    public static boolean addStudent(String name, String email, String phone, String gender, String dob, int courseId) {
        String sql = "INSERT INTO student (STUDENT_NAME, EMAIL, PHONE, GENDER, DOB, COURSE_ID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, gender);
            ps.setString(5, dob);
            ps.setInt(6, courseId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ
    public static List<Object[]> getAllStudents() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT STUDENT_ID, STUDENT_NAME, EMAIL, PHONE, GENDER, DOB, COURSE_ID FROM student ORDER BY STUDENT_ID ASC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("STUDENT_ID"),
                    rs.getString("STUDENT_NAME"),
                    rs.getString("EMAIL"),
                    rs.getString("PHONE"),
                    rs.getString("GENDER"),
                    rs.getString("DOB"),
                    rs.getInt("COURSE_ID")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. UPDATE
    public static boolean updateStudent(int id, String name, String email, String phone, String gender, String dob, int courseId) {
        String sql = "UPDATE student SET STUDENT_NAME=?, EMAIL=?, PHONE=?, GENDER=?, DOB=?, COURSE_ID=? WHERE STUDENT_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, gender);
            ps.setString(5, dob);
            ps.setInt(6, courseId);
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. DELETE
    public static boolean deleteStudent(int id) {
        String sql = "DELETE FROM student WHERE STUDENT_ID=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}