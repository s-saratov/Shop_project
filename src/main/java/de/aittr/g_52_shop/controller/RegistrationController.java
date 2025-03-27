package de.aittr.g_52_shop.controller;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.exception_handling.Response;
import de.aittr.g_52_shop.service.interfaces.ConfirmationService;
import de.aittr.g_52_shop.service.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService service;
    private final ConfirmationService confirmationService;

    public RegistrationController(UserService service, ConfirmationService confirmationService) {
        this.service = service;
        this.confirmationService = confirmationService;
    }

    @PostMapping
    public Response register(@RequestBody User user) {
        service.register(user);
        return new Response("User registered successfully. Please check your email for confirmation.");
    }

    @GetMapping("/{code}")
    public Response confirm(@PathVariable String code) {
        try {
            confirmationService.confirmRegistration(code);
            return new Response("Email confirmed successfully. You can now log in.");
        } catch (Exception e) {
            return new Response("Error: " + e.getMessage());
        }
    }

}
