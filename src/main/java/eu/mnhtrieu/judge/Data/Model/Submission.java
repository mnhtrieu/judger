package eu.mnhtrieu.judge.Data.Model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @NotNull
    private Problem problem;

    @ManyToOne
    @NotNull
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Document program;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "submission")
    @OrderBy("id DESC")
    private Set<TestInput> testInputs;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creation;

    @NotNull
    private Integer state;

    @PrePersist
    public void onCreate(){
        creation = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Document getProgram() {
        return program;
    }

    public void setProgram(Document program) {
        this.program = program;
    }

    public Set<TestInput> getTestInputs() {
        return testInputs;
    }

    public void setTestInputs(Set<TestInput> testInputs) {
        this.testInputs = testInputs;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public int getCorrectTests(){
        int i = 0;
        if(testInputs == null) return 0;
        for(TestInput testInput: testInputs){
            if(testInput.getError() == null || testInput.getError().length() <= 0) i++;
        }
        return i;
    }
}
