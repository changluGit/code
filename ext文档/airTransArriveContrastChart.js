// <%@page contentType="text/html; charset=utf-8" %>
Ext.ns('SF.cdp.airTransArriveContrastView');

var airTransArriveContrastView_msgTitle = "提示";
var airTransArriveContrastView_loadingMsg = "正在加载,请稍后......";
var airTransArriveContrastView_paramFormat='Y-m-d';
var airTransArriveContrastView_format = "m月d日";
var airTransArriveContrastView_title="航管运力到货对比图";
var airTransArriveContrastView_unitText1 = "单<br/>位<br/>︵<br/>kg<br/>︶";
var airTransArriveContrastView_unitText2 = "单<br/>位<br/>︵<br/>个<br/>数<br/>︶";
var airTransArriveContrastView_unitText3 = "单<br/>位<br/>︵<br/>票<br/>数<br/>︶";
//统计方式
var airTransArriveContrastView_countTypeStore = new Ext.data.ArrayStore({
	fields:['id','name'],
	data:[[1,"发出"],[2,"到达"]]
});

//0-5代表网点层级
var airTransArriveContrastView_levelDataSource = {
	0 : [[1,"经营本部"],[2,"地区"],[4,"城市/网点"]],
	1 : [[2,"地区"],[4,"城市/网点"]],
	2 : [[4,"城市/网点"]],
	3 : [[4,"城市/网点"]],
	4 : [[4,"城市/网点"]]
};
//单位
var airTransArriveContrastView_unitTypeStore = new Ext.data.ArrayStore({
	fields:['id','name'],
	data:[[1,"重量"],[2,"运力数"],[3,"票数"]]
});
var airTransArriveContrastView_isMax=false;
var airTransArriveContrastView_maxChartHeight = (parent.Ext.getBody().getHeight()-100)*0.7-20;
var airTransArriveContrastView_chart1;
var airTransArriveContrastView_config1; 

//设定大图及小图获取配置信息
function setairTransArriveContrastView_Config1(){
	var minChart_ = {
		type : 'column',
		renderTo : 'airTransArriveContrastView'
	};
	var maxChart_ = {
		type : 'column',
		height : airTransArriveContrastView_maxChartHeight,
		renderTo : 'airTransArriveContrastViewMax'
	};
	var chartType=minChart_;
	
	if(airTransArriveContrastView_isMax){
		chartType=maxChart_;
	}
	airTransArriveContrastView_config1 = {
		chart:chartType,
		title : {
			text : '',
			useHTML:true
		},
		xAxis : [{
			labels: {
	            rotation: -45,
	            align: 'right',
	            style: {
	                padding:'3px 0 0 0',
	                fontSize:'13px'
	            },
	            x: 10
	        }
		}],
		yAxis : [{
			title: {
	            text: '',
	            rotation: 0,
	            style: {
	                color: '#095B94'
	            }
	        },
	        labels : {
				style : {
					color : '#4572A7'
				}
			},
			allowDecimals : false
		}],
		tooltip: {
			formatter : function() {
				var names = [];
				Ext.each(arguments[0].chart.series,function(serie){
					names.push(serie.name);
				});
				var ratios = [false,false,false];
				return Highcharts.chartDataFormat(this,names,ratios);
			}
        },
		plotOptions : {
			column: {
				stacking: 'normal'
			}
		},
		series : [{
			name : "全货机",
			type: 'column',
            yAxis: 0,
			data : []
		},{
			name : "散航",
			type: 'column',
            yAxis: 0,
			data : []
		},{
			name : "铁路",
			type: 'column',
            yAxis: 0,
			data : []
		}]
	};
	
}

setairTransArriveContrastView_Config1();

