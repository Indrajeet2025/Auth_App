package com.example.auth.auth_app_backend;

import com.example.auth.auth_app_backend.config.AppConstants;
import com.example.auth.auth_app_backend.entities.Role;
import com.example.auth.auth_app_backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AuthAppBackendApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(AuthAppBackendApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		// create default roles
		// ADMIN
		// GUEST
		roleRepository.findByRoleName("ROLE_"+AppConstants.ADMIN_ROLE)
				.ifPresentOrElse(role->{
					System.out.println("Role already exist "+role.getRoleName());
				},()->{
					Role role=new Role();
					role.setRoleName("ROLE_"+AppConstants.ADMIN_ROLE);
					roleRepository.save(role);
				});
		roleRepository.findByRoleName("ROLE_"+AppConstants.GUEST_ROLE)
				.ifPresentOrElse(role->{
					System.out.println("Role already exist "+role.getRoleName());
				},()->{
					Role role=new Role();
					role.setRoleName("ROLE_"+AppConstants.GUEST_ROLE);
					roleRepository.save(role);
				});


	}
}
