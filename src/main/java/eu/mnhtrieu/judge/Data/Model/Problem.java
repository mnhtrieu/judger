package eu.mnhtrieu.judge.Data.Model;

import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

// @TODO edit lock

@Entity
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "{msg.problems.name} {form.error.notnull}")
    private String name;

    @NotBlank(message = "{msg.problems.description} {form.error.notnull}")
    @Lob
    private String description;

    @ManyToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Document solution;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy
    private List<TestCase> testCases;

    @NotNull(message = "{msg.problems.points} {form.error.notnull}")
    private double points = 1.0;

    @NotNull(message = "{msg.problems.compilation} {form.error.notnull}")
    private String compilation = "g++ -Wall -pedantic";

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date creation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "problem")
    private List<Submission> submissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Document getSolution() {
        return solution;
    }

    public void setSolution(Document solution) {
        this.solution = solution;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getCompilation() {
        return compilation;
    }

    public void setCompilation(String compilation) {
        this.compilation = compilation;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    @PrePersist
    public void onCreate(){
        creation = new Date();
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public int getTestCount(){
        int i = 0;
        if(testCases == null) return 0;
        for(TestCase testcase: testCases){
            i += testcase.getNumOfRuns();
        }
        return i;
    }

}