SF.cdp.airTransArriveContrastView.QueryPanel = Ext.extend(Ext.form.FormPanel, { 
	constructor : function(config){
		config = Ext.apply({
			id:'airTransArriveContrastViewQueryPanel',
			autoHeight:true,
			frame:true,
			labelWidth:60,
			labelAlign:'right',
			bodyStyle:'padding: 1px 5px 0px 1px',
			margins:'0 0 5 0',
			tbar:[{
				text:"查询",
				iconCls:'search',
				handler : this.searchHandler.createDelegate(this)
			}],
			items:[{
				xtype:'fieldset',
				title:"查询条件",
				layout:'column',
				defaults:{
					defaults:{
						anchor:'80%'
					}
				},
				items:[{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'textfield',
						name:'deptCode',
						fieldLabel:"网络代码",
						value:'${deptCode}',
						validateOnBlur : false,
						validationEvent : false,
						validator : getDeptCodeValidator({
							//checkLevel : 2
						}),
						listeners : {
							change : this.onChangeHandler.createDelegate(this),
							specialkey: function(field, e){
								if (e.getKey() == e.ENTER) {
									this.onChangeHandler(field);
								}
							}.createDelegate(this)
						}
					}
				},{
					xtype:'hidden',
					name : 'deptLevel'
				},{
					xtype:'hidden',
					name : 'deptName'
				}]
			}]
		},config);
		SF.cdp.airTransArriveContrastView.QueryPanel.superclass.constructor.call(this,config);
	},
	onChangeHandler : function(f,n,o){
		if(f.getValue() == ""){
			return ;
		}
		var form = this.getForm();
		form.findField('deptCode').blur();
		f.setValue(f.getValue().toUpperCase());
		form.findField('deptName').setValue(f.deptName);
		form.findField('deptLevel').setValue(f.typeLevel);
	},
	searchHandler : function(){
		 //设定标题
		airTransArriveContrastView_title = this.chartInfo.chartName.trim();
		//更新报表
		airTransArriveContrastView_updateChartAll();
	}
}
);
Ext.reg("SF.cdp.airTransArriveContrastView.QueryPanel", SF.cdp.airTransArriveContrastView.QueryPanel);

SF.cdp.airTransArriveContrastView.MainPanel = Ext.extend(Ext.Panel,{
	constructor : function(config){
		config = Ext.apply({
			id:'airTransArriveContrastViewId',
			html:"<div id='airTransArriveContrastView' style='border:1px solid #8db2e3;height:99.5%;width:99.5%;'></div>",
			listeners : {
				'afterlayout':function(){
					if(!this.firstLoad){
						this.firstLoad=true;
						airTransArriveContrastView_chart1 = new Highcharts.Chart(airTransArriveContrastView_config1);
					}
				}
			}
		},config);
		SF.cdp.airTransArriveContrastView.MainPanel.superclass.constructor.call(this,config);
	}
});
Ext.reg('SF.cdp.airTransArriveContrastView.MainPanel',SF.cdp.airTransArriveContrastView.MainPanel);


/***
 * 航管运力到货对比图
 * @class SF.cdp.airTransArriveContrastView.Panel1Win
 * @extends Ext.Window
 */
