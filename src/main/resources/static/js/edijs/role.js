$(document).ready(function() {
	roleDatagridInit();
});

function roleDatagridInit(){
    var role_datagrid=$("#role_datagrid");
    if(null!=role_datagrid){
        var _fields=[
        	{field:'roleId',title:"ID",width:1,hidden:true},
        	{field:'roleName',title:'角色名称',align:'center',width:20}
        ];
        var _query_params=getQueryParams();
        var _title='角色信息';
        var _datagrid_id='role_datagrid';
        var _url='intf/commonintf?method=getEdiRoles';
        var _double_click_function=function(index,field,value){
        	var selectedRow = $(this).datagrid('getSelected');
        	roleDialogOpen(selectedRow);
        };
        var _on_row_context_menu=function(e, rowIndex, rowData) { 
            e.preventDefault(); 
            $(this).datagrid("selectRow", rowIndex); 
            $('#menu').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
        }
        var _click_function=function(rowIndex,rowData){
        	menuDatagridInit(rowIndex,rowData);

        }
        
        var _load_success_function=function(datas){
            var datagridRows=$('#role_datagrid').datagrid('getRows');
            menuDatagridInit(0,datagridRows[0]);
        }
        ediDatagridInit(_datagrid_id,_query_params,_title,_url,_fields,_click_function,_double_click_function,_load_success_function,_on_row_context_menu);
    }
}

function getQueryParams(){
    var _parammap={};
    return _parammap;
}

