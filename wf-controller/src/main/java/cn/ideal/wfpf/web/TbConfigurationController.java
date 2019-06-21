package cn.ideal.wfpf.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wf.table.draw.PureTableService;
import cn.ideal.wfpf.model.FMsg;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.model.TableBrief;
import cn.ideal.wfpf.model.TableElement;
import cn.ideal.wfpf.model.TableLayout;
import cn.ideal.wfpf.service.ElementService;
import cn.ideal.wfpf.service.TableService;
import cn.ideal.wfpf.service.WorkflowService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@RequestMapping("/tb")
public class TbConfigurationController {

	@Autowired
	private TableService tableService;
	@Autowired
	private ElementService elementService;
	@Autowired
	private PureTableService plattenTableService;
	@Autowired
	private WorkflowService wfService;
	
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
        mav.addObject("wfLst", wfService.findAll());
        mav.addObject("templateLst", tableService.findBizTemplates());
        return mav;
    }
	
	/**
	 * 管理表单
	 * */
	@GetMapping("/tabledefination/{tbId}")
    public ModelAndView defineTable(@PathVariable Long tbId, 
    		@RequestParam(value = "scope", defaultValue = "") String scope,
    		@RequestParam(value = "fieldsetting", defaultValue = "no") String fieldsetting,
    		HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("config/tableDefination");
		scope = (scope.equals(""))?"表体":scope;
		mav.addObject("emList",elementService.findValidAllWithTable(tbId,scope));
		
		try {
			List<TableElement> te = tableService.findTableAllElements(tbId,scope);			
			ObjectMapper mapper = new ObjectMapper();
			mav.addObject("tbems",mapper.writeValueAsString(te));	
			mav.addObject("tbemList",te);
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}
		TableBrief tb = tableService.find(tbId);		
		for(TableLayout tl : tb.getLayout()){
			if(tl.getScope().equals("表头")) mav.addObject("headCols",tl.getCols());  
			if(tl.getScope().equals("表体")) mav.addObject("bodyCols",tl.getCols()); 
			if(tl.getScope().equals("表尾")) mav.addObject("footCols",tl.getCols()); 
			if(tl.getScope().equals(scope)) mav.addObject("layout", tl.getCols()+" 列");				
		}
		mav.addObject("layouts",tb.getLayout());
		mav.addObject("brief",tb);
		if(fieldsetting.equals("yes")) mav.addObject("table",plattenTableService.draw(tbId, scope,true).toString());
		else mav.addObject("table",plattenTableService.draw(tbId, scope,false).toString());
		mav.addObject("tbId",tbId);
		mav.addObject("scope",scope);
		mav.addObject("fieldsetting",fieldsetting);
		mav.addObject("tbList", tableService.findTableAllElementsWithListLevelElements(tbId));
		mav.addObject("subTbs", tableService.findAllSubTables(tbId));
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
			telement.setEmId(emId[i]);
			telement.setTbId(tbId);			
			telement.setScope(scope);			
			te[i] = telement;
		}
		return tableService.saveTableElement(te);        
    }
	
	/**
	 * 保存表单页面中元素的向上调顺序
	 * @param request
	 * @return
	 */
	@GetMapping("/moveup/{tbId}/{id}/{scope}")
    public @ResponseBody boolean moveup(@PathVariable Long tbId, @PathVariable Long id,@PathVariable String scope,HttpServletRequest request) {	
		return tableService.moveUp(tbId,id,scope);		
    }
	
	/**
	 * 保存表单页面中元素的向下调顺序
	 * @param request
	 * @return
	 */
	@GetMapping("/movedown/{tbId}/{id}/{scope}")
    public @ResponseBody boolean movedown(@PathVariable Long tbId, @PathVariable Long id,@PathVariable String scope,HttpServletRequest request) {	
		return tableService.moveDown(tbId,id,scope);		
    }
	
	@GetMapping("/remove/{tbId}/{id}")
    public @ResponseBody boolean remove(@PathVariable Long tbId, @PathVariable Long id,HttpServletRequest request) {	
		tableService.deleteElement(id);	
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
		return tableService.findTableFieldsToDBCheck(tbId);	
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
		String scope = request.getParameter("escope");
		element.setScope(scope);
		tableService.updateTableElement(element);
        return new ModelAndView("redirect:/tb/tabledefination/"+tbId+"?fieldsetting=yes").addObject("scope",scope);
    }
	
	
	/**
	 * 启用表单
	 * @param wfId
	 * @param request
	 * @return
	 */
	@GetMapping("/startup/{tbId}")
    public @ResponseBody boolean startUp(@PathVariable Long tbId, HttpServletRequest request) {			
        return tableService.setStatus(tbId, true);        
    }
	
	/**
	 * 停用表单
	 * @param wfId
	 * @param request
	 * @return
	 */
	@GetMapping("/shutdown/{tbId}")
    public @ResponseBody boolean shutdown(@PathVariable Long tbId, HttpServletRequest request) {			
        return tableService.setStatus(tbId, false);        
    }
	
	@GetMapping("/remove/{tbId}")
    public @ResponseBody boolean remove(@PathVariable Long tbId, HttpServletRequest request) {			
        return tableService.deleteTable(tbId);      
    }
	
	@GetMapping("/savelayout/{tbId}")
    public @ResponseBody boolean savelayout(@PathVariable Long tbId, HttpServletRequest request) {	
		Long headCols = null;
		Long bodyCols = null;
		Long footCols = null;
		if(!StringUtils.isEmpty(request.getParameter("headCols"))) headCols = Long.parseLong(request.getParameter("headCols"));
		if(!StringUtils.isEmpty(request.getParameter("bodyCols"))) bodyCols = Long.parseLong(request.getParameter("bodyCols"));
		if(!StringUtils.isEmpty(request.getParameter("footCols"))) footCols = Long.parseLong(request.getParameter("footCols"));
		return tableService.saveLayout(tbId,headCols,bodyCols,footCols); 		
    }
	
	/**
	 * 设置子表单
	 * @param tbId
	 * @param request
	 * @return
	 */
	@GetMapping("/setSubTable/{tbId}/{subTbId}/{scope}")
    public @ResponseBody boolean setSubTable(@PathVariable Long tbId,@PathVariable Long subTbId,@PathVariable String scope, HttpServletRequest request) {			
        return tableService.setSubTable(tbId,scope,subTbId);      
    }
	
	/**
	 * 重新绘制表单
	 * @param tbId
	 * @param scope
	 * @param request
	 * @return
	 */
	@GetMapping("/redraw/{tbId}/{scope}/{fieldsetting}")
    public @ResponseBody FMsg redraw(@PathVariable Long tbId,@PathVariable String scope,@PathVariable String fieldsetting, HttpServletRequest request) {			
        FMsg fmsg = new FMsg();
        fmsg.setCode(FMsg.SUCCESS);
		if(fieldsetting.equals("yes")) {			
			fmsg.setMessage(plattenTableService.draw(tbId, scope, true).toString()); 		
		}
        else fmsg.setMessage(plattenTableService.draw(tbId, scope, false).toString());
		
		return fmsg;
    }
	
	/**
	 * 预览
	 * @param tbId
	 * @param request
	 * @return
	 */
	@GetMapping("/review/{tbId}")
    public @ResponseBody FMsg redraw(@PathVariable Long tbId,HttpServletRequest request) {			
        FMsg fmsg = new FMsg();
        fmsg.setCode(FMsg.SUCCESS);
		fmsg.setMessage(plattenTableService.draw(tbId).toString());
		
		return fmsg;
    }
	
	@GetMapping("/setTemplate/{tbId}")
    public @ResponseBody boolean setTemplate(@PathVariable Long tbId,@RequestParam("template") String template,HttpServletRequest request) {	
		TableBrief tb = new TableBrief();
		tb.setTbId(tbId);
		tb.setTemplate(template);
		tb = tableService.updateTableBrief(tb);	
		if(tb != null) return true;
		return false;
    }
	
	@GetMapping("/getPlugIns")
    public @ResponseBody List<TableBrief> getPlugIns(@RequestParam("newFieldType") String newFieldType, HttpServletRequest request) {	
		return tableService.findTableBriefWithTemplate(newFieldType);
    }
	
	/**
	 * 表单拷贝
	 * @param newFieldType
	 * @param request
	 * @return
	 */
	@GetMapping("/copy/{tbId}")
    public @ResponseBody boolean copy(@PathVariable("tbId") Long tbId, HttpServletRequest request) {				
		return tableService.copy(tbId);
    }
	
	/**
	 * 设置流程绑定
	 * */
	@GetMapping("/setbinding/{tbId}")
    public @ResponseBody boolean setBinding(@PathVariable Long tbId, @RequestParam("wfId") Long wfId, HttpServletRequest request) {	
		TableBrief tb = new TableBrief();
		tb.setWfId(wfId);
		tb.setTbId(tbId);
        tb = tableService.updateTableBrief(tb);
        
        if(tb != null) return true;
        return false;
    }
	
	/**
	 * 取消流程绑定
	 * */
	@GetMapping("/removebinding/{tbId}")
    public @ResponseBody boolean removeBinding(@PathVariable Long tbId, HttpServletRequest request) {	
        return tableService.removeBinding(tbId);       
    }
	
	/**
	 * 删除指定的库表
	 * @param request
	 * @return
	 */
	@GetMapping("/droptable/{tbId}")
    public @ResponseBody FMsg dropTable(@PathVariable Long tbId, HttpServletRequest request) {
		FMsg fmsg = null;
		try{
			boolean res = tableService.dropTable(tbId);
			fmsg = new FMsg(res);
		}catch(Exception e){
			fmsg = new FMsg(FMsg.ERROR,e.getMessage());
		}
		
		return fmsg;
    }
	
	
	/**
	 * 设置业务模板绑定
	 * */
	@GetMapping("/settemplate/{tbId}")
    public @ResponseBody boolean settemplate(@PathVariable Long tbId, @RequestParam("templateName") String templateName, HttpServletRequest request) {	
		TableBrief tb = new TableBrief();
		tb.setTemplateName(templateName);
		tb.setTbId(tbId);
        tb = tableService.updateTableBrief(tb);
        
        if(tb != null) return true;
        return false;
    }
}
