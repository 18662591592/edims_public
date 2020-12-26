
$(document).ready(function() {
    areaComboboxInit('count_area_rank_',2,'CN',true);

    $('#conditionTabs').tabs({
        border:false,
        onSelect:function(title,index){
            invokingRecordsRankDatagridReload()
        }
    });


//	$('input:radio[name="chartType"]').change(function(){
//		 invokingRecordsRankDatagridReload();
//	});

});


function mapInit(mapurl,dataMap){
    $.getJSON(mapurl, function(mapdata) {
        var data = [];
        // 随机数据
        Highcharts.each(mapdata.features, function(md, index) {

            var dataInfo=dataMap[md.properties.fullname];
            if(dataInfo!=undefined){
                dataInfo.name=md.properties.name;
                data.push(dataInfo);
            }else{
                var tmp = {
                    name: md.properties.name,
                    value: 0,
                    alpha_value: 0,
                    beta_value: 0,
                    total_num: 0
                };
                data.push(tmp);
            }

        });
        map = new Highcharts.Map('container1', {
            chart: {
                events: {
                    drillup: function() {
                        map.setTitle({
                            text: '中国'
                        });
                    }
                }
            },
            title: {
                text: '中国地贫人口数据库'
            },
            mapNavigation: {
                enabled: true,
                buttonOptions: {
                    verticalAlign: 'bottom'
                }
            },
            tooltip: {
                useHTML: true,
                headerFormat: '<table><tr><td>{point.name}</td></tr>',
                pointFormat: '<tr><td>名称</td><td>{point.properties.fullname}</td></tr>' +
                '<tr><td>受检人数</td><td>{point.total_num}</td></tr>' +
                '<tr><td>地贫检出率</td><td>{point.value}%</td></tr>' +
                '<tr><td>α检出率</td><td>{point.alpha_value}%</td></tr>' +
                '<tr><td>β检出率</td><td>{point.beta_value}%</td></tr>' ,
                footerFormat: '</table>'
            },
            colorAxis: {
                min: 0,
                minColor: '#fff',
                maxColor: '#006cee',
                labels:{
                    style:{
                        "color":"red","fontWeight":"bold"
                    }
                }
            },
            series: [{
                data: data,
                mapData: mapdata,
                joinBy: 'name',
                name: '中国地图',
                states: {
                    hover: {
                        color: '#a4edba'
                    }
                }
            }]
        });
    });
}

function dataGridInit(_parammap,_url){
    var fields=[{field:'town',title:'乡镇',align:'center',width:20},
        {field:'sex',title:'性别',width:16,align:'center',add:false,formatter:function(value,rowData,rowIndex){
//	            	 if(value=='-'){
//	            		 return '-';
//	            	 }else if(value=='1'){
//	            		 return '男';
//	            	 }else if(value=='2'){
//	            		 return '女';
//	            	 }else {
//	            		 return '其他';
//	            	 }
            return value;
        }},
        {field:'nation',title:'民族',align:'center',width:18,edit:false,add:false},
        {field:'totalNum',title:'检测人数',align:'center',width:18,edit:false,add:false},
        {field:'positiveNum',title:'地贫人数',align:'center',width:18,edit:false,add:false},
        {field:'positivePer',title:'检出率',align:'center',width:18,edit:false,add:false,formatter:function(value,rowData,rowIndex){

            return Math.round((rowData.positiveNum/rowData.totalNum)*10000)/100+"%";
        }},
        {field:'alphaPositiveNum',title:'α地贫人数',align:'center',width:20,edit:false,add:false,formatter:function(value,rowData,rowIndex){
            return (rowData.positiveNum-rowData.betaPositiveNum);
        }},
        {field:'alphaPositivePer',title:'检出率',align:'center',width:18,edit:false,add:false,formatter:function(value,rowData,rowIndex){
            return Math.round(((rowData.positiveNum-rowData.betaPositiveNum)/rowData.totalNum)*10000)/100+"%";
        }},
        {field:'betaPositiveNum',title:'β地贫人数',align:'center',width:20,edit:false,add:false,formatter:function(value,rowData,rowIndex){
            return (rowData.positiveNum-rowData.alphaPositiveNum);
        }},
        {field:'betaPositivePer',title:'检出率',align:'center',width:18,edit:false,add:false,formatter:function(value,rowData,rowIndex){
            return Math.round(((rowData.positiveNum-rowData.alphaPositiveNum)/rowData.totalNum)*10000)/100+"%";
        }},
        {field:'abPositiveNum',title:'α合并β地贫人数',align:'center',width:28,edit:false,add:false,formatter:function(value,rowData,rowIndex){
            return rowData.betaPositiveNum+rowData.alphaPositiveNum-rowData.positiveNum;
        }},

        {field:'abPositivePer',title:'检出率',align:'center',width:18,edit:false,add:false,formatter:function(value,rowData,rowIndex){
            return Math.round(((rowData.betaPositiveNum+rowData.alphaPositiveNum-rowData.positiveNum)/rowData.totalNum)*10000)/100+"%";;
        }},
        {field:'hmlbnPositiveNum',title:'异常血红蛋白人数',align:'center',width:30},
        {field:'hmlbnPositivePer',title:'检出率',align:'center',width:18,formatter:function(value,rowData,rowIndex){
            return Math.round((rowData.hmlbnPositiveNum/rowData.totalNum)*10000)/100+"%";
        }}

    ];
    createDatagrid('invoking_records_rank_datagrid',"",_url,fields,null,"auto",_parammap);
}

