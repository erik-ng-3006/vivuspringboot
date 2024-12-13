package fa.training.vivuspringboot.services;

import fa.training.vivuspringboot.dtos.auth.RegisterRequestDTO;
import fa.training.vivuspringboot.entities.User;
import fa.training.vivuspringboot.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService implements IAuthService, UserDetailsService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(RegisterRequestDTO registerDTO) {
        // check null object
        if (registerDTO == null) {
            throw new IllegalArgumentException("Register is null");
        }
        //find user by username or phoneNumber
        var user = userRepository.findByUsernameOrPhoneNumber(registerDTO.getUsername(), registerDTO.getPhoneNumber());

        // check if username already existed
        if (user != null && user.getUsername().equals(registerDTO.getUsername())) {
            throw new IllegalArgumentException("Username already existed");
        }

        // check if phoneNumber already existed
        if (user != null && user.getPhoneNumber().equals(registerDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already existed");
        }

        // check if password and confirm password not match
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password not match");
        }

        // create new user
        user = new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setUsername(registerDTO.getUsername());
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setAvatar(registerDTO.getAvatar());
        user.setActive(true);
        user.setInsertedAt(ZonedDateTime.now());
        // encode password
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        // save user
        userRepository.save(user);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> authorities = Set.of();

        // Check if user has roles and map them to authorities
        if (user.getRoles() != null) {
            authorities = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getName())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }

        // Return a UserDetails object with username, password and authorities (empty if
        // no roles)
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }
}
