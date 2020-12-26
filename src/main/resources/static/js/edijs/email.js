$(document).ready(function() {
	initEmailConfig();
});

function initEmailConfig(){
    $.ajax({
        url :"/intf/commonintf?method=getSystemConfigs&type=1",
        type : 'post',
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		var rows=data.rows;
        		if(rows){
        			for(var i=0;i<rows.length;i++){
        				var sysconf=rows[i];
        				if('email_scan_interval'==sysconf.configKey){
        					$('#email_scan_interval').combobox('setValue',sysconf.configValue);
        				}
        				if('error_email_interval'==sysconf.configKey){
        					$('#error_email_interval').combobox('setValue',sysconf.configValue);
        				}
        				if('emailhost'==sysconf.configKey){
        					$('#emailhost').textbox('setValue',sysconf.configValue);
        				}
        				if('emailport'==sysconf.configKey){
        					$('#emailport').textbox('setValue',sysconf.configValue);
        				}
        				if('emailaddr'==sysconf.configKey){
        					$('#emailaddr').textbox('setValue',sysconf.configValue);
        				}
        				if('emailpwd'==sysconf.configKey){
        					$('#emailpwd').textbox('setValue',sysconf.configValue);
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


function getQueryParams(){
    var _parammap={};
    _parammap['emailhost']=$('#emailhost').textbox('getValue');
    _parammap['emailport']=$('#emailport').textbox('getValue');
    _parammap['emailaddr']=$('#emailaddr').textbox('getValue');
    _parammap['emailpwd']=$('#emailpwd').textbox('getValue');
    _parammap['email_scan_interval']=$('#email_scan_interval').combobox('getValue');
    return _parammap;
}

function testEmailConfig(){
	var _parammap=getQueryParams();	
    $.ajax({
        url :"/intf/commonintf?method=testEmail",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		warnAlert("邮箱配置可用");
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	alertSystemError('网络异常，请联系管理员');
        }
    });
}
function updateEmailConfig(){
	var _parammap=getQueryParams();	
    $.ajax({
        url :"/intf/commonintf?method=updateEmailConfig",
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

