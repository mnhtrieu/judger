package eu.mnhtrieu.judge.Business.Impl;

import eu.mnhtrieu.judge.Business.TestInputService;
import eu.mnhtrieu.judge.Data.Model.TestInput;
import eu.mnhtrieu.judge.Data.Repositories.TestInputRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestInputServiceDb implements TestInputService {

    private TestInputRepository testInputRepository;

    @Autowired
    public TestInputServiceDb(TestInputRepository testInputRepository) {
        this.testInputRepository = testInputRepository;
    }

    @Override
    public void save(TestInput input) {
        testInputRepository.save(input);
    }

    @Override
    public TestInput findById(Integer id) {
        Optional<TestInput> testInput = testInputRepository.findById(id);
        return testInput.orElse(null);
    }

    @Override
    public void reveal(Integer id) {
        Optional<TestInput> testInput = testInputRepository.findById(id);
        if(testInput.isPresent()){
            testInput.get().setHint(true);
            testInputRepository.save(testInput.get());
        }
    }
}
