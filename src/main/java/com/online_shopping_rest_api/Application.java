package com.online_shopping_rest_api;

import com.online_shopping_rest_api.models.Product;
import com.online_shopping_rest_api.models.Role;
import com.online_shopping_rest_api.models.User;
import com.online_shopping_rest_api.loadData.Loader;
import com.online_shopping_rest_api.repositories.ProductRepository;
import com.online_shopping_rest_api.repositories.RoleRepository;
import com.online_shopping_rest_api.repositories.UserRepository;
import com.online_shopping_rest_api.utils.DateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Application class has the main method which initiates the start of the
 * application.
 * The Application class is responsible for the startup of the program. It
 * implements spring's
 * CommandLineRunner interface, which automatically runs when the application
 * context has finished loaded.
 * When the CommandLineRunner starts, it will automatically call the run method
 * {@link #run(String... args)}
 *
 * @SpringBootApplication Indicates a configuration class that declares one or
 *                        more @Bean methods and also triggers
 *                        auto-configuration and component scanning.
 * @Autowired Marks a constructor, field, setter method, or config method as to
 *            be autowired by Spring's dependency injection facilities.
 * @Override Indicates that a method declaration is intended to override a
 *           method declaration in a supertype.
 * @Transctional Describes a transaction attribute on an individual method or on
 *               a class.
 * @return void
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
	/**
	 * responsible for stopping the method run {@link #run} from creating duplicate
	 * entries in the databases.
	 * if false this meaning that the database has not been set up.
	 * if true this means that the database has been set up.
	 */
	private static boolean alreadySetup = false;
	/**
	 * this instance variable UserRepository is responsible for holding the
	 * UserRepository {@link UserRepository#} dependent object.
	 */
	@Autowired
	private UserRepository userRepository;
	/**
	 * this instance variable is responsible for holding the RoleRepository
	 * {@link RoleRepository#} dependent object.
	 */
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ProductRepository productRepository;

	private static Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(Application.class, args);
		LOG.info("APPLICATION FINISHED");
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (alreadySetup)
			return;

		final Optional<Role> masterAdminRole = createRoleIfNotFound("ROLE_MASTER_ADMIN");
		final Optional<Role> adminRole = createRoleIfNotFound("ROLE_ADMIN");
		final Optional<Role> userRole = createRoleIfNotFound("ROLE_USER");

		List<Role> roles = new ArrayList<>();
		roles.add(masterAdminRole.get());
		roles.add(adminRole.get());
		roles.add(userRole.get());

		User user = new User.Builder("danmargreen", "Danmar", "Green", "Terren17",
				"5955 Skyline Dr, West Linn, OR, 97068", "1996-17-01",
				"danmargreen0@gmail.com",
				"(503)-833-2453", roles, DateGenerator.getLocalDate(),
				DateGenerator.getLocalDate()).build();

		// User user = new User.Builder("danmargreenn", "Danmarr", "Greenn",
		// "Terren177",
		// "5955 Skyline Dr, West Linn, OR, 97068", "1996-17-01",
		// "danmargreen01@gmail.com",
		// "(503)-833-2453", roles, DateGenerator.getLocalDate(),
		// DateGenerator.getLocalDate()).build();

		userRepository.saveAndFlush(user);

		dataLoader();
	}

	@Transactional
	private Optional<Role> createRoleIfNotFound(String role) {

		Optional<Role> roles = roleRepository.findByRole(role);

		if (!roles.isPresent()) {
			roles = Optional.of(new Role.Builder(role).users(new ArrayList<>()).build());
			roleRepository.saveAndFlush(roles.get());
		}
		;

		return roles;
	}

	@Transactional
	private void dataLoader() {

		Loader loader = new Loader();
		List<Product> newProducts = loader.products();

		if (newProducts.size() == 0)
			throw new IllegalArgumentException("loader has no products to add");

		// for (int i = 0; i < newProducts.size(); i++)
		productRepository.saveAll(newProducts);

	}
}
