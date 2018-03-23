(function($){
	$.fn.extend({
		popTopBar:function(title){
			var divId = $(this).attr("id");
			var topBarDiv="<div class='topBar'><table><tr><td>"+title+"</td><td style='width:19px'><div class='close' id='"+divId+"_close'></div></td></tr></table></div>";
			$(this).prepend(topBarDiv);
			$(this).find("#"+divId+"_close").click(function(){
				if ($("#"+divId).css("z-index")<10){
					window.parent.$("#dvIfm").hide();
				}else{
					$("#"+divId).hide();
					if ($("#board").css("z-index")<10)
						$("#board").hide();
					else
						$("#board").css("z-index", $("#board").css("z-index")-10);
				}
			});
		},
		
		setListBottom:function(shuJu, fun){
			var divId = $(this).attr("id");
			var btmDiv = "<div id='"+divId+"_btm' style='width:100%;text-align:center;margin-top:1px;'>"
					   + "<table style='float:none;margin:auto;width:700px;border:0'><tr><td style='width:20%; text-align:right;border:0'>"
					   + "<input type='button' value='第一页'><input type='button' value='上一页'></td>"
					   + "<td style='width:30%; text-align:center;border:0'>"
					   + "第<a id='"+divId+"_yeXu'>0</a>页/共<a id='"+divId+"_zys'>0</a>页/共<a id='"+divId+"_jls'>0</a>条记录"
					   + "</td>"
					   + "<td style='text-align:left;border:0'>"
					   + "<input type='button' value='下一页'><input type='button' value='最末页' class='btnLp'>"
					   + "直接跳到第<input type='text' id='"+divId+"_thePage' class='thePage'>页"
					   + "<input type='button' class='btnGo'></td></tr></table></div>";
			$(this).parent().append(btmDiv);
			$(this).parent().find("#"+divId+"_btm input[type=button]").each(function(i){
				switch(i){
					case 0:
						$(this).click(function(){
							if (shuJu.yx>1){
								shuJu.yx = 1;
								fun();
							}else{
								alert("已经是第一页了！不能再往前翻页了!");
							}
						});
						break;
					case 1:
						$(this).click(function(){
							if (shuJu.yx>1){
								shuJu.yx = shuJu.yx-1;
								fun();
							}else{
								alert("已经是第一页了！不能再往前翻页了!");
							}
						});
						break;
					case 2:
						$(this).click(function(){
							if (shuJu.yx>=shuJu.zys){
								alert("已经是最后一页了！不能再往后翻页了!");
							}else{
								shuJu.yx = shuJu.yx+1;
								fun();
							}
						});
						break;
					case 3:
						$(this).click(function(){
							if (shuJu.yx>=shuJu.zys){
								alert("已经是最后一页了！不能再往后翻页了!");
							}else{
								shuJu.yx = shuJu.zys;
								fun();
							}
						});
						break;
					case 4:
						$(this).click(function(){
							var zhiDingYeXu = parseInt($("#"+divId+"_thePage").val());
							if (isNaN(zhiDingYeXu)){ return alert('页序数字不正确，请重新输入123！'); }
							if (zhiDingYeXu>shuJu.zys){ return alert('页序数字不正确，请重新输入！'); }
							if (zhiDingYeXu<1){ return alert('页序数字不正确，请重新输入！'); }
							if (zhiDingYeXu===shuJu.yx){ return alert('页序为当前页，请重新输入！'); }
							shuJu.yx = zhiDingYeXu;
							fun();
						});
						break;
					default:
						alert("不可识别的对象！");
				}
			});
		},
		
		setPage:function(yx, zys){
			var divId = $(this).attr("id");
			if (zys===undefined){
				var j=yx;
				$("#" + divId + "_yeXu").html(j.yx);
				$("#" + divId + "_zys").html(j.zys);
				$("#" + divId + "_jls").html(j.jls);
			}else{
				$("#" + divId + "_yeXu").html(yx);
				$("#" + divId + "_zys").html(zys);
				$("#" + divId + "_jls").html("未知");
			}
		},
		
		tableHilight:function(obj){
			$(this).mouseover(function(){
				$(this).siblings().removeClass("selected");
				$(this).addClass("selected");
			});
			$(this).mousedown(function(){ obj.xh=obj.seq = $(this).prevAll().length-1; });
			return this;
		},
		
		built:function(option){
			var col = $(this).find("tr:first td").size();
			var isKeep = $(this).find("tr:first").hasClass("biaoTou");
			var theWidth;
			if (isKeep){
				$(this).find("tr:gt(0)").remove();
			}else{
				theWidth = $.map($(this).find("tr:first td"), function(e){ return $(e).width(); });
				$(this).find("tr").remove();
			}
				
			if ((option)&&(option.data)&&(option.data.length>0)){
				var data=option.data;
				var obj=option.obj;
				var fun=option.dbclick;
				var fit=option.fit;
				for (var i=0; i<data.length; i++){
					var tr = data[i];
					var td = tr.td;
					var s = "";
					for (var j=0; j<col; j++){ s += "<td>"+td[j]+"</td>"; }
					$(this).append((tr.c===null) ? "<tr>"+s+"</tr>" : "<tr class='"+tr.c+"'>"+s+"</tr>");
				}
				if (!fit){
					if (option.row===undefined) option.row = 20;
					for (var i=data.length, row=option.row; i<row; i++){
						var s = "";
						for (var j=0; j<col; j++){ s += "<td></td>"; }
						$(this).append("<tr>"+s+"</tr>");
					}
				}
				$(this).find("tr").mousedown(function(){
					obj.xh = obj.seq = $(this).parent().find("tr:first").hasClass("biaoTou") ?  $(this).prevAll().length-1: $(this).prevAll().length;
				});
				if ((obj)&&(fun)&&(typeof(fun)==="function")){
					if (isKeep){
						$(this).find("tr:gt(0)").dblclick(function(){ fun(); });
					}else{
						$(this).find("tr").dblclick(function(){ fun(); });
					}
					$(this).listKeyDown(obj, fun);
				}
			}else{
				if ((option)&&(option.fit===true)) return;
				var row= ((option)&&(option.row!==undefined)) ? option.row : 20;
				for (var i=0; i<row; i++){
					var s = "";
					for (var j=0; j<col; j++) s += "<td></td>";
					$(this).append("<tr>"+s+"</tr>");
				}
			}
			if (!isKeep) $(this).setColWidth(theWidth);
			return this;
		},
		
		listKeyDown:function(obj, funName){
			$(this).keydown(function(e){
				var event = $.event.fix(e);
				var evObj = event.target;
				switch (event.keyCode){
				case 13:
					var f = eval(funName);
					f();
					break;
				case 38:
					if ((obj.seq<=0)||(obj.seq>=obj.list.length)) obj.seq = obj.list.length;
					--obj.seq;
					$(this).find("tr:gt(0)").removeClass("selected");
					$(this).find("tr:eq("+(obj.seq+1)+")").addClass("selected");
					obj.xh = obj.seq;
					break;
				case 40:
					if ((obj.seq<0)||(obj.seq>(obj.list.length-2))) obj.seq=-1;
					++obj.seq;
					$(this).find("tr").removeClass("selected");
					$(this).find("tr:eq("+(obj.seq+1)+")").addClass("selected");
					obj.xh = obj.seq;
					break;
				}
			});
		},
		
		setColWidth:function(opt){
			if (!opt) return;
			$(this).find("tr:eq(0)>td:not(:last)").each(function(i){
				if (opt[i]!==undefined){
					$(this).width(opt[i]+"px");
				}
			});
		},
		
		setGunDongTi:function(opt){
			$(this).addClass("gunDong");
			$(this).find(".gunDongBiao_bt").wrap("<div class='dvGunDongBiao_bt'></div>")
			$(this).find(".gunDongBiao").wrap("<div class='ke4dvGunDongBiao'><div class='dvGunDongBiao'></div></div>");
			if ((opt!==undefined)&&(opt.set)){
				if (opt["max-height"]) $(this).find(".dvGunDongBiao").css("max-height", opt["max-height"]+"px");
				if (opt["min-height"]) $(this).find(".dvGunDongBiao").css("min-height", opt["min-height"]+"px");
				if (opt.width){
					$(this).css("width", opt.width+"px");
					$(this).find(".gunDongBiao").css("width", opt.width+"px");
					$(this).find(".gunDongBiao_bt").css("width", opt.width+"px");
					$(this).find(".dvGunDongBiao_bt").css("width", opt.width+"px");
					$(this).find(".ke4dvGunDongBiao").css("width", opt.width+"px");
					$(this).find(".dvGunDongBiao").css("width", (opt.width+20)+"px");
				}
				if (opt.set){
					var tdNum = $(this).find(".gunDongBiao_bt td").size() - 1;
					if (tdNum < 0) return;
					var width = $.map($(this).find(".gunDongBiao_bt td"), function (obj) { return $(obj).width(); });
					var tdStr = "";
					for (var i = 0; i < tdNum; i++) {
						tdStr += "<td style='width:" + width[i] + "px'></td>";
					}
					var trStr = "<tr>" + tdStr + "<td></td></tr>";
					var row = opt.row;
					if (row !== undefined) {
						for (var i = 0, len = row - 1; i < len; i++) {
							trStr += "<tr>" + tdStr + "<td></td></tr>";
						}
					}
					$(this).find(".gunDongBiao").html(trStr);
				}
			}
		}
	});
})(jQuery);