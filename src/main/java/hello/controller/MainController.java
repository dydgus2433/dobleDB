package hello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.domain.h2.Customer;
import hello.domain.mysql.User;
import hello.repository.h2.CustomerRepository;
import hello.repository.mysql.UserRepository;

@Controller
@RequestMapping(path="/demo")
public class MainController {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	@RequestMapping( value = "/add", method = RequestMethod.GET)
//	@GetMapping(path = "/add")
	public @ResponseBody String addNewUser(@RequestParam String name,
			@RequestParam String email) {
		User n = new User();
		n.setName(name);
		n.setEmail(email);
		userRepository.save(n);
		return "Saved";
		
	}
	
	@RequestMapping( value = "/all", method = RequestMethod.GET)
//	@GetMapping(path = "/all")
	public @ResponseBody Iterable<User> getAllUsers(){
		
		return userRepository.findAll();
	}
	
	@RequestMapping( value = "/add1", method = RequestMethod.GET)
	public @ResponseBody String addNewUser1(@RequestParam String name) {
		Customer n = new Customer();
	
		n.setName(name);
		customerRepository.save(n);
		return "Saved1";
		
	}
	
	@RequestMapping( value = "/all1", method = RequestMethod.GET)
	public @ResponseBody Iterable<Customer> getAllUsers1(){
		
		return customerRepository.findAll();
	}

}
