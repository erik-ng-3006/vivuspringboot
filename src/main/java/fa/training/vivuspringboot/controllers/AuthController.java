package fa.training.vivuspringboot.controllers;


import fa.training.vivuspringboot.dtos.auth.RegisterRequestDTO;
import fa.training.vivuspringboot.services.IAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    // for web mvc + thymeleaf
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        model.addAttribute("registerRequestDTO", registerRequestDTO);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequestDTO registerRequestDTO, BindingResult bindingResult, Model model) {
        // Validate data from form
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // Register
        var result = authService.register(registerRequestDTO);

        // Redirect to log in if register successfully else show error
        if (result) {
            return "redirect:/auth/login";
        } else {
            model.addAttribute("error", "Register failed");
            return "auth/register";
        }
    }
}
