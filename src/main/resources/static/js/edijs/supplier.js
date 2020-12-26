$(document).ready(function() {
	supplierDatagridInit();
});


function supplierDatagridInit(){
    var suppliers_datagrid=$("#suppliers_datagrid");
    if(null!=suppliers_datagrid){
        var _fields=[
        	{field:'supplierId',title:"supplierId",width:1,hidden:true},
        	{field:'supplierCode',title:'供应商编码',align:'center',width:20},
            {field:'supplierName',title:'供应商名称',width:16,align:'center'},
            {field:'supplierAddr',title:'地址',align:'center',width:18},
            {field:'linkmanName',title:'联系人',align:'center',width:18},
            {field:'linkmanMobile',title:'联系电话',align:'center',width:18}
        ];
        var _query_params=getQueryParams();
        var _title='供应商信息';
        var _datagrid_id='suppliers_datagrid';
        var _url='intf/commonintf?method=getEdiSuppliers';
        var _double_click_function=function(index,field,value){
        	var selectedRow = $(this).datagrid('getSelected');
        	supplierDialogOpen(selectedRow);
        };
        var _on_row_context_menu=function(e, rowIndex, rowData) { 
            e.preventDefault(); 
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
    _parammap['supplierName']=$('#supplier_combobox').combobox('getText');
    _parammap['linkmanName']=$('#linkman_name_textbox').textbox('getValue');
    return _parammap;
}


function supplierDatagridReload(){
    var _datagrid_id='suppliers_datagrid'
    datagridReload(_datagrid_id);
}



function supplierDialogClose(){
	$('#supplier_edit_dialog').dialog('close');
}

function supplierDialogOpen(supplier){
	$('#supplier_edit_form').form('clear');
	if(null==supplier){
		$('#supplierCode').textbox('enable');
		$('#supplier_edit_dialog').dialog('open').dialog('setTitle','添加供应商');
	}else{
		$('#supplierCode').textbox('disable');
		$('#supplier_edit_dialog').dialog('open').dialog('setTitle','编辑供应商');
		$('#supplierId').textbox('setValue',supplier.supplierId);
		$('#supplierCode').textbox('setValue',supplier.supplierCode);
		$('#supplierName').textbox('setValue',supplier.supplierName);
		$('#supplierAddr').textbox('setValue',supplier.supplierAddr);
		$('#linkmanName').textbox('setValue',supplier.linkmanName);
		$('#linkmanMobile').textbox('setValue',supplier.linkmanMobile);
	}
}

function supplierEditFormSubmit(){
	var _parammap={};
	_parammap['supplierId']=$('#supplierId').textbox('getValue');
	_parammap['supplierCode']=$('#supplierCode').textbox('getValue');
	_parammap['supplierName']=$('#supplierName').textbox('getValue');
	_parammap['supplierAddr']=$('#supplierAddr').textbox('getValue');
	_parammap['linkmanName']=$('#linkmanName').textbox('getValue');
	_parammap['linkmanMobile']=$('#linkmanMobile').textbox('getValue');	
	
    $.ajax({
        url :"/intf/commonintf?method=updateEdiSupplier",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		supplierDialogClose();
        		warnAlert("保存成功");
        		supplierDatagridReload();
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	warnAlert('网络异常，请联系管理员');
        }
    });
}
function getSelectItemIds(){
	var rows = $('#suppliers_datagrid').datagrid('getSelections');
	var ids = [];
	for(var i=0; i<rows.length; i++){
		 ids.push(rows[i].supplierCode);
	}
	var idStr=ids.join(',');
	return idStr;
}
function deleteSupplier(){
    swal({title:"您确定要删除此供应商吗？",
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
                    url :"/intf/commonintf?method=deleteEdiSupplier&supplierCode="+idStr,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    	if(data.code=='200'){
                    		warnAlert("删除成功");
                    		supplierDatagridReload();
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
