package br.com.guilchaves.dscatalog.services;

import br.com.guilchaves.dscatalog.entities.Role;
import br.com.guilchaves.dscatalog.entities.User;
import br.com.guilchaves.dscatalog.projections.UserDetailsProjection;
import br.com.guilchaves.dscatalog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);

        if (result.isEmpty())
            throw new UsernameNotFoundException("Username not found!");

        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        result.forEach(e -> user.addRole(new Role(e.getRoleId(), e.getAuthority())));
        return user;
    }
}
