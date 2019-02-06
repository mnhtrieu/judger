package eu.mnhtrieu.judge.Business;

import eu.mnhtrieu.judge.Data.Model.Problem;
import eu.mnhtrieu.judge.Data.Model.Submission;
import eu.mnhtrieu.judge.Data.Model.User;

import java.util.List;

public interface SubmissionService {


    /**
     * Gets all the submission of the user and of the problem
     * @param user the user
     * @param problem the problem
     * @return the list of all the submissions of the problem owned by the user
     */
    List<Submission> findAll(User user, Problem problem);

    /**
     * Saves the submission
     * @param submission the submission to save
     * @return the saved submission
     */
    Submission save(Submission submission);
}
