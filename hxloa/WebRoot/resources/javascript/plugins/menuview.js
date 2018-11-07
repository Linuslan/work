(function($) {
	/**
	 * 查询当前登录用户的左侧菜单
	 */
	$.fn.menuview = function(options) {
		var $this = $(this);
		if(url && "" != $.trim(url)) {
			$.ajax({
				url: url,
				data: {},
				type: "POST",
				success: function(data) {
					var json = eval("("+data+")");
					if(json.success) {
						if(true == json.success) {
							var data = json.root;
							if(data) {
								var totalRecord = data[totalRecordKey];
								var list = data[dataKey];
								$this.setTotalRecord(totalRecord);
								$this.setData(list);
								$this.refresh();
							}
						} else {
							alert(json[msg]);
						}
					} else {
						var data = json[dataKey];
						if(data) {
							var totalRecord = json[totalRecordKey];
							$this.setTotalRecord(totalRecord);
							$this.setData(data);
							$this.refresh();
						}
					}
					
				},
				error: function() {
					alert("系统异常，请联系管理员！");
				}
			});
		}
	}
})(jQuery);