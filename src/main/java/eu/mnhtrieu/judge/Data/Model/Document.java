package eu.mnhtrieu.judge.Data.Model;


import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String path;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creation;

    public Integer getId() {
        return id;
    }

    public Document(){ }

    public Document(String path) {
        this.path = path;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
    
    @PreRemove
    public void onDelete() throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

}
