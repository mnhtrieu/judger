package eu.mnhtrieu.judge.Data.Repositories;

import eu.mnhtrieu.judge.Data.Model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role,Integer> {

    Role findByRoleName(String role);
}
