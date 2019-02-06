package eu.mnhtrieu.judge.Business.Impl;

import eu.mnhtrieu.judge.Business.SubmissionService;
import eu.mnhtrieu.judge.Data.Model.Problem;
import eu.mnhtrieu.judge.Data.Model.Submission;
import eu.mnhtrieu.judge.Data.Model.User;
import eu.mnhtrieu.judge.Data.Repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubmissionServiceDb implements SubmissionService {

    private SubmissionRepository submissionRepository;

    @Autowired
    public SubmissionServiceDb(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public List<Submission> findAll(User user, Problem problem) {
        return submissionRepository.findByUserAndProblemOrderByIdDesc(user,problem);
    }

    @Override
    public Submission save(Submission submission) {
        return submissionRepository.save(submission);
    }
}
