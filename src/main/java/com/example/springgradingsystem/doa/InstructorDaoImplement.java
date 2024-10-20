package com.example.springgradingsystem.doa;

import com.example.springgradingsystem.model.StudentGrade;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class InstructorDaoImplement implements InstructorDao {

    private final JdbcTemplate jdbcTemplate;

    public InstructorDaoImplement(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ArrayList<String> showInstructorCourses(String instructorUsername) {
        String sql = """
        SELECT courseName
        FROM Courses
        JOIN Users ON Courses.instructorId = Users.userId
        WHERE Users.username = ? AND Users.userType = 'INSTRUCTOR'
    """;

        return new ArrayList<>(jdbcTemplate.queryForList(sql, String.class, instructorUsername));
    }

    @Override
    public String enterStudentGrades(String courseName, String studentUsername, int grade) {
        String sql = """
            INSERT INTO Grades (courseId, userId, grade)
            SELECT Courses.courseId, Users.userId, ?
            FROM Courses
            JOIN Users ON Users.username = ?
            WHERE Courses.courseName = ? AND Users.userType = 'STUDENT'
            ON DUPLICATE KEY UPDATE grade = VALUES(grade)
        """;

        int rowsAffected = jdbcTemplate.update(sql, grade, studentUsername, courseName);

        if (rowsAffected > 0) {
            return "Grade for student '" + studentUsername + "' in course '" + courseName + "' updated successfully.";
        } else {
            return "Failed to update grade. Check if the student or course exists.";
        }
    }

    @Override
    public ArrayList<String> showStudentsInTheCourse(String courseName, String instructorUsername) {
        String sql = """
            SELECT Users.username, Grades.grade
            FROM Grades
            JOIN Users ON Grades.userId = Users.userId
            JOIN Courses ON Grades.courseId = Courses.courseId
            JOIN Users AS Instructor ON Courses.instructorId = Instructor.userId
            WHERE Courses.courseName = ? 
            AND Instructor.username = ? 
            AND Instructor.userType = 'INSTRUCTOR'
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, courseName, instructorUsername);
        ArrayList<String> result = new ArrayList<>();

        if (rows.isEmpty()) {
            result.add("No students found for the course or you are not authorized to view this course.");
            return result;
        }

        for (Map<String, Object> row : rows) {
            String studentUsername = (String) row.get("username");
            Double grade = row.get("grade") != null ? ((Number) row.get("grade")).doubleValue() : null;

            if (grade == null) {
                result.add("Student: " + studentUsername + ", Grade: Not assigned yet");
            } else {
                result.add("Student: " + studentUsername + ", Grade: " + grade.intValue());
            }
        }

        return result;
    }

    public ArrayList<StudentGrade> showStudentsWithCourse(String courseName, String instructorUsername) {
        String sql = """
            SELECT Users.username, Grades.grade
            FROM Grades
            JOIN Users ON Grades.userId = Users.userId
            JOIN Courses ON Grades.courseId = Courses.courseId
            JOIN Users AS Instructor ON Courses.instructorId = Instructor.userId
            WHERE Courses.courseName = ? 
            AND Instructor.username = ? 
            AND Instructor.userType = 'INSTRUCTOR'
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, courseName, instructorUsername);
        ArrayList<StudentGrade> result = new ArrayList<>();

        if (rows.isEmpty()) {
            return result;
        }

        for (Map<String, Object> row : rows) {
            String studentUsername = (String) row.get("username");
            Double grade = row.get("grade") != null ? ((Number) row.get("grade")).doubleValue() : null;

            if (grade != null) {
                result.add(new StudentGrade(studentUsername, grade.intValue()));
            } else {
                result.add(new StudentGrade(studentUsername, null));
            }
        }
        return result;
    }
}