var airTransArriveContrastView_chart1_1 = null;
SF.cdp.airTransArriveContrastView.Panel1Win = Ext.extend(Ext.Window,{
	constructor : function(config){
		
		var airTransArriveContrastViewQueryPanelForm = Ext.getCmp('airTransArriveContrastViewQueryPanel').getForm();
		var deptLevel = airTransArriveContrastViewQueryPanelForm.findField('deptLevel').getValue();
		 
		config = Ext.apply({
			closeAction:'close',
			id:'Panel1Win',
			modal:true,
			plain:true,
			resizable:false,
			width:Math.round(Ext.getBody().getWidth()*0.95),
			height:Math.round(Ext.getBody().getHeight()*0.9),
			layout:'border',
			tbar:[{
				text:"查询",
				iconCls:'search',
				handler : this.searchHandler.createDelegate(this)
			}],
			items:[{
				region : 'north',
				xtype:'form',
				autoHeight:true,
				frame:true,
				labelWidth:70,
				labelAlign:'right',
				bodyStyle:'padding: 1px 5px 0px 1px',
				margins:'0 0 5 0',
				showErrorMsg:true,
				items:[{
					xtype:'fieldset',
					title:"查询条件",
					style:'padding-top: 2px',
					layout:'column',
					defaults:{
						defaults:{
							allowBlank:false,
							anchor:'95%'
						}
					},
					items:[{
						columnWidth:.001,
						layout:'form',
						items:{
							xtype:'combo',
							fieldLabel:"统计方式",
							triggerAction:"all",
							lazyRender : true,
							editable: false,
							mode:"local",
							displayField:"name",
							valueField:"id",
							hiddenName:'countType',
							store : airTransArriveContrastView_countTypeStore,
							value:'2',
							emptyText:"请选择"
						}
					},{
						columnWidth:.15,
						layout:'form',
						items:{
							xtype:'datefield',
							fieldLabel:"日期",
							name:'date',
							editable : false,
							value:new Date(),
							format:'Y-m-d'
						}
					},{
						columnWidth:.15,
						layout:'form',
						items:{
							xtype:'textfield',
							name:'deptCode',
							fieldLabel:"网络代码",
							validateOnBlur : false,
							validationEvent : false,
							validator : getDeptCodeValidator({
								//checkLevel : 2
							}),
							listeners : {
								change : this.onChangeHandler.createDelegate(this),
								specialkey: function(field, e){
									if (e.getKey() == e.ENTER) {
										this.onChangeHandler(field);
									}
								}.createDelegate(this)
							}
						}
					},{
						columnWidth:.15,
						layout:'form',
						items:{
							xtype:'combo',
							hiddenName:'showLevel',
							triggerAction:'all',
							fieldLabel:"展示方式 ",
							displayField:'name',
							editable: false,
							valueField:'id',
							mode:'local',
							store: new Ext.data.ArrayStore({
								fields:['id','name'],
								data:airTransArriveContrastView_levelDataSource[deptLevel]
							}),
					   listeners : {
						'afterrender' : function (v){v.setValue(v.getStore().getAt(0).data.id);}
    	    		}
						}
					},{
						columnWidth:.15,
						layout:'form',
						items:{
							xtype:'combo',
							fieldLabel:"单位",
							triggerAction:"all",
							lazyRender : true,
							editable: false,
							mode:"local",
							displayField:"name",
							valueField:"id",
							hiddenName:'unitType',
							store : airTransArriveContrastView_unitTypeStore,
							value:'1',
							emptyText:"请选择",
							allowBlank:false,
							anchor:'90%'
						}
					},{
						xtype:'hidden',
						name:'maximized',
						value:true
					},{
						xtype:'hidden',
						name : 'deptLevel'
					},{
						xtype:'hidden',
						name : 'deptName'
					}]
				}]
			},{
				region : 'center',
				frame:true,
				autoScroll:true,
				items:[{
					html:'<div id="airTransArriveContrastViewMax" style="height:100%;width:100%;"></div>'
				}]
			}],
			listeners : {
				show : function(){
					//小图form对象
					var airTransArriveContrastViewQueryPanelForm = Ext.getCmp('airTransArriveContrastViewQueryPanel').getForm();
					var deptCode = airTransArriveContrastViewQueryPanelForm.findField('deptCode').getValue();
		            var deptLevel = airTransArriveContrastViewQueryPanelForm.findField('deptLevel').getValue();
		            var deptName = airTransArriveContrastViewQueryPanelForm.findField('deptName').getValue();
		            
		            //大图form对象
		            var form = this.items.get(0).getForm();
		            form.findField('countType').hide();
					form.findField('deptCode').setValue(deptCode);
					form.findField('deptName').setValue(deptName);
		            form.findField('deptLevel').setValue(deptLevel); 
		            
		            airTransArriveContrastView_isMax=true;
					setairTransArriveContrastView_Config1(); 
					airTransArriveContrastView_chart1_1 = new Highcharts.Chart(airTransArriveContrastView_config1);
					this.searchHandler();
				}.createDelegate(this),
				hide : function(){
					airTransArriveContrastView_isMax=false;
					setairTransArriveContrastView_Config1();
				}.createDelegate(this)
				
			}
		},config);
		SF.cdp.airTransArriveContrastView.Panel1Win.superclass.constructor.call(this,config);
	},
	onChangeHandler : function(f){
		if(f.getValue()==""){
			return ;
		}
		// 如果填写的代码没有对应层级则不做操作
		if(!f.typeLevel) return;
		
		var form = this.items.get(0).getForm();
		form.findField('deptName').setValue(f.deptName);
		form.findField('deptLevel').setValue(f.typeLevel);
		
		var showLevel = form.findField("showLevel");
		showLevel.getStore().removeAll();
		//设置展示层级数据
		showLevel.getStore().loadData(airTransArriveContrastView_levelDataSource[f.typeLevel]);
		showLevel.setValue(showLevel.getStore().getAt(0).data.id);
	},
	searchHandler : function(){
		var form = this.items.get(0).getForm();

		var date = new Date(form.findField('date').getValue());
		var deptName = form.findField('deptName').getValue();
		var unitType = form.findField('unitType').getValue();
		var deptLevel = form.findField('deptLevel').getValue();
		var showLevel = form.findField('showLevel').getValue();
		var countType = form.findField('countType').getValue();
		
		deptName = Highcharts.convertDeptName(deptName);
		if(showLevel == 1){
			deptName+="各经营本部";
		}else if(showLevel == 2){
			deptName+="各地区";
		}else if(showLevel == 4){
			deptName+="各网点";
		}
		var d1 = date.format(airTransArriveContrastView_format);
		
		if(form.isValid()){
			var title = d1+deptName+airTransArriveContrastView_title;
			this.setTitle(title);
			
			airTransArriveContrastView_chart1_1.setTitle({
				text:title
			});
			
			form.submit({
				waitTitle:"提示",
				waitMsg:"正在加载,请稍后......",
				url: "../gswmgmt/transportView_sendGetContChart.action",
				success: function(form,action) {
					var data = Ext.util.JSON.decode(action.result.datas);
					airTransArriveContrastView_initChart1Data(airTransArriveContrastView_chart1_1,unitType,data,15);
					
				}.createDelegate(this)
			});
		}
	}
});
Ext.reg('SF.cdp.airTransArriveContrastView.Panel1Win',SF.cdp.airTransArriveContrastView.Panel1Win);

