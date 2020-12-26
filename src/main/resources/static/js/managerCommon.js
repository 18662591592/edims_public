function addTab(title,action,param){
	var managerTabs = parent.$('#manager_tabs');
	if (managerTabs.tabs('exists', title)){	
		managerTabs.tabs('select', title);
	} else {
		var content='<div class="easyui-panel" style="width:auto;height:auto;margin-top:20px;">'
			+'<iframe scrolling="yes" frameborder="0" height="97%" width="100%" src="'+action+'.do?'+param+'" frameborder="0" ></iframe></div>';
		managerTabs.tabs('add',{
	    	title:title,
	    	closable:true,
	    	content:content,
	    	iconCls:'icon-default'
    	});
	}
}
function windowOpen(window_id,title,_url){
	$('#'+window_id).window({
		href:_url,
	    title: title,
	    shadow: true,
	    modal: true,
	    fit:true,
	    iconCls: 'icon-add',
	    closed: true,
	    minimizable: false,
	    maximizable: false,
	    collapsible: false
//	    ,onOpen:applicationDetailsReload
	}).window('open');
	
}

function formatDate(dateTime){
	var date = new Date(dateTime);
    var month = date.getMonth()+1;
    if( "" != date ){
        if( date.getMonth() +1 < 10 ){
            month = '0' + (date.getMonth() +1);
        }
        var day = date.getDate();
        if( date.getDate() < 10 ){
            day = '0' + date.getDate();
        }
        return date.getFullYear()+'-'+month+'-'+day+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    }else{
        return "";
    }
}
function formatOffsetDate(_offset){
	var date = new Date(new Date()-_offset);
    var month = date.getMonth()+1;
    if( "" != date ){
        if( date.getMonth() +1 < 10 ){
            month = '0' + (date.getMonth() +1);
        }
        var day = date.getDate();
        if( date.getDate() < 10 ){
            day = '0' + date.getDate();
        }
        return date.getFullYear()+'-'+month+'-'+day+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    }else{
        return "";
    }
}