//金额格式控制
$.validator.addMethod("Money", function(value, element) {
	var v_regex=/\d+\.?\d{0,2}/;	
	if(value && value.length > 0){
		if(!v_regex.test(value)){
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
}, "请正确填写金额");

$.validator.addMethod("Text", function(value, element) {
	var v_regex=/^([A-Za-z])+_?[A-Za-z0-9]*$/;	
	if(value  && value.length > 0){
		if(!v_regex.test(value)){
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
}, "字符和下划线组合");
