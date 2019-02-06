package eu.mnhtrieu.judge.Business;

import eu.mnhtrieu.judge.Data.Model.Problem;
import eu.mnhtrieu.judge.Data.Model.Submission;
import eu.mnhtrieu.judge.Data.Model.User;

import java.io.IOException;
import java.util.concurrent.Future;

public interface CodeService {

    Future<String> send(String source);
    Future<Boolean> submit(String source, User user, Submission submission) throws Exception;
}
