package cn.ideal.wf.table.draw;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.jdbc.dao.SQLConnector;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.processor.WorkflowProcessor;
import cn.ideal.wf.service.WorkflowTableService;

@Service
public class PlattenTableServiceImpl implements PureTableService {
	@Autowired
	private WorkflowTableService workflowTableService;
	@Autowired
	private WorkflowProcessor workflowProcessor;
	@Override
	public StringBuffer draw(Long tbId) {
		StringBuffer sb = new StringBuffer();
		WorkflowTableBrief tb = workflowTableService.find(tbId);
		//获得当前办理节点名称
		String nodeName = workflowProcessor.findNodeName(tb.getWfId(), null);
		//表单名称
		String tableName = (tb.getTableName()==null)?"":tb.getTableName();
		sb.append("<label style='text-align:center;width:100%;font-size:30px;'>"+tableName+"</label>");	
		//表单内容
		List<WorkflowTableLayout> layouts = workflowTableService.findTableLayout(tbId);
		if(layouts == null || layouts.size() == 0) return null;
		//主表关键字
		sb.append("<input type='hidden' name='bizId' value='' />");
		if(tb.getWfId() != null) sb.append("<input type='hidden' name='wfId' value="+tb.getWfId()+" />");
		String border = "0";
		for(WorkflowTableLayout tl : layouts){
			if(tl.getScope().equals("表体")) border = "1" ;
			else border = "0" ;
			Object[][] tesary = packTableElements(tbId,tl.getScope(),tl.getCols(),tb.getWfId(),nodeName);
			int percent = 100/tl.getCols().intValue();
			sb.append("<table border='"+border+"' style='width:100%'>\n");		
			for(int i=0;i<tesary.length-1;i++){
				sb.append("<tr style='height:50px'>\n");
				for(int j=0;j<tesary[i].length;j++){
					if(tesary[i][j] == null){
						sb.append("<td>\n");					
						sb.append("</td>\n");
					}else{
						if(tesary[i][j] instanceof WorkflowTableElement){
							WorkflowTableElement obj = (WorkflowTableElement)tesary[i][j];
							String style = null;
							if(obj.getNewFieldType().equals("标签")) style="text-align: right;font-size:15px";
							else style="text-align: left;padding-left:5px;font-size:15px";
							sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" style='"+style+";width:"+percent+"%;'>\n");
							sb.append(drawTableElement(obj,tb.getName(),null,tb.getWfId(),nodeName));											
							sb.append("</td>\n");
						}					
					}
				}
				sb.append("</tr>\n");
			}		
			sb.append("</table><br>\n");	
			if(tl.getScope().equals("表体")){
				StringBuffer ssb = drawSubTable(tl.getStbId(),5,null,null,null);
				if(ssb != null) sb.append(ssb.toString());
			}
		}
		return sb;
	}

	@Override
	public StringBuffer draw(Long tbId, String scope, boolean setting) {		
		WorkflowTableLayout layout = workflowTableService.findTableLayoutWithScope(tbId, scope);
		if(layout == null) return new StringBuffer();
		Object[][] tesary = packTableElements(tbId,scope,layout.getCols(),null,null);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' style='width:100%'>\n");
		for(int i=0;i<tesary.length;i++){
			sb.append("<tr style='height:50px'>\n");
			for(int j=0;j<tesary[i].length;j++){
				if(tesary[i][j] == null){
					sb.append("<td>\n");
					if(setting) sb.append("<span id='btn-up' class='btn-edit-pointer' onclick=\"showPos(event,'','head');\" title='编辑'>⤧</span>\n");
					sb.append("</td>\n");
				}else{
					if(tesary[i][j] instanceof WorkflowTableElement){
						WorkflowTableElement obj = (WorkflowTableElement)tesary[i][j];
						sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+">\n");
						if(setting) sb.append("<span id='btn-up' class='btn-edit-pointer' onclick=\"showPos(event,'"+obj.getId()+"');\" title='编辑'>⤧</span>\n");
						sb.append("<label style='color:gray;font-size:0.8rem;'>["+obj.getNewLabelName()+"]</label>");
						sb.append(drawTableElement(obj,null,null,null,null));						
						sb.append("</td>\n");
					}					
				}
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");		
		WorkflowTableBrief stb = workflowTableService.findSubTable(tbId, scope);
		if(stb != null) sb.append("<br><span style='margin-right:98%;font-size:1.5rem;color:green;border:1px solid;border-color:green;cursor:pointer;background-color: #8aea8a;' title='关联子表["+stb.getTableName()+"]' onclick=\"$('#subTable-div').show();\">⟰</span>\n");
		else if(scope.equals("表体")) sb.append("<br><span style='margin-right:98%;font-size:1.5rem;color:#adb90e;border:1px solid;border-color:#b8bb5f;cursor:pointer;' title='增加子表' onclick=\"$('#subTable-div').show();\">⟰</span>\n");
		return sb;
	}

	
	@Override
	public StringBuffer draw(Long tbId, Long bizId) {
		if(bizId == null) return draw(tbId);
		StringBuffer sb = new StringBuffer();
		WorkflowTableBrief tb = workflowTableService.find(tbId);	
		//获得当前办理节点名称
		String nodeName = workflowProcessor.findNodeName(tb.getWfId(), bizId);
		//获得业务数据
		List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from "+tb.getName()+" where id = "+bizId);
		Map<String,Object> resMap = res.get(0);
		
		//表单名称
		String tableName = (tb.getTableName()==null)?"":tb.getTableName();
		sb.append("<label style='text-align:center;width:100%;font-size:30px;'>"+tableName+"</label>");	
		//表单内容
		List<WorkflowTableLayout> layouts = workflowTableService.findTableLayout(tbId);
		if(layouts == null || layouts.size() == 0) return null;
		//主表关键字		
		sb.append("<input type='hidden' name='bizId' value="+bizId+" />");
		//流程编号
		if(tb.getWfId() != null) sb.append("<input type='hidden' name='wfId' value="+tb.getWfId()+" />");
		for(WorkflowTableLayout tl : layouts){
			Object[][] tesary = packTableElements(tbId,tl.getScope(),tl.getCols(),tb.getWfId(),nodeName);	
			int percent = 100/tl.getCols().intValue();
			sb.append("<table border='1' style='width:100%'>\n");		
			for(int i=0;i<tesary.length-1;i++){
				sb.append("<tr style='height:40px'>\n");
				for(int j=0;j<tesary[i].length;j++){
					if(tesary[i][j] == null){
						sb.append("<td>\n");					
						sb.append("</td>\n");
					}else{
						if(tesary[i][j] instanceof WorkflowTableElement){
							WorkflowTableElement obj = (WorkflowTableElement)tesary[i][j];
							String style = null;
							if(obj.getNewFieldType().equals("标签")) style="text-align: right;font-size:15px";
							else style="text-align: left;padding-left:5px;font-size:15px";
							sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" style='"+style+";width:"+percent+";'>\n");	
							sb.append(drawTableElement(obj,tb.getName(),resMap,tb.getWfId(),nodeName));												
							sb.append("</td>\n");
						}					
					}
				}
				sb.append("</tr>\n");
			}		
			sb.append("</table><br>\n");	
			if(tl.getScope().equals("表体")){
				StringBuffer ssb = drawSubTable(tl.getStbId(),5,bizId,tb.getWfId(),nodeName);
				if(ssb != null) sb.append(ssb.toString());
			}
		}
		return sb;
	}
	
	/**
	 * 表单元素位置设置
	 * @param tbId
	 * @param scope
	 * @param layoutCols
	 * @return
	 */
	private Object[][] packTableElements(Long tbId, String scope, Long layoutCols,Long wfId,String nodeName){		
		List<WorkflowTableElement> tes = workflowTableService.findTableAllElements(tbId,scope,wfId,nodeName);
		Object[][] tesary = null; 
		int crowes = 0;
		for(WorkflowTableElement te : tes){
			crowes +=te.getRowes();
		}
		if(tes.size() == 0) tesary = new Object[1][layoutCols.intValue()];
		else {
			Object[][] temp = new Object[crowes][layoutCols.intValue()];
			int cols = 0,rows=0;
			for(WorkflowTableElement te : tes){				
				if(cols<layoutCols) {
					String pos = findPosition(temp,rows);
					rows = Integer.parseInt(pos.split(",")[0]);	
					cols = Integer.parseInt(pos.split(",")[1]);	
					temp[rows][cols] = te;
					for(int p=0;p<te.getRowes().intValue();p++){
						//列数修复处理
						int len = 0;
						len = ((te.getCols().intValue()+cols) <= layoutCols.intValue())?te.getCols().intValue():(layoutCols.intValue()-cols);
						for(int k=0;k<len;k++){
							if(temp[rows+p][cols+k] == null) temp[rows+p][cols+k] = new String("v");
						}
					}
					cols += te.getCols().intValue();
				}
				if(cols>=layoutCols){
					rows++;
					cols=0;
				}				
			}
			
			for(int i=rows;i<temp.length;i++){
				for(int j=0;j<temp[i].length;j++){
					if(temp[i][j] != null){
						rows++;
						break;
					}
				}
			}
			tesary = new Object[rows+1][layoutCols.intValue()];
			for(int i=0;i<tesary.length-1;i++){
				for(int j=0;j<tesary[i].length;j++){
					tesary[i][j] = temp[i][j];
				}
			}
		}
		
		return tesary;
	}
	
	/**
	 * 定位表单元素在多维表中的位置
	 * @param ary
	 * @param rows
	 * @return
	 */
	private String findPosition(Object[][] ary, int rows ){
		for(int i=0;i<ary[0].length;i++){
			if(ary[rows][i] ==null) {				
				return rows+","+i;				
			}
		}
		return findPosition(ary,++rows);
	}
	/**
	 * 子表
	 * @param stbId
	 * @return
	 */
	private StringBuffer drawSubTable(Long stbId, int rows,Long bizId,Long wfId,String nodeName){
		if(stbId == null) return null;
		String scope = "表体";
		WorkflowTableBrief tb = workflowTableService.find(stbId);
		WorkflowTableLayout layout = workflowTableService.findTableLayoutWithScope(stbId, scope);
		if(layout == null) return null;
		List<WorkflowTableElement> tes = workflowTableService.findTableAllElements(stbId,scope,wfId,nodeName);
		List<WorkflowTableElement> labels = new LinkedList<WorkflowTableElement>();
		List<WorkflowTableElement> ems = new LinkedList<WorkflowTableElement>();
		
		//分类
		for(WorkflowTableElement em : tes){
			if(em.getNewFieldType().equals("标签")) labels.add(em);
			if(!em.getNewFieldType().equals("标签")) ems.add(em);			
		}
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-weight:bold;font-size:18px;cursor:pointer;' onclick=\"addsubitem('"+tb.getName()+"')\"> + </span>\n");
		sb.append("<span style='font-weight:bold;font-size:18px;cursor:pointer;' onclick=\"delsubitem('"+tb.getName()+"')\"> - </span>\n");
		sb.append("<table border='1' style='width:100%' id='"+tb.getName()+"'>\n");
		sb.append("<tr style='height:35px;background-color:#e6e8e1;'>\n");
		sb.append("<td style='text-align: center;font-size:15px;width:5%'>选择</td>\n");
		for(WorkflowTableElement obj : labels){
			sb.append("<td style='text-align: center;font-size:15px;width:"+100/labels.size()+"%'>"+obj.getNewLabelName()+"</td>\n");
		}
		sb.append("</tr>\n");

		List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from "+tb.getName()+" where id in (select skey from table_keys where zkey= "+bizId+") order by id");
		Map<String,Object> resMap = null;
		for(int i=0;i<rows;i++){
			sb.append("<tr style='height:35px'>\n");
			//赋值：
			if(res.size() > i ) resMap = res.get(i);
			else resMap = null;
			//定义主键作为隐藏项
			String sId = "";
			if(resMap != null) sId = resMap.get("ID").toString();
			sb.append("<td style='text-align: left;padding-left:5px;font-size:15px;'><input type='checkbox' name='"+tb.getName()+"_check'></td>\n");
			for(WorkflowTableElement obj : ems){				
				sb.append("<td style='text-align:left;padding-left:5px;font-size:15px'>\n");
				sb.append(drawTableElement(obj,tb.getName(),resMap,wfId,nodeName));								
				sb.append("</td>\n");					
			}	
			sb.append("<input type='hidden' name='"+tb.getName()+"_ID' value='"+sId+"'>");
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return sb;
	}
	
	/**
	 * 组件
	 * @param stbId
	 * @param tbName 主表的库表名称
	 * @return
	 */
	private StringBuffer drawPlugIn(Long stbId,String tbName,Map<String,Object> resMap){
		if(stbId == null) return null;
		String scope = "表体";
		WorkflowTableLayout layout = workflowTableService.findTableLayoutWithScope(stbId, scope);
		if(layout == null) return null;
		List<WorkflowTableElement> tes = workflowTableService.findTableAllElements(stbId,scope);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='0' style='width:100%'>\n");		
		sb.append("<tr style='height:35px'>\n");
		for(WorkflowTableElement obj : tes){			
			sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" style='text-align:left;padding-left:5px;font-size:15px'>\n");
			sb.append(drawTableElement(obj,tbName,resMap,null,null));							
			sb.append("</td>\n");					
			}
		sb.append("</tr>\n");		
		sb.append("</table>\n");
		return sb;
	}

	
	private String drawTableElement(WorkflowTableElement obj,String tbName,Map<String,Object> resMap,Long wfId,String nodeName){
		StringBuffer sb = new StringBuffer();
		String pre = "";
		String width = "";
		String readOnly = "";
		String required = "";
		if(obj.isReadOnly()) readOnly = "readOnly";
		if(obj.getWidth() != null) width = "width:"+obj.getWidth()+"%";
		if(obj.getRequired() != null && obj.getRequired().equals("是")) required = "required";
		if(tbName != null) pre = tbName+"_";
		Object value = resMap != null ? resMap.get(obj.getFieldName()):null;		
		switch(obj.getNewFieldType()) {
		case "标签":
			if(!obj.getNewLabelName().equals("&nbsp;"))sb.append("<label style='padding-right:5px;'> " + obj.getNewLabelName()+"：</label>\n");
			else sb.append(obj.getNewLabelName()+"\n");
			break;
		case "输入框":
			if(value != null)sb.append("<input class='input-02' type='text' name='"+pre+obj.getFieldName()+"' value='"+value+"' style='"+width+"' "+readOnly+">\n");
			else sb.append("<input class='input-02' type='text' name='"+pre+obj.getFieldName()+"' value='' "+required+" style='"+width+"' "+readOnly+">\n");
			break;
		case "下拉框":
			String selected = "";
			if(!StringUtils.isEmpty(readOnly))readOnly = "disabled";
			sb.append("<select class='input-01' name='"+pre+obj.getFieldName()+"' "+required+">\n");
			sb.append("<option value=''>请选择</option>");
			for(String content : obj.getNewDataContent().split(",")){
				selected = "";
				if(value != null && value.equals(content)) selected = "selected";
				sb.append("<option "+selected+" "+readOnly+" value='"+content+"'>"+content+"</option>\n");
			}
			sb.append("</select>");
			break;
		case "多选框":
			String checkboxchecked = "";
			if(!StringUtils.isEmpty(readOnly))readOnly = "disabled";
			for(String content : obj.getNewDataContent().split(",")){
				checkboxchecked = "";
				if(value != null && value.toString().contains(content)) checkboxchecked = "checked";
				sb.append("<input type='checkbox' name='"+pre+obj.getFieldName()+"' "+checkboxchecked+" value='"+content+"' "+readOnly+">");
				sb.append(content +"\n");
			}
			break;
		case "单选框":
			String radiochecked = "";
			if(!StringUtils.isEmpty(readOnly))readOnly = "disabled";
			int i=0;
			for(String content : obj.getNewDataContent().split(",")){				
				if(value != null ){
					radiochecked = "";
					if(value.equals(content)) radiochecked = "checked";					
				}else{					
					if(i==0) radiochecked = "checked";
					else radiochecked="";
				}
				
				sb.append("<input type='radio' name='"+pre+obj.getFieldName()+"' "+radiochecked+" value='"+content+"' "+readOnly+">");
				sb.append(content +"\n");
				i++;
			}
			break;
		case "文本框":
			if(value != null) sb.append("<textarea class='input-03' name='"+pre+obj.getFieldName()+"' style='"+width+"' rows=8 "+readOnly+">"+value+"</textarea>\n");
			else sb.append("<textarea class='input-03' name='"+pre+obj.getFieldName()+"' "+required+" style='"+width+"' rows=8 "+readOnly+"></textarea>\n");
			break;
		case "审批意见":
			if(resMap != null){
				List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from workflow_comment where fieldName ='"+obj.getFieldName().toUpperCase()+"' and tbId = "+obj.getTbId()+" and bizId = "+resMap.get("ID"));
				sb.append("<div style='position:relative;height:150px'>");
				for(Map<String,Object> comments : res){						
					sb.append("<span style='padding-left:5px;'>"+comments.get("remark")+"</span><p>");
					sb.append("<span style='padding-left:70%'>"+comments.get("userName") + "&nbsp; &nbsp; "+comments.get("remarkDate")+"</span><p>");					
				}
				sb.append("</div>");
			}
			if(readOnly.equals("")){
				if(value != null) sb.append("<textarea class='input-03' name='"+pre+obj.getFieldName()+"' style='"+width+"' rows=8 >"+value+"</textarea>\n");
				else sb.append("<textarea class='input-03' name='"+pre+obj.getFieldName()+"' "+required+" style='"+width+"' rows=8 ></textarea>\n");
			}
			break;
		case "子表":	
			Long bizId = null;
			if(resMap != null && resMap.get("ID") != null) {
				if(resMap.get("ID") instanceof Integer) bizId = Long.parseLong(((Integer)resMap.get("ID")).toString());
				else if(resMap.get("ID") instanceof BigDecimal) bizId = Long.parseLong(((BigDecimal)resMap.get("ID")).toString());
			}
			sb.append(drawSubTable(obj.getStbId(),3,bizId,wfId,nodeName).toString());								
			break;
		case "组件":	
			sb.append(drawPlugIn(obj.getStbId(),tbName,resMap).toString());								
			break;									
		default:
			break;
		}
		if(obj.getNewHiddenFieldName() != null){
			value="";
			if(resMap != null && resMap.get(obj.getNewHiddenFieldName()) != null) value = resMap.get(obj.getNewHiddenFieldName());
			sb.append("<input type=hidden name='"+pre+obj.getNewHiddenFieldName()+"' value='"+value+"'>");
		}
		return sb.toString();
	}
}