function menuDatagridInit(rowIndex,rowData){
	var roleId=rowData.roleId;
	var _query_params={};
	var _datagrid_id='menu_datagrid';
	var _url="intf/commonintf?method=getRoleMenus&roleId="+roleId;
	var _title=rowData.roleName+'的菜单权限';
    var _fields=[
    	{field:'roleId',title:"roleId",width:1,hidden:true},
    	{field:'menuId',title:"menuId",width:1,hidden:true},
    	{field:'menuName',title:'模块名称',align:'center',width:20},
    	{field:'query',title:'查询',align:'center',width:20,formatter: function (value, rec, rowIndex) {
    		var data_value="&roleId="+rec.roleId+'&menuId='+rec.menuId;
    		var ischecked="";
    		if(value){
    			ischecked="checked=\"checked\"";
    		}
            return "<input type=\"checkbox\"  id=\""+rec.roleId+rec.menuId+"query\" value=\""+rec.menuId+"\" name=\"role_query\" "+ischecked+" data-value=\"" + data_value + "\">";
        }},
    	{field:'create',title:'新增',align:'center',width:20,formatter: function (value, rec, rowIndex) {
    		var data_value="&roleId="+rec.roleId+'&menuId='+rec.menuId;
    		var ischecked="";
    		if(value){
    			ischecked="checked=\"checked\"";
    		}
            return "<input type=\"checkbox\"  id=\""+rec.roleId+rec.menuId+"create\" value=\""+rec.menuId+"\" name=\"role_create\" "+ischecked+"  data-value=\"" + data_value + "\">";
        }},
    	{field:'update',title:'修改',align:'center',width:20,formatter: function (value, rec, rowIndex) {
    		var data_value="&roleId="+rec.roleId+'&menuId='+rec.menuId;
    		var ischecked="";
    		if(value){
    			ischecked="checked=\"checked\"";
    		}
            return "<input type=\"checkbox\"  id=\""+rec.roleId+rec.menuId+"update\" value=\""+rec.menuId+"\" name=\"role_update\" "+ischecked+"  data-value=\"" + data_value + "\">";
        }},
    	{field:'delete',title:'删除',align:'center',width:20,formatter: function (value, rec, rowIndex) {
    		var data_value="&roleId="+rec.roleId+'&menuId='+rec.menuId;
    		var ischecked="";
    		if(value){
    			ischecked="checked=\"checked\"";
    		}
            return "<input type=\"checkbox\"  id=\""+rec.roleId+rec.menuId+"delete\" value=\""+rec.menuId+"\" name=\"role_delete\" "+ischecked+"  data-value=\"" + data_value + "\">";
        }},
    	{field:'upload',title:'上传',align:'center',width:20,formatter: function (value, rec, rowIndex) {
    		var data_value="&roleId="+rec.roleId+'&menuId='+rec.menuId;
    		var ischecked="";
    		if(value){
    			ischecked="checked=\"checked\"";
    		}
            return "<input type=\"checkbox\"  id=\""+rec.roleId+rec.menuId+"upload\" value=\""+rec.menuId+"\" name=\"role_upload\" "+ischecked+"  data-value=\"" + data_value + "\">";
        }},
    	{field:'download',title:'下载',align:'center',width:20,formatter: function (value, rec, rowIndex) {
    		var data_value="&roleId="+rec.roleId+'&menuId='+rec.menuId;
    		var ischecked="";
    		if(value){
    			ischecked="checked=\"checked\"";
    		}
            return "<input type=\"checkbox\"  id=\""+rec.roleId+rec.menuId+"download\" value=\""+rec.menuId+"\" name=\"role_download\" "+ischecked+"  data-value=\"" + data_value + "\">";
        }}
    ];
    
    var _load_success_function=function(datas){
        $("input[name='role_query']").unbind().bind("click",function () {
        	var data_value = $(this).attr('data-value');
        	var isChecked=this.checked;        	
                $.ajax({
                    url :"/intf/commonintf?method=updateEdiRoleMenu"+data_value+"&query="+isChecked,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    	if(data.code=='200'){
                    		warnAlert("修改成功");
                    	}else{
                    		dealWithErrorMSG(data);
                    	}
                    },
                    error : function(data) {
                    	warnAlert('网络异常，请联系管理员');
                    }
                });	

        });
        $("input[name='role_create']").unbind().bind("click",function () {
        	var data_value = $(this).attr('data-value');
        	var isChecked=this.checked;        	
        	var row = $('#menu_datagrid').datagrid('getSelected');
        	if(row){
                $.ajax({
                    url :"/intf/commonintf?method=updateEdiRoleMenu"+data_value+"&create="+isChecked,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    	if(data.code=='200'){
                    		warnAlert("修改成功");
                    	}else{
                    		dealWithErrorMSG(data);
                    	}
                    },
                    error : function(data) {
                    	warnAlert('网络异常，请联系管理员');
                    }
                });	
        	}

        });
        $("input[name='role_update']").unbind().bind("click",function () {
        	var data_value = $(this).attr('data-value');
        	var isChecked=this.checked;        	
        	var row = $('#menu_datagrid').datagrid('getSelected');
        	if(row){
                $.ajax({
                    url :"/intf/commonintf?method=updateEdiRoleMenu"+data_value+"&update="+isChecked,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    	if(data.code=='200'){
                    		warnAlert("修改成功");
                    	}else{
                    		dealWithErrorMSG(data);
                    	}
                    },
                    error : function(data) {
                    	warnAlert('网络异常，请联系管理员');
                    }
                });	
        	}

        });
        $("input[name='role_delete']").unbind().bind("click",function () {
        	var data_value = $(this).attr('data-value');
        	var isChecked=this.checked;        	
        	var row = $('#menu_datagrid').datagrid('getSelected');
        	if(row){
                $.ajax({
                    url :"/intf/commonintf?method=updateEdiRoleMenu"+data_value+"&delete="+isChecked,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    	if(data.code=='200'){
                    		warnAlert("修改成功");
                    	}else{
                    		dealWithErrorMSG(data);
                    	}
                    },
                    error : function(data) {
                    	warnAlert('网络异常，请联系管理员');
                    }
                });	
        	}

        });
        $("input[name='role_upload']").unbind().bind("click",function () {
        	var data_value = $(this).attr('data-value');
        	var isChecked=this.checked;        	
        	var row = $('#menu_datagrid').datagrid('getSelected');
        	if(row){
                $.ajax({
                    url :"/intf/commonintf?method=updateEdiRoleMenu"+data_value+"&upload="+isChecked,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    	if(data.code=='200'){
                    		warnAlert("修改成功");
                    	}else{
                    		dealWithErrorMSG(data);
                    	}
                    },
                    error : function(data) {
                    	warnAlert('网络异常，请联系管理员');
                    }
                });	
        	}

        });
        $("input[name='role_download']").unbind().bind("click",function () {
        	var data_value = $(this).attr('data-value');
        	var isChecked=this.checked;        	
        	var row = $('#menu_datagrid').datagrid('getSelected');
        	if(row){
                $.ajax({
                    url :"/intf/commonintf?method=updateEdiRoleMenu"+data_value+"&download="+isChecked,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    	if(data.code=='200'){
                    		warnAlert("修改成功");
                    	}else{
                    		dealWithErrorMSG(data);
                    	}
                    },
                    error : function(data) {
                    	warnAlert('网络异常，请联系管理员');
                    }
                });	
        	}

        });
    }
    
    $("#"+_datagrid_id).datagrid({
        title:_title,
        queryParams:_query_params,
        url:_url,
        method:"post",
        striped:true,
        dataType: 'json',
        fitColumns:true,
        fit:true,
        singleSelect: true,
        remoteSort: false,
		striped:true,
        columns: [_fields],
        onLoadSuccess:_load_success_function
    });
}