//地区联动加载
function areaComboboxInit(areacbname,rank,parent_code,isFirstInit){
    var areacb=$("#"+areacbname+rank);
    if(null!=areacb){
        areacb.combobox({
            url:'getAreas.do?areaParentCode='+parent_code+'&areaRank='+rank,
            valueField:'areaCode',
            textField:'areaName',
            loadFilter:function(data){
                return data.rows;
            },
            onBeforeLoad: function(param){
            },
            onLoadSuccess:function(data){
                var object = data.rows[0];
                if(rank==2){
                    object = data.rows[12];
//					$('#count_area_rank_2').combobox('enable');
                }
                if(rank==3){
                    object = data.rows[9];
//					$('#count_area_rank_3').combobox('enable');
                }
                if(rank==4){
                    object = data.rows[1];
//					$('#count_area_rank_4').combobox('enable');
                }
                if(object&&object.areaCode){
                    var _areaCode=object.areaCode;
                    var _areaCombobox=$("#"+areacbname+rank);
                    _areaCombobox.combobox('setValue',_areaCode);
                    areaComboboxInit(areacbname,rank+1,object.areaCode,isFirstInit);
                }
                if(rank==4&&isFirstInit){
                    var _url=path + "/getDatagridResponseBean.do?";
                    var _parammap=getQueryParams();
                    dataGridInit(_parammap,_url);
                }
            },
            onChange:function(newValue,oldValue){
                areaComboboxInit(areacbname,rank+1,newValue,false);
            }
        });
    }
}



function createPieCharts(_chart_id,pieData,title){
    $('#'+_chart_id).highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: title
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.point.name + '</b><br/><b>人数</b>:'+this.point.y+'<br/><b>占比</b>: ' + Highcharts.numberFormat(this.percentage, 2) + ' %';
            }
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                distance: -50,//通过设置这个属性，将每个小饼图的显示名称和每个饼图重叠
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: '人口比率',
            data: pieData
        }]
    });
}



