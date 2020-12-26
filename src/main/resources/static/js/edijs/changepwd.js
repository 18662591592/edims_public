$.extend($.fn.validatebox.defaults.rules, {  
    /*必须和某个字段相等*/
    equalTo: {
        validator:function(value,param){
            return $(param[0]).val() == value;
        },
        message:'字段不匹配'
    }
           
});
$(document).ready(function() {
	
	
	
});

function changePwd(){
    var _parammap={};
    _parammap['password']=$('#password').val();
    _parammap['repassword']=$('#repassword').val();	
    $.ajax({
        url :"/intf/commonintf?method=changePwd",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
        	if(data.code=='200'){
        		warnAlert("修改成功");
        	}else{
        		dealWithErrorMSG(data);
        	}
        },
        error : function(data) {
        	alertSystemError('网络异常，请联系管理员');
        }
    });
	
}



