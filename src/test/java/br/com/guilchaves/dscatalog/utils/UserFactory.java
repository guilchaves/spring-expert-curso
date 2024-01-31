package br.com.guilchaves.dscatalog.utils;

import br.com.guilchaves.dscatalog.dto.UserInsertDTO;
import br.com.guilchaves.dscatalog.dto.UserUpdateDTO;
import br.com.guilchaves.dscatalog.entities.Role;
import br.com.guilchaves.dscatalog.entities.User;
import br.com.guilchaves.dscatalog.projections.ProductProjection;
import br.com.guilchaves.dscatalog.projections.UserDetailsProjection;

import java.util.List;

public class UserFactory {
    public static User createClientUser() {
        User user = new User(1L, "Maria", "Brown", "maria@gmail.com", "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser() {
        User user = new User(1L, "Alex", "Green", "alex@gmail.com", "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String username) {
        User user = new User(id, "Maria", "Brown", username, "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String username) {
        User user = new User(id, "Alex", "Green", username, "$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static UserInsertDTO createCustomUserInsertDTO() {
        UserInsertDTO user = new UserInsertDTO();
        user.setPassword("$2a$10$N7SkKCa3r17ga.i.dF9iy.BFUBL2n3b6Z1CWSZWi/qy7ABq/E6VpO");
        return user;
    }

    public static UserUpdateDTO createCustomUserUpdateDTO() {
        UserUpdateDTO user = new UserUpdateDTO();
        return user;
    }

}

