(function ($)
{
	$.fn.wresize = function (f)
	{
		version = '1.1';
		wresize = {fired: false, width: 0};

		function resizeOnce()
		{
			if ($.support.msie)
			{
				if (!wresize.fired)
				{
					wresize.fired = true;
				} else
				{
					var version = parseInt(jQuery.browser.version, 10);
					wresize.fired = false;
					if (version < 7)
					{
						return false;
					} else if (version === 7)
					{
						var width = $(window).width();
						if (width !== wresize.width)
						{
							wresize.width = width;
							return false;
						}
					}
				}
			}

			return true;
		}

		function handleWResize(e)
		{
			if (resizeOnce())
			{
				return f.apply(this, [e]);
			}
		}

		this.each(function ()
		{
			if (this === window)
			{
				$(this).resize(handleWResize);
			} else
			{
				$(this).resize(f);
			}
		});

		return this;
	};

})(jQuery);

$(document).ready(function(){
	setSize();
	$("iframe[name='banner_top']").attr("src","/yg/ht.do?dt="+new Date());
	$("iframe[name='menu']").attr("src","/yg/cd.do?dt="+new Date());
	$("iframe[name='bottom']").attr("src","/qiYe/zhuoMian/yeJiao.html");
});

$(window).wresize(function () {
	setSize();
});

function setSize() {
	var w = $(window).width();
	var h = $(window).height();
	$("#middle").width(w - 6);
	$("#middle").height(h - $("#banner").height() - $("#bottom").height() - 6);
	$("#content").width($("#middle").width() - $("#menu").width() - 12);
	$("#content").height($("#middle").height() - 5);
	$("#workspace").width($("#content").width() - 11);
	$("#workspace").height($("#content").height());
}

function workZone() {
	this.divList = new Array();
	this.focusObj = null;

	this.addDiv = function (title, url, id) {
		var s = "<div id='" + id + "'><iframe name='content' scrolling='no' marginwidth='0' marginheight='0' topmargin='0' leftmargin='0'"
				+ "height='100%' width='100%' frameborder='0' src='" + url + "?dt=" + new Date() + "'></iframe></div>";
		if (this.divList.length === 0) {
			var dv = $("#dvTap", window.frames["banner_top"].document);
			this.divList[0] = $("<div   id='" + id + "' ><div class='dvTapTitle'>" +
				title + "</div><div class='dvTapClose'></div></div>").appendTo(dv);
			this.focusObj = this.divList[0];
			this.focusObj.find("div[class=dvTapClose]").click(function () {
				wz.delDiv($(this).parent().attr("id"));
			});
			this.focusObj.find("div[class=dvTapTitle]").click(function () {
				wz.focusObj = $(this).parent();
				wz.refresh();
			});
			$("#workspace").append(s);
		} else {
			var curID = null;
			$(window.frames["banner_top"].document).contents().find("#dvTap>div").each(function () {
				if ($(this).attr("id") === id) {
					wz.focusObj = $(this);
					curID = $(this).attr("id");
					return;
				}
			});
			if (curID === null) {
				if (this.divList.length >= 8) {
					return alert("您已经打开太多工作区了，请关闭部分您不需要的工作区，再做此操作吧。");
				}
				var dv = $(window.frames["banner_top"].document).contents().find("#dvTap");
				this.focusObj = this.divList[this.divList.length] = $("<div id='" + id + "'  ><div class='dvTapTitle'>" +
					title + "</div><div class='dvTapClose'></div></div>").appendTo(dv);
				this.focusObj.find("div[class=dvTapClose]").click(function () {
					wz.delDiv($(this).parent().attr("id"));
				});
				this.focusObj.find("div[class=dvTapTitle]").click(function () {
					wz.focusObj = $(this).parent();
					wz.refresh();
				});
				$("#workspace").append(s);
			}
		}
		this.refresh();
		return 0;
	};
	this.delDiv = function (id) {
		var focusId = this.focusObj === null? "":this.focusObj.attr("id");
		for (var i = 0; i < this.divList.length; i++ ) {
			var dv = this.divList[i];
			var dvId = dv.attr("id");
			if (dvId === id) {
				if (focusId === dvId) {
					if (this.divList.length === 1) {
						this.focusObj = null;
					} else {
						if (i === 0) {
							this.focusObj = this.divList[1];
						} else {
							this.focusObj = this.divList[i - 1];
						}
					}
				}
				$(dv).remove();
				$("#workspace>div[id=" + id + "]").remove();
				this.divList.splice(i, 1);
				break;
			}
		}
		this.refresh();
	};
	this.refresh = function () {
		if (this.divList.length === 0) {
			$("#workspace").empty();
		} else {
			for (var i = 0; i < this.divList.length; i++) {
				var dv = this.divList[i];
				dv.css("left", 130 * i + "px");
				dv.css("z-index", 10 - i);
				dv.prop("class", "backBoard");
			}
			this.focusObj.css("z-index", 20);
			this.focusObj.prop("class", "frontBoard");
			var id = this.focusObj.attr("id");
			$("#workspace div").hide();
			$("#" + id).show();
		}
	};
	this.refreshBiaoTi = function () {
		if (this.divList.length > 0) {
			var dv = $(window.frames["banner_top"].document).contents().find("#dvTap");
			var focusObjId = this.focusObj.attr("id");
			for (var i = 0; i < this.divList.length; i++) {
				var id = this.divList[i].attr("id");
				var title = this.divList[i].find("div[class=dvTapTitle]").html();
				this.divList[i] = $("<div id='" + id + "' ><div class='dvTapTitle'>" +
						title + "</div><div class='dvTapClose'></div></div>").appendTo(dv);
				if (id === focusObjId) {
					this.focusObj = this.divList[i];
				}
				this.divList[i].find("div[class=dvTapClose]").click(function () {
					wz.delDiv($(this).parent().attr("id"));
				});
				this.divList[i].find("div[class=dvTapTitle]").click(function () {
					wz.focusObj = $(this).parent();
					wz.refresh();
				});
			}
			this.refresh();
		}
	};
}
var wz = new workZone();
