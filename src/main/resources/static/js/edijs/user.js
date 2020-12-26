$(document).ready(function() {
	userDatagridInit();
});


function userDatagridInit(){
    var user_datagrid=$("#users_datagrid");
    if(null!=user_datagrid){
        var _fields=[
        	{field:'userId',title:"userId",width:1,hidden:true},
        	{field:'roleId',title:"roleId",width:1,hidden:true},
        	{field:'userName',title:'用户名',align:'center',width:10},
            {field:'realName',title:'姓名',width:10,align:'center'},
            {field:'role',title:'用户角色',align:'center',width:10,
                formatter:function(val){
                    if(val){
                      return val.roleName;
                    }
                  }},
            {field:'supplierCode',title:'供应商编码',align:'center',width:10},
            {field:'supplier',title:'供应商名称',align:'center',width:10,
                      formatter:function(val){
                          if(val){
                            return val.supplierName;
                          }
                        }},
            {field:'isEmailOn',title:'异常邮件发送',align:'center',width:10,
                            formatter:function(val){
                                if(val){
                                	return '是';
                                }else{
                                	return '否';
                                }
                              }
                        },
            {field:'email',title:'邮箱',align:'center',width:10}
        ];
        var _query_params=getQueryParams();
        var _title='用户信息';
        var _datagrid_id='users_datagrid';
        var _url='intf/commonintf?method=getEdiUsers';
        var _double_click_function=function(index,field,value){
        	var selectedRow = $(this).datagrid('getSelected');
        	userDialogOpen(selectedRow);
        };
        
        var _on_row_context_menu=function(e, rowIndex, rowData) { 
            e.preventDefault(); 
//            $(this).datagrid("clearSelections"); 
            $(this).datagrid("selectRow", rowIndex); 
            $('#menu').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
        }
        ediDatagridInit(_datagrid_id,_query_params,_title,_url,_fields,null,_double_click_function,null,_on_row_context_menu);
    }
}

function getQueryParams(){
    var _parammap={};
    _parammap['supplierCode']=$('#supplier_combobox').combobox('getValue');
    _parammap['realName']=$('#user_name_textbox').textbox('getValue');
    _parammap['isEmailOn']=$('#isemailon_combobox').combobox('getValue');
    _parammap['roleId']=$('#user_role_combobox').combobox('getValue');
    return _parammap;
}


function userDatagridReload(){
    var _datagrid_id='users_datagrid'
    datagridReload(_datagrid_id);
}



function userDialogClose(){
	$('#user_edit_dialog').dialog('close');
}

function userDialogOpen(user){
	$('#user_edit_form').form('clear');
	if(null==user){
		$('#passWord').textbox('enable');
		$('#user_edit_dialog').dialog('open').dialog('setTitle','添加用户');
		
	}else{
		$('#passWord').textbox('disable');
		$('#userId').textbox('setValue',user.userId);
		$('#userName').textbox('setValue',user.userName);
		$('#realName').textbox('setValue',user.realName);
		$('#email').textbox('setValue',user.email);
		$("#edit_supplier_combobox").combobox("setValue", user.supplierCode);
		$("#edit_isemailon_combobox").combobox("setValue", user.isEmailOn);
		$("#edit_role_combobox").combobox("setValue", user.roleId);
		$('#user_edit_dialog').dialog('open').dialog('setTitle','编辑用户');

	}
}
function getSelectItemIds(){
	var rows = $('#users_datagrid').datagrid('getSelections');
	var ids = [];
	for(var i=0; i<rows.length; i++){
		 ids.push(rows[i].userId);
	}
	var idStr=ids.join(',');
	return idStr;
}
function deleteUser(){
    swal({title:"您确定要删除此用户吗？",
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
                    url :"/intf/commonintf?method=deleteEdiUser&userId="+idStr,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    	if(data.code=='200'){
                    		warnAlert("删除成功");
                    		userDatagridReload();
                    	}else{
                    		dealWithErrorMSG(data);
                    	}
                    },
                    error : function(data) {
                    	alertSystemError('网络异常，请联系管理员');
                    }
                });
            } 
        }  
        );
	
	

	
}
function userEditFormSubmit(){
	var _parammap={};
	_parammap['userId']=$('#userId').textbox('getValue');
	_parammap['userName']=$('#userName').textbox('getValue');
	_parammap['realName']=$('#realName').textbox('getValue');
	_parammap['email']=$('#email').textbox('getValue');
	_parammap['passWord']=$('#passWord').textbox('getValue');
	_parammap['roleId']=$('#edit_role_combobox').combobox('getValue');	
	_parammap['isEmailOn']=$('#edit_isemailon_combobox').combobox('getValue');
	_parammap['supplierCode']=$('#edit_supplier_combobox').combobox('getValue');	
	
    $.ajax({
        url :"/intf/commonintf?method=updateEdiUser",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		userDialogClose();
        		warnAlert("保存成功");
        		userDatagridReload();
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	alertSystemError('网络异常，请联系管理员');
        }
    });
}

