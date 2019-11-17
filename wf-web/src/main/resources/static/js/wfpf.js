/*********************************************************
 * 
 * 应用表单操作
 *********************************************************/

//选择单位
function getOrg(idName){
	alert("请在第三方业务系统定义组织功能！");
}

//选择人员
function getUser(idName) {
	alert("请在第三方业务系统定义用户功能！");
}

//选择附件
function getFj(fjId, fjName) {
	alert("请在第三方业务系统定义文件上传功能！");
}

/**
 * 为子表增加一个新的空行
 * @param tableId
 */
function addsubitem(tableId){
	let i=0,posi=0;	
	let tr = "";
	let added = false;
	
	$('#'+tableId+' > tbody  > tr').each(function() {
		if($(this).find("td").find("input").length > 0){
			if(tr.length == 0){
				tr = "<tr style='height:35px'>"
				$(this).find("td").each(function(){				
					tr +="<td colspan="+this.getAttribute("colspan")+" rowspan=1 class='table-unlabel'>"+$(this).html()+"</td>";
		        });	
				tr += "</tr>";
				
				const regular = /value="\S+"/g;	
				tr = tr.replace(regular,"");
				
				added = true;
			}	
			posi = i;
		}		
		i=i+1;		
	});
	
	//在指定的对象后面增加行
	$('#'+tableId+' > tbody > tr:eq('+posi+')').after(tr); 
}

/**
 * 在子表中删除选中的行
 * @param tableId
 */
function delsubitem(tableId){	
	$("input:checkbox[name='"+tableId+"_check']:checked").each(function(){ 
		if(document.getElementsByName(tableId+"_ID").length > 1){
			var row = $(this).closest("tr"); 
		    row.remove();	
		}		
	});
}

/**
 * 获取图章
 */
function getStamp(item,item2){
	$("#stamp-dialog").show();	
}

/**
 * 给图片赋值
 * @param e
 * @param item
 * @param item2
 */
function selectedStamp(e,item,item2){
	var vals = e.value.split("|");
	$("#"+item+"_src").attr("src",vals[0]);
	$("#"+item2).val(vals[0]);	
	$("#"+item+"_div").show();
	$("#"+item).val(vals[1]);
	
	$("#"+item+"-error").hide();
	$("#stamp-dialog").hide();
}

/**
 * 表单非标准图片输入项提交时非空校验
 */
function formSpecialValid(){
	//图片非空校验
	var img = imgValid();
	
	//文件校验
	var file = fileValid();
	
	if(img || file) return true;
	else return false;
}

function imgValid(){
	var els = $("img[imagecheck=true]");
	if(els == undefined) return false;
	var ary;
	var invalid = false;
	if(els.length == 1){
		ary = [els];
	}else{
		ary = els;
	}
	if(ary != null){
		for(var i=0;i<ary.length;i++){
			var el = ary[i];
			var src = el.attr("src");
			var id = el.attr("id");
			if(src == ""){
				invalid = true;				  		
				$("#"+id+"-error").show();		
			}   		
		}
	}
	return invalid;
	
}

function fileValid(){
	var els = $("ol[imagecheck=true]");
	if(els == undefined) return false;
	var ary;
	var invalid = false;
	if(els.length == 1){
		ary = [els];
	}else{
		ary = els;
	}
	if(ary != null){
		for(var i=0;i<ary.length;i++){
			var el = ary[i];
			var content = el.innerText;
			var id = el.id;
			if(content == "" || content == undefined){
				invalid = true;				
				$("#"+id+"-error").show();		
			}   		
		}
	}
	return invalid;
}

