package cn.ideal.wfpf.model;

public class FMsg {

	public static final String ERROR = "0";           //返回错误
	public static final String SUCCESS = "1";         //返回成功
	public static final String PROCESSING = "2";      //返回处理中
	
	private String code;
	private String message;
	private String codeName;
	
	public FMsg() {
	}

	public FMsg(boolean res){
		if(res) code = FMsg.SUCCESS;
		if(!res) code = FMsg.ERROR;
	}
	
	public FMsg(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
		if(code.equals(ERROR)) this.setCodeName("错误");
		else if(code.equals(SUCCESS)) this.setCodeName("成功");
		else if(code.equals(PROCESSING)) this.setCodeName("处理中");
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
}
