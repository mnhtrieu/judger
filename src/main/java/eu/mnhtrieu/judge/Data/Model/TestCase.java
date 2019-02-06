package eu.mnhtrieu.judge.Data.Model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "{msg.problems.tests.name} {form.error.notnull}")
    private String name;

    @NotBlank(message = "{msg.problems.tests.description} {form.error.notnull}")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    private Document file;

    @NotNull(message = "{msg.problems.tests.numOfRuns} {form.error.notnull}")
    private Integer numOfRuns = 1;

    @NotNull(message = "{msg.problems.tests.timelimit} {form.error.notnull}")
    private Double timeLimit = 10.0;

    @NotNull(message = "{msg.problems.tests.memorylimit} {form.error.notnull}")
    private Long memoryLimit = 128000L;

    @NotNull
    @Column(columnDefinition = "TINYINT")
    private Boolean isGenerator = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Document getFile() {
        return file;
    }

    public void setFile(Document file) {
        this.file = file;
    }

    public Integer getNumOfRuns() {
        return numOfRuns;
    }

    public void setNumOfRuns(Integer numOfRuns) {
        this.numOfRuns = numOfRuns;
    }

    public Double getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Double timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Long getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Long memoryLimit) {
        this.memoryLimit = memoryLimit;
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

    public Boolean getGenerator() {
        return isGenerator;
    }

    public void setGenerator(Boolean generator) {
        isGenerator = generator;
    }
}
