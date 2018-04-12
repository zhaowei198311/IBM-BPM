$(function() {
	
});

function zTreeOnClick(event, treeId, treeNode) {
	if (treeNode.itemType == "processMeta") {
		// 查询
		$.ajax({
			url: common.getPath() + "/processDefinition/listDefinitionByProcessMeta",
			dataType: "json",
			data: {
				"metaUid": treeNode.id
			},
			type: "post",
			success: function(result) {
				
			}
		});
	}
}