package br.com.guilchaves.dscatalog.services;

import br.com.guilchaves.dscatalog.dto.EmailDTO;
import br.com.guilchaves.dscatalog.entities.PasswordRecover;
import br.com.guilchaves.dscatalog.entities.User;
import br.com.guilchaves.dscatalog.repositories.PasswordRecoverRepository;
import br.com.guilchaves.dscatalog.repositories.UserRepository;
import br.com.guilchaves.dscatalog.services.exceptions.EmailException;
import br.com.guilchaves.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

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

}
