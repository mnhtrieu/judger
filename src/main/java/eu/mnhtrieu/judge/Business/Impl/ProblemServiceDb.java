package eu.mnhtrieu.judge.Business.Impl;

import eu.mnhtrieu.judge.Business.ProblemService;
import eu.mnhtrieu.judge.Data.Model.Problem;
import eu.mnhtrieu.judge.Data.Repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemServiceDb implements ProblemService {

    ProblemRepository problemRepository;

    @Autowired
    public ProblemServiceDb(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @Override
    public void saveProblem(Problem problem) {
        problemRepository.save(problem);
    }

    @Override
    public Problem findById(Integer id) {
        Optional<Problem> problem = problemRepository.findById(id);
        return problem.orElse(null);
    }

    @Override
    public List<Problem> findAll() {
        List<Problem> problems = new ArrayList<>();
        problemRepository.findAll().forEach(problems::add);
        return problems;
    }

    @Override
    public boolean deleteProblem(Integer id) {
        if(!problemRepository.existsById(id)) return false;
        problemRepository.deleteById(id);
        return !problemRepository.existsById(id);
    }

    @Override
    public long getCount(){
        return problemRepository.count();
    }
}
