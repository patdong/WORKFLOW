package cn.ideal.wf.table.draw;

import java.math.BigDecimal;
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
import cn.ideal.wf.service.WorkflowTableService;

@Service("PrintTableService")
public class PrintTableServiceImpl extends DTUtils implements PureTableService {
	@Autowired
	private WorkflowTableService workflowTableService;
	
	@Override
	public StringBuffer draw(Long tbId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer draw(Long tbId, String scope, boolean setting) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer draw(Long tbId, Long wfId, Long defId, Long bizId, WorkflowUser user) {
		StringBuffer sb = new StringBuffer();
		WorkflowTableBrief tb = workflowTableService.find(tbId);
		
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
		
		for(WorkflowTableLayout tl : layouts){
			Object[][] tesary = packTableElements(tbId,tl.getScope(),tl.getCols(),null,null);	
			int percent = 100/tl.getCols().intValue();
			String border = "0";
			if(tl.getBorder().equals("是")) border = "1";
			sb.append("<table border="+border+" class='table-print-width'>\n");
			for(int i=0;i<tesary.length-1;i++){
				sb.append("<tr class='table-tr' >\n");
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
							sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" class='"+style+" table-td-print' style='width:"+(percent*obj.getCols())+"%;'>\n");	
							sb.append(drawTableElement(obj,tb.getName(),resMap,null,null,null));												
							sb.append("</td>\n");
						}					
					}
				}
				sb.append("</tr>\n");
			}
			if(border.equals("1")) sb.append("</table></br>\n");	
			else sb.append("</table>\n");	
			if(tl.getScope().equals("表体")){
				StringBuffer ssb = drawSubTable(tl.getStbId(),bizId,null,null);
				if(ssb != null) sb.append(ssb.toString());
			}
		}
		return sb;
	}
	
	private String drawTableElement(WorkflowTableElement obj,String tbName,Map<String,Object> resMap,List<Map<String,Object>> resLst,Long wfId,String nodeName){
		StringBuffer sb = new StringBuffer();
		
		Object value = resMap != null ? resMap.get(obj.getNewFieldName()):obj.getDefaultValue();
		if(value == null) value = "";
		switch(obj.getNewFieldType()) {
		case "标签":			
			String labelName = null;			
			if(!StringUtils.isEmpty(obj.getFormula())){
				labelName = DataFormulaProcessor.caculate(obj.getFormula(), resMap, resLst);
			}else labelName = obj.getNewLabelName();			
			sb.append(labelName+"\n");			
			break;
		case "输入框":			
			if(!StringUtils.isEmpty(obj.getFormula())){
				value = DataFormulaProcessor.caculate(obj.getFormula(), resMap, resLst);
			}
			if(value == null) value = "";
			
			sb.append(value+"\n");		
			break;
		case "下拉框":
			sb.append(value+"\n");
			break;
		case "多选框":
			sb.append(value+"\n");
			break;
		case "单选框":
			sb.append(value+"\n");
			break;
		case "文本框":						
			sb.append(value+"\n");			
			break;
		case "审批意见":
			if(resMap != null){
				List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from workflow_comment where fieldName ='"+obj.getNewFieldName().toUpperCase()+"' and tbId = "+obj.getTbId()+" and bizId = "+resMap.get("ID"));
				sb.append("<div style='position:relative;'>");
				for(Map<String,Object> comments : res){						
					sb.append("<span style='padding-left:5px;'>"+comments.get("remark")+"</span><p>");
					sb.append("<span style='padding-left:50%'>"+comments.get("userName") + "&nbsp; &nbsp; "+comments.get("remarkDate")+"</span><p>");					
				}
				sb.append("</div>");
			}
			
			break;
		case "日期":	
			sb.append(value+"\n");	
			break;
		case "图片":	
			Object imgSrc = null;
			if(resMap != null && resMap.get(obj.getNewHiddenFieldName()) != null) imgSrc = resMap.get(obj.getNewHiddenFieldName());
			sb.append("<div style='position:relative;'>");
			sb.append("<img src='"+imgSrc+"'>");
			sb.append("<div class='imagetext' >"+value+"</div>");
			sb.append("</div>");
			
			break;
		case "文件":			
			Object value2 = resMap != null ? resMap.get(obj.getNewHiddenFieldName()):null;			
			if(value2 == null) value2 = "";
			sb.append("<ol class='tags'>");
			String[] names = value.toString().split(",");
			String[] ids = value2.toString().split(",");
			for(int p=0;p<ids.length;p++){
				if(!StringUtils.isEmpty(ids[p])){
					if(names[p].indexOf("]") <= 0) continue;
					String imgName = names[p].substring(1,names[p].indexOf("]"));									
					sb.append(imgName);								
				}
			}
			sb.append("</ol>");				
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
		
		return sb.toString();
	}

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
	
}
