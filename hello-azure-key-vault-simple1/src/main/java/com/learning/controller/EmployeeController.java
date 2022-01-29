package com.learning.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

@RestController
public class EmployeeController {

	@RequestMapping("get/{resourceGrp}/{keyVaultName}/{secretName}")
	public String getSecret(@PathVariable("keyVaultName") String keyVaultName,
			@PathVariable("secretName") String secretName) {
		System.out.println(String.format("Geting Secret from valut %s", keyVaultName));
		System.out.println(String.format("Geting Secret %s", secretName));
		String keyVaultUri = "https://" + keyVaultName + ".vault.azure.net";
		System.out.println(String.format("keyVaultUri %s", keyVaultUri));

		SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUri)
				.credential(new DefaultAzureCredentialBuilder().build()).buildClient();

		KeyVaultSecret retrievedSecret = secretClient.getSecret(secretName);

		return retrievedSecret.getValue();
	}

	@RequestMapping("/")
	public String greet() {
		System.out.println("Greetings");

		return "server is up!!!";
	}

}
