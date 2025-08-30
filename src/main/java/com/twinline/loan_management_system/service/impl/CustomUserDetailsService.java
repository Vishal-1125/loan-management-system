package com.twinline.loan_management_system.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.twinline.loan_management_system.entity.User;
import com.twinline.loan_management_system.repo.UserRepository;
import com.twinline.loan_management_system.security.UserDetailsImpl;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    return new UserDetailsImpl(
	            user.getUserId(),
	            user.getUsername(),
	            user.getPassword(),
	            user.getRole()
	    );
	}

}
