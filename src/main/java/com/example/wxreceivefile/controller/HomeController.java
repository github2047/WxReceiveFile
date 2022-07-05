package com.example.wxreceivefile.controller;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class HomeController {
    @RequestMapping(value = "/page/{page}", method = RequestMethod.GET)
    public ModelAndView toPage(@PathVariable("page") String page) {
        return new ModelAndView(String.format("%s", page));
    }
    @RequestMapping("/home")
    private String home(String userid, Model model){
        if(userid==null){
            log.error("未收到userid null");
//            return "error";
        }
        System.out.println(userid);
        Map resultMap=new HashMap();
        resultMap.put("userid",userid);
        model.addAttribute("map",resultMap);
        return "home";
    }
}
