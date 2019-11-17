package cn.ideal.wf.table.draw;

import cn.ideal.wf.model.WorkflowUser;

/**
 * 将节点装饰城节点树
 * @author 郭佟燕
 * @version 2.0
 */

public interface PureTableService {

	StringBuffer draw(Long tbId);
	
	StringBuffer draw(Long tbId,String scope, boolean setting);
	
	StringBuffer draw(Long tbId, Long wfId, Long defId, Long bizId,WorkflowUser user);
}
