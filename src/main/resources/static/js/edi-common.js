
function ediDatagridInit(_datagrid_id,_query_params,_title,_url,_fields,_click_function,_double_click_function,_load_success_function,_on_row_context_menu){
	if(null==_click_function){
		_click_function=function(rowIndex,rowData){
     
        };
	}
	if(null==_double_click_function){
		_double_click_function=function(index,field,value){
        };
	}
	if(null==_load_success_function){
		_load_success_function=function(datas){

        };
	}
	if(null==_on_row_context_menu){
		_on_row_context_menu=function(e, rowIndex, rowData) {
        };
	}
    $("#"+_datagrid_id).datagrid({
        title:_title,
        queryParams:_query_params,
        url:_url,
        method:"post",
        rownumbers:true,
        striped:true,
        dataType: 'json',
        fitColumns:true,
        fit:true,
        singleSelect: true,
        remoteSort: false,
		pagination:true,
		rownumbers:true,
		striped:true,
		pageSize:20,
		pageList:[10,20,30,40],
        columns: [_fields],
        onClickRow:_click_function,
        onDblClickCell:_double_click_function,
        onLoadSuccess:_load_success_function,
        onRowContextMenu:_on_row_context_menu
    });
}


function datagridReload(_datagrid_id){
	var _parammap=getQueryParams();
    var queryParams = $('#'+_datagrid_id).datagrid('options').queryParams;
    for(var _key in _parammap){
        queryParams[_key]=_parammap[_key];
    }
    $("#"+_datagrid_id).datagrid('reload');
}

function warnAlert(msg){
	swal({title:msg,  
        type:"success"}) 
}

function alertSystemError(msg){
	swal({title:"系统异常", text:msg,type:"error"}) 
}

function dealWithErrorMSG(data){
	if(data.code=='10086'){
		//没有登录
		swal({title:data.msg, text:"点击确定返回登录页",type:"error"}, function() {
            $.ajax({
                url :"logout",
                type : 'post',
                async : false
            }); 
	    }) ;
	}else if(data.code=='10400'){
		//没有权限
		swal({title:'无操作权限', text:data.msg,type:"error"}) ;
	}else {
		swal({title:'错误', text:data.msg,type:"error"}) ;
	}
	
}