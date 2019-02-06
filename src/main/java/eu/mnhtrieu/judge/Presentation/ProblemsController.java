package eu.mnhtrieu.judge.Presentation;


import eu.mnhtrieu.judge.Business.*;
import eu.mnhtrieu.judge.Business.Storage.RawFile;
import eu.mnhtrieu.judge.Data.Model.*;
import eu.mnhtrieu.judge.Utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@SessionAttributes("problem")
public class ProblemsController {

    private final ProblemService problemService;
    private final SubmissionService submissionService;
    private final UserService userService;
    private final CodeService codeService;
    private final StorageService storageService;
    private final TestInputService testInputService;
    private final MessageSource messageSource;


    @Autowired
    public ProblemsController(ProblemService problemService, SubmissionService submissionService,
                              UserService userService, CodeService codeService, StorageService storageService,
                              MessageSource messageSource, TestInputService testInputService) {
        this.problemService = problemService;
        this.submissionService = submissionService;
        this.userService = userService;
        this.codeService = codeService;
        this.storageService = storageService;
        this.messageSource = messageSource;
        this.testInputService = testInputService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping("/problems")
    public String renderProblems(Model model){
        model.addAttribute("problems",problemService.findAll());
        return "problems/default";
    }

    @RequestMapping("/problems/{id}")
    public String renderProblem(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        Problem problem = problemService.findById(id);
        //System.out.println("problem has " + problem.getTestCases().size());
        if(problem == null) {
            redirectAttributes.addFlashAttribute("msg",messageSource.getMessage("msg.problems.cant.find",null, Locale.getDefault()));
            redirectAttributes.addFlashAttribute("msgtype","alert-warning");
            return "redirect:/problems/";
        }
        model.addAttribute("problem",problem);
        List<Submission> submissionList = submissionService.findAll(getUser(),problem);
        model.addAttribute("submissions",submissionList);
        return "problems/view";
    }

    @RequestMapping(value = "/problems/{id}/submissions/fetch/", method = RequestMethod.GET)
    public String fetchSubmissions(Model model, @PathVariable("id") Integer id){
        Problem problem = problemService.findById(id);
        List<Submission> submissionList = submissionService.findAll(getUser(),problem);
        model.addAttribute("subCode",getCode(submissionList));
        model.addAttribute("submissions",submissionList);


        return "problems/_submissions :: submissions";
    }


    @RequestMapping(value = "/problems/{id}/submissions/submit/", method = RequestMethod.POST)
    public String sendSubmission(Model model, @PathVariable("id") Integer id, @RequestParam("source_code") String code)
            throws InterruptedException, ExecutionException {
        Future<Boolean> future;
        Problem problem = problemService.findById(id);
        if(problem == null) fetchSubmissions(model, id);
        try {

            Submission submission = createNewSubmission(problem,code);
            future = codeService.submit(code, getUser(), submission);
        } catch (Exception e) {
            e.printStackTrace();
            return fetchSubmissions(model, id);
        }
        while(true){
            if(future.isDone()){
                break;
            }
            Thread.sleep(1000);
        }
        return fetchSubmissions(model, id);
    }

    @RequestMapping(value = "/problems/{id}/submissions/reveal/{idTest}", method = RequestMethod.GET)
    public String revealHint(Model model, @PathVariable("id") Integer id, @PathVariable("idTest") Integer test){
        testInputService.reveal(test);
        return fetchSubmissions(model,id);
    }

    private User getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEmail(auth.getName());
    }

    private Submission createNewSubmission(Problem problem, String source){
        Submission submission = new Submission();
        submission.setProblem(problem);
        submission.setUser(getUser());
        submission.setState(0);
        byte[] array = new byte[12];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        RawFile rawFile = new RawFile(source, StringUtils.webalize(getUser().getId() + "." + problem.getId() + "." + generatedString +".submit.c"));
        submission.setProgram(new Document(storageService.store(rawFile).toString()));
        return submissionService.save(submission);
    }

    private List<String> getCode(List<Submission> submissions){
        List<String> code = new ArrayList<>();
        for(Submission submission: submissions){
            String source = storageService.getContent(Paths.get(submission.getProgram().getPath()));
            System.out.println(source);
            code.add(source);
        }
        return code;
    }
}
