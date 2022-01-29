package com.learning.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.keyvault.models.Vault;

@RestController
public class EmployeeController {

	@RequestMapping("get/{resourceGrp}/{keyVaultName}/{secretName}")
	public String getSecret(@PathVariable("resourceGrp") String resourceGrp,@PathVariable("keyVaultName") String keyVaultName,
			@PathVariable("secretName") String secretName) {
		System.out.println(String.format("Geting Secret from valut %s", keyVaultName));
		System.out.println(String.format("Geting Secret %s", secretName));
		AzureProfile azureProfile = new AzureProfile(AzureEnvironment.AZURE);
		ManagedIdentityCredential managedIdentityCredential = new ManagedIdentityCredentialBuilder().build();
		AzureResourceManager azure = AzureResourceManager.authenticate(managedIdentityCredential, azureProfile)
				.withSubscription("ed7ce0f9-bc74-4424-90dc-2e341c22f051");

		Vault vault = azure.vaults().getByResourceGroup(resourceGrp, keyVaultName);

		String secret = vault.secrets().getByName(secretName).getValue();

		return String.format("The value of secret is %s", secret);
	}

}
