package cn.ideal.wf.web;
/**
 * 流程配置中心的首页
 * @author 郭佟燕
 * @version 2.0
 * */

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wf.model.User;
import cn.ideal.wf.service.SecurityService;

@Controller
public class HomeController {
	@Autowired
    private SecurityService securityService;
	/**
	 * 首页
	 * */
	@GetMapping("/")
    public ModelAndView homePage(HttpServletRequest request) {
        return new ModelAndView("center");
    }
	
	/**
	 * 默认布局
	 * */
	@RequestMapping("/layout")
	public String layout() {
	    return "layout";
	}
	
	/**
	 * 进入登陆页面
	 * */
	@GetMapping("/login")
    public ModelAndView getLoginPage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("userForm", new User());
        return mav;
    }
	
	/**
	 * 登陆页面选择登陆按钮操作
	 * */
	@PostMapping("/login")
    public ModelAndView postLogin(@ModelAttribute("userForm") User userForm,HttpServletRequest request) {		
		securityService.autoLogin(userForm.getUsername(), userForm.getPassword());
        return new ModelAndView("redirect:/wf/workflowcenter");
    }
}
