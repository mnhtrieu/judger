package eu.mnhtrieu.judge.Data.Repositories;

import eu.mnhtrieu.judge.Data.Model.Problem;
import eu.mnhtrieu.judge.Data.Model.Role;
import eu.mnhtrieu.judge.Data.Model.Submission;
import eu.mnhtrieu.judge.Data.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubmissionRepository extends CrudRepository<Submission,Integer> {

    List<Submission> findByUserAndProblemOrderByIdDesc(User user, Problem problem);
}