//根据网点查找报表
function airTransArriveContrastView_updateChartAll(){
	var form = Ext.getCmp('airTransArriveContrastViewQueryPanel').getForm();
	
	if (!form.isValid()) {
		return;
	} 
	var date = new Date();
	date1=date.format(airTransArriveContrastView_paramFormat);
	
	
	var deptCode = form.findField('deptCode').getValue();
	var deptLevel = form.findField('deptLevel').getValue();
	 
	var showLevel=4;//展示方式
	if(deptLevel==0){
      showLevel=1;
	}else if(deptLevel==1){
		showLevel=2;
	}
	var deptName = form.findField('deptName').getValue();
	deptName = deptName.replace("区部","区").replace("區部","區");
	if(deptName == "总部"){
		deptName = "全网";
	}
	var chart1_2_dept_name = deptName;
	if(showLevel == 1){
		chart1_2_dept_name+="各经营本部";
	}else if(showLevel == 2){
		chart1_2_dept_name+="各地区";
	}else if(showLevel == 4){
		chart1_2_dept_name+="各网点";
	}
	  Ext.MessageBox.show({
			title : airTransArriveContrastView_msgTitle,
			msg : airTransArriveContrastView_loadingMsg,
			progress : true,
			width : 300,
			closable : true,
			wait : true,
			waitConfig : {
				interval : 1000
			}
		});
		var countType = 2; //1：发货 ，  2：到货
	    var unitType = 1 ;//默认重量
	 	var params = {
			deptCode : deptCode,
			date:date1,
			deptLevel:deptLevel,
			countType:countType,
			unitType:unitType,
			showLevel:showLevel
		};
	 	var d2=date.format(airTransArriveContrastView_format);
	
	//*********************第1个图表 start***************************//
	Ext.Ajax.request({
		url: "../gswmgmt/transportView_sendGetContChart.action",
		params:params,
		success: function(response) {
			Ext.MessageBox.hide();
			var info = Ext.util.JSON.decode(response.responseText);
			var data = Ext.util.JSON.decode(info.datas);
			var titleTmp=d2+chart1_2_dept_name+airTransArriveContrastView_title;
			titleTmp=formatChartTitle(titleTmp,19);
			var title = '<a href="#" onclick="airTransArriveContrastView_maximize()">'+titleTmp+'</a>';
			airTransArriveContrastView_chart1.setTitle({
				text:title
			});
			airTransArriveContrastView_initChart1Data(airTransArriveContrastView_chart1,unitType,data,5);
		}.createDelegate(this)
	});
	//*********************第1个图表 end***************************//
}

//初始化报表
function airTransArriveContrastView_initChart1Data(chart,unitType,data,leng){
	if(data.s1.length <= leng){
		chart.xAxis[0].update({
			labels : {
				rotation : 0,
				align: 'center',
				x: 0
			}
		});
	}else{
		chart.xAxis[0].update({
			labels : {
				rotation : -45,
				align: 'right',
				x: 10
			}
		});
	}
	var unitTex = airTransArriveContrastView_unitText1;
	if(unitType == 2){
		unitTex = airTransArriveContrastView_unitText2;
	}else if(unitType == 3){
		unitTex = airTransArriveContrastView_unitText3;
	}
	chart.yAxis[0].setTitle({
		text : unitTex
	});
	
	chart.xAxis[0].setCategories(data.s1);
	chart.series[0].setData(data.s2);
	chart.series[1].setData(data.s3);
	chart.series[2].setData(data.s4);
}

function airTransArriveContrastView_maximize(){
		var win = new SF.cdp.airTransArriveContrastView.Panel1Win();
		win.show();
}
