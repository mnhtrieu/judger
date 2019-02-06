package eu.mnhtrieu.judge.Presentation.Admin;

import eu.mnhtrieu.judge.Business.CodeService;
import eu.mnhtrieu.judge.Business.ProblemService;
import eu.mnhtrieu.judge.Business.StorageService;
import eu.mnhtrieu.judge.Business.UserService;
import eu.mnhtrieu.judge.Data.Model.Problem;
import eu.mnhtrieu.judge.Data.Model.TestCase;
import eu.mnhtrieu.judge.Presentation.Forms.ProblemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller("adminProblemController")
@RequestMapping("/admin")
@SessionAttributes("problem")
public class ProblemsController {

    private final ProblemService problemService;
    private final StorageService storageService;
    private final MessageSource messageSource;
    private final UserService userService;
    private ProblemForm problemForm;
    private final CodeService codeService;
    @Autowired
    public ProblemsController(MessageSource messageSource, ProblemService problemService, StorageService storageService,
                              UserService userService, CodeService codeService) {
        this.problemService = problemService;
        this.messageSource = messageSource;
        this.storageService = storageService;
        this.userService = userService;
        this.codeService = codeService;
    }

    @RequestMapping("/problems")
    public String renderProblems(Model model){
        model.addAttribute("problems",problemService.findAll());
        return "admin/problems/default";
    }

    @RequestMapping(value = "/problems/add", method = RequestMethod.GET)
    public String renderAdd(Model model){
        problemForm = new ProblemForm();
        model.addAttribute("problem",problemForm);
        return "admin/problems/add";
    }


    @RequestMapping(value = "/problems/delete/{id}", method = RequestMethod.GET)
    public String deleteProblem(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        if(problemService.deleteProblem(id)){
            redirectAttributes.addFlashAttribute("msg",messageSource.getMessage("msg.problems.delete.success",null, Locale.getDefault()));
            redirectAttributes.addFlashAttribute("msgtype","alert-success");
        }
        else{
            redirectAttributes.addFlashAttribute("msg",messageSource.getMessage("msg.problems.delete.fail",null, Locale.getDefault()));
            redirectAttributes.addFlashAttribute("msgtype","alert-warning");
        }
        return "redirect:/admin/problems";
    }


    @RequestMapping(value = "/problems/solution/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serverFile(@PathVariable String filename){
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().body(file);
    }


    @RequestMapping(value = "/problems/edit/{id}", method = RequestMethod.GET)
    public String editProblem(@PathVariable("id") Integer id, Model model,RedirectAttributes redirectAttributes){
        Problem p = problemService.findById(id);
        if(p == null){
            redirectAttributes.addFlashAttribute("msg",messageSource.getMessage("msg.problems.cant.find",null, Locale.getDefault()));
            redirectAttributes.addFlashAttribute("msgtype","alert-warning");
            return "redirect:/admin/problems";
        }
        problemForm = new ProblemForm(p);
        model.addAttribute("problem",problemForm);
        model.addAttribute("editing",true);
        return "/admin/problems/add";
    }

    //================================================================================= ADD
    @RequestMapping(value="/problems/add", method = RequestMethod.POST)
    public String addProblem(@Valid @ModelAttribute("problem") ProblemForm problem, BindingResult results, RedirectAttributes redirectAttributes){
        problemForm = problem;
        if(results.hasErrors()){
            List<FieldError> errors = results.getFieldErrors();
            for (FieldError error : errors ) {
                System.out.println (error.getField() + " - " + error.getDefaultMessage());
            }
            return "admin/problems/add";
        }
        //----------------------------------- adding the problem
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long id;
        if(problem.getProblem().getId() == null) id = problemService.getCount()+1;
        else id = problem.getProblem().getId();
        if(problem.process(storageService,results,id)){
            if(problem.getProblem().getId() == null) problem.getProblem().setUser(userService.findByEmail(auth.getName()));
            problemService.saveProblem(problem.getProblem());
            redirectAttributes.addFlashAttribute("msg",messageSource.getMessage("msg.problems.add.success",null, Locale.getDefault()));
            redirectAttributes.addFlashAttribute("msgtype","alert-success");
            return "redirect:/admin/problems/";
        }
        return "admin/problems/add";
    }

    //================================================================================= TESTS
    @RequestMapping(value="/problems/addtest", method = RequestMethod.POST)
    public String addTest(@ModelAttribute("problem") ProblemForm tmp, Model model){
        problemForm = tmp;
        problemForm.addTestCase();
        return renderTests(model);
    }

    @RequestMapping(value="/problems/removetest/{id}", method = RequestMethod.POST)
    public String removeTest(@ModelAttribute("problem") ProblemForm tmp, @PathVariable("id") Integer id, Model model){
        problemForm = tmp;
        problemForm.removeTestCase(id);
        return renderTests(model);
    }

    @RequestMapping(value="/problems/rendertests", method = RequestMethod.GET)
    public String renderTests(Model model){
        model.addAttribute("problem",problemForm);
        return "admin/problems/_tests :: testCases";
    }

    @RequestMapping(value="/problems/run/generator/", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String runGenerator(@RequestParam("source_code") String source) throws InterruptedException, ExecutionException {
        System.out.println("Invoking an asynchronous method. " + Thread.currentThread().getName());
        Future<String> future = codeService.send(source);
        while(true){
            if(future.isDone()){
                System.out.println("result " + future.get());
                return future.get();
            }
            System.out.println("continue doing");
            Thread.sleep(1000);
        }
    }

    //=================================================================================
    //=================================================================================




}
