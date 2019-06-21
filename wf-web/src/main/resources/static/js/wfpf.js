
function getFj(fjId, fjName) {
	alert("请在第三方业务系统定义文件上传功能！");
}
/**
 * 为子表增加一个新的空行
 * @param tableId
 */
function addsubitem(tableId){
	var i=0;
	var tr = "<tr style='height:35px'>";
	
	$('#'+tableId+' > tbody  > tr').each(function() {
		if(i==1){
			$(this).find("td").each(function(){				
				tr +="<td colspan=1 rowspan=1 style='text-align:left;padding-left:5px;font-size:15px'>"+$(this).html()+"</td>";
	        });	
			tr += "<input type='hidden' name='"+tableId+"_ID' value=''>";
			tr += "</tr>";
			$('#'+tableId+' > tbody:last').append(tr); 
		}		
		i=i+1;
		if(i>1) return;
	});
}

/**
 * 在子表中删除选中的行
 * @param tableId
 */
function delsubitem(tableId){
	$("input:checkbox[name='"+tableId+"_check']:checked").each(function(){ 
		var row = $(this).closest("tr"); 
	    row.remove();		
	});
}