(function ($) {
	$(document).on("click", ".spinner .btn:first-of-type", function() {
		$('.spinner input').val( parseInt($('.spinner input').val(), 10) + 1);
	});
	$(document).on("click", ".spinner .btn:last-of-type", function() {
		$('.spinner input').val( parseInt($('.spinner input').val(), 10) - 1);
	});
})(jQuery);