var dm_KeHuList={"sz":[],"xh":-1};
var dm_YuanGongList={"sz":[],"xh":-1};
var dm_BuMenList={"sz":[],"xh":-1};
var dm_WenJianFenLeiList={"sz":[],"xh":-1};
var dm_JueSeList={"sz":[],"xh":-1};
var curList;
var preEventObjData;

//事件
function setEvent(){
	
}

/********************************查询函数*********************************/
function dm_getData(url, upData, dataObj, funName){
	var s = $.ajax({
		url: url,
		data: upData,
		dataType: "json",
		type: "post",
		cache: false,
		error: function(msg, textStatus){ failureResp(msg.responseText); },
		success:function(json){ if(checkResult(json)){ dm_setData(dataObj, json, funName); } }
	});
}

function dm_setData(dataObj, json, funName){
	dataObj.sz=json.sz;
	dataObj.xh=-1;
	if ((funName===null)||(funName!==undefined)) funName(json);
}

//查询部门
function dm_cxBuMen(funName){
	var json = {};
	json={"jsonObj":JSON.stringify(json)};
	dm_getData("/bm/cxBmzd.do", json, dm_BuMenList, funName);
}

//查询客户
function dm_cxKeHu(funName){
	var json = {};
	json={"jsonObj":JSON.stringify(json)};
	dm_getData("/kh/cxKhzd.do", json, dm_KeHuList, funName);
}

//查询员工
function dm_cxYuanGong(funName){
	var json = {};
	json={"jsonObj":JSON.stringify(json)};
	dm_getData("/yg/cxYgzd.do", json, dm_YuanGongList, funName);
}

//查询分类
function dm_cxWenJianFenLei(funName){
	var json = {};
	json={"jsonObj":JSON.stringify(json)};
	dm_getData("/wjfl/cxWjflzd.do", json, dm_WenJianFenLeiList, funName);
}

//查询角色
function dm_cxJueSe(funName){
        var json = {};
	json={"jsonObj":JSON.stringify(json)};
	dm_getData("/yg/cxJszd.do", json, dm_JueSeList, funName);
}