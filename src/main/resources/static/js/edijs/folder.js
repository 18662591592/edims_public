$(document).ready(function() {
	initFolderConfig();
});

function initFolderConfig(){
    $.ajax({
        url :"/intf/commonintf?method=getSystemConfigs&type=2",
        type : 'post',
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		var rows=data.rows;
        		if(rows){
        			for(var i=0;i<rows.length;i++){
        				var sysconf=rows[i];
        				if('order_scan_path'==sysconf.configKey){
        					$('#order_scan_path').textbox('setValue',sysconf.configValue);
        				}
        				if('order_scan_interval'==sysconf.configKey){
        					$('#order_scan_interval').combobox('setValue',sysconf.configValue);
        				}
        				if('inner_order_scan_path'==sysconf.configKey){
        					$('#inner_order_scan_path').textbox('setValue',sysconf.configValue);
        				}
        				if('inner_order_scan_interval'==sysconf.configKey){
        					$('#inner_order_scan_interval').combobox('setValue',sysconf.configValue);
        				}
        				if('asn_scan_path'==sysconf.configKey){
        					$('#asn_scan_path').textbox('setValue',sysconf.configValue);
        				}
        				if('asn_scan_interval'==sysconf.configKey){
        					$('#asn_scan_interval').combobox('setValue',sysconf.configValue);
        				}
        			}
        		}
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	alertSystemError('网络异常，请联系管理员');
        }
    });
}


function getOrderQueryParams(){
    var _parammap={};
    _parammap['order_scan_path']=$('#order_scan_path').textbox('getValue');
    _parammap['order_scan_interval']=$('#order_scan_interval').combobox('getValue');
    return _parammap;
}
function getInnerOrderQueryParams(){
    var _parammap={};
    _parammap['inner_order_scan_path']=$('#inner_order_scan_path').textbox('getValue');
    _parammap['inner_order_scan_interval']=$('#inner_order_scan_interval').combobox('getValue');
    return _parammap;
}
function getAsnQueryParams(){
    var _parammap={};
    _parammap['asn_scan_path']=$('#asn_scan_path').textbox('getValue');
    _parammap['asn_scan_interval']=$('#asn_scan_interval').combobox('getValue');
    return _parammap;
}

function updateOrderConfig(){
	var _parammap=getOrderQueryParams();	
    $.ajax({
        url :"/intf/commonintf?method=updateOrderConfig",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		warnAlert("保存成功");
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	alertSystemError('网络异常，请联系管理员');
        }
    });
}

function updateInnerOrderConfig(){
	var _parammap=getInnerOrderQueryParams();	
    $.ajax({
        url :"/intf/commonintf?method=updateInnerOrderConfig",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		warnAlert("保存成功");
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	alertSystemError('网络异常，请联系管理员');
        }
    });
}
function updateAsnConfig(){
	var _parammap=getAsnQueryParams();	
    $.ajax({
        url :"/intf/commonintf?method=updateAsnConfig",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		warnAlert("保存成功");
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	alertSystemError('网络异常，请联系管理员');
        }
    });
}

function testOrderConfig(){
    var _parammap={};
    _parammap['path']=$('#order_scan_path').textbox('getValue');
    testScanPath(_parammap);
}

function testInnerOrderConfig(){
    var _parammap={};
    _parammap['path']=$('#inner_order_scan_path').textbox('getValue');	
    testScanPath(_parammap);

}
function testAsnConfig(){
    var _parammap={};
    _parammap['path']=$('#asn_scan_path').textbox('getValue');
    testScanPath(_parammap);

}

function testScanPath(_parammap){
    $.ajax({
        url :"/intf/commonintf?method=testScanPath",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		warnAlert("文件夹路径可用");
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	alertSystemError('网络异常，请联系管理员');
        }
    });
}
