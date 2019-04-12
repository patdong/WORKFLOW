package cn.ideal.wf.table.draw;

/**
 * 将节点装饰城节点树
 * @author 郭佟燕
 * @version 2.0
 */

public interface PureTableService {

	StringBuffer draw(Long tbId);
	
	StringBuffer draw(Long tbId,String scope, boolean setting);
	
	StringBuffer draw(Long tbId, Long bizId);
}
