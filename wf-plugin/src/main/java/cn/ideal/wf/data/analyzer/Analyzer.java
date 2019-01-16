package cn.ideal.wf.data.analyzer;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据解析器
 * @author 郭佟燕
 * @version 2.0
 *
 */
public interface Analyzer {

	Storage dataAnalyze(HttpServletRequest request, Long wfId) throws Exception;
}
