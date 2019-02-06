package eu.mnhtrieu.judge.Presentation.Forms;

import eu.mnhtrieu.judge.Business.Storage.RawFile;
import eu.mnhtrieu.judge.Business.StorageService;
import eu.mnhtrieu.judge.Data.Model.Document;
import eu.mnhtrieu.judge.Data.Model.Problem;
import eu.mnhtrieu.judge.Data.Model.TestCase;
import eu.mnhtrieu.judge.Data.Model.User;
import eu.mnhtrieu.judge.Utils.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

// TODO, remove when failed to save

public class ProblemForm {

    @Valid
    private Problem problem;
    @Valid
    private List<TestCasesForm> testCases;

    @Valid
    private DocumentForm solution;


    public ProblemForm(Problem problem){
        this.problem = problem;
        this.testCases = new ArrayList<>();
        for(TestCase testCase: problem.getTestCases()){
            testCases.add(new TestCasesForm(testCase));
        }
        this.solution = new DocumentForm(problem.getSolution());

    }

    public ProblemForm() {
        this.problem = new Problem();
        this.testCases = new ArrayList<>();
        this.solution = new DocumentForm();
    }
    //------------------------------------------- GETTERS && SETTERS
    public Problem getProblem() { return problem; }
    public void setProblem(Problem problem) { this.problem = problem; }
    public DocumentForm getSolution() { return solution; }
    public void setSolution(DocumentForm solution) { this.solution = solution; }
    public List<TestCasesForm> getTestCases(){ return testCases; }
    public void setTestCases(List<TestCasesForm> testCases){ this.testCases = testCases; }
    //-------------------------------------------
    //-------------------------------------------
    public void addTestCase(){ testCases.add(new TestCasesForm()); }
    public void removeTestCase(Integer idx){ testCases.remove(idx.intValue()); }
    //-------------------------------------------
    //-------------------------------------------
    //-------------------------------------------
    public boolean process(StorageService storageService, BindingResult results, long count){

        if(!solution.process(storageService, StringUtils.webalize(count + ".solution.c"))){
            results.addError(new FieldError("saving","process","Couldn't not save the file (solution)!"));
            return false;
        }
        problem.setSolution(this.solution.getDocument());

        List<TestCase> cases = new ArrayList<>();
        int i = 1;
        for(TestCasesForm testCase: testCases){
            if(!testCase.process(storageService,results,count + "." + (i++) + ".test")) {
                results.addError(new FieldError("saving","process","Couldn't not save the testcase!"));
                return false;
            }
            cases.add(testCase.getTestCase());
        }
        problem.setTestCases(cases);
        return true;
    }

}
