package cn.ideal.wfpf.web;
/**
 * 元素控制器
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	 * 元素中心
	 * */
	@GetMapping("/elementcenter")
    public ModelAndView enterElementCenter(ModelMap map, HttpServletRequest request) {		
        return new ModelAndView("redirect:/em/elementcenter/1");
    }
		
	/**
	 * 元素列表翻页处理
	 * @param pageNumber
	 * @param request
	 * @return
	 */
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
	
	/**
	 * 进入创建新元素页面
	 * @param request
	 * @return
	 */
	@GetMapping("/newelement")
    public ModelAndView newElement(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("config/elementDefination");
		mav.addObject("element", new Element());
		return mav;
    }
	
	@GetMapping("/elementdefination/{emId}")
    public ModelAndView elementDefination(@PathVariable Long emId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("config/elementDefination");
		mav.addObject("element", elementService.find(emId));
		return mav;
    }
	
	/**
	 * 保存元素
	 * @param element
	 * @param request
	 * @return
	 */
	@PostMapping("/saveElement")
    public ModelAndView saveElement(@ModelAttribute("element") Element element,
    		HttpServletRequest request) {
		if(element.getEmId() != null) elementService.update(element);
		else elementService.save(element);
        return new ModelAndView("redirect:/em/elementcenter");
    }
}
