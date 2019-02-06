package eu.mnhtrieu.judge.Business;
import eu.mnhtrieu.judge.Data.Model.TestInput;
public interface TestInputService {


    /**
     * Save the new test input
     * @param input the input to save
     */
    void save(TestInput input);

    /**
     * Find the test input by the ID
     * @param id the id to find
     * @return returns the TestInput
     */
    TestInput findById(Integer id);

    /**
     * Reveals the hint of the test input
     * @param id the id of the test input
     */
    void reveal(Integer id);
}
