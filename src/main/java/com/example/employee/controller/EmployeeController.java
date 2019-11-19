package com.example.employee.controller;

import com.example.employee.models.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@RestController
public class EmployeeController {


    @GetMapping(path = "/employees")
    public ArrayList<Employee> getEmployee() {
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<Employee> list = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://35.197.145.177:5432/employee", "postgres", "postgres");
            c.setAutoCommit(false);

            StringBuffer sql = new StringBuffer("SELECT * FROM employee");
            stmt = c.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getString("id"));
                employee.setEmployeeId(rs.getString("employee_id"));
                employee.setFirstName(rs.getString("firstname"));
                employee.setLastName(rs.getString("lastname"));
                employee.setEmail(rs.getString("email"));
                employee.setSex(rs.getString("sex"));
                employee.setAge(rs.getString("age"));
                list.add(employee);
            }

            stmt.close();
            c.commit();
            c.close();
            System.out.println("Close database");
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }

        return list;
    }

    @PostMapping(path = "/employees")
    public String addEmployee(@RequestBody Employee request) {
        Connection c = null;
        PreparedStatement stmt = null;
        boolean flag = false;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://35.197.145.177:5432/employee", "postgres", "postgres");
            c.setAutoCommit(false);

            System.out.println("Opened database successfully");

            StringBuffer sql = new StringBuffer("INSERT INTO employee (employee_id, firstname, lastname, email, sex, age) VALUES (?, ?, ?, ?, ?, ?) ");
            stmt = c.prepareStatement(sql.toString());
            stmt.setString(1, request.getEmployeeId());
            stmt.setString(2, request.getFirstName());
            stmt.setString(3, request.getLastName());
            stmt.setString(4, request.getEmail());
            stmt.setString(5, request.getSex());
            stmt.setString(6, request.getAge());

            System.out.println("After set stmt");

            int result = stmt.executeUpdate();
            if (result > 0) {
                flag = true;
                System.out.println("flag = true");
            } else {
                flag = false;
                System.out.println("flag = false");
            }
            stmt.close();
            c.commit();
            c.close();
            System.out.println("Close database");
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            return e.getMessage();
        }

        if(flag) {
            System.out.println("Records created successfully");
            return "Records created successfully";
        } else {
            System.out.println("Records created failed");
            return "Records created failed";
        }

    }
}
