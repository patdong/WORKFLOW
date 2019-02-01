package cn.ideal.wf.tree;

/**
 * 将节点装饰城节点树
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;

import cn.ideal.wf.model.WorkflowNode;

public interface NodeTreeService {
	
	WorkflowNode[][] decorateNodeTree(List<WorkflowNode> nodes);

}
