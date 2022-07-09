package com.example.wxreceivefile.controller;

import com.example.wxreceivefile.mapper.PlatUserMapper;
import com.example.wxreceivefile.pojo.PlatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    private PlatUserMapper platUserMapper;
    @RequestMapping(value = "/page/{page}", method = RequestMethod.GET)
    public ModelAndView toPage(@PathVariable("page") String page) {
        System.out.println(page);
        return new ModelAndView(String.format("%s", page));
    }
    @RequestMapping("/home")
    private String home(String userid, Model model, HttpServletRequest request){
        if(userid==null){
            log.error("未收到userid null");
            return "error";
        }
        HttpSession session = request.getSession();
        session.setAttribute("loginName", userid);
        Map resultMap=new HashMap();
        resultMap.put("userid",userid);
        model.addAttribute("map",resultMap);
        return "home";
    }
    @RequestMapping("/verify")
    @ResponseBody
    private Map verify(@RequestBody Map map){
        String userid= (String) map.get("userid");
        String type="error";
        Map resultMap=new HashMap();
        if(userid==null || type==null){
            log.error("未收到userid null");
            resultMap.put("status","500");
            return resultMap;
        }
        PlatUser user = platUserMapper.getInfo(userid, type);
        if (user==null) {
            log.error("userid 不正确");
            resultMap.put("status","500");
            return resultMap;
        }
        String loginName=user.getLoginName();
        resultMap.put("status","200");
        resultMap.put("loginName",loginName);
        return resultMap;
    }
}
