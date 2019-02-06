package eu.mnhtrieu.judge.Presentation.Forms;

import eu.mnhtrieu.judge.Business.Storage.RawFile;
import eu.mnhtrieu.judge.Business.StorageService;
import eu.mnhtrieu.judge.Data.Model.Document;
import eu.mnhtrieu.judge.Utils.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DocumentForm {

    @Valid
    private Document document;

    @NotBlank(message = "{form.error.notnull}")
    private String content;

    public DocumentForm(Document document) {
        this.document = document;
        try {
            this.content = new String(Files.readAllBytes(Paths.get(this.document.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DocumentForm() {
        document = new Document();
        this.content = "";
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean process(StorageService storageService, String name){
        Path path = storageService.store(new RawFile(getContent(), StringUtils.webalize(name)));
        if(path == null) return false;
        document.setPath(path.toString());
        return true;
    }
}
