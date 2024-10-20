package com.example.springgradingsystem.doa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class StudentDaoImplement implements StudentDao {

    private final JdbcTemplate jdbcTemplate;

    public StudentDaoImplement(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ArrayList<String> showGrades(String studentUsername) {
        String sql = """
        SELECT course.courseName, Grades.grade
        FROM Grades
        JOIN Courses AS course ON Grades.courseId = course.courseId
        JOIN Users AS student ON Grades.userId = student.userId
        WHERE student.username = ?
    """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, studentUsername);
        ArrayList<String> gradesList = new ArrayList<>();

        if (rows.isEmpty()) {
            gradesList.add("No grades found for student: " + studentUsername + ".");
            return gradesList;
        }

        for (Map<String, Object> row : rows) {
            String courseName = (String) row.get("courseName");
            // Fetch the grade as BigDecimal and convert it to Double
            BigDecimal grade = (BigDecimal) row.get("grade");

            if (grade == null) {
                gradesList.add("Course: " + courseName + ", Grade: Not assigned yet");
            } else {
                gradesList.add("Course: " + courseName + ", Grade: " + grade.doubleValue());
            }
        }

        return gradesList;
    }


    @Override
    public String showGPA(String studentUsername) {
        String sql = """
            SELECT Grades.grade
            FROM Grades
            JOIN Users AS student ON Grades.userId = student.userId
            WHERE student.username = ?
        """;

        List<Double> grades = jdbcTemplate.queryForList(sql, Double.class, studentUsername);

        if (grades.isEmpty()) {
            return "No grades found for student: " + studentUsername + ".";
        }

        double totalGrades = 0;
        int gradeCount = 0;

        for (Double grade : grades) {
            if (grade != null) {
                totalGrades += grade;
                gradeCount++;
            }
        }

        if (gradeCount == 0) {
            return "No valid grades found for student: " + studentUsername + ".";
        }

        double average = totalGrades / gradeCount;
        return "The GPA for " + studentUsername + " is: " + String.format("%.2f", average);
    }
}
