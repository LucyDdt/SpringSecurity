package fr.solutec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import fr.solutec.entities.Roles;
import fr.solutec.repository.RolesRepository;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class SolutecAuthentificationApplication implements CommandLineRunner{
	@Autowired
	private RolesRepository roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(SolutecAuthentificationApplication.class, args);
		System.out.println("Lancement terminé.");
	}
	

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Lancement en cours..");
		
		Roles r1 = new Roles(null,"ROLE_ADMIN");
		Roles r2 = new Roles(null,"ROLE_USER");
		
		roleRepo.save(r1);
		roleRepo.save(r2);
	}

}
