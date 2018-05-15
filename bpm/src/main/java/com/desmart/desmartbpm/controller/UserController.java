package com.desmart.desmartbpm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.sql.DriverManager;

@Controller
public class UserController extends BaseWebController {
    
   @RequestMapping(value = "/bpmindex") 
   public ModelAndView toIndex() {
       return new ModelAndView("desmartbpm/index");
   }


}