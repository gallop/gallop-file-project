package com.gallop.file.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * author gallop
 * date 2021-07-22 9:02
 * Description:
 * Modified By:
 */

@Controller
@RequestMapping("/ui")
public class IndexController {

    //@RequiresPermissions("admin:ui:index")
    //@RequiresPermissionsDesc(menu = {"系统管理", "我的文档ui"}, button = "查询")
    @GetMapping("/index")
    public ModelAndView index(Model model, HttpServletRequest request) {
        System.err.println("into index page........getServletPath="+request.getServletPath()+",getServerName="+request.getServerName()+",getServerPort="+request.getServerPort());
        String token = request.getParameter("token");
        //System.err.println("token="+token);
        model.addAttribute("userName", "gallop");
        model.addAttribute("token", token);
        return new ModelAndView("index");
    }

}
