/**
 * 获得日期
 * @param fieldName
 */
function getDate(fieldName){
	laydate.render({
        elem: '#'+fieldName,
		theme: '#3d80d3'
    });
}