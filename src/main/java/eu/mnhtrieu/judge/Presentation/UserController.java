package eu.mnhtrieu.judge.Presentation;

import eu.mnhtrieu.judge.Business.UserService;
import eu.mnhtrieu.judge.Data.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
public class UserController {

    private final UserService userService;

    private final MessageSource messageSource;

    @Autowired
    public UserController(UserService userService, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginView(RedirectAttributes redirectAttributes){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!(auth instanceof AnonymousAuthenticationToken)) {
            redirectAttributes.addFlashAttribute("msg",messageSource.getMessage("msg.already.logged",null,Locale.getDefault()));
            redirectAttributes.addFlashAttribute("msgtype","alert-warning");
            return "redirect:/";
        }
        return "login";
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registrationView(Model model){
        model.addAttribute("user",new User());
        return "register";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@Valid User user, BindingResult results, RedirectAttributes redirectAttributes){
        User userExist = userService.findByEmail(user.getEmail());
        if(userExist != null){
            results.rejectValue("email","error.user","msg.form.email.exist");
        }
        if(results.hasErrors()){
            List<FieldError> errors = results.getFieldErrors();
            for (FieldError error : errors ) {
                System.out.println (error.getField() + " - " + error.getDefaultMessage());
            }
            return "register";
        }
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("formmessage",messageSource.getMessage("msg.registration.success",null, Locale.getDefault()));
        redirectAttributes.addFlashAttribute("formmessagetype","alert-success");
        return "redirect:/login";
    }
}
