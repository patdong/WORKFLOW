package cn.ideal.wfpf.web;

/**
 * 流程节点处理
 * @author 郭佟燕
 * @version 2.0
 */
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wfpf.service.SysService;

@Controller
@RequestMapping("/sys")
public class SysConfigurationController {

	@Autowired
	private SysService sysService;
	
	/**
	 * 系统设置中心
	 * */
	@GetMapping("/syscenter")
    public ModelAndView enterWorkflowCenter(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("config/sysCenter");
		
		mav.addObject("confs", sysService.findAll());
		return mav;
    }
}
