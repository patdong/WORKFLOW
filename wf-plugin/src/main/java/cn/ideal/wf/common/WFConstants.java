package cn.ideal.wf.common;
/**
 * 常量表
 * @author 郭佟燕
 * @version 2.0
 */
public class WFConstants {

	/*
	  工作流操作一览
	 */
	public final static String WF_ACTION_START = "启动流程";
	public final static String WF_ACTION_DOING = "办理";
	public final static String WF_ACTION_PASS = "办理完成";
	public final static String WF_ACTION_END = "办结";
	public final static String WF_ACTION_WAITING = "尚未办理";
	public final static String WF_ACTION_RETURN = "退回";
	public final static String WF_ACTION_CALLBACK = "收回";
	public final static String WF_ACTION_TERMINATION = "终止";
	public final static String WF_ACTION_POSTPHONE = "暂缓";
	public final static String WF_ACTION_DISPATCH = "调度";
	
	/*
	  流程状态
	 */
	public final static String WF_STATUS_PASSING = "办理";
	public final static String WF_STATUS_SLEEP = "休眠";
	public final static String WF_STATUS_END = "办理完毕";
	public final static String WF_STATUS_UNDO = "未办理";
	
	/*
	  流程默认节点
	 */	
	public final static String WF_NODE_STRAT = "创建";
	
	/*
	 流程节点办理人类型
	 */
	public final static String WF_NODE_WORK_USER = "用户";
	public final static String WF_NODE_WORK_ROLE = "角色";
	public final static String WF_NODE_WORK_ORG = "单位";
	
	/**
	 * 流程中的固定节点定义
	 */
	public final static String WF_NODE_START = "创建";
	public final static String WF_NODE_END = "结束";
	
	/**
	 * 平台支持后备用户/单位的默认值
	 */
	public final static Long WF_BACKUP_VALUE = 9999L;
	
	/**
	 * 节点类型
	 */
	public final static String WF_NODE_TYPE_SINGLE = "单人";
	public final static String WF_NODE_TYPE_SERIAL = "串行";
	public final static String WF_NODE_TYPE_PARALLEL = "并行";
	
	/**
	 * 消息传送方式
	 */
	public final static String WF_MSG_DISPENSE = "分发";
	public final static String WF_MSG_RESPONSE = "相应";
	
	/**
	 * 流程按钮action分类
	 */
	public final static String WF_BUTTON_ACTION_FLOW = "流程";
	public final static String WF_BUTTON_ACTION_ANSWER = "应答";
	public final static String WF_BUTTON_ACTION_PUSH = "行为";
	public final static String WF_BUTTON_ACTION_PUSHWITHMSG = "行为消息";
}