function create3DRankCharts(rankCahrtName,xdata,_series,_title){
    $('#'+rankCahrtName).highcharts({
        chart: {
            type: 'column',
            margin: 75,
            options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 110
            }
        },
        plotOptions: {
            column: {
                depth: 40,
                stacking: true,
                grouping: false,
                groupZPadding: 10
            }
        },
        series: [{
            data: [1, 2, 4, 3, 2, 4],
            stack: 0
        }, {
            data: [5, 6, 3, 4, 1, 2],
            stack: 0
        }, {
            data: [7, 9, 8, 7, 5, 8],
            stack: 1
        }]
    });
}
function createRankCharts(rankCahrtName,xdata,_series,_title,_ytitle){
    var _series_data=_series[0].data;
    var _series_data_length=_series_data.length;
    for(var j=0;j<_series_data.length-1;j++){
        for(var i=0;i<_series_data.length-1-j;i++){
            if(_series_data[i]<_series_data[i+1]){
                var temp = _series_data[i];
                var tempx=xdata[i];
                _series_data[i] = _series_data[i+1];
                _series_data[i+1] = temp;
                xdata[i] = xdata[i+1];
                xdata[i+1] = tempx;
            }
        }
    }

//    while (_series_data_length>10){
//    	_series[0].data.pop();
//    	xdata.pop();
//    }
    $('#'+rankCahrtName).highcharts({
        chart: {
            type: 'column'
        },
        credits: {
            enabled: false   //右下角不显示LOGO
        },
        title: {
            text: _title
        },
        xAxis: {
            categories: xdata
        },
        yAxis: {
            min: 0,
            title: {
                text: _ytitle
            }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            formatter: function() {
                return '<b>'+ this.x +'</b><br>'+
                    this.series.name +': '+ this.y +'%<br>';
            }
        },
        plotOptions: {
            series: {
                borderWidth: 0,
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.2f}'
                }
            }
        },
        series:_series
    });
}
//用于生成双击乡镇后的显示的柱状图
function createRankChartsToDblClick(rankCahrtName,xdata,_series,_title,_ytitle,belongArea,gctype){
    var _series_data=_series[0].data;
    var _series_data_length=_series_data.length;
    for(var j=0;j<_series_data.length-1;j++){
        for(var i=0;i<_series_data.length-1-j;i++){
            if(_series_data[i]<_series_data[i+1]){
                var temp = _series_data[i];
                var tempx=xdata[i];
                _series_data[i] = _series_data[i+1];
                _series_data[i+1] = temp;
                xdata[i] = xdata[i+1];
                xdata[i+1] = tempx;
            }
        }
    }

//    while (_series_data_length>10){
//    	_series[0].data.pop();
//    	xdata.pop();
//    }
    $('#'+rankCahrtName).highcharts({
        chart: {
            type: 'column'
        },
        credits: {
            enabled: false   //右下角不显示LOGO
        },
        title: {
            text: _title
        },
        xAxis: {
            categories: xdata
        },
        yAxis: {
            min: 0,
            title: {
                text: _ytitle
            }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            formatter: function() {
                return '<b>'+ this.x +'</b><br>'+
                    this.series.name +': '+ this.y +'%<br>';
            }
        },
        plotOptions: {
            //当点击树形图时，会有相对应的操作
            column: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function(e) {

                            var genceType = e.point.category;

                            $("#infoDialog").dialog({
                                height:'50%',
                                width:'50%',
                                title: "基因型详细信息",
                                maximizable:true
                            });

                            if(gctype=="β"){
                                $("#InfoList").datagrid({
                                    url:path + "/BDetailInfo.do",
                                    queryParams: {
                                        result: genceType,
                                        town:belongArea
                                    },
                                    columns:[[{
                                        field:"sampleName",
                                        align:"center",
                                        title:"姓名"
                                    },{
                                        field:"aresult",
                                        align:"center",
                                        title:"α基因"
                                    },{
                                        field:"bresult",
                                        align:"center",
                                        title:"β基因"
                                    },{
                                        field:"sex",
                                        align:"center",
                                        title:"性别"
                                    },{
                                        field:"id",
                                        align:"center",
                                        title:"身份证"
                                    },{
                                        field:"nation",
                                        align:"center",
                                        title:"民族"
                                    },{
                                        field:"address",
                                        align:"center",
                                        title:"乡镇"
                                    },{
                                        field:"phoneNum",
                                        align:"center",
                                        title:"联系电话"
                                    }]],

                                    singleSelect:true,
                                    // 行号
                                    rownumbers: true,
                                    // 是否可以调整列宽 left right both
                                    resizeHandle: "right",
                                    fitColumns: true,
                                    // 为true在同一行显示，没有间断的英文字母或数字
                                    nowrap: true,
                                    // 唯一标识字段
                                    idField: "sid",
                                    // 是否启用多列排序
                                    multiSort: true,
                                    // 是否允许使用列排序
                                    sortable: true,
                                    // 排序的列
                                    sortName: "id",
                                    // 排序的规则
                                    sortOrder: "asc",
                                    // 加载数据时的提示消息
                                    loadMsg: "正在拼命为您加载数据...",
                                    //设置为true将交替显示行背景
                                    striped: true
                                });
                            }else if(gctype=="α"){
                                $("#InfoList").datagrid({
                                    url:path + "/ADetailInfo.do",
                                    queryParams: {
                                        result: genceType,
                                        town:belongArea
                                    },
                                    columns:[[{
                                        field:"sampleName",
                                        align:"center",
                                        title:"姓名"
                                    },{
                                        field:"aresult",
                                        align:"center",
                                        title:"α基因"
                                    },{
                                        field:"bresult",
                                        align:"center",
                                        title:"β基因"
                                    },{
                                        field:"sex",
                                        align:"center",
                                        title:"性别"
                                    },{
                                        field:"id",
                                        align:"center",
                                        title:"身份证"
                                    },{
                                        field:"nation",
                                        align:"center",
                                        title:"民族"
                                    },{
                                        field:"address",
                                        align:"center",
                                        title:"乡镇"
                                    },{
                                        field:"phoneNum",
                                        align:"center",
                                        title:"联系电话"
                                    }]],

                                    singleSelect:true,
                                    // 行号
                                    rownumbers: true,
                                    // 是否可以调整列宽 left right both
                                    resizeHandle: "right",
                                    fitColumns: true,
                                    // 为true在同一行显示，没有间断的英文字母或数字
                                    nowrap: true,
                                    // 唯一标识字段
                                    idField: "sid",
                                    // 是否启用多列排序
                                    multiSort: true,
                                    // 是否允许使用列排序
                                    sortable: true,
                                    // 排序的列
                                    sortName: "id",
                                    // 排序的规则
                                    sortOrder: "asc",
                                    // 加载数据时的提示消息
                                    loadMsg: "正在拼命为您加载数据...",
                                    //设置为true将交替显示行背景
                                    striped: true

                                });
                            }






                        }
                    }
                }

            },
            series: {
                borderWidth: 0,
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.2f}'
                }
            }
        },
        series:_series
    });
}
var provinceCodes={
    '天津市':'tianjin',
    '河北省':'hebei',
    '辽宁省':'liaoning',
    '吉林省':'jilin',
    '黑龙江省':'heilongjiang',
    '上海市':'shanghai',
    '江苏省':'jiangsu',
    '浙江省':'zhejiang',
    '安徽省':'anhui',
    '河南省':'henan',
    '湖南省':'hunan',
    '广东省':'guangdong',
    '广西壮族自治区':'guangxi',
    '西藏自治区':'xizang',
    '陕西省':'shanxi2',
    '甘肃省':'gansu',
    '宁夏回族自治区':'ningxia',
    '新疆维吾尔自治区':'xinjiang',
    '北京市':'beijing',
    '山西省':'shanxi',
    '内蒙古自治区':'neimenggu',
    '福建省':'fujian',
    '江西省':'jiangxi',
    '山东省':'shandong',
    '湖北省':'hebei',
    '四川省':'sichuan',
    '重庆市':'chongqing',
    '海南省':'hainan',
    '贵州省':'guizhou',
    '云南省':'yunnan',
    '青海省':'qinghai',
    '台湾省':'taiwan',
    '香港特别行政区':'xianggang',
    '澳门特别行政区':'aomen'
};
function initPie(rowIndex,rowData){
    var _parammap=getQueryParams();
    var pieName='';
    _parammap['groupbySex']='1';
    if(rowIndex==0){
        _parammap['groupbyNextRegion']='0';
        _parammap['groupbyNation']='0';
        pieName='巴马县';
    }else{
        var groupbyNextRegion=_parammap['groupbyNextRegion'];
        var groupbyNation=_parammap['groupbyNation'];
        if('1'==groupbyNextRegion){
            _parammap['townFilter']=rowData.town;
            pieName=pieName+rowData.town;
        }
        if('1'==groupbyNation){
            _parammap['nationFilter']=rowData.nation;
            pieName=pieName+rowData.nation+'族';
        }
    }

    $.ajax({
        url :path + "/getSexPercents.do?",
        type : 'post',
        data:_parammap,
        async : false,
        success : function(data) {
            var dataObj = JSON.parse(data);
            var rows=dataObj.rows;
            var _pie_series=[];
            for(var _index in rows){
                var rowdata=rows[_index];
                var totalNum=rowdata.totalNum;
                var positiveNum=rowdata.positiveNum;
                var betaPositiveNum=rowdata.betaPositiveNum;
                var alphaPositiveNum=rowdata.alphaPositiveNum;
                var abPositiveNum=(betaPositiveNum+alphaPositiveNum)-positiveNum;//双重地贫人口
                var sex=rowdata.sex;
                if(sex=='男'){
                    sex='男性';
                }else if(sex=='女'){
                    sex='女性';
                }else if(sex=='未知'){
                    sex='性别未知';
                }
                var onlyAlphaPositiveNum=[sex+"纯α地贫人口",alphaPositiveNum-abPositiveNum];
                var onlyBetaPositiveNum=[sex+"纯β地贫人口",betaPositiveNum-abPositiveNum];
                var abPositiveNumData=[sex+"α合并β地贫人口",abPositiveNum];
                var normalNumData=[sex+"正常人口",rowdata.totalNum-rowdata.positiveNum];
                _pie_series.push(onlyAlphaPositiveNum);
                _pie_series.push(onlyBetaPositiveNum);
                _pie_series.push(abPositiveNumData);
                _pie_series.push(normalNumData);
            }
            createPieCharts("positivePercentPie",_pie_series,pieName+"阳性人口比例图");
        }
    });
}


