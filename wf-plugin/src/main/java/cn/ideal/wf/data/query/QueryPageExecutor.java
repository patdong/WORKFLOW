package cn.ideal.wf.data.query;

import java.util.List;
import java.util.Map;

import cn.ideal.wf.data.analyzer.Storage;

public interface QueryPageExecutor {

	Long queryAll(Storage storage);
	Long queryWorkflowAll(Storage storage);
	List<Map<String,Object>> queryPage(Storage storage);
	List<Map<String,Object>> queryWorkflowPage(Storage storage);
	Map<String,Object> query(Storage storage);
}
