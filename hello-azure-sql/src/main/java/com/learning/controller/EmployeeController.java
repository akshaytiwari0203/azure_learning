package com.learning.controller;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	@RequestMapping("get/{empName}")
	public String getEmployee(@PathVariable("empName") String empName) {
		System.out.println(String.format("Geting Employee %s", empName));

		int empSalDB = 0;

		try {
			empSalDB = new JdbcTemplate(mysqlDataSource()).queryForObject(
					String.format("SELECT EMP_SAL FROM EMPLOYEE where EMP_NAME='%s'", empName), Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

		return String.format("Employee Name is %s and Salary is %s", empName, empSalDB);
	}

	@RequestMapping("create/{empName}/{empSal}")
	public String insertEmployee(@PathVariable("empName") String empName, @PathVariable("empSal") int empSal) {
		System.out.println(String.format("Inserting Employee %s:%s", empName, empSal));
		new JdbcTemplate(mysqlDataSource()).update("INSERT INTO EMPLOYEE (EMP_NAME,EMP_SAL) VALUES (?, ?)",
				new Object[] { empName, empSal });
		return "Inserted Employee " + empName;
	}

	private DataSource mysqlDataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		// dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		// TODO: put in your connection string here. Remove username and password sections
		dataSource.setUrl("");
		// TODO: put in your username
		dataSource.setUsername("");
		// TODO: put in your password
		dataSource.setPassword("");

		return dataSource;
	}

}
