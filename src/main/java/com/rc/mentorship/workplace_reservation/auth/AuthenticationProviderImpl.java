package com.rc.mentorship.workplace_reservation.auth;

import com.rc.mentorship.workplace_reservation.entity.User;
import com.rc.mentorship.workplace_reservation.exception.BadCredentialsException;
import com.rc.mentorship.workplace_reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Override
    public UserAuthentication authenticate(UserAuthentication authentication) {
        Optional<User> user = userRepository.findByEmail(authentication.getEmail());
        if (user.isEmpty() || !user.get().getPassword().equals(authentication.getPassword())) {
            throw new BadCredentialsException();
        }
        authentication.setUser(user.get());
        return authentication;
    }
}
