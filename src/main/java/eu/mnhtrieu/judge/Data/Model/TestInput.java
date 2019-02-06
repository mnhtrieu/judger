package eu.mnhtrieu.judge.Data.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class TestInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean hint = false;

    @NotNull
    private Double memory;

    @NotNull
    private Double time;

    @Lob
    private String stdout;

    @Lob
    private String error;

    @Lob
    private String stderr;

    @Lob
    private String refOut;

    @Lob
    private String compileOutput;

    @ManyToOne
    private Submission submission;

    @OneToOne
    private TestCase testCase;

    @Lob
    private String stdin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getHint() {
        return hint;
    }

    public void setHint(Boolean hint) {
        this.hint = hint;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStderr() {
        return stderr;
    }

    public void setStderr(String stderr) {
        this.stderr = stderr;
    }

    public String getRefOut() {
        return refOut;
    }

    public void setRefOut(String refOut) {
        this.refOut = refOut;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public String getStdin() {
        return stdin;
    }

    public void setStdin(String stdin) {
        this.stdin = stdin;
    }

    public String getCompileOutput() {
        return compileOutput;
    }

    public void setCompileOutput(String compileOutput) {
        this.compileOutput = compileOutput;
    }
}

