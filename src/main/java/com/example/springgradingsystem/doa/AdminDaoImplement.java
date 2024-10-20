package com.example.springgradingsystem.doa;

import com.example.springgradingsystem.model.User;
import com.example.springgradingsystem.services.selectUserTypeServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminDaoImplement implements AdminDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public AdminDaoImplement(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String createUser(String username, String hashedPassword, String salt, String userType) {
        User user = selectUserTypeServiceImplement.selectUserType(username, hashedPassword, userType);

        if (user == null) {
            return "User could not be created due to invalid type.";
        }

        String sql = "INSERT INTO Users (username, password, salt, userType) VALUES (?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, user.getName(), user.getPassword(), salt, user.getUserType().toString());

        if (rowsAffected > 0) {
            return user.toString();
        } else {
            return "An error occurred while inserting a new user.";
        }
    }


    @Override
    public String createCourse(String courseName, String instructorUsername) {
        String sql = """
            INSERT INTO Courses (courseName, instructorId)
            SELECT ?, instructor.userId
            FROM Users AS instructor
            WHERE instructor.username = ? AND instructor.userType = 'Instructor'
        """;

        int rowsAffected = jdbcTemplate.update(sql, courseName, instructorUsername);

        if (rowsAffected > 0) {
            return "Course '" + courseName + "' created successfully with instructor '" + instructorUsername + "'.";
        } else {
            return "The instructor '" + instructorUsername + "' is not valid or does not exist.";
        }
    }

    @Override
    public String enrollStudentInTheCourse(String studentUsername, String courseName) {
        String sql = """
        INSERT INTO Grades (userId, courseId)
        SELECT student.userId, course.courseId
        FROM Users AS student
        JOIN Courses AS course ON course.courseName = ?
        WHERE student.username = ? AND student.userType = 'STUDENT'
    """;

        int rowsAffected = jdbcTemplate.update(sql, courseName, studentUsername);

        if (rowsAffected > 0) {
            return "Student '" + studentUsername + "' has been successfully enrolled in the course '" + courseName + "'.";
        } else {
            return "Enrollment failed. The student or course might not exist.";
        }
    }

    public List<String> getAllStudents() {
        String sql = "SELECT username FROM Users WHERE userType = 'student'";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<String> getAllCourses() {
        String sql = "SELECT courseName FROM Courses";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public List<String> getAllInstructors() throws SQLException {
        String sql = "SELECT username FROM Users WHERE userType = 'instructor'";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
