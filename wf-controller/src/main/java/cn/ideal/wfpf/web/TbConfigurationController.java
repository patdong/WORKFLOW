package cn.ideal.wfpf.web;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wfpf.model.FMsg;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;
import cn.ideal.wfpf.service.ElementService;
import cn.ideal.wfpf.service.TableService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        ModelAndView mav = new ModelAndView("config/tableCenter");
        List<TableBrief> emLst = tableService.findAll();
        Page<TableBrief> page = new Page<TableBrief>(new Long(emLst.size()),pageNumber);
        page.setPageList(tableService.findAll(page));
        page.setUrl("/tb/tablecenter");
        mav.addObject("page",page);
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
		ModelAndView mav = new ModelAndView("config/tableDefination");
		scope = (scope.equals(""))?"body":scope;
		mav.addObject("emList",elementService.findValidAllWithTable(tbId,scope));
		mav.addObject("tbemList",tableService.findTableAllElements(tbId,scope));
		List<TableElement> headLst = tableService.findTableAllElements(tbId,"head");
		List<TableElement> bodyLst = tableService.findTableAllElements(tbId,"body");		
		List<TableElement> footLst = tableService.findTableAllElements(tbId,"foot");
		
		if(bodyLst.size() % Long.parseLong(style) != 0){
			for(int i=0; i< bodyLst.size() % Long.parseLong(style) ; i++){
				bodyLst.add(new TableElement());
			}
		}
		mav.addObject("headList",headLst);
		mav.addObject("bodyList",bodyLst);
		mav.addObject("footList",footLst);
		mav.addObject("brief",tableService.find(tbId));
		mav.addObject("tbId",tbId);
		mav.addObject("scope",scope);
		mav.addObject("style",style);
		mav.addObject("fieldsetting",fieldsetting);
		mav.addObject("tbList", tableService.findTableAllElementsWithSpecialElements(tbId));
		try {
			ObjectMapper mapper = new ObjectMapper();
			mav.addObject("heads",mapper.writeValueAsString(headLst));
			mav.addObject("bodys",mapper.writeValueAsString(bodyLst));
			mav.addObject("foots",mapper.writeValueAsString(footLst));
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
	
	/**
	 * 
	 * 保存表单名称
	 */
	@GetMapping("/setTableName/{tbId}")
    public @ResponseBody boolean setTableName(@PathVariable Long tbId,@RequestParam("tableName") String tableName,HttpServletRequest request) {	
		return tableService.setTableName(tbId,tableName);			
    }
		
	/**
	 * 保存表单概述信息
	 */
	@GetMapping("/savetbrief/{tbId}")
    public @ResponseBody boolean savetbrief(@PathVariable Long tbId,HttpServletRequest request) {
		String template = request.getParameter("template");
		String style = request.getParameter("style");
		TableBrief tb = new TableBrief();
		tb.setTbId(tbId);
		tb.setTemplate(template);
		tb.setCols(Long.parseLong(style));
		tb = tableService.updateTableBrief(tb);
		if(tb == null) return false;
		return true;
    }
	
	/**
	 * 设置列表展现的字段
	 * @param request
	 * @return
	 */
	@GetMapping("/setlist/{tbId}")
    public @ResponseBody boolean setList(@PathVariable Long tbId,
    		@RequestParam("checkedIds[]") String[] emtdIds,HttpServletRequest request) {
		List<Long> emIds = new ArrayList<Long>();
		List<Long> newEmIds = new ArrayList<Long>();
		for(String id :emtdIds){
			String[] key = id.split("-");
			if(key.length == 2) emIds.add(Long.parseLong(key[0]));
			else newEmIds.add(Long.parseLong(key[0]));
		}
		return tableService.updateTableElementList(tbId,emIds.toArray(new Long[emIds.size()]),newEmIds.toArray(new Long[newEmIds.size()]));
		
    }	
	
	/**
	 * 为指定的表单创建数据库表
	 * @param request
	 * @return
	 */
	@GetMapping("/createtable/{tbId}")
    public @ResponseBody FMsg createTable(@PathVariable Long tbId,
    		@RequestParam("tbName") String tbName , HttpServletRequest request) {
		FMsg fmsg = null;
		try{
			boolean res = tableService.createTable(tbId, tbName);
			fmsg = new FMsg(res);
		}catch(Exception e){
			fmsg = new FMsg(FMsg.ERROR,e.getMessage());
		}
		
		return fmsg;
    }
	
	/**
	 * 返回表单有效的字段
	 * @param request
	 * @return
	 */
	@GetMapping("/getTableScheme/{tbId}")
    public @ResponseBody List<TableElement> getTableElements(@PathVariable Long tbId, HttpServletRequest request) {	
		return tableService.findTableAllElements(tbId);	
    }
	
	/**
	 * 保存表单字段的设置
	 * @param node
	 * @param preNodeId
	 * @param request
	 * @return
	 */
	@PostMapping("/saveElement/{tbId}")
    public ModelAndView saveElement(@ModelAttribute("element") TableElement element, @PathVariable Long tbId,
    		HttpServletRequest request) {	
		tableService.updateTableElement(element);
        return new ModelAndView("redirect:/tb/tabledefination/"+tbId+"?scope=body&style=2&fieldsetting=yes");
    }
}
