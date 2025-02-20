package lt.ca.javau11.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController handles requests to the home page.
 */
@Controller
public class HomeController {

    /**
     * Displays the home page.
     *
     * @param model the model to pass data to the view.
     * @return the name of the home template.
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to BellTime!");
        return "home"; 
    }
    @GetMapping("/calendar")
    public String calendar(Model model) {
        model.addAttribute("pageTitle", "Schedule Calendar");
        return "calendar";
    }
}
