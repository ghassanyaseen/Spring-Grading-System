package com.example.springgradingsystem.doa;

import java.sql.SQLException;
import java.util.ArrayList;

public interface StudentDao {
    public ArrayList<String> showGrades(String studentName) throws SQLException;
    public String showGPA(String name) throws SQLException;
}
