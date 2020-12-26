$(document).ready(function() {
	orderDatagridInit();
});


function orderDatagridInit(){
    var order_datagrid=$("#order_datagrid");
    if(null!=order_datagrid){
        var _fields=[
        	{field:'_checkbox',checkbox:true,hidden:false},
			{field:'asnId',title:"ID",width:20,hidden:true},
        	{field:'supplierCode',title:'供应商编码',align:'center',width:20},
            {field:'supplier',title:'供应商名称',width:16,align:'center',
            formatter:function(val){
                if(val){
                  return val.supplierName;
                }
              }},
            {field:'fileName',title:'文件名称',align:'center',width:18},
            {field:'uploadTime',title:'上传时间',align:'center',width:18,formatter:function(value,row,index){                   
                return new Date(value).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " "); 
            }},
            {field:'uploadUser',title:'上传用户',align:'center',width:18,formatter:function(val){
                if(val){
                    return val.userName;
                  }
                }}
        ];
        var _query_params=getQueryParams();
        var _title='ASN信息';
        var _datagrid_id='order_datagrid';
        var _url='intf/commonintf?method=getEdiAsns';
        var _on_row_context_menu=function(e, rowIndex, rowData) { 
            e.preventDefault(); 
//            $(this).datagrid("clearSelections"); 
            $(this).datagrid("selectRow", rowIndex); 
            $('#menu').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
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
            remoteSort: false,
    		pagination:true,
    		rownumbers:true,
    		striped:true,
    		pageSize:20,
    		pageList:[10,20,30,40],
            columns: [_fields],
            onRowContextMenu:_on_row_context_menu
        });
    }
}

function getQueryParams(){
    var _parammap={};
    _parammap['supplierCode']=$('#supplier_combobox').combobox('getValue');
    _parammap['uploadStartTime']=$("#upload_start_datetimebox").datetimebox("getValue");
    _parammap['uploadEndTime']=$("#upload_end_datetimebox").datetimebox("getValue");
    return _parammap;
}


function orderDatagridReload(){
    var _datagrid_id='order_datagrid'
    datagridReload(_datagrid_id);
}


function getSelectItemIds(){
	var rows = $('#order_datagrid').datagrid('getSelections');
	var ids = [];
	for(var i=0; i<rows.length; i++){
		 ids.push(rows[i].asnId);
	}
	var idStr=ids.join(',');
	return idStr;
}

function deleteOrders(){
    swal({title:"您确定要删除此信息吗？",
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
                    url :"/intf/commonintf?method=deleteEdiAsns&asnIds="+idStr,
                    type : 'post',
                    async : false,
                    success : function(data) {
                    		warnAlert("上传成功");
                    		orderDatagridReload();
                    },
                    error : function(data) {
                    	alertSystemError('上传失败');
                    }
                });
            } 
        }  
        );
	
	

	
}
function downloadOrders(){
	var idStr=getSelectItemIds();
	location.href="/intf/downloadAsnZip?asnIds="+idStr;
}
function downloadModel(){
	location.href="/intf/downloadAsnModel";
}
function uploadAsnFiles(){
    $("#asnFileForm").form('submit', {
        type:"post",  //提交方式    
        url:"/intf/uploadAsnFiles", //请求url
            success : function(data) {
            	var dataJson = jQuery.parseJSON(data);
            	if(dataJson.code=='200'){
            		warnAlert("上传成功");
            		orderDatagridReload();	
            	}else{
            		alertSystemError(dataJson.msg);
            	}

            },
            error : function(data) {
            	alertSystemError('网络异常，请联系管理员');
            }
    });
	
}
