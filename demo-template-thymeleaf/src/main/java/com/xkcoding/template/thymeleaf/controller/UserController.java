package com.xkcoding.template.thymeleaf.controller;

import com.xkcoding.template.thymeleaf.listener.LoginEvent;
import com.xkcoding.template.thymeleaf.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户页面
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-10-10 10:11
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    SimpleApplicationEventMulticaster mySimpleApplicationEventMulticaster;

    @PostMapping("/login")
    public ModelAndView login(User user, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();

        mv.addObject(user);
        mv.setViewName("redirect:/");

        request.getSession().setAttribute("user", user);

        mySimpleApplicationEventMulticaster.multicastEvent(new LoginEvent(this));
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("page/login");
    }
}
