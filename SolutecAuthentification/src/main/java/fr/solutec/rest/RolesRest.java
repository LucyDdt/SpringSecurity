package fr.solutec.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.solutec.entities.Roles;
import fr.solutec.entities.User;
import fr.solutec.repository.RolesRepository;
import fr.solutec.repository.UserRepository;

@RestController
@CrossOrigin("*")
public class RolesRest {
	@Autowired
	private RolesRepository rolesRepo;
	@Autowired
	private UserRepository userRepo;


	@PostMapping("roles")
	public Roles createRole(@RequestBody Roles role) {
		return rolesRepo.save(role);
	}
	
	@PutMapping("addRole/{idUser}/{idRole}")
	public User addRole(@PathVariable Long idUser, @PathVariable Long idRole) {
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		java.util.Optional<User> u = userRepo.findById(idUser);
		java.util.Optional<Roles> r = rolesRepo.findById(idRole);
		u.get().getRoles().add(r.get());
		for(Roles ro : u.get().getRoles()) {
        list.add(new SimpleGrantedAuthority(ro.getRoleName()));
		}
		return userRepo.save(u.get());
	}
}
