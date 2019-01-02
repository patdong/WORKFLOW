package cn.ideal.wf.web;
/**
 * 流程配置中心的首页
 * @author 郭佟燕
 * @version 2.0
 * */

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
	@GetMapping("/cf/center")
    public ModelAndView homePage(HttpServletRequest request) {
        return new ModelAndView("config/center");
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
	 * 采用spring授权认证时，此方法生效。
	 * */
	@GetMapping("/")
    public ModelAndView getLoginPage0(HttpServletRequest request) {		
        return new ModelAndView("redirect:/cf/center");
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
	 * 不采用spring的授权认证时，可以采用此方法实现
	 * 采用spring的授权认证时，此方法将作废。 页面会跳转到getLoginPage0方法进行授权处理。
	 * */
	@PostMapping("/login")
    public ModelAndView postLogin(@ModelAttribute("userForm") User userForm,HttpServletRequest request) {		
		securityService.autoLogin(userForm.getUsername(), userForm.getPassword());
        return new ModelAndView("redirect:/center");
    }
}
