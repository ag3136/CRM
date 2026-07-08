package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String showAllUsers(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("currentUser", userService.findByUsername(currentUser.getUsername()));
        model.addAttribute("users", userService.listAllUsers());
        return "user/list";
    }

    @GetMapping("/edit/{id}")
    public String showFormEditUser(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.showUser(id));
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    public String processRequestEditUser(@PathVariable Long id, @Valid User user,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/user/edit/" + id;
        } else {
            userService.editUser(user);
            return "redirect:/user/list";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(userService.showUser(id));
        return "redirect:/user/list";
    }

}
