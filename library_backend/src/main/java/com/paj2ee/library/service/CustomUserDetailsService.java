package com.paj2ee.library.service;

import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
import com.paj2ee.library.repository.LibAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private LibAppUserRepository libAppUserRepository;

	//@NOTE: READ_COMMITTED: Prevents dirty reads by ensuring that a transaction can only read data that has been committed.
	//@NOTE: REPEATABLE_READ: In addition to preventing dirty reads, ensures that if a row is read twice in the same
	// transaction, the result will not change due to another transaction modifying or inserting data.
	@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LibAppUser libAppUser = libAppUserRepository
			.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));

		Set<LibAppUserAuthority> authorities = libAppUser.getAuthorities();
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (LibAppUserAuthority libAppUserAuthority : authorities) {

			grantedAuthorities.add(new MyGrantedAuthority(libAppUserAuthority.getAuthority()));

		}

		UserDetails myUserDetails = User.withDefaultPasswordEncoder()
			.username(libAppUser.getUsername())
			.password(libAppUser.getPassword())
			.disabled(! libAppUser.isEnabled())
			.authorities(grantedAuthorities)
			.build();

		return myUserDetails;
	}

	public static class MyGrantedAuthority implements GrantedAuthority {

		private String authority;

		public MyGrantedAuthority() {
			super();
		}

		public MyGrantedAuthority(String authority) {
			super();
			this.authority = authority;
		}

		@Override
		public String getAuthority() {
			return authority;
		}
	}
}
