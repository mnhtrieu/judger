package eu.mnhtrieu.judge.Business;

import eu.mnhtrieu.judge.Data.Model.Problem;

import java.util.List;

public interface ProblemService {

    /**
     * Saves the problem to the database
     * @param problem the new problem
     */
    void saveProblem(Problem problem);

    /**
     * Find the user by the email
     * @param id the id of the problem
     * @return the problem
     */
    Problem findById(Integer id);

    /**
     * Find all the problems
     * @return the list of all the problems
     */
    List<Problem> findAll();

    /**
     * Delete the problem
     * @param id the id of the problem. Must not be null
     * @return true if it succeeded in deleting
     */
    boolean deleteProblem(Integer id);

    /**
     * Get the problem count
     * @return the problem count
     */
    long getCount();
}
