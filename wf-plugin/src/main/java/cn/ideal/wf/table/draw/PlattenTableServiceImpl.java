package cn.ideal.wf.table.draw;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.formula.DataFormulaProcessor;
import cn.ideal.wf.jdbc.dao.SQLConnector;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.processor.WorkflowProcessor;
import cn.ideal.wf.service.WorkflowTableService;

@Service("PlattenTableService")
public class PlattenTableServiceImpl extends DTUtils implements PureTableService {
	@Autowired
	private WorkflowTableService workflowTableService;
	@Autowired
	private WorkflowProcessor workflowProcessor;
	@Override
	public StringBuffer draw(Long tbId) {
		StringBuffer sb = new StringBuffer();
		WorkflowTableBrief tb = workflowTableService.find(tbId);
		
		//表单名称
		String tableName = (tb.getTableName()==null)?"":tb.getTableName();
		sb.append("<label class='table-caption'>"+tableName+"</label>");	
		//表单内容
		List<WorkflowTableLayout> layouts = workflowTableService.findTableLayout(tbId);
		if(layouts == null || layouts.size() == 0) return null;		
		String border = "0";
		for(WorkflowTableLayout tl : layouts){
			if(tl.getScope().equals("表体")) border = "1" ;
			else border = "0" ;
			Object[][] tesary = packTableElements(tbId,tl.getScope(),tl.getCols(),tb.getWfId(),null);
			int percent = 100/tl.getCols().intValue();
			sb.append("<table border='"+border+"' class='table-width'>\n");		
			for(int i=0;i<tesary.length-1;i++){
				sb.append("<tr class='table-tr'>\n");
				for(int j=0;j<tesary[i].length;j++){
					if(tesary[i][j] == null){
						sb.append("<td>\n");					
						sb.append("</td>\n");
					}else{
						if(tesary[i][j] instanceof WorkflowTableElement){
							WorkflowTableElement obj = (WorkflowTableElement)tesary[i][j];
							String style = null;
							if(obj.getNewFieldType().equals("标签")) style="table-label";
							else style="table-unlabel";
							sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" class='"+style+"'  style='width:"+percent+"%;'>\n");
							sb.append(drawTableElement(obj,tb.getName(),null,null,tb.getWfId(),null));											
							sb.append("</td>\n");
						}					
					}
				}
				sb.append("</tr>\n");
			}		
			sb.append("</table><br>\n");	
			if(tl.getScope().equals("表体")){
				StringBuffer ssb = drawSubTable(tl.getStbId(),null,null,null);
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
		sb.append("<table border='1' class='table-width'>\n");
		for(int i=0;i<tesary.length;i++){
			sb.append("<tr class='table-tr'>\n");
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
						sb.append("<label class='table-conf-label'>["+obj.getNewLabelName()+"]</label>");
						sb.append(drawTableElement(obj,null,null,null,null,null));						
						sb.append("</td>\n");
					}					
				}
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");		
		WorkflowTableBrief stb = workflowTableService.findSubTable(tbId, scope);
		if(stb != null) sb.append("<br><span class='table-out-bef-conf-sub' title='关联子表["+stb.getTableName()+"]' onclick=\"$('#subTable-div').show();\">⟰</span>\n");
		else if(scope.equals("表体")) sb.append("<br><span class='table-out-af-conf-sub' title='增加子表' onclick=\"$('#subTable-div').show();\">⟰</span>\n");
		return sb;
	}

