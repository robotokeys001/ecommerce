package com.chiararadaelli.ecommerce.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {
    
    
    @RequestMapping(value ="/login",method = { RequestMethod.GET, RequestMethod.POST })
    public String displayLoginPage(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "logout", required = false) String logout,
                                   @RequestParam(value = "registazione", required = false) String registazione,
                                   Model model) {
        String errorMessage = null;
        if(error != null) {
            errorMessage = "Username or Password is incorrect !!";
        } else if(logout != null) {
            errorMessage = "You have been successfully logged out !!";
        } else if(registazione != null) {
            errorMessage = "You registration successful. Login with registered credentials !!";
        }
        
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
   
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }


}
