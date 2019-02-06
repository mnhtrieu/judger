package eu.mnhtrieu.judge.Presentation;

import eu.mnhtrieu.judge.Business.UserService;
import eu.mnhtrieu.judge.Data.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class CurrentUserAdvice {

    private final UserService userService;
    @Autowired
    public CurrentUserAdvice(UserService userService) {
        this.userService = userService;
    }
    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEmail(auth.getName());
    }
}
