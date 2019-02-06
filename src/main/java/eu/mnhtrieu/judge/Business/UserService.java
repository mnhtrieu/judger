package eu.mnhtrieu.judge.Business;

import eu.mnhtrieu.judge.Data.Model.User;

public interface UserService {

    /**
     * Saves the user to the database and encrypt the pass
     * @param user the new user
     */
    void saveUser(User user);

    /**
     * Find the user by the email
     * @param email the email address of the user
     * @return the Admin
     */
    User findByEmail(String email);
}
