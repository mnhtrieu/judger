package eu.mnhtrieu.judge.Data.Repositories;

import eu.mnhtrieu.judge.Data.Model.Problem;
import org.springframework.data.repository.CrudRepository;

public interface ProblemRepository extends CrudRepository<Problem,Integer> {
}
