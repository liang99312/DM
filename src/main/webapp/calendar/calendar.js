$(document).ready(function(){ addCalendar(); });

function calendar(obj){
	if ($("#calendarLayer").size()==0) addCalendar();
	$("#calendarIframe")[0].contentWindow.calendar(obj);
	$(window.frames["calendarIframe"]).focus();
}

function addCalendar(){
	$("body").append("<div id='calendarLayer' style='position:absolute;z-index:9999;width:140px;height:193px;display:none'></div>");
	$("#calendarLayer").append("<iframe id='calendarIframe' name='calendarIframe' src='/calendar/calendar.html' scrolling=no frameborder=0 width=100% height=100%></iframe>");
}

/**
$(document).click(function(e){
	var event = $.event.fix(e);
	var evObj = event.target;
	if(window.calendarIframe.cal.eventSrc != evObj)
		window.calendarIframe.hiddenCalendar();
});
*/

(function($){
$.fn.extend({
	setCalendar:function(){
		$(this).focus(function(e){ 
			if ($("#calendarLayer").size()==0) addCalendar();
			$("#calendarIframe")[0].contentWindow.calendar(e);
			$("#calendarIframe").focus();
		});
		$(document).click(function(e){
			var event = $.event.fix(e);
			var evObj = event.target;
			if(window.calendarIframe.cal.eventSrc != evObj) window.calendarIframe.hiddenCalendar();
		});
	}
})
})(jQuery);