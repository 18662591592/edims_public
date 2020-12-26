
window.onload = function(){
	$('#loading-mask').fadeOut();
}

var onlyOpenTitle="欢迎使用";//不允许关闭的标签的标题



//初始化左侧
function InitLeftMenu(_menus) {
	$("#nav").accordion({animate:false,fit:true,border:false});
	var selectedPanelname = '';
    $.each(_menus.menus, function(i, n) {
		var menulist ='';
		menulist +='<ul class="navlist">';
        $.each(n.menus, function(j, o) {
			menulist += '<li><div ><a  ref="'+o.menuid+'" href="#" rel="' + o.url + '" ><span  class="icon '+o.icon+'" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div> ';
			if(o.child && o.child.length>0)
			{
				menulist += '<ul class="third_ul">';
				$.each(o.child,function(k,p){
					menulist += '<li><div><a ref="'+p.menuid+'" href="#" rel="' + p.url + '" ><span class="icon '+p.icon+'" >&nbsp;</span><span class="nav">' + p.menuname + '</span></a></div> </li>'
				});
				menulist += '</ul>';
			}
			menulist+='</li>';
        })
		menulist += '</ul>';

		$('#nav').accordion('add', {
            title: n.menuname,
            content: menulist,
			border:false,
            iconCls: 'icon ' + n.icon
        });

		if(i==0)
			selectedPanelname =n.menuname;

    });
	$('#nav').accordion('select',selectedPanelname);
	$('.navlist li a').click(function(){
		var tabTitle = $(this).children('.nav').text();

		var url = $(this).attr("rel");
		var menuid = $(this).attr("ref");
		var icon = $(this).find('.icon').attr('class');

		var third = find(_menus,menuid);
		if(third && third.child && third.child.length>0)
		{
			$('.third_ul').slideUp();

			var ul =$(this).parent().next();
			if(ul.is(":hidden"))
				ul.slideDown();
			else
				ul.slideUp();
		}
		else{
			addTab(tabTitle,url,icon);
			$('.navlist li div').removeClass("selected");
			$(this).parent().addClass("selected");
		}
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});
}


function getIcon(menuid){
	var icon = 'icon ';
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				icon += o.icon;
			}
		 })
	})

	return icon;
}

function find(_menus,menuid){
	var obj=null;
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				obj = o;
			}
		 });
	});

	return obj;
}

function addTab(subtitle,url,icon){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(url),
			closable:true,
			icon:icon
		});
	}else{
		$('#tabs').tabs('select',subtitle);
		var currentTab =$('#tabs').tabs('getSelected');
        var iframe = $(currentTab.panel('options').content);
        var src = iframe.attr('src');
        $('#tabs').tabs('update', {
            tab: currentTab,
            options: {
                content: createFrame(src)
            }
        })
	}
}

function createFrame(url)
{
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}
