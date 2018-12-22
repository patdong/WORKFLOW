package cn.ideal.wf.service;

/**
 * 将节点装饰城节点树
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;

import cn.ideal.wf.model.Node;

public interface NodeTreeService {
	
	Node[][] decorateNodeTree(List<Node> nodes);

}
