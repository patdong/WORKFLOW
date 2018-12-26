package cn.ideal.wf.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ideal.wf.model.Page;
import cn.ideal.wf.model.TableBrief;
import cn.ideal.wf.model.TableElement;
import cn.ideal.wf.service.ElementService;
import cn.ideal.wf.service.TableService;

@Controller
@RequestMapping("/tb")
public class TbConfigurationController {

	@Autowired
	private TableService tableService;
	@Autowired
	private ElementService elementService;
	/**
	 * 表单管理中心
	 * */
	@GetMapping("/tablecenter")
    public ModelAndView enterTableCenter(ModelMap map, HttpServletRequest request) {		
        return new ModelAndView("redirect:/tb/tablecenter/1");
    }
	
	/**
	 * 表单翻页
	 */
	@GetMapping("/tablecenter/{pageNumber}")
    public ModelAndView enterTableCenterWithPage( @PathVariable Long pageNumber,HttpServletRequest request) {		
        ModelAndView mav = new ModelAndView("tableCenter");
        List<TableBrief> emLst = tableService.findAll();
        Page page = new Page(new Long(emLst.size()),pageNumber);
        mav.addObject("page",page);
        mav.addObject("tbList", tableService.findAll(pageNumber,Page.pageSize));
        return mav;
    }
	
	/**
	 * 管理表单
	 * */
	@GetMapping("/tabledefination/{tbId}")
    public ModelAndView defineWorkflow(@PathVariable Long tbId, 
    		@RequestParam(value = "scope", defaultValue = "") String scope,
    		@RequestParam(value = "style", defaultValue = "2") String style,
    		@RequestParam(value = "fieldsetting", defaultValue = "no") String fieldsetting,
    		HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("tableDefination");
		scope = (scope.equals(""))?"body":scope;
		mav.addObject("emList",elementService.findValidAllWithTable(tbId,scope));
		mav.addObject("tbemList",tableService.findAllTableElements(tbId,scope));
		mav.addObject("headList",tableService.findAllTableElements(tbId,"head"));
		mav.addObject("bodyList",tableService.findAllTableElements(tbId,"body"));
		mav.addObject("footList",tableService.findAllTableElements(tbId,"foot"));
		mav.addObject("brief",tableService.find(tbId));
		mav.addObject("tbId",tbId);
		mav.addObject("scope",scope);
		mav.addObject("style",style);
		mav.addObject("fieldsetting",fieldsetting);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mav.addObject("heads",mapper.writeValueAsString(tableService.findAllTableElements(tbId,"head")));
			mav.addObject("bodys",mapper.writeValueAsString(tableService.findAllTableElements(tbId,"body")));
			mav.addObject("foots",mapper.writeValueAsString(tableService.findAllTableElements(tbId,"foot")));
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}
        return mav;
    }
		
	/**
	 * 创建一个新的表单
	 * */
	@GetMapping("/newtablebrief")
    public ModelAndView newTableBrief(HttpServletRequest request) {	
		TableBrief tb = tableService.saveTableBrief(new TableBrief());
		ModelAndView mav = new ModelAndView("redirect:/tb/tabledefination/"+tb.getTbId());
        mav.addObject("emList",elementService.findValidAll());
        mav.addObject("tbId",tb.getTbId());
        
        return  mav;
    }
	
	/**
	 * 保存表单页面中选中的元素
	 * @param request
	 * @return
	 */
	@GetMapping("/savecheckedelements/{tbId}")
    public @ResponseBody boolean savecheckedelements(@PathVariable Long tbId,
    		@RequestParam("checkedIds[]") Long[] emId,
    		@RequestParam("scope") String scope,HttpServletRequest request) {	
		TableElement[] te = new TableElement[emId.length];
		for(int i=0;i<emId.length; i++){
			TableElement telement = new TableElement();
			telement.setCreatedDate(new Date());
			telement.setEmId(emId[i]);
			telement.setTbId(tbId);			
			telement.setScope(scope);
			telement.setSeq(new Long(i));
			te[i] = telement;
		}
		return tableService.saveTableElement(te);        
    }
	
	/**
	 * 保存表单页面中元素的向上调顺序
	 * @param request
	 * @return
	 */
	@GetMapping("/moveup/{tbId}/{emId}")
    public @ResponseBody boolean moveup(@PathVariable Long tbId, @PathVariable Long emId,HttpServletRequest request) {	
		return tableService.moveUp(tbId,emId);		
    }
	
	/**
	 * 保存表单页面中元素的向下调顺序
	 * @param request
	 * @return
	 */
	@GetMapping("/movedown/{tbId}/{emId}")
    public @ResponseBody boolean movedown(@PathVariable Long tbId, @PathVariable Long emId,HttpServletRequest request) {	
		return tableService.moveDown(tbId,emId);		
    }
	
	@GetMapping("/remove/{tbId}/{emId}")
    public @ResponseBody boolean remove(@PathVariable Long tbId, @PathVariable Long emId,HttpServletRequest request) {	
		tableService.delete(tbId,emId);	
		return true;
    }
	
	@GetMapping("/setTableName/{tbId}")
    public @ResponseBody boolean setTableName(@PathVariable Long tbId,@RequestParam("tableName") String tableName,HttpServletRequest request) {	
		return tableService.setTableName(tbId,tableName);			
    }
}
