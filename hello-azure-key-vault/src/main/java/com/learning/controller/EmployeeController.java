package com.learning.controller;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.util.polling.SyncPoller;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.DeletedSecret;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

@RestController
public class EmployeeController {

	// TODO: Modify to set correct jdbc url
	private String jdbcUrl = "jdbc:sqlserver://tiwaraksdbserver.database.windows.net:1433;database=tiwaraksdb;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

	// TODO: Modify to set correct jdbc user name
	private String jdbcUsername = "akshay@tiwaraksdbserver";

	// TODO: Modify to set correct secret name
	private String secretName = "DBPASSWORD";

	// TODO: Modify to set correct secret name
	private String keyVaultName = "tiwarakskv1";

	private String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";

	@RequestMapping("getEmp/{empName}")
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

	@RequestMapping("createEmp/{empName}/{empSal}")
	public String insertEmployee(@PathVariable("empName") String empName, @PathVariable("empSal") int empSal) {
		System.out.println(String.format("Inserting Employee %s:%s", empName, empSal));
		new JdbcTemplate(mysqlDataSource()).update("INSERT INTO EMPLOYEE (EMP_NAME,EMP_SAL) VALUES (?, ?)",
				new Object[] { empName, empSal });
		return "Inserted Employee " + empName;
	}

	private DataSource mysqlDataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		// dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(jdbcUsername);
		dataSource.setPassword(getSecret(secretName));

		return dataSource;
	}

	private String getSecret(String secretName) {

		System.out.println(String.format("keyVaultUri %s", keyVaultUri));

		SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUri)
				.credential(new DefaultAzureCredentialBuilder().build()).buildClient();

		KeyVaultSecret retrievedSecret = secretClient.getSecret(secretName);

		return retrievedSecret.getValue();

	}

	@RequestMapping("modifySecret/{secretName}/{secretValue}")
	public String setSecret(@PathVariable("secretName") String secretName,
			@PathVariable("secretValue") String secretValue) {
		System.out.println(String.format("Get Request %s:%s", secretName, secretValue));

		SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUri)
				.credential(new DefaultAzureCredentialBuilder().build()).buildClient();

		secretClient.setSecret(new KeyVaultSecret(secretName, secretValue));

		return String.format("The value of secret %s is updated to %s", secretName, secretValue);
	}

	@RequestMapping("deleteSecret/{secretName}")
	public String deleteSecret(@PathVariable("secretName") String secretName) {
		System.out.println(String.format("Delete Request %s", secretName));

		SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUri)
				.credential(new DefaultAzureCredentialBuilder().build()).buildClient();

		SyncPoller<DeletedSecret, Void> deletionPoller = secretClient.beginDeleteSecret(secretName);
		deletionPoller.waitForCompletion();

		return String.format("The value of secret %s is deleted", secretName);
	}

	@RequestMapping("/")
	public String greet() {
		System.out.println("Greetings");

		return "server is up!!!";
	}

}
