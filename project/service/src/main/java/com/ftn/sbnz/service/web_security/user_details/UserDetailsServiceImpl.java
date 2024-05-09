package com.ftn.sbnz.service.web_security.user_details;

import com.ftn.sbnz.service.core.user.abstract_user.db.UserRepository;
import com.ftn.sbnz.service.core.user.abstract_user.db.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '%s' not found!".formatted(username)));
        return new UserDetailsImpl(user);
    }
}
