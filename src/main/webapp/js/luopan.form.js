(function($){
	$.fn.extend({
		fmBuilt:function(){
			$(this).find("input,select").r2t();
		},
		
		r2t:function(){
			$(this).keydown(function(e){
				var event = $.event.fix(e);
				var evObj = event.target;
				if (event.keyCode===13){
					var list = $(evObj).parentsUntil("table").parent().find("select,input:visible").not($("[readonly]")).not($("[disabled]"));
					var seq = list.index(this)+1;
					if (seq<=list.length){
						list[seq].focus();
						if (list[seq].type==="text") list[seq].select();
					}
				}
			});
		},
		
		inputKit:function(opt){
			var srcData=opt.src;
			var objData=opt.obj;
			var fun = opt.fun;
			if ($("#dvTiShi").size()===0) $("body").append("<div id='dvTiShi' class='tiShi'><table></table></div>");
			
			$(this).focus(function(){
				var offset = $(this).offset();
				$("#dvTiShi table").html("");
				$("#dvTiShi").css("top", $(this).outerHeight()+offset.top);
				$("#dvTiShi").css("left", offset.left);
				window.preEvData=null;
				$(this).tiShiKit(opt);
				window.inputKitEvObj=this;
				if ((window.inputKitTimer!==null)&&(window.inputKitTimer!==undefined)) clearInterval(window.inputKitTimer);
				window.inputKitTimer=setInterval(function(){ $(window.inputKitEvObj).tiShiKit(opt); }, 200);
				$(this).blur(function(){
					if ((window.inputKitTimer!==null)&&(window.inputKitTimer!==undefined)) clearInterval(window.inputKitTimer);
					window.inputKitTimer=null;
					$("#dvTiShi").hide();
					$(window.inputKitEvObj).unbind("blur");
				});
			});

			//响应按键事件
			$(this).keydown(function(e){
				var event = $.event.fix(e);
				var evObj = event.target;
				switch (event.keyCode){
				case 13:
					var bh=$("#dvTiShi tr[class=focus] td:first").html();
					var sz = srcData.sz;
					for (var i=0; i<sz.length; i++){
						if (sz[i].bh===bh){
							if (fun){
								fun(sz[i]);
							}else{
								var u={"bh":sz[i].bh,"mc":sz[i].mc,"dm":sz[i].dm};
								$(evObj).val(u.mc);
								if ((objData!==null)&&(objData!==undefined)&&(objData.length>0)) eval(objData+"=u");
							}
							break;
						}
					}
					return;
				case 38:
					var row = $("#dvTiShi tr").size();
					if (row>0){
						var i = $("#dvTiShi tr").index($("#dvTiShi tr[class=focus]")[0]);
						i = (i>0) ? --i : row-1;
						$("#dvTiShi tr").removeClass("focus");
						$("#dvTiShi tr:eq("+i+")").addClass("focus");
					}
					break;
				case 40:
					var row = $("#dvTiShi tr").size();
					if (row>0){
						var i = $("#dvTiShi tr").index($("#dvTiShi tr[class=focus]")[0]);
						i = ((i<0)||(i===(row-1))) ? 0 : ++i;
						$("#dvTiShi tr").removeClass("focus");
						$("#dvTiShi tr:eq("+i+")").addClass("focus");
					}
					break;
				}
			});
		},
		
		tiShiKit:function(opt){
			var srcData=opt.src;
			var objData=opt.obj;
			var fun=opt.fun;
			var guoLvTiaoJian=opt.gltj;

			//如果输入框的内容有变动，重新生成提示框
			var evData=$(this).val();
			if (evData!==window.preEvData){
				window.preEvData=evData;
				var count=0;
				var s="";
				var sz=srcData.sz;
				for (var i=0, len=sz.length; i<len; i++){
					if ((sz[i].dm===null)||(sz[i].dm===undefined)) sz[i].dm="";
					if ((guoLvTiaoJian===null)||(guoLvTiaoJian===undefined)){
						if ((sz[i].mc.indexOf(evData)>-1)||(sz[i].dm.indexOf(evData)>-1)){
							s+="<tr><td style='display:none;'>"+sz[i].bh+"</td><td>"+sz[i].mc+"</td></tr>";
							if (++count>9) break;
						}
					}else{
						if (((sz[i].mc.indexOf(evData)>-1)||(sz[i].dm.indexOf(evData)>-1))&&(eval(guoLvTiaoJian))){
							s+="<tr><td style='display:none;'>"+sz[i].bh+"</td><td>"+sz[i].mc+"</td></tr>";
							if (++count>9) break;
						}
					}
				}
				$("#dvTiShi table").html(s);
				$("#dvTiShi tr:eq(0)").addClass("focus");
				$("#dvTiShi").show();

				$("#dvTiShi tr").mouseover(function(){
					$(this).siblings().removeClass("focus");
					$(this).addClass("focus");
				});

				var evObj=this;
				$("#dvTiShi tr").mousedown(function(){
					var bh=$(this).find("td:first").html();
					for (var i=0; i<sz.length; i++){
						if (sz[i].bh===bh){
							if (fun){
								fun(sz[i]);
							}else{
								var u={"bh":sz[i].bh,"mc":sz[i].mc,"dm":sz[i].dm};
								$(evObj).val(u.mc);
								if ((objData!==null)&&(objData!==undefined)&&(objData.length>0)) eval(objData+"=u");
							}
							break;
						}
					}
				});

			};
		},
		
		setGeXingShuJu:function(data){
			var sz=data.sz;
			var opt=data.opt;
			var nr=data.nr;
			$(this).geXingShuJuBuilt(sz, opt, nr);
		},
		
		geXingShuJuBuilt:function(sz, opt, zi){
			if ((sz===null)||(sz===undefined)||(sz.length<1)){
				$(this).parent().parent().hide();
				$(this).html("");
				return;
			}
			$(this).parent().parent().show();
			//var w = $(this).find("td:first").css("width");
			$(this).html("");
			var len=sz.length;
			var col = (len>4) ? 2 : 1;
			var str = (col===2) ? "<tr><td class='title'></td><td style='width:280px'></td><td class='title'></td><td></td></tr>" : "<tr><td class='title'></td><td></td></tr>";
			for (var i=0; i<len; i++){
				if (i%col===0) $(this).append(str);
				var gxsjId="sjId"+i;
				var sj=sz[i];
				var sr;
				var sjgs=$(document).shuJuGeShi(sj);
				switch(sj.sjlx){
				case 0:
					sr="";
					break;
				case 1:
					sr="<input id='"+gxsjId+"' "+sjgs+" type='text'/>";
					break;
				case 2:
					sr="<textarea id='"+gxsjId+"' "+sjgs+" rows='3' cols='50'></textarea>";
					break;
				case 3:
					sr="<select id='"+gxsjId+"' "+sjgs+"></select>";
				default:
					sr="";
				}
				//$(this).append("<tr><td class='title'>"+sj.mc+"：</td><td>"+sr+"</td></tr>");
				if (sj.mc===undefined) sj.mc=sj.bq;//过渡方案，工序模版、工序的bq要改回mc
				$(this).find("td:eq("+(i*2)+")").html(sj.mc+"：");
				$(this).find("td:eq("+(i*2+1)+")").html(sr);
				if (sj.val!==undefined) $("#"+gxsjId).val(sj.val);
				$(document).xiangYingShiJian(gxsjId, sj);
			}
			if (zi!==undefined){
				for (var i=0, len=zi.length; i<len; i++){
					e = zi[i];
					for (var key in e){
						$("#"+key).val(e[key]);
					}
				}
			}
			if (opt!==undefined){
				$(this).find("td").mousedown(function(){ opt.xh = parseInt($(this).parentsUntil("table").find("td").index(this)/2); });
			}
		},
		
		shuJuGeShi:function(shuJu){
			switch(shuJu.sjgs){
				case 0:
					return "";
				case 1:
					return " class='text'";
				case 2:
					return " class='shuZi'";
				case 3:
					return " class='riLi'";
				default:
					return "";
			}
		},
		
		xiangYingShiJian:function(id, shuJu){
			if (shuJu.sjgs===3){
				$("#"+id).focus(function(e){ calendar(e); });
				return;
			}
			if ($("#dvTiShi").size()===0) $("body").append("<div id='dvTiShi' class='tiShi'><table></table></div>");
			switch(shuJu.xysj){
				case 0:
					break;
				case 1:
					$("#"+id).inputKit({"src":$(document).piPeiShuJu(shuJu),"obj":""});
					break;
				default:
			}
		},

		piPeiShuJu:function(shuJu){
			switch(shuJu.sjly){
				case 0: return null;
				case 1: return null;
				case 2:
					if (lp_CangKuList.sz.length===0) lp_cxCangKu();
					return lp_CangKuList;
				case 3:
					if (lp_WuZiLeiBieList.sz.length===0) lp_cxWuZiLeiBie();
					return lp_WuZiLeiBieList;
				case 4:
					if (lp_WuZiZiDianList.sz.length===0) lp_cxWuZiZiDian();
					return lp_WuZiZiDianList;
				case 5:
					if (lp_YuanGongList.sz.length===0) lp_cxYuanGong();
					return lp_YuanGongList;
				case 6:
					if (lp_KeHuList.sz.length===0) lp_cxKeHu();
					return lp_KeHuList;
				case 7:
					if (lp_XuQiuLeiBieList.sz.length===0) lp_cxXuQiuLeiBie();
					return lp_XuQiuLeiBieList;
				case 8:
					if (lp_XuQiuBiaoZhunList.sz.length===0) lp_cxXuQiuMoBan();
					return lp_XuQiuBiaoZhunList;
				case 9:
					if (lp_XuQiuList.sz.length===0) lp_cxXuQiu();
					return lp_XuQiuList;
				case 10:
					var fl = shuJu.zdfl;
					if((lp_ZiDianList[fl]===null)||(lp_ZiDianList[fl]===undefined)||(lp_ZiDianList[fl].sz.length===0)){
						lp_cxZhuZiZiDian(fl);
					}
					return lp_ZiDianList[fl];
				default: return null;
			}
		},
		
		setAidMsg:function(opt){
			var id=$(this).attr("id");
			$(this).addClass("aidMsg");
			var nr = $(this).html();
			$(this).html("<div class='arrow'></div><div id='"+id+"_detail' class='detail'>"+nr+"</div>");
			$(this).find(".aidHead").wrap("<div class='aidHeadShell'></div>")
			$(this).find(".aidBody").wrap("<div class='aidBodyShell1'><div class='aidBodyShell2'></div></div>");
		}
	});
})(jQuery);