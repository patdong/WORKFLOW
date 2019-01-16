package cn.ideal.wfpf.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wfpf.model.Element;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.service.ElementService;

@Controller
@RequestMapping("/em")
public class EmConfigurationController {

	@Autowired
	private ElementService elementService;
	/**
	 * 表单元素中心
	 * */
	@GetMapping("/elementcenter")
    public ModelAndView enterElementCenter(ModelMap map, HttpServletRequest request) {		
        return new ModelAndView("redirect:/em/elementcenter/1");
    }
		
	@GetMapping("/elementcenter/{pageNumber}")
    public ModelAndView enterElementCenterWithPage( @PathVariable Long pageNumber,HttpServletRequest request) {		
        ModelAndView mav = new ModelAndView("config/elementCenter");
        List<Element> emLst = elementService.findAll();
        Page<Element> page = new Page<Element>(new Long(emLst.size()),pageNumber);
        page.setPageList(elementService.findAll(page));
        page.setUrl("/em/elementcenter");
        mav.addObject("page",page);        
        return mav;
    }
}
