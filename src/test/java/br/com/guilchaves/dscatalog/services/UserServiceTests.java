package br.com.guilchaves.dscatalog.services;

import br.com.guilchaves.dscatalog.dto.UserDTO;
import br.com.guilchaves.dscatalog.dto.UserInsertDTO;
import br.com.guilchaves.dscatalog.dto.UserUpdateDTO;
import br.com.guilchaves.dscatalog.entities.Role;
import br.com.guilchaves.dscatalog.entities.User;
import br.com.guilchaves.dscatalog.projections.UserDetailsProjection;
import br.com.guilchaves.dscatalog.repositories.RoleRepository;
import br.com.guilchaves.dscatalog.repositories.UserRepository;
import br.com.guilchaves.dscatalog.services.exceptions.DatabaseException;
import br.com.guilchaves.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.guilchaves.dscatalog.utils.UserDetailsFactory;
import br.com.guilchaves.dscatalog.utils.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private AuthService authService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Long existingId, nonExistingId, dependableId;
    private String existingUsername, nonExistingUsername;
    private User client, admin, user;
    private UserInsertDTO userInsertDTO;
    private UserUpdateDTO userUpdateDTO;
    private String password;
    private Role role;
    private Page<User> page;
    private List<UserDetailsProjection> userList;

    @BeforeEach
    void setUp() throws Exception {
        nonExistingId = 0L;
        existingId = 1L;
        dependableId = 2L;
        existingUsername = "maria@gmail.com";
        nonExistingUsername = "user@gmail.com";
        client = UserFactory.createClientUser();
        admin = UserFactory.createAdminUser();
        user = UserFactory.createCustomClientUser(1L, "guilherme@gmail.com");
        role = new Role(1L, "ROLE_OPERATOR");
        userInsertDTO = UserFactory.createCustomUserInsertDTO();
        userUpdateDTO = UserFactory.createCustomUserUpdateDTO();
        page = new PageImpl<>(List.of(client, admin));
        password = user.getPassword();
        userList = UserDetailsFactory.createCustomAdminUser("Alex");

        Mockito.when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(user));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(authService.authenticated()).thenReturn(user);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(user);
        Mockito.when(roleRepository.findByAuthority(ArgumentMatchers.any())).thenReturn(role);
        Mockito.when(passwordEncoder.encode(ArgumentMatchers.any())).thenReturn(password);

        Mockito.when(repository.getReferenceById(existingId)).thenReturn(user);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependableId)).thenReturn(true);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependableId);

        Mockito.when(repository.searchUserAndRolesByEmail(existingUsername)).thenReturn(userList);
        Mockito.when(repository.searchUserAndRolesByEmail(nonExistingUsername)).thenThrow(UsernameNotFoundException.class);

        Mockito.when(roleRepository.getReferenceById(ArgumentMatchers.any())).thenReturn(role);
    }

    @Test
    public void findAllShouldReturnPagedUserDTO() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<UserDTO> result = service.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getSize());
        Assertions.assertEquals(1, result.getTotalPages());
    }

    @Test
    public void findByIdShouldReturnUserDTOWhenIdExists() {
        UserDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            UserDTO result = service.findById(nonExistingId);
        });
    }

    @Test
    public void findMeShouldReturnUserDTO() {
        UserDTO result = service.findMe();

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldReturnUserDTO() {
        UserDTO result = service.insert(userInsertDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getFirstName(), user.getFirstName());
    }

    @Test
    public void updateShouldReturnUserDTOWhenIdExists() {
        UserDTO result = service.update(existingId, userUpdateDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getFirstName(), user.getFirstName());
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            UserDTO result = service.update(nonExistingId, userUpdateDTO);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> service.delete(existingId));
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdIsDependable() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependableId);
        });
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists(){
        UserDetails result = service.loadUserByUsername(existingUsername);
        Assertions.assertNotNull(result);
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExists(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails result = service.loadUserByUsername(nonExistingUsername);
        });
    }
}