function createDatagrid(_datagrid_id,_datagrid_title,_datagrid_url,_datagrid_fields,_toolbar_options,_height,_query_params){
    var betaArr  = new Array();
    var bxdata  = new Array();
    var alphaArr  = new Array();
    var axdata  = new Array();
    var _β_series=[];
    var _α_series=[];
    $("#"+_datagrid_id).datagrid({
        title:_datagrid_title,
        queryParams:_query_params,
        url:_datagrid_url,
        method:"post",
        rownumbers:true,
        striped:true,
        dataType: 'json',
        height:'100%',
        fitColumns:true,
        fit:false,
        singleSelect: true,
        remoteSort: false,
        columns: [_datagrid_fields],
        onClickRow:function(rowIndex,rowData){
            initPie(rowIndex,rowData);

        },
        //双击乡镇下面的内容，可以显示详细的该地区的两种地贫基因型下的所有基因型
        //下标索引，列名，该列数据
        onDblClickCell:function(index,field,value){

            //如果双击的是巴马县总计（因为数据库没有这个字段，所以不查询）
            if(value=="巴马县总计"){

            }else{
                //如果双击的是乡镇这一列，则弹出一个树形图dialog
                if (field == "town") {
                    $("#userUpdateDialog").dialog({
                        height: '50%',
                        width: '70%',
                        title: "乡镇信息详情",
                        maximizable:true,
                        onResize:function(){
                            createRankChartsToDblClick("clickalpha_rank",axdata,_α_series,value+" α基因型统计",'α基因型占比率(%)',value,"α");
                            createRankChartsToDblClick("clickbeta_rank",bxdata,_β_series,value+" β基因型统计",'β基因型占比率(%)',value,"β");
                        }
                    });
                    //$('#userUpdateDialog').Dialog('show');
                }
            }

            //查询巴XX地区所有的Beta基因型
            $.ajax({
                url: path+"/getSomeWhereAllBetaGene.do?town="+value,
                type: "post",
                dataType: "json",
                async:true,
                success: function(data){

                    data.forEach(function(value,index,array){
                        genoType= (value.genoType).replace(/\n/g,"<br>");
                        betaArr.push(Math.round((JSON.stringify(value.personNum)/JSON.stringify(value.total))*10000)/100);
                        bxdata.push(genoType);

                    });

                    _β_series= [{name: value+" β基因型",color: '#0099CC',data: betaArr}];
                    //2018/5/29 18:31，树状图的显示效果,第一个参数：元素id名，第二个：每个树状图的对应的含义,对应的地区乡镇(即X坐标)，第三个：计算好的百分比，第四个：上方标题，第五个：（即Y坐标）
                    createRankChartsToDblClick("clickbeta_rank",bxdata,_β_series,value+" β基因型统计",'β基因型占比率(%)',value,"β");
                    betaArr=[];
                }

            });

            //查询巴XX地区所有的Alpha基因型
            $.ajax({
                type: "post",
                url: path+"/getSomeWhereAllAlphaGene.do?town="+value,
                dataType: "json",
                async:true,
                success: function(data){

                    data.forEach(function(value,index,array){

                        genoType= (value.genoType).replace(/\n/g,"<br>");
                        alphaArr.push(Math.round((JSON.stringify(value.personNum)/JSON.stringify(value.total))*10000)/100);
                        axdata.push(genoType);
                    });

                    _α_series= [{name: value+"α基因型",color: '#0099CC',data: alphaArr}];
                    //2018/5/29 18:31，树状图的显示效果,第一个参数：元素id名，第二个：每个树状图的对应的含义,对应的地区乡镇(即X坐标)，第三个：计算好的百分比，第四个：上方标题，第五个：（即Y坐标）
                    createRankChartsToDblClick("clickalpha_rank",axdata,_α_series,value+" α基因型统计",'α基因型占比率(%)',value,"α");
                    alphaArr=[];
                }

            });
        },
        onLoadSuccess:function(datas){

            var selectTab=_query_params.selectTab;
//
            var axdata=[];
            var bxdata=[];
            var totalNumArr=[];
            var positiveNumArr=[];
            var alphaPositiveNumArr=[];
            var betaPositiveNumArr=[];
            var _pie_series=[];

            var alphaPositivePercentArr=[];
            var betaPositivePercentArr=[];


            var _title='地贫人口排名';
            var nationShow=true;
            var sexShow=true;
            var cityShow=true;
            var countyShow=true;
            var townShow=true;
            var firstValidate=true;
            var datagridRows=$('#invoking_records_rank_datagrid').datagrid('getRows');
            initPie(0,datagridRows[0]);
            var dataMap ={};
            for(var _index in datagridRows){
                if(_index==0){
                    continue;
                }
                var data=datagridRows[_index];
                var province=data.province;

                var addr_name="";
                if(province=='-'){
                    province="";
                }else if(province==""||province=='null'||province==undefined){
                    addr_name='其他';
                }else{
                    addr_name=province;
                }
                var city=data.city;
                if(city=='-'){
                    if(_index!=0&&firstValidate){
                        cityShow=false;
                    }
                    city="";
                }else if(city==""||city=='null'||city==undefined){
                    addr_name='其他';
                }else{
                    addr_name=city;
                }
                var county=data.county;
                if(county=='-'){
                    if(_index!=0&&firstValidate){
                        countyShow=false;
                    }
                    county="";
                }else{

                    addr_name=county;
                }

                var town=data.town;
                if(town=='-'){
                    if(_index!=0&&firstValidate){
                        townShow=false;
                    }
                    town="";
                }else{
                    addr_name=town;
                }

                var nation=data.nation;
                if(nation=='-'){
                    if(_index!=0&&firstValidate){
                        nationShow=false;
                    }
                    nation="";
                }else{
                    nation='-'+nation;
                }
                var sex=data.sex;
                if(sex=='-'){
                    if(_index!=0&&firstValidate){
                        sexShow=false;
                    }
                    sex="";
                }else{
//	    			if(sex=='1'){
//	    				sex='男性';
//	    			}else if (sex=='2'){
//	    				sex='女性';
//	    			}else{
//	    				sex='未知';
//	    			}

                }
                if(_index!=0){
                    firstValidate=false;
                }

                addr_name=addr_name+nation+sex;
                var strs= new Array();
                strs=addr_name.split("-");
                addr_name=strs[strs.length-1];
                if(!addr_name){
                    addr_name='未知';
                }
                var totalNum=data.totalNum;
                var positiveNum=data.positiveNum;
                var alphaPositiveNum=data.alphaPositiveNum;
                var betaPositiveNum=data.betaPositiveNum;
                var alphaTestNum=data.alphaTestNum;
                var betaTestNum=data.betaTestNum;
                var pieData=[addr_name,positiveNum];

                if(alphaTestNum==0){
                    alphaTestNum=1;
                }
                if(betaTestNum==0){
                    betaTestNum=1;
                }
                if(0==selectTab){
                    if(town=='东山乡'){
                        var percent1=Math.round((data.positiveNum/data.totalNum)*10000)/100+"%";
                        $(".percent1").text(percent1);
                    }
                    if(town=='凤凰乡'){
                        $(".percent2").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }
                    if(town=='西山乡'){
                        $(".percent3").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }
                    if(town=='巴马镇'){
                        $(".percent4").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }
                    if(town=='甲篆镇'){
                        $(".percent5").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }
                    if(town=='那社乡'){
                        $(".percent6").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }
                    if(town=='所略乡'){
                        $(".percent7").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }
                    if(town=='燕洞镇'){
                        $(".percent8").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }
                    if(town=='那桃乡'){
                        $(".percent9").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }
                    if(town=='百林乡'){
                        $(".percent10").text(Math.round((data.positiveNum/data.totalNum)*10000)/100+"%");
                    }

                }
                alphaPositivePercentArr.push(Math.round(((data.positiveNum-data.betaPositiveNum)/totalNum)*10000)/100);
                betaPositivePercentArr.push(Math.round(((data.positiveNum-data.alphaPositiveNum)/totalNum)*10000)/100);
                _pie_series.push(pieData);
                axdata.push(addr_name);
                bxdata.push(addr_name);
                totalNumArr.push(totalNum-positiveNum);
                positiveNumArr.push(positiveNum);
                alphaPositiveNumArr.push(alphaPositiveNum);
                betaPositiveNumArr.push(betaPositiveNum);
                if(city==""){
                    var tmp = {
                        name: province,
                        value: Math.round(positiveNum*100/totalNum),
                        alpha_value: Math.round(alphaPositiveNum*100/totalNum),
                        beta_value: Math.round(betaPositiveNum*100/totalNum),
                        total_num: totalNum

                    };
                    dataMap[province]=tmp;
                }else{
                    var tmp = {
                        name: city,
                        value: Math.round(positiveNum*100/totalNum),
                        alpha_value: Math.round(alphaPositiveNum*100/totalNum),
                        beta_value: Math.round(betaPositiveNum*100/totalNum),
                        total_num: totalNum

                    };
                    dataMap[city]=tmp;
                }
            }
            if(nationShow){
                $("#"+_datagrid_id).datagrid('showColumn', 'nation');
            }else{
                $("#"+_datagrid_id).datagrid('hideColumn', 'nation');
            }
            if(sexShow){
                $("#"+_datagrid_id).datagrid('showColumn', 'sex');
            }else{
                $("#"+_datagrid_id).datagrid('hideColumn', 'sex');
            }
            if(townShow){
                $("#"+_datagrid_id).datagrid('showColumn', 'town');
            }else{
                $("#"+_datagrid_id).datagrid('hideColumn', 'town');
            }
//	    	if(countyShow){
//		    	$("#"+_datagrid_id).datagrid('showColumn', 'county');
//	    	}else{
//	    		$("#"+_datagrid_id).datagrid('hideColumn', 'county');
//	    	}
            $("div.pieData").css("display","inline-block");
            var _α_series= [{name: 'α地贫检出率',color: '#6699FF',data: alphaPositivePercentArr}];
            var _β_series= [{name: 'β地贫检出率',color: '#0099CC',data: betaPositivePercentArr}];
            createRankCharts("alpha_rank",axdata,_α_series,'α地贫检出率','α地贫检出率(%)');
            createRankCharts("beta_rank",bxdata,_β_series,'β地贫检出率','β地贫检出率(%)');
            var provinceCode=provinceCodes[_query_params.province];
            if(null==provinceCode||provinceCode==""){
                provinceCode='china'
            }

        }
    });
}
function mouseOver(obj){
    obj.children[0].style.color = "#000";
}
function mouseOut(obj){
    obj.children[0].style.color = "#f00";
}
function getQueryParams(){
    var _parammap={};
    _parammap['province']=$('#count_area_rank_2').combobox('getText');
    _parammap['city']=$('#count_area_rank_3').combobox('getText');
    _parammap['county']=$('#count_area_rank_4').combobox('getText');

//	_parammap['groupbySex']=$('#groupby_sex').combobox('getValue');
//	_parammap['groupbyNation']=$('#groupby_nation').combobox('getValue');
//	_parammap['groupbyNextRegion']=$('#groupby_addr').combobox('getValue');
    var tab = $('#conditionTabs').tabs('getSelected');
    var conditionTabIndex= $('#conditionTabs').tabs('getTabIndex',tab);
    _parammap['selectTab']=conditionTabIndex;
    if(0==conditionTabIndex){
        $('#agene_type').combobox('setValue','');
        $('#bgene_type').combobox('setValue','');
        $('#agene_type').combobox('disable');
        $('#bgene_type').combobox('disable');
        _parammap['groupbySex']='0';
        _parammap['groupbyNextRegion']='1'
        _parammap['groupbyNation']='0';
        _parammap['ageneType']='';
        _parammap['bgene_type']='';
    }else if(1==conditionTabIndex){
        $('#agene_type').combobox('disable');
        $('#bgene_type').combobox('disable');
        $('#agene_type').combobox('setValue','');
        $('#bgene_type').combobox('setValue','');
        _parammap['groupbySex']='0';
        _parammap['groupbyNextRegion']='0'
        _parammap['groupbyNation']='1';
        _parammap['ageneType']='';
        _parammap['bgene_type']='';
    }else if(2==conditionTabIndex){
        _parammap['groupbySex']='0';
        _parammap['groupbyNextRegion']='1'
        _parammap['groupbyNation']='0';
        $('#agene_type').combobox('enable');
        $('#bgene_type').combobox('enable');
        _parammap['ageneType']=$('#agene_type').combobox('getValue');
        _parammap['bgeneType']=$('#bgene_type').combobox('getValue');
    }

    return _parammap;
}
function invokingRecordsRankDatagridReload(){
    var _datagrid_id='invoking_records_rank_datagrid'
    var _parammap=getQueryParams();
    datagridReload(_datagrid_id,_parammap);
}

function datagridReload(_datagrid_id,_parammap){
    var queryParams = $('#'+_datagrid_id).datagrid('options').queryParams;
    for(var _key in _parammap){
        queryParams[_key]=_parammap[_key];
    }
    $("#"+_datagrid_id).datagrid('reload');
}

function more(){
    var isShow=$("#moreData").css("display");

    if(isShow == "none"){
        $("#moreData").css("display","table-row");
    }else{
        $("#moreData").css("display","none");
    }

}

//----------------------修改时间：2018/5/29 17：53  -----以下的是新增的功能，对于双击的信息



