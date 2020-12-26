$(document).ready(function() {
	supplierComboboxInit();
	roleComboboxInit();
});


function supplierComboboxInit(){
    var supplier_combobox=$("#supplier_combobox");
    if(null!=supplier_combobox){
    	supplier_combobox.combobox({
            url:'intf/commonintf?method=getEdiSuppliers',
            valueField:'supplierCode',
            textField:'supplierName',
            loadFilter:function(data){
                return data.rows;
            },
            onBeforeLoad: function(param){
            },
            onLoadSuccess:function(data){
            },
            onChange:function(newValue,oldValue){
            }
        });
    }
    
    var edit_supplier_combobox=$("#edit_supplier_combobox");
    if(null!=edit_supplier_combobox){
    	edit_supplier_combobox.combobox({
            url:'intf/commonintf?method=getEdiSuppliers',
            valueField:'supplierCode',
            textField:'supplierName',
            loadFilter:function(data){
                return data.rows;
            },
            onBeforeLoad: function(param){
            },
            onLoadSuccess:function(data){
            },
            onChange:function(newValue,oldValue){
            }
        });
    }
}

function roleComboboxInit(){
    var user_role_combobox=$("#user_role_combobox");
    if(null!=user_role_combobox){
    	user_role_combobox.combobox({
            url:'intf/commonintf?method=getEdiRoles',
            valueField:'roleId',
            textField:'roleName',
            loadFilter:function(data){
                return data.rows;
            },
            onBeforeLoad: function(param){
            },
            onLoadSuccess:function(data){
            },
            onChange:function(newValue,oldValue){
            }
        });
    }
    
    var edit_role_combobox=$("#edit_role_combobox");
    if(null!=edit_role_combobox){
    	edit_role_combobox.combobox({
            url:'intf/commonintf?method=getEdiRoles',
            valueField:'roleId',
            textField:'roleName',
            loadFilter:function(data){
                return data.rows;
            },
            onBeforeLoad: function(param){
            },
            onLoadSuccess:function(data){
            },
            onChange:function(newValue,oldValue){
            }
        });
    }
}


function intervalComboboxInit(){
    var inner_order_scan_interval=$("#inner_order_scan_interval");
    if(null!=inner_order_scan_interval){
    	inner_order_scan_interval.combobox({
            url:'intf/commonintf?method=getEdiRoles',
            valueField:'roleId',
            textField:'roleName',
            loadFilter:function(data){
                return data.rows;
            },
            onBeforeLoad: function(param){
            },
            onLoadSuccess:function(data){
            },
            onChange:function(newValue,oldValue){
            }
        });
    }
}