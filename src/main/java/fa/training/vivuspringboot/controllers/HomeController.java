package fa.training.vivuspringboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home( @RequestParam(value ="name", defaultValue = "Jane", required = false) String name,
                        @RequestParam(value ="age", defaultValue = "18", required = false) int age,Model model) {
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        model.addAttribute("numbers", numbers);
        model.addAttribute("message", "Hello World");
        return "home/index";
    }

    @GetMapping("/about")
    public ModelAndView about() {
        ModelAndView modelAndView = new ModelAndView("home/about");
        modelAndView.addObject("companyName", "Vivu Store");
        return modelAndView;
    }

    @GetMapping("/contact/{id}")
    public String contact(@PathVariable("id") int id, Model model) {
        model.addAttribute("id", id);
        return "home/contact";
    }
}
