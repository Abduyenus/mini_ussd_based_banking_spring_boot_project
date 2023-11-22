package com.example.bank.Controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/v1/bank/welcome")
public class HomeController {
    @Autowired
    private MessageSource messageSource;
	
    @GetMapping
    public String[] changeLanguage(@RequestHeader("Accept-Language") String locale) {
        Locale locale1 = new Locale(locale);
        String[] messages = new String[8];
        for(int i = 0;i<messages.length;i++)
        {
            String messageKey = "message" + i;
            messages[i] = messageSource.getMessage(messageKey,null,locale1);
        }
        return messages;
    }
}
