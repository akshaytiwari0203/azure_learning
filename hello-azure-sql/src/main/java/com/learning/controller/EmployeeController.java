package com.learning.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	// TODO: put in your connection string here. Also put in correct password
	String connectionString = "jdbc:sqlserver://tiwarakssqlserver.database.windows.net:1433;database=tiwarakssqldb;user=akshay@tiwarakssqlserver;password=Password@123;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

	@RequestMapping("get/{empName}")
	public String getSecret(@PathVariable("empName") String empName) {
		System.out.println(String.format("Geting Employee %s", empName));
		String empNameDB = "";
		int empSalDB = 0;

		try {
			Connection conn = DriverManager.getConnection(connectionString);
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement
					.executeQuery(String.format("SELECT EMP_NAME,EMP_SAL FROM EMPLOYEE where EMP_NAME='%s'", empName));
			resultSet.next();
			empNameDB = resultSet.getString("EMP_NAME");
			empSalDB = resultSet.getInt("EMP_SAL");
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

		return String.format("Employee Name is %s and Salary is %s", empNameDB, empSalDB);
	}

	@RequestMapping("create/{empName}/{empSal}")
	public String insertEmployee(@PathVariable("empName") String empName, @PathVariable("empSal") int empSal) {
		System.out.println(String.format("Inserting Employee %s:%s", empName, empSal));
		Connection conn;
		try {
			conn = DriverManager.getConnection(connectionString);
			PreparedStatement insertStatement = conn
					.prepareStatement("INSERT INTO EMPLOYEE (EMP_NAME,EMP_SAL) VALUES (?, ?);");

			insertStatement.setString(1, empName);
			insertStatement.setInt(2, empSal);
			insertStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}

		return "Inserted Employee " + empName;
	}

}
