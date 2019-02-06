package eu.mnhtrieu.judge.Business.Impl;

import eu.mnhtrieu.judge.Business.CodeService;
import eu.mnhtrieu.judge.Business.Storage.RawFile;
import eu.mnhtrieu.judge.Business.StorageService;
import eu.mnhtrieu.judge.Business.SubmissionService;
import eu.mnhtrieu.judge.Business.TestInputService;
import eu.mnhtrieu.judge.Data.Model.*;
import eu.mnhtrieu.judge.Utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Future;

// TODO: make the language_id generate better

@Service
public class AsyncCodeService implements CodeService {

    private final String URL = "http://localhost:3000";

    private final TestInputService testInputService;
    private final SubmissionService submissionService;

    @Autowired
    public AsyncCodeService(TestInputService testInputService, SubmissionService submissionService) {
        this.testInputService = testInputService;
        this.submissionService = submissionService;
    }

    @Override
    @Async
    public Future<String> send(String source) {
        System.out.println("sending code method asynchronously - " + Thread.currentThread().getName());
        return new AsyncResult<>(generator(source));
    }

    private String generator(String source){
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("source_code",source);
        map.add("language_id","1");
        return getResponse(URL + "/submissions?wait=true",map);
    }

    @Override
    @Async
    public Future<Boolean> submit(String source, User user, Submission submission) throws Exception {
        System.out.println("submitting code method asynchronously - " + Thread.currentThread().getName());

        Problem problem = submission.getProblem();
        MultiValueMap<String,String> mapSubmit = new LinkedMultiValueMap<>();
        MultiValueMap<String,String> mapReference = new LinkedMultiValueMap<>();
        mapSubmit.add("language_id","10");
        mapSubmit.add("source_code",source);
        mapSubmit.add("stdin",null);

        mapReference.add("language_id","10");
        mapReference.add("source_code", new String(Files.readAllBytes(Paths.get(problem.getSolution().getPath()))));
        mapReference.add("stdin",null);
        for(TestCase testCase: problem.getTestCases()){
            // pocet behu pro kazdy testCase
            for(int i = 0; i < testCase.getNumOfRuns(); i ++){

                String stdin = getInput(testCase);
                mapReference.set("stdin",stdin);
                mapSubmit.set("stdin",stdin);

                // get submit
                JSONObject submit = new JSONObject(getResponse(URL + "/submissions?wait=true",mapSubmit));

                int status = submit.getJSONObject("status").getInt("id");
                TestInput test = new TestInput();
                setupTest(test,submit,submission,testCase,stdin);
                if(status == 6){ // compilation error
                    System.out.println("COMPILATION ERROR");
                    setSubmissionState(1,submission);
                    test.setError(submit.getJSONObject("status").getString("description"));
                    testInputService.save(test);
                    return new AsyncResult<>(false);
                }
                // get reference
                JSONObject reference = new JSONObject(getResponse(URL + "/submissions?wait=true",mapReference));

                test.setRefOut(reference.getString("stdout"));

                if(status != 3){ // not accepted
                    System.out.println("STATUS: " + status);
                    test.setError(submit.getJSONObject("status").getString("description"));
                    setSubmissionState(1,submission);
                    testInputService.save(test);
                    return new AsyncResult<>(false);
                }

                String out = submit.getString("stdout");
                String ref = reference.getString("stdout");

                if(!out.equals(ref)){
                    System.out.println("WRONG ANSWER");
                    test.setError("Wrong Answer");
                    setSubmissionState(1,submission);
                    testInputService.save(test);
                    return new AsyncResult<>(false);
                }

                testInputService.save(test);
            }
        }
        setSubmissionState(2,submission);
        return new AsyncResult<>(true);
    }

    private String getResponse(String url, MultiValueMap<String,String> map) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.POST,request, String.class);
        return response.getBody();
    }

    private String getInput(TestCase testCase) throws IOException, JSONException {
        if(testCase.getGenerator()){ // pokud je generator
            JSONObject json = new JSONObject(generator(new String(Files.readAllBytes(Paths.get(testCase.getFile().getPath())))));
            System.out.println(json);
            System.out.println("INPUT: ");
            System.out.println(json.getString("stdout"));
            return json.getString("stdout");
        }
        return new String(Files.readAllBytes(Paths.get(testCase.getFile().getPath())));

    }



    private void setupTest(TestInput test, JSONObject submit, Submission submission, TestCase testCase, String stdin)  {

        try {
            test.setStderr(submit.getString("stderr"));
            test.setMemory(submit.isNull("memory") ? 0.0 : submit.getDouble("memory"));
            test.setTime(submit.isNull("time") ? 0.0 : submit.getDouble("time"));
            test.setCompileOutput(submit.getString("compile_output"));
            test.setStdout(submit.getString("stdout"));
            test.setSubmission(submission);
            test.setTestCase(testCase);
            test.setStdin(stdin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Submission setSubmissionState(Integer state, Submission submission){
        submission.setState(state);
        return submissionService.save(submission);
    }
}
