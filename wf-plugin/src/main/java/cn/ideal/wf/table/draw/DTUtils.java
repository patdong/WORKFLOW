package cn.ideal.wf.table.draw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.service.WorkflowTableService;

public class DTUtils {
	@Autowired
	private WorkflowTableService workflowTableService;
	
	/**
	 * 表单元素位置设置
	 * @param tbId
	 * @param scope
	 * @param layoutCols
	 * @param wfId
	 * @param nodeName
	 * @return
	 */
	public Object[][] packTableElements(Long tbId, String scope, Long layoutCols,Long wfId,String nodeName){		
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
}