function menuDatagridReload(){
    var _datagrid_id='menu_datagrid'
    datagridReload(_datagrid_id);
}

function roleDatagridReload(){
    var _datagrid_id='role_datagrid'
    datagridReload(_datagrid_id);
}


function roleDialogClose(){
	$('#role_edit_dialog').dialog('close');
}

function roleDialogOpen(role){
	$('#role_edit_form').form('clear');
	if(null==role){
		$('#role_edit_dialog').dialog('open').dialog('setTitle','添加角色');
	}else{
		$('#role_edit_dialog').dialog('open').dialog('setTitle','编辑角色');
		$('#roleId').textbox('setValue',role.roleId);
		$('#roleName').textbox('setValue',role.roleName);
	}
}

function getSelectItemIds(){
	var rows = $('#role_datagrid').datagrid('getSelections');
	var ids = [];
	for(var i=0; i<rows.length; i++){
		 ids.push(rows[i].roleId);
	}
	var idStr=ids.join(',');
	return idStr;
}
function deleteRole(){
	
    swal({title:"您确定要删除此角色吗？",
            text:"删除后将无法恢复，请谨慎操作！",  
            type:"info",  
            showCancelButton:true,  
            confirmButtonColor:"#428bca",  
            confirmButtonText:"确定",  
            cancelButtonText:"取消",  
            closeOnConfirm:false,  
            closeOnCancel:false  
            },  
            function(isConfirm,param,obj){  
                if(isConfirm){  
                	
                	var idStr=getSelectItemIds();
                    $.ajax({
                        url :"/intf/commonintf?method=deleteEdiRole&roleId="+idStr,
                        type : 'post',
                        async : false,
                        success : function(data) {
                        	if(data.code=='200'){
                        		warnAlert("删除成功");
                        		roleDatagridReload();
                        	}else{
                        		dealWithErrorMSG(data);
                        	}
                        },
                        error : function(data) {
                        	alertSystemError('网络异常，请联系管理员');
                        }
                    });
                }  
                else{  
                    swal({title:"已取消",  
                        type:"success"})  
                }  
            }  
            );
	
	

	
}
function roleEditFormSubmit(){
	var _parammap={};
	_parammap['roleId']=$('#roleId').textbox('getValue');
	_parammap['roleName']=$('#roleName').textbox('getValue');

	
    $.ajax({
        url :"/intf/commonintf?method=updateEdiRole",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		roleDialogClose();
        		warnAlert("保存成功");
        		roleDatagridReload();
        	}else{
        		warnAlert(data.msg);
        	}
        },
        error : function(data) {
        	warnAlert('网络异常，请联系管理员');
        }
    });
}

