package hello.repository.h2;

import org.springframework.data.repository.CrudRepository;

import hello.domain.h2.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{

	
}
