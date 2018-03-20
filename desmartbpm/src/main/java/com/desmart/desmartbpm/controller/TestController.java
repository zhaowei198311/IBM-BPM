package com.desmart.desmartbpm.controller;

import com.alibaba.fastjson.JSONArray;
import com.desmart.desmartbpm.service.BpmProcessSnapshotService;
import com.desmart.desmartbpm.service.TestService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class TestController {
    @Autowired
    private TestService testService;
    @Autowired
    private BpmProcessSnapshotService bpmProcessSnapshotService;

    @RequestMapping(value = "/error.do")
    @ResponseBody
    public String error(String username, String password) {
        return "error";
    }

    @RequestMapping(value = "/toLogin.do")
    public ModelAndView toLogin() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/user/login.do")
    @ResponseBody
    public String login(String username, String password, HttpServletRequest request) {
        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        user.login(token);
        Session session = SecurityUtils.getSubject().getSession();
        session.setTimeout(17200000L);

        return "hello " + username;
    }

    @RequestMapping(value = "/document/open.do")
    @ResponseBody
    public String openDocument(String docId) {
        return "opened " + docId;
    }

    @RequestMapping(value = "/document/update.do")
    @ResponseBody
    public String updateDocument(String docId) {
        return "update " + docId;
    }

    @RequestMapping(value = "/task/submit.do")
    @ResponseBody
    public String submitTask(String taskId) {
        return "task submited " + taskId;
    }

    @RequestMapping(value = "/url1.do")
    @ResponseBody
    public String url1() {
        Subject user = SecurityUtils.getSubject();
        String _curruser = (String) user.getSession().getAttribute("_currUserNum");
        System.out.println(user.getSession().getTimeout());
        testService.method1();
        return "hello," + _curruser + "url1 success";
    }

    @RequestMapping(value = "/url2.do")
    @ResponseBody
    public String url2() {
        Subject user = SecurityUtils.getSubject();
        String _curruser = (String) user.getSession().getAttribute("_currUserNum");
        System.out.println(user.getSession().getTimeout());
        System.out.println(user.getSession().getLastAccessTime());
        return "hello," + _curruser + "url2 success";
    }

    @RequestMapping(value = "/unauthorizedUrl.do")
    @ResponseBody
    public String url3() {
        Subject user = SecurityUtils.getSubject();
        String _curruser = (String) user.getSession().getAttribute("_currUserNum");
        System.out.println(user.getSession().getTimeout());
        System.out.println(user.getSession().getLastAccessTime());
        return "hello," + _curruser + " this is  unauthorizedUrl ";
    }

    @RequestMapping(value = "/test.do")
    @ResponseBody
    public Object test(HttpServletRequest request) {
        //bpmProcessSnapshotService.processModel(request, "25.0e089867-84d7-489b-a114-2f8d92c226a9", "", "2066.49fe4fdc-8488-4663-92d1-d1befcb671c2");
        bpmProcessSnapshotService.processModel(request, "25.0e089867-84d7-489b-a114-2f8d92c226a9", "", "2066.49fe4fdc-8488-4663-92d1-d1befcb671c2", "");
        return null;
    }

}