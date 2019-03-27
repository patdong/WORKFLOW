package cn.ideal.wf.flowchat.draw;

/**
 * 将节点装饰城节点树
 * @author 郭佟燕
 * @version 2.0
 */

public interface FlowChatService {

	StringBuffer draw(Long wfId);
	
	StringBuffer draw(Long wfId,Long bizId);
}
