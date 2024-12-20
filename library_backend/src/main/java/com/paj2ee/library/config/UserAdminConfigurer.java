package com.paj2ee.library.config;

import com.paj2ee.library.model.LibAppUser;
import com.paj2ee.library.model.LibAppUserAuthority;
import com.paj2ee.library.repository.LibAppUserAuthorityRepository;
import com.paj2ee.library.repository.LibAppUserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class UserAdminConfigurer {

	@Value("${libapp.admin.username:#{null}}")
	private String ADMIN_USERNAME;
	@Value("${libapp.admin.password:#{null}}")
	private String ADMIN_PASSWORD;
	@Value("${libapp.admin.email:#{null}}")
	private String ADMIN_EMAIL;
	@Value("${libapp.admin.phone-number:#{null}}")
	private String ADMIN_PHONE_NUMBER;
	@Value("${libapp.admin.authorities:#{null}}")
	private String[] ADMIN_AUTHORITIES;

	private static final Logger logger = LoggerFactory.getLogger(UserAdminConfigurer.class);

	@Autowired
	private LibAppUserRepository libAppUserRepository;

	@Autowired
	private LibAppUserAuthorityRepository libAppUserAuthorityRepository;

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ)
	@PostConstruct
	public void createCustomAdminUser() {

		Optional<LibAppUser> existingAdminUser = libAppUserRepository.findByUsername(ADMIN_USERNAME);

		if (existingAdminUser.isPresent()) {

			//noop

		}
		else {

			boolean hasUsernameDefined 		= (null != ADMIN_USERNAME);
			boolean hasPasswordDefined 		= (null != ADMIN_PASSWORD);
			boolean hasEmailDefined 		= (null != ADMIN_EMAIL);
			boolean hasPhoneNumberDefined 	= (null != ADMIN_PHONE_NUMBER);
			boolean hasAuthoritiesDefined 	= (null != ADMIN_AUTHORITIES);

			boolean hasAllPropertiesDefined = (
				hasUsernameDefined
				&& hasPasswordDefined
				&& hasEmailDefined
				&& hasPhoneNumberDefined
				&& hasAuthoritiesDefined
			);

			if (true == hasAllPropertiesDefined) {

				LibAppUser pendingAdminUser = new LibAppUser();
				pendingAdminUser.setUsername(ADMIN_USERNAME);
				pendingAdminUser.setPassword(ADMIN_PASSWORD);
				pendingAdminUser.setEmail(ADMIN_EMAIL);
				pendingAdminUser.setPhoneNumber(ADMIN_PHONE_NUMBER);
				pendingAdminUser.setEnabled(true);

				LibAppUser adminUser = libAppUserRepository.save(pendingAdminUser);

				for (String adminAuthority : ADMIN_AUTHORITIES) {

					LibAppUserAuthority pendingAdminUserAuthority = new LibAppUserAuthority();
					pendingAdminUserAuthority.setOwnerUser(adminUser);
					pendingAdminUserAuthority.setAuthority(adminAuthority);

					LibAppUserAuthority adminUserAuthority = libAppUserAuthorityRepository.save(pendingAdminUserAuthority);

				}

			}
			else {

				List<String> errorList = new ArrayList<>();

				if (! hasUsernameDefined) {
					errorList.add("username property is not defined");

				}
				if (! hasPasswordDefined) {

					errorList.add("password property is not defined");

				}
				if (! hasEmailDefined) {

					errorList.add("email property is not defined");

				}
				if (! hasPhoneNumberDefined) {

					errorList.add("phone-number property is not defined");

				}
				if (! hasAuthoritiesDefined) {

					errorList.add("authorities property is not defined");

				}

				logger.warn("Admin user account could not be created. Cause: {}", errorList);

			}

		}

	}

}