	//如果是新流程，则使用默认的流程对表单的定义
	@Override
	public StringBuffer draw(Long tbId, Long wfId, Long defId, Long bizId,WorkflowUser user) {
		StringBuffer sb = new StringBuffer();
		WorkflowTableBrief tb = null;
		if(defId != null) tb = workflowTableService.findDefinationTableBrief(defId);
		//默认的流程对表单定义
		if(tb == null) tb = workflowTableService.findByIds(tbId, wfId);	
		if(tb == null) tb = workflowTableService.findDefinationTableBrief(tbId, wfId);		
		
		//获得当前办理节点名称
		String nodeName = workflowProcessor.findCurrentNode(wfId, bizId,user).get("nodeName");
		
		//获得业务数据
		List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from "+tb.getName()+" where id = "+bizId);
		Map<String,Object> resMap = null;
		if(res != null && res.size() > 0) resMap = res.get(0);
		
		//表单名称
		String tableName = (tb.getTableName()==null)?"":tb.getTableName();
		sb.append("<label class='table-caption'>"+tableName+"</label>");
		
		//表单内容
		List<WorkflowTableLayout> layouts = workflowTableService.findTableLayout(tbId);
		if(layouts == null || layouts.size() == 0) return null;
		
		//主表关键字
		sb.append("<input type='hidden' id='tbId' name='tbId' value="+tbId+" />");
		if(bizId == null) sb.append("<input type='hidden' id='bizId' name='bizId' value='' />");
		else sb.append("<input type='hidden' id='bizId' name='bizId' value="+bizId+" />");		
		if(wfId == null) sb.append("<input type='hidden' id='wfId' name='wfId' value='' />");
		else sb.append("<input type='hidden' id='wfId' name='wfId' value="+wfId+" />");
		if(defId == null) sb.append("<input type='hidden' id='defId' name='defId' value='' />");
		else sb.append("<input type='hidden' id='defId' name='defId' value="+defId+" />");
		
		//寻找默认流程为了获得表单字段的信息
		WorkflowTableBrief deftb = workflowTableService.find(tbId);
		
		for(WorkflowTableLayout tl : layouts){
			Object[][] tesary = packTableElements(tbId,tl.getScope(),tl.getCols(),deftb.getWfId(),nodeName);	
			int percent = 100/tl.getCols().intValue();
			String border = "0";
			if(tl.getBorder().equals("是")) border = "1";
			sb.append("<table border="+border+" class='table-width'>\n");
			for(int i=0;i<tesary.length-1;i++){
				sb.append("<tr class='table-tr'>\n");
				for(int j=0;j<tesary[i].length;j++){
					if(tesary[i][j] == null){
						sb.append("<td>\n");					
						sb.append("</td>\n");
					}else{
						if(tesary[i][j] instanceof WorkflowTableElement){
							WorkflowTableElement obj = (WorkflowTableElement)tesary[i][j];
							String style = null;
							if(obj.getNewFieldType().equals("标签")) {
								if(!StringUtils.isEmpty(obj.getPosition())) {
									if(obj.getPosition().equals("左")) style="table-label-left";
									else if(obj.getPosition().equals("中")) style="table-label-center";
									else if(obj.getPosition().equals("右")) style="table-label";
								}
							}
							else style="table-unlabel";
							sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" class='"+style+"' style='width:"+(percent*obj.getCols())+"%;'>\n");	
							sb.append(drawTableElement(obj,tb.getName(),resMap,null,deftb.getWfId(),nodeName));												
							sb.append("</td>\n");
						}					
					}
				}
				sb.append("</tr>\n");
			}
			if(border.equals("1")) sb.append("</table></br>\n");	
			else sb.append("</table>\n");	
			if(tl.getScope().equals("表体")){
				StringBuffer ssb = drawSubTable(tl.getStbId(),bizId,deftb.getWfId(),nodeName);
				if(ssb != null) sb.append(ssb.toString());
			}
		}
		return sb;
	}
	
	
	/**
	 * 子表
	 * @param tbId
	 * @return
	 */
	private StringBuffer drawSubTable(Long tbId,Long bizId,Long wfId,String nodeName){
		if(tbId == null) return null;
		String scope = "表体";		
		WorkflowTableLayout layout = workflowTableService.findTableLayoutWithScope(tbId, scope);
		if(layout == null) return null;
		
		WorkflowTableBrief tb = workflowTableService.find(tbId);
		List<Map<String,Object>> res = null;
		if(bizId != null) res = SQLConnector.getSQLExecutor().query("select * from "+tb.getName()+" where id in (select skey from table_keys where zkey= "+bizId+" and stablename='"+tb.getName()+"') order by id");
		Map<String,Object> resMap = null;
		int rows = 0;
		if(res != null) rows = res.size() > 1 ? res.size()-1  : 0;
		
		Object[][] tesary = packTableElements(tbId,layout.getScope(),layout.getCols(),wfId,nodeName);
		Object[][] newTesary = new Object[tesary.length+rows][tesary[0].length];
		boolean moreRecords = false;
		int l=0;//新数组的下标
		for(int i=0;i<tesary.length-1;i++){	
			for(int j=0;j<tesary[i].length;j++){
				if(tesary[i][j] instanceof WorkflowTableElement){
					WorkflowTableElement obj = (WorkflowTableElement)tesary[i][j];
					if(!obj.getNewFieldType().equals("标签")) moreRecords=true;					
				}
				newTesary[l][j] = tesary[i][j];
			}
			//判断是否有多条记录，在数组中增加多余的记录
			if(moreRecords){
				++l;
				for(int k=0;k<rows;k++,++l){					
					for(int j=0;j<newTesary[i].length;j++){
						newTesary[l][j] = tesary[i][j];
					}					
				}				
				moreRecords = false;
			}else{
				l++;
			}
			
		}
		int percent = 100/layout.getCols().intValue();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span class='table-sub-tr-plus' onclick=\"addsubitem('"+tb.getName()+"')\"> + </span>\n");
		sb.append("<span class='table-sub-tr-plus' onclick=\"delsubitem('"+tb.getName()+"')\"> - </span>\n");
		sb.append("<table border='1' class='table-width' id='"+tb.getName()+"'>\n");
		int idx=0; //设置多记录在查询集合中的下标
		for(int i=0;i<newTesary.length-1;i++){
			//赋值：
			if(res != null && res.size() > 0 && idx < res.size()) resMap = res.get(idx);
			else resMap = null;
			sb.append("<tr class='table-tr'>\n");
			for(int j=0;j<newTesary[i].length;j++){
				if(newTesary[i][j] == null){
					sb.append("<td>\n");					
					sb.append("</td>\n");
				}else{
					if(newTesary[i][j] instanceof WorkflowTableElement){
						WorkflowTableElement obj = (WorkflowTableElement)newTesary[i][j];
						String style = "table-unlabel";
						if(obj.getNewFieldType().equals("标签")) style="table-label-center";	
						
						sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" class='"+style+"' style='width:"+(percent*obj.getCols())+"%;'>\n");	
						if(j==0 && style.equals("table-unlabel")){
							//定义主键作为隐藏项
							String sId = "";
							if(resMap != null) sId = resMap.get("ID").toString();
							sb.append("<input type='hidden' name='"+tb.getName()+"_ID' value='"+sId+"'>");
							sb.append("<input type='checkbox' name='"+tb.getName()+"_check'>");
							idx++;
						}
						sb.append(drawTableElement(obj,tb.getName(),resMap,res,wfId,nodeName));												
						sb.append("</td>\n");
					}					
				}
			}
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
		sb.append("<table border='0' class='table-width'>\n");		
		sb.append("<tr class='table-sub-tr'>\n");
		for(WorkflowTableElement obj : tes){			
			sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" class='table-sub-td-text-left'>\n");
			sb.append(drawTableElement(obj,tbName,resMap,null,null,null));							
			sb.append("</td>\n");					
			}
		sb.append("</tr>\n");		
		sb.append("</table>\n");
		return sb;
	}

	
	private String drawTableElement(WorkflowTableElement obj,String tbName,Map<String,Object> resMap,List<Map<String,Object>> resLst,Long wfId,String nodeName){
		StringBuffer sb = new StringBuffer();
		String pre = "";
		String width = "";
		String readOnly = "";
		String required = "";
		if(obj.isReadOnly()) readOnly = "readOnly";
		if(!StringUtils.isEmpty(obj.getWidth())) width = "width:"+obj.getWidth()+"%";
		if(obj.getRequired() != null && obj.getRequired().equals("是")) required = "required";
		if(tbName != null) pre = tbName+"_";
		Object value = resMap != null ? resMap.get(obj.getNewFieldName()):obj.getDefaultValue();
		switch(obj.getNewFieldType()) {
		case "标签":			
			String labelName = null;			
			if(!StringUtils.isEmpty(obj.getFormula())){
				labelName = DataFormulaProcessor.caculate(obj.getFormula(), resMap, resLst);
			}else labelName = obj.getNewLabelName();
			
			sb.append("<label> " + labelName+"</label>\n");			
			break;
		case "输入框":			
			if(!StringUtils.isEmpty(obj.getFormula())){
				value = DataFormulaProcessor.caculate(obj.getFormula(), resMap, resLst);
			}
			if(value == null) value = "";
			
			sb.append("<input class='input-02 "+obj.getNewFieldDataType()+"' type='text' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' value='"+value+"' "+required+" style='"+width+"' "+readOnly+" maxlength='"+Math.round(obj.getNewLength()/2)+"'>");			
			
			if(!StringUtils.isEmpty(obj.getNewUnit())) sb.append(obj.getNewUnit());
			if(!readOnly.equals("readOnly")){
				if(!StringUtils.isEmpty(obj.getNewFunctionName())){
					String funcName = obj.getNewFunctionName();
					if(obj.getNewHiddenFieldName()!=null) funcName = funcName.replace(obj.getNewHiddenFieldName(), pre+obj.getNewHiddenFieldName());
					else funcName = funcName.replace(obj.getNewFieldName(), pre+obj.getNewFieldName());
					sb.append("<span class='img-cursor' onclick='"+funcName+"'>✍</span>");
				}
			}
			break;
		case "下拉框":
			String selected = "";
			if(!StringUtils.isEmpty(readOnly))readOnly = "disabled";
			if(readOnly.equals("disabled")){
				sb.append("<input type='hidden' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' value='"+value+"'>\n");
				sb.append("<select class='input-01' "+required+" "+readOnly+">\n");
			}else{
				sb.append("<select class='input-01' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' "+required+">\n");
			}
			sb.append("<option value=''>请选择</option>");
			if(!StringUtils.isEmpty(obj.getNewDataContent())){
				for(String content : obj.getNewDataContent().split(",")){
					selected = "";
					if(value != null && value.equals(content)) selected = "selected";
					sb.append("<option "+selected+"  value='"+content+"'>"+content+"</option>\n");
				}
			}
			sb.append("</select>");
			break;
		case "多选框":
			String checkboxchecked = "";
			if(!StringUtils.isEmpty(readOnly))readOnly = "disabled";
			if(!StringUtils.isEmpty(obj.getNewDataContent())){
				for(String content : obj.getNewDataContent().split(",")){
					checkboxchecked = "";
					if(value != null && value.toString().contains(content)) checkboxchecked = "checked";
					if(readOnly.equals("disabled")){
						sb.append("<input type='checkbox' "+checkboxchecked+" value='"+content+"' "+readOnly+">");
					}else{
						sb.append("<input type='checkbox' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' "+checkboxchecked+" value='"+content+"' "+readOnly+">");
					}
					sb.append(content +"\n");
				}
				
				if(readOnly.equals("disabled")){
					sb.append("<input type='hidden' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' value='"+value+"' >\n");
				}
			}
			break;
		case "单选框":
			String radiochecked = "";
			if(!StringUtils.isEmpty(readOnly))readOnly = "disabled";
			int i=0;
			if(!StringUtils.isEmpty(obj.getNewDataContent())){
				for(String content : obj.getNewDataContent().split(",")){				
					if(value != null ){
						radiochecked = "";
						if(value.equals(content)) radiochecked = "checked";					
					}else{					
						if(i==0) radiochecked = "checked";
						else radiochecked="";
					}
					if(readOnly.equals("disabled")){
						sb.append("<input type='radio' "+radiochecked+" value='"+content+"' "+readOnly+">");
					}else{
						sb.append("<input type='radio' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' "+radiochecked+" value='"+content+"' "+required+" "+readOnly+">");
					}
					sb.append(content +"\n");
					i++;
				}
				if(readOnly.equals("disabled")){
					sb.append("<input type='hidden' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' value='"+value+"' >\n");
				}
			}
			break;
		case "文本框":			
			if(value == null) value = "";			
			sb.append("<textarea class='input-03' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' "+required+" style='"+width+"' rows=4 "+readOnly+" maxlength='"+Math.round(obj.getNewLength()/2)+"'>"+value+"</textarea>\n");			
			break;
		case "审批意见":
			if(resMap != null){
				List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from workflow_comment where fieldName ='"+obj.getNewFieldName().toUpperCase()+"' and tbId = "+obj.getTbId()+" and bizId = "+resMap.get("ID"));
				sb.append("<div style='position:relative;height:150px'>");
				for(Map<String,Object> comments : res){						
					sb.append("<span style='padding-left:5px;'>"+comments.get("remark")+"</span><p>");
					sb.append("<span style='padding-left:70%'>"+comments.get("userName") + "&nbsp; &nbsp; "+comments.get("remarkDate")+"</span><p>");					
				}
				sb.append("</div>");
			}
			if(readOnly.equals("")){
				if(value == null) value = "";
				sb.append("<textarea class='input-03' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' "+required+" style='"+width+"' rows=4 maxlength='"+Math.round(obj.getNewLength()/2)+"'>"+value+"</textarea>\n");
				sb.append("<span class='img-cursor' onclick=\"getOpinion('"+pre+obj.getNewFieldName()+"');\">✍</span>");
			}
			break;
		case "日期":	
			if(value == null) value = "";
			else{
				if(value.toString().equals("sysdate")){
					SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd"); 
					value = formatter.format(new Date());					
				}
			}
			String functionName = "";
			String cssClass = "";
			if(!StringUtils.isEmpty(obj.getNewFunctionName())){
				String[] ary = obj.getNewFunctionName().split("\'");
				int count = 1;
				for(int j=0;j<ary.length;j++){
					if(j == count) {
						ary[j] = pre+"f_"+ary[j];
						count += 2;
					}
					functionName += ary[j]+"'";
				}
				functionName = functionName.substring(0,functionName.length()-1);
				functionName = "onchange=\""+functionName+"\" ";
				cssClass = " Date";
			}
			sb.append("<input class='input-02"+cssClass+"' type='text' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' value='"+value+"' "+required+" style='"+width+"' "+readOnly+" maxlength='"+obj.getNewLength()+"' "+functionName+">");			
			if(!readOnly.equals("readOnly")){
				sb.append("<script>laydate.render({elem: '#"+pre+obj.getNewFieldName()+"',theme: '#3d80d3'});</script>");
			}
			
			break;
		case "图片":				
			if(value == null) value = "";	
			Object imgSrc = "";
			if(resMap != null && resMap.get(obj.getNewHiddenFieldName()) != null) imgSrc = resMap.get(obj.getNewHiddenFieldName());
			if(required.equals("required")) required = "imagecheck=true";
			if(!readOnly.equals("readOnly")){
				//参数名称替换				
				sb.append("<span class='img-cursor' onclick=\"getStamp('"+pre+obj.getNewFieldName()+"','"+pre+obj.getNewHiddenFieldName()+"')\">✍</span>");
			}
			sb.append("<div style='position:relative;'>");
			sb.append("&nbsp;<img id='"+pre+obj.getNewFieldName()+"_src' src='"+imgSrc+"' "+required+">");
			String display = "display:none";
			if(!StringUtils.isEmpty(value)){
				display = "display:";
			}			
			sb.append("<div class='imagetext' style='"+display+"' id='"+pre+obj.getNewFieldName()+"_div'><input class='inputnoborder' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' value='"+value+"'></div>");						
			sb.append("<label id='"+pre+obj.getNewFieldName()+"_src-error' class='imageerror' for='"+pre+obj.getNewFieldName()+"' style='display:none;'>此项为必输项！</label>");
			sb.append("</div>");
			break;
		case "文件":
			if(required.equals("required")) required = "imagecheck=true";
			if(!readOnly.equals("readOnly")){
				sb.append("<span class='img-cursor' onclick=\"getFj('"+pre+obj.getNewHiddenFieldName()+"','"+pre+obj.getNewFieldName()+"')\" >✍</span>");
			}
			Object value2 = resMap != null ? resMap.get(obj.getNewHiddenFieldName()):null;
			if(value == null) value = "";
			if(value2 == null) value2 = "";
			sb.append("<ol class='tags' id='"+pre+obj.getNewFieldName()+"Desc' "+required+">");
			String[] names = value.toString().split(",");
			String[] ids = value2.toString().split(",");
			for(int p=0;p<ids.length;p++){
				if(!StringUtils.isEmpty(ids[p])){
					if(names[p].indexOf("]") <= 0) continue;
					String imgName = names[p].substring(1,names[p].indexOf("]"));
					String imgFileName = names[p].substring(names[p].indexOf("]")+1);
					if(!readOnly.equals("readOnly")){
						sb.append("<li class='img-garbage' id='" + ids[p] + "'>" +imgName);					
						sb.append("<span onclick='removeFj(this,'"+pre+obj.getNewHiddenFieldName()+"','"+pre+obj.getNewFieldName()+"')' class='img-cursor'>✘</span>");
						sb.append("</li>");	
					}else{
						sb.append(imgName+"<a href='/file/"+imgFileName+"/download' class='img-cursor'>⬇️</a>&nbsp;&nbsp;");
					}					
				}
			}
			sb.append("</ol>");			
			sb.append("<input type='hidden' id='"+pre+obj.getNewFieldName()+"' name='"+pre+obj.getNewFieldName()+"' value='"+value+"'/>");
			sb.append("<label id='"+pre+obj.getNewFieldName()+"Desc-error' class='imageerror' for='"+pre+obj.getNewFieldName()+"Desc' style='display:none;'>此项为必输项！</label>");
			break;
		case "子表":	
			Long bizId = null;
			if(resMap != null && resMap.get("ID") != null) {
				if(resMap.get("ID") instanceof Integer) bizId = Long.parseLong(((Integer)resMap.get("ID")).toString());
				else if(resMap.get("ID") instanceof BigDecimal) bizId = Long.parseLong(((BigDecimal)resMap.get("ID")).toString());
			}
			sb.append(drawSubTable(obj.getStbId(),bizId,wfId,nodeName).toString());								
			break;
		case "组件":	
			sb.append(drawPlugIn(obj.getStbId(),tbName,resMap).toString());								
			break;									
		default:
			break;
		}
		if(!StringUtils.isEmpty(obj.getNewHiddenFieldName())){
			value="";
			if(resMap != null && resMap.get(obj.getNewHiddenFieldName()) != null) value = resMap.get(obj.getNewHiddenFieldName());
			sb.append("<input type='hidden' id='"+pre+obj.getNewHiddenFieldName()+"' name='"+pre+obj.getNewHiddenFieldName()+"' value='"+value+"'>");
		}
		return sb.toString();
	}
}
