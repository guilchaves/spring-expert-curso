package br.com.guilchaves.dscatalog.services;

import br.com.guilchaves.dscatalog.dto.EmailDTO;
import br.com.guilchaves.dscatalog.dto.NewPasswordDTO;
import br.com.guilchaves.dscatalog.entities.PasswordRecover;
import br.com.guilchaves.dscatalog.entities.User;
import br.com.guilchaves.dscatalog.repositories.PasswordRecoverRepository;
import br.com.guilchaves.dscatalog.repositories.UserRepository;
import br.com.guilchaves.dscatalog.services.exceptions.EmailException;
import br.com.guilchaves.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createRecoverToken(EmailDTO body) {
        User user = userRepository.findByEmail(body.getEmail());

        if (user == null) throw new EmailException("Email not found");

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity = passwordRecoverRepository.save(entity);

        String bodyContent = "Access link to redefine your password\n\n"
                + recoverUri + token;

        emailService.sendEmail(body.getEmail(), "Password recovery", bodyContent);
    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO dto) {
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(dto.getToken(), Instant.now());

        if (result.isEmpty()) throw new ResourceNotFoundException("Invalid token");

        User user = userRepository.findByEmail(result.get(0).getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = userRepository.save(user);
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

}
