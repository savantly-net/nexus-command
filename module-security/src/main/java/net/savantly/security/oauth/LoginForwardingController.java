package net.savantly.security.oauth;

import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping({"/login"})
public class LoginForwardingController {

    @RequestMapping(
        produces = {"text/html"}
    )
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/wicket/";
    }
}