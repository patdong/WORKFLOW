package cn.ideal.wf.table.draw;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.dao.WorkflowTableMapper;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;

@Service
public class PlattenTableServiceImpl implements PureTableService {
	@Autowired
	private WorkflowTableMapper workflowTableMapper;
	@Override
	public StringBuffer draw(Long tbId) {
		StringBuffer sb = new StringBuffer();
		WorkflowTableBrief tb = workflowTableMapper.find(tbId);	
		//表单名称
		String tableName = (tb.getTableName()==null)?"":tb.getTableName();
		sb.append("<label style='text-align:center;width:100%;font-size:30px;'>"+tableName+"</label>");	
		//表单内容
		List<WorkflowTableLayout> layouts = workflowTableMapper.findTableLayout(tbId);
		if(layouts == null || layouts.size() == 0) return null;
		for(WorkflowTableLayout tl : layouts){
			Object[][] tesary = packTableElements(tbId,tl.getScope(),tl.getCols());			
			sb.append("<table border='1' style='width:100%'>\n");		
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
							sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" style='"+style+"'>\n");							
							switch(obj.getNewFieldType()) {
							case "标签":
								if(!obj.getNewLabelName().equals("&nbsp;"))sb.append(obj.getNewLabelName()+"：\n");
								else sb.append(obj.getNewLabelName()+"\n");
								break;
							case "输入框":									
								sb.append("<input type='text' name='"+tb.getName()+"_"+obj.getFieldName()+"' >\n");
								break;
							case "下拉框":
								sb.append("<select name='"+tb.getName()+"_"+obj.getFieldName()+"' >\n");
								for(String content : obj.getNewDataContent().split(",")){
									sb.append("<option>"+content+"</option>\n");
								}						
								sb.append("</select>");
								break;
							case "多选框":
								for(String content : obj.getNewDataContent().split(",")){
									sb.append("<input type='checkbox' name='"+tb.getName()+"_"+obj.getFieldName()+"' >");
									sb.append(content +"\n");
								}
								break;
							case "单选框":
								for(String content : obj.getNewDataContent().split(",")){
									sb.append("<input type='radio' name='"+tb.getName()+"_"+obj.getFieldName()+"' >");
									sb.append(content +"\n");
								}
								break;
							case "文本框":
								sb.append("<textarea name='"+tb.getName()+"_"+obj.getFieldName()+"'/>\n");
								break;
							case "子表":									
								sb.append(drawSubTable(obj.getStbId(),3).toString());								
								break;
							case "组件":	
								sb.append(drawPlugIn(obj.getStbId()).toString());								
								break;									
							default:
								break;
							}						
							sb.append("</td>\n");
						}					
					}
				}
				sb.append("</tr>\n");
			}		
			sb.append("</table><br>\n");	
			if(tl.getScope().equals("body")){
				StringBuffer ssb = drawSubTable(tl.getStbId(),5);
				if(ssb != null) sb.append(ssb.toString());
			}
		}
		return sb;
	}

	@Override
	public StringBuffer draw(Long tbId, String scope, boolean setting) {		
		WorkflowTableLayout layout = workflowTableMapper.findTableLayoutWithScope(tbId, scope);
		if(layout == null) return null;
		Object[][] tesary = packTableElements(tbId,scope,layout.getCols());
		
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
						sb.append("<label style='color:gray;font-size:0.8rem;'>["+obj.getNewLabelName()+"]</label>");
						switch(obj.getNewFieldType()) {
						case "标签":
							sb.append(obj.getNewLabelName()+"\n");
							break;
						case "输入框":							
							sb.append("<input type='text' name='"+obj.getFieldName()+"' >\n");
							break;
						case "下拉框":
							sb.append("<select name='"+obj.getFieldName()+"' >\n");
							for(String content : obj.getNewDataContent().split(",")){
								sb.append("<option>"+content+"</option>\n");
							}						
							sb.append("</select>");
							break;
						case "多选框":
							for(String content : obj.getNewDataContent().split(",")){
								sb.append("<input type='checkbox' name='"+obj.getFieldName()+"' >");
								sb.append(content +"\n");
							}
							break;
						case "单选框":
							for(String content : obj.getNewDataContent().split(",")){
								sb.append("<input type='radio' name='"+obj.getFieldName()+"' >");
								sb.append(content +"\n");
							}
							break;
						case "文本框":
							sb.append("<textarea name='"+obj.getFieldName()+"'/>\n");
							break;						
						default:
							break;
						}
						if(setting) sb.append("<span id='btn-up' class='btn-edit-pointer' onclick=\"showPos(event,'"+obj.getId()+"','head');\" title='编辑'>⤧</span>\n");
						sb.append("</td>\n");
					}					
				}
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");		
		WorkflowTableBrief stb = workflowTableMapper.findSubTable(tbId, scope);
		if(stb != null) sb.append("<br><span style='margin-right:98%;font-size:1.5rem;color:green;border:1px solid;border-color:green;cursor:pointer;background-color: #8aea8a;' title='关联子表["+stb.getTableName()+"]' onclick=\"$('#subTable-div').show();\">⟰</span>\n");
		else if(scope.equals("body")) sb.append("<br><span style='margin-right:98%;font-size:1.5rem;color:#adb90e;border:1px solid;border-color:#b8bb5f;cursor:pointer;' title='增加子表' onclick=\"$('#subTable-div').show();\">⟰</span>\n");
		return sb;
	}


	/**
	 * 表单元素位置设置
	 * @param tbId
	 * @param scope
	 * @param layoutCols
	 * @return
	 */
	private Object[][] packTableElements(Long tbId, String scope, Long layoutCols){
		List<WorkflowTableElement> tes = workflowTableMapper.findTableAllElements(tbId,scope);
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
	private StringBuffer drawSubTable(Long stbId, int rows){
		if(stbId == null) return null;
		String scope = "body";
		WorkflowTableBrief tb = workflowTableMapper.find(stbId);
		WorkflowTableLayout layout = workflowTableMapper.findTableLayoutWithScope(stbId, scope);
		if(layout == null) return null;
		List<WorkflowTableElement> tes = workflowTableMapper.findTableAllElements(stbId,scope);
		List<WorkflowTableElement> labels = new LinkedList<WorkflowTableElement>();
		List<WorkflowTableElement> ems = new LinkedList<WorkflowTableElement>();
		
		//分类
		for(WorkflowTableElement em : tes){
			if(em.getNewFieldType().equals("标签")) labels.add(em);
			if(!em.getNewFieldType().equals("标签")) ems.add(em);			
		}
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' style='width:100%'>\n");
		sb.append("<tr style='height:35px;background-color:#e6e8e1;'>\n");
		for(WorkflowTableElement obj : labels){
			sb.append("<td style='text-align: center;font-size:15px;width:"+100/labels.size()+"%'>"+obj.getNewLabelName()+"</td>\n");
		}
		sb.append("</tr>\n");
		for(int i=0;i<rows;i++){
		sb.append("<tr style='height:35px'>\n");
		for(WorkflowTableElement obj : ems){			
			sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" style='text-align:left;padding-left:5px;font-size:15px'>\n");
			switch(obj.getNewFieldType()) {			
			case "输入框":
				sb.append("<input type='text' name='"+tb.getName()+"_"+obj.getFieldName()+"'>\n");
				break;
			case "下拉框":
				sb.append("<select name='"+tb.getName()+"_"+obj.getFieldName()+"' >\n");
				for(String content : obj.getNewDataContent().split(",")){
					sb.append("<option>"+content+"</option>\n");
				}						
				sb.append("</select>");
				break;
			case "多选框":
				for(String content : obj.getNewDataContent().split(",")){
					sb.append("<input type='checkbox' name='"+tb.getName()+"_"+obj.getFieldName()+"' >");
					sb.append(content +"\n");
				}
				break;
			case "单选框":
				for(String content : obj.getNewDataContent().split(",")){
					sb.append("<input type='radio' name='"+tb.getName()+"_"+obj.getFieldName()+"' >");
					sb.append(content +"\n");
				}
				break;
			case "文本框":
				sb.append("<textarea name='"+tb.getName()+"_"+obj.getFieldName()+"'/>\n");
				break;						
			default:
				break;
			}						
			sb.append("</td>\n");
					
			}
		sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return sb;
	}
	
	/**
	 * 组件
	 * @param stbId
	 * @return
	 */
	private StringBuffer drawPlugIn(Long stbId){
		if(stbId == null) return null;
		String scope = "body";
		WorkflowTableBrief tb = workflowTableMapper.find(stbId);
		WorkflowTableLayout layout = workflowTableMapper.findTableLayoutWithScope(stbId, scope);
		if(layout == null) return null;
		List<WorkflowTableElement> tes = workflowTableMapper.findTableAllElements(stbId,scope);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='0' style='width:100%'>\n");		
		sb.append("<tr style='height:35px'>\n");
		for(WorkflowTableElement obj : tes){			
			sb.append("<td colspan="+obj.getCols()+" rowspan="+obj.getRowes()+" style='text-align:left;padding-left:5px;font-size:15px'>\n");
			switch(obj.getNewFieldType()) {			
			case "输入框":
				sb.append("<input type='text' name='"+tb.getName()+"_"+obj.getFieldName()+"'>\n");
				break;
			case "下拉框":
				sb.append("<select name='"+tb.getName()+"_"+obj.getFieldName()+"' >\n");
				for(String content : obj.getNewDataContent().split(",")){
					sb.append("<option>"+content+"</option>\n");
				}						
				sb.append("</select>");
				break;
			case "多选框":
				for(String content : obj.getNewDataContent().split(",")){
					sb.append("<input type='checkbox' name='"+tb.getName()+"_"+obj.getFieldName()+"' >");
					sb.append(content +"\n");
				}
				break;
			case "单选框":
				for(String content : obj.getNewDataContent().split(",")){
					sb.append("<input type='radio' name='"+tb.getTableName()+"_"+obj.getFieldName()+"' >");
					sb.append(content +"\n");
				}
				break;
			case "文本框":
				sb.append("<textarea name='"+tb.getName()+"_"+obj.getFieldName()+"'/>\n");
				break;						
			default:
				break;
			}						
			sb.append("</td>\n");
					
			}
		sb.append("</tr>\n");		
		sb.append("</table>\n");
		return sb;
	}
}
