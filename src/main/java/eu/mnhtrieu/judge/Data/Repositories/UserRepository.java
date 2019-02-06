package eu.mnhtrieu.judge.Data.Repositories;

import eu.mnhtrieu.judge.Data.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);
}
