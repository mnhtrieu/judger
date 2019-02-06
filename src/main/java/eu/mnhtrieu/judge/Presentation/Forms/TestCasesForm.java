package eu.mnhtrieu.judge.Presentation.Forms;
import eu.mnhtrieu.judge.Business.StorageService;
import eu.mnhtrieu.judge.Data.Model.TestCase;
import eu.mnhtrieu.judge.Utils.StringUtils;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

public class TestCasesForm {

    @Valid
    private TestCase testCase;

    private DocumentForm code;

    public TestCasesForm(){
        code = new DocumentForm();
        testCase = new TestCase();
    }

    public TestCasesForm(TestCase testCase){
        this.testCase = testCase;
        this.code = new DocumentForm(this.testCase.getFile());
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public DocumentForm getCode() {
        return code;
    }

    public void setCode(DocumentForm code) {
        this.code = code;
    }

    public boolean process(StorageService storageService, BindingResult results, String name){
        if(!code.process(storageService, StringUtils.webalize(name))) return false;
        testCase.setFile(code.getDocument());
        return true;
    }

}
