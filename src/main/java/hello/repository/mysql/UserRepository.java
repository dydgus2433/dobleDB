package hello.repository.mysql;

import org.springframework.data.repository.CrudRepository;

import hello.domain.mysql.User;

public interface UserRepository extends CrudRepository<User, Integer>{

}
