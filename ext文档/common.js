// <%@page contentType="text/html; charset=utf-8" %>
Ext.namespace('SF');

Ext.QuickTips.init();

Ext.pageSize = 25;
Ext.base = "/cdp";
Ext.HOME = "../ext-3.4.0/";
Ext.BLANK_IMAGE_URL = Ext.HOME + "resources/images/default/s.gif";
/** 请求有效时间十五分钟 */
Ext.Ajax.timeout = 15 * 60 * 1000;
Ext.Msg.minWidth = 160;

var prompt = "提示";
Ext.form.Field.prototype.msgTarget = "qtip"; // side|qtip


/**获取服务器端实时时间 SF.SYSTEM_DATE 是静态的，SF.DYNAMIC_SYSTEM_DATE 是动态的,需要在请求的Action里面写方法 getSystemTime() 放回格式为"Y-m-d H:i:s" 的字符串**/
/*try {
if("${systemTime}"){
	SF.SYSTEM_DATE = Date.parseDate("${systemTime}","Y-m-d H:i:s");
	SF.DYNAMIC_SYSTEM_DATE = Date.parseDate("${systemTime}","Y-m-d H:i:s");
	window.setInterval(function(){
		SF.DYNAMIC_SYSTEM_DATE = SF.DYNAMIC_SYSTEM_DATE.add(Date.SECOND,1);
	},1000)
}
} catch (e) {}*/

/**
 * XML格式数据格式化
 */
SF.htmlCodeFormat = function(v){
	try {
		if(v && typeof(v) == 'string'){
			v = v.replace(/[&]/gm,'&amp;')
				.replace(/[<]/gm,'&lt;')
				.replace(/[>]/gm,'&gt;')
				.replace(/[']/gm,'&apos;')
				.replace(/["]/gm,'&quot;');
		}
	} catch (e) {
	}
	return v;
}

/**
 * grid刷新后滚动条位置不变
 */
Ext.override(Ext.grid.GridView, {
    scrollToTop : function() {
        var dom = this.scroller.dom;
        
        dom.scrollTop  = 0;
//        dom.scrollLeft = 0;
    }
});


/**
 * 重写模版的取值方式
 */
Ext.override( Ext.XTemplate , {
	compileTpl : function(tpl){
        var fm = Ext.util.Format,
            useF = this.disableFormats !== true,
            sep = Ext.isGecko ? "+" : ",",
            body;

        function fn(m, name, format, args, math){
            if(name.substr(0, 4) == 'xtpl'){
                return "'"+ sep +'this.applySubTemplate('+name.substr(4)+', values, parent, xindex, xcount)'+sep+"'";
            }
            var v;
            if(name === '.'){
                v = 'values';
            }else if(name === '#'){
                v = 'xindex';
            }else if(name.indexOf('.') != -1){
                v = name;
            }else{
                v = "SF.htmlCodeFormat(values['" + name + "'])";
            }
            if(math){
                v = '(' + v + math + ')';
            }
            if (format && useF) {
                args = args ? ',' + args : "";
                if(format.substr(0, 5) != "this."){
                    format = "fm." + format + '(';
                }else{
                    format = 'this.call("'+ format.substr(5) + '", ';
                    args = ", values";
                }
            } else {
                args= ''; format = "("+v+" === undefined ? '' : ";
            }
            return "'"+ sep + format + v + args + ")"+sep+"'";
        }

        function codeFn(m, code){
            // Single quotes get escaped when the template is compiled, however we want to undo this when running code.
            return "'" + sep + '(' + code.replace(/\\'/g, "'") + ')' + sep + "'";
        }

        // branched to use + in gecko and [].join() in others
        if(Ext.isGecko){
            body = "tpl.compiled = function(values, parent, xindex, xcount){ return '" +
                   tpl.body.replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn).replace(this.codeRe, codeFn) +
                    "';};";
        }else{
            body = ["tpl.compiled = function(values, parent, xindex, xcount){ return ['"];
            body.push(tpl.body.replace(/(\r\n|\n)/g, '\\n').replace(/'/g, "\\'").replace(this.re, fn).replace(this.codeRe, codeFn));
            body.push("'].join('');};");
            body = body.join('');
        }
        eval(body);
        return this;
    }
});

/**
 * grid刷新后滚动条位置不变
 */

Ext.getComboTpl = function(displayField){
	return '<tpl for="."><div class="x-combo-list-item" ext:qtip="{'+displayField+'}">{'+displayField+'}</div></tpl>';
}

Ext.apply(Ext.form.BasicForm.prototype, {
	showErrorMsg : true
});

/** extjs的gridpanel的数据内容不能复制解决办法. */
if (!Ext.grid.GridView.prototype.templates) {   
    Ext.grid.GridView.prototype.templates = {};   
}   
Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(   
     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,   
     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,   
     '</td>'   
);

// 修复办法，谷歌浏览器中,table的单元格实际宽度=指定宽度+padding，所以只要重写gridview里的一个方法，如下：
Ext.override(Ext.grid.GridView,{
    getColumnStyle : function(colIndex, isHeader) {
        var colModel  = this.cm,
            colConfig = colModel.config,
            style     = isHeader ? '' : colConfig[colIndex].css || '',
            align     = colConfig[colIndex].align;
         
        if(Ext.isChrome){
            style += String.format("width: {0};", parseInt(this.getColumnWidth(colIndex))-2+'px');
        }else{
            style += String.format("width: {0};", this.getColumnWidth(colIndex));
        }
         
        if (colModel.isHidden(colIndex)) {
            style += 'display: none; ';
        }
         
        if (align) {
            style += String.format("text-align: {0};", align);
        }
         
        return style;
    }
});

/** 重写表单验证方法,返回验证错误信息 */
Ext.override(Ext.form.BasicForm, {
	isValid : function() {
		var valid = true;
		for (var i = 0; i < this.items.length; i++) {
			var f = this.items.get(i);
			if (!f.validate()) {
				valid = false;
				if (this.showErrorMsg) {
					Ext.Msg.alert("提示", f.fieldLabel + ":" + f.activeError,
					function() {
						f.focus(true, false);
					}
					);
				}
				break;
			}
		}
		return valid;
	}
});

/** 最大长度计算汉字处理函数 */
maxLengthOverride = function(value) {
	if (this.allowBlank == false) {
		if (value == null || value == '') {
			this.markInvalid(String.format(this.blankText, value));
			return false;
		}
	}
	var maxLen = this.maxLength;
	var maxLenText = this.maxLengthText;
	if (maxLen != null && maxLen != 'undefined' && maxLen > 0) {
		var regex = /[^\x00-\xff]/g;
		var len;
		var repalceValue = value.replace(regex, '***');
		len = repalceValue.length;
	}
	if (len > maxLen) {
		this.markInvalid(String.format(maxLenText, value));
		return false;
	}
	return true;
};

maxLengthValid = function(field, value) {
	if (field.allowBlank == false) {
		if (value == null || value == '') {
			return String.format(field.blankText, value);
		}
	}
	var maxLen = field.maxLength;
	var maxLenText = field.maxLengthText;
	if (maxLen != null && maxLen != 'undefined' && maxLen > 0) {
		var regex = /[^\x00-\xff]/g;
		var len;
		var repalceValue = value.replace(regex, '***');
		len = repalceValue.length;
	}
	if (len > maxLen) {
		return String.format(maxLenText + "当前已输入" + len + "个.");
	}
	return true;
};

/** 日期验证,开始日期不能大于结束日期 */
Ext.apply(Ext.form.VTypes, {
	daterangeText : '',
	daterange : function(val, field) {

		var date = field.parseDate(val);

		if (!date) {
			this.daterangeText = String.format(field.invalidText, val,
				field.format
			);
			return false;
		}
		if (field.startDateField) {
			var start = Ext.getCmp(field.startDateField);
			if (!start.maxValue || (date.getTime() != start.maxValue.getTime())) {
				start.setMaxValue(date);
				start.validate();
			}
		} else if (field.endDateField) {
			var end = Ext.getCmp(field.endDateField);
			if (!end.minValue || (date.getTime() != end.minValue.getTime())) {
				end.setMinValue(date);
				end.validate();
			}
		}
		/*
		 * Always return true since we're only using this vtype to set the
		 * min/max allowed values (these are tested for after the vtype test)
		 */
		return true;
	},
	dateLimit : function(val, field) {
		
		 var date = field.parseDate(val);
		 
		 if(!Ext.isDate(date)) return;

	     if (field.endDateField) {
	         
	    	 var endDateField = Ext.getCmp(field.endDateField);
	    	 
	    	 // 设置最小时间
	         endDateField.setMinValue(date);
	         
	         // 设置最大时间
	         if(field.dateLimit){
		         var maxDate = new Date(date.getFullYear(),date.getMonth(),date.getDate() + (field.dateLimit - 1));
		         endDateField.setMaxValue(maxDate);	         
//		         if(!window.maxText) window.maxText = endDateField.maxText;
//     			 var maxText = String.format(window.maxText, endDateField.formatDate(maxDate));
//     			 $('td.x-date-disabled').attr('title',maxText);
		         
	         }

	         if(!Ext.isDate(endDateField.getValue())) return true;
	    	 // 获取结束时间
	         var end = endDateField.getValue();
	         
	         if(end.getTime() < date.getTime()){
	        	 endDateField.setValue(date);
	         }
	         
	         if(field.dateLimit){
		         // 如果时间间隔超过最大间隔数
		         if(end.getTime() - date.getTime() >= (field.dateLimit * 3600 * 24 * 1000)){
		        	 endDateField.setValue(maxDate);
		         }
	         }

	     }
	     return true;
	}, // 不做值限制只提示
	dateLimit2 : function(val, field) {
		
		 var date = field.parseDate(val);
		 
		 if(!Ext.isDate(date)) return false;

	     if (field.endDateField) {
	         
	    	 // 获取结束时间
	    	 var endDateField = Ext.getCmp(field.endDateField);
	    	 
	         if(!Ext.isDate(endDateField.getValue())) return false;
	         
	    	 // 获取时间
	         var end = endDateField.getValue();
	    	 
	         if(end.getTime() < date.getTime()){
	        	 field.vtypeText = field.fieldLabel + '不能大于' + endDateField.fieldLabel;
	        	 return false;
	         }
	        
	         if(field.dateLimit){
		         // 如果时间间隔超过最大间隔数
		         if(end.getTime() - date.getTime() >= (field.dateLimit * 3600 * 24 * 1000)){
		        	 field.vtypeText = field.fieldLabel + '与' + endDateField.fieldLabel +'的间隔不能超过' + field.dateLimit +'天!';
		        	 return false;
		         }
	         }
	         endDateField.clearInvalid();
	     }else if(field.startDateField){
	    	 // 获取结束时间
	    	 var startDateField = Ext.getCmp(field.startDateField);
	    	 
	         if(!Ext.isDate(startDateField.getValue())) return false;
	         
	    	 // 获取时间
	         var start = startDateField.getValue();
	    	 
	         if(start.getTime() > date.getTime()){
	        	 field.vtypeText = field.fieldLabel + '不能小于' + startDateField.fieldLabel;
	        	 return false;
	         }
	        
	         if(field.dateLimit){
		         // 如果时间间隔超过最大间隔数
		         if( date.getTime() - start.getTime() >= (field.dateLimit * 3600 * 24 * 1000)){
		        	 field.vtypeText = startDateField.fieldLabel + '与' + field.fieldLabel +'的间隔不能超过' + field.dateLimit +'天!';
		        	 return false;
		         }
	         }
	         startDateField.clearInvalid()
	     }
	     field.clearInvalid();
	     
	     return true;
	}
}
);

/**
 * 重写GridPanel 隐藏列表头的头部下拉菜单
 */
Ext.override(Ext.grid.GridPanel, {
	enableHdMenu : false
});

/**
 * 重写Ext.grid.RowNumberer 使GRID分页时实现序号自增
 */
/*
 * Ext.override(Ext.grid.RowNumberer,{ renderer : function(value, cellmeta,
 * record, rowIndex, columnIndex,store) { if(store.lastOptions &&
 * store.lastOptions.params){ return store.lastOptions.params.start + rowIndex +
 * 1; } return rowIndex + 1; } });
 */

Ext.renderDateTime = function(value) {
	if (value) {
		value = value.replace('T', ' ');
		return value;
	}
	return '';
};

Ext.renderYMDHMTime = function(value) {
	if (value) {
		value = value.replace('T', ' ');
		return value.split('T')[0].substr(0, value.length - 3);
	}
	return '';
};

Ext.renderDate = function(value) {
	if (value) {
		return value.split('T')[0];
	}
	return '';
};

Ext.renderTime = function(value) {
	if (value && value.length > 12) {
		return value.substr(11, value.length);
	}
	return '';
};

Ext.renderHMTime = function(value) {
	value = Ext.renderTime(value);
	if(value.length > 5 ){
		return value.substr(0, 5);
	}
	return value;
};

Ext.renderRate = function(value) {
	if (value || value != null) {
		return new Number(value * 100).toFixed(2) + '%';
	}
	return "";
}

Ext.renderDoubleRate = function(value) {
	if (value != null) {
		return value + '%';
	}
	return "";
}

//格式千分位
Ext.formatThousands = function(value){
	if(value != null){
		return (parseInt(value) + '').replace(/\d{1,3}(?=(\d{3})+$)/g, '$&,');
	}
    return value;
}

/**
 * 灯种展示为图片
 * 
 */
Ext.formatLampImg = function formatLampImg(value){
	if (value) {
		if(value==1){
			value= "<img src='../images/gswmgmt/light-green.png'>"; 
		}else if(value==2){
			value= "<img src='../images/gswmgmt/light-yellow.png'>"; 
		}else if(value==3){
			value= "<img src='../images/gswmgmt/light-red.png'>"; 
		}else{
			value='无';
		}
		return value;
	}
	return '无';
}

/**
 * 给所有设置为readOnly的输入灰色显示
 */
Ext.override(Ext.form.Field, {
	getAutoCreate : function() {
		var cfg = Ext.isObject(this.autoCreate) ? this.autoCreate : Ext.apply(
		{}, this.defaultAutoCreate
		);
		// 文件域只读
		if (this.inputType == 'file') {
			cfg.onkeydown = 'return false;';
		}
		if (this.id && !cfg.id) {
			cfg.id = this.id;
		}
		return cfg;
	},
	setReadOnly : function(readOnly) {
		if (this.rendered) {
			this.el.dom.readOnly = readOnly;
			if (readOnly) {
				var readOnlyColor = this.readOnlyColor ? this.readOnlyColor : 'red';
				this.el.setStyle('color', readOnlyColor);
			} else {
				this.el.setStyle('color', "#000000");
			}
		}
		this.readOnly = readOnly;
	},
	onBlur : function() {
		this.beforeBlur();
		if (this.focusClass) {
			this.el.removeClass(this.focusClass);
		}
		this.hasFocus = false;
		if (this.validationEvent !== false
			&& (this.validateOnBlur || this.validationEvent == 'blur')) {
			this.validate();
		}
		var v = this.getValue();
		if ((this.xtype == 'textfield' || this.xtype == 'textarea' || this.xtype == 'combo')
			&& this.inputType != 'file') {
			if ((v || v != null) && !Ext.isNumber(v)) {
				v = v.trim();
				this.setValue(v);
			}
		}
		if (String(v) !== String(this.startValue)) {
			this.fireEvent('change', this, v, this.startValue);
		}
		this.fireEvent('blur', this);
		this.postBlur();
	}
}
);

/**
 * 给所有设置为readOnly的输入灰色显示
 */
Ext.override(Ext.form.TriggerField, {
	updateEditState : function() {
		if (this.rendered) {
			if (this.readOnly) {
				this.el.dom.readOnly = true;
				this.el.addClass('x-trigger-noedit');
				this.mun(this.el, 'click', this.onTriggerClick, this);
				this.trigger.addClass('x-item-disabled');
				this.disabled = true;
			} else {
				if (!this.editable) {
					this.el.dom.readOnly = true;
					this.el.addClass('x-trigger-noedit');
					this.mon(this.el, 'click', this.onTriggerClick, this);
				} else {
					this.el.dom.readOnly = false;
					this.el.removeClass('x-trigger-noedit');
					this.mun(this.el, 'click', this.onTriggerClick, this);
				}
				this.disabled = false;
				this.trigger.removeClass('x-item-disabled');
				this.trigger.setDisplayed(!this.hideTrigger);
			}
			this.onResize(this.width || this.wrap.getWidth() - 2);
		}
	},
	setReadOnly : function(readOnly) {
		if (readOnly != this.readOnly) {
			this.readOnly = readOnly;
			this.updateEditState();
		}
		if (this.el) {
			if (this.readOnly) {
				this.el.setStyle('color', "#C0C0C0");
			} else {
				this.el.setStyle('color', "#000000");
			}
		}
	}
});

/**
 * 重写Grid.Column.renderer 每列悬停时提示内容信息
 */
Ext.override(Ext.grid.Column, {
	renderer : function(val, metadata, record, rowIdx, colIdx, ds) {
		var value = SF.htmlCodeFormat(val);
		if (this.rendererCall) {
			var ret = this.rendererCall(value, metadata, record, rowIdx,
				colIdx, ds
			);
			if (this.showTip) {
				return '<div ext:qtitle="' + this.header + '" ext:qtip="'
					+ (ret == null ? "" : ret) + '">'
					+ (ret == null ? "" : ret) + '</div>';
			}
			return ret;
		} else {
			if (this.showTip) {
				return '<div ext:qtitle="' + this.header + '" ext:qtip="'
					+ (value == null ? "" : value) + '">'
					+ (value == null ? "" : value) + '</div>';
			}
			return value;
		}
	}
});

/**
 * 重写Ext.QuickTip 给悬浮内容编码
 */
Ext.override(Ext.QuickTip, {
	getTipCfg :  function(e) {
	    var t = e.getTarget(), 
	        ttp, 
	        cfg;
	    if(this.interceptTitles && t.title && Ext.isString(t.title)){
	        ttp = t.title;
	        t.qtip = ttp;
	        t.removeAttribute("title");
	        e.preventDefault();
	    }else{
	        cfg = this.tagConfig;
	        ttp = t.qtip || Ext.fly(t).getAttribute(cfg.attribute, cfg.namespace);
	    }
	    return SF.htmlCodeFormat(ttp);
	}
});





// 重置文件上传框
Ext.resetFileInput = function(ff) {
	if (ff != undefined) {
		if (typeof(ff) == 'string') {
			ff = document.getElementById(ff);
		}
		var pn = ff.parentNode;
		var tf = document.createElement("form");
		pn.replaceChild(tf, ff);
		tf.appendChild(ff);
		tf.reset();
		pn.replaceChild(ff, tf);
	}
};
/**
 * 更新grid分页工具条
 */
Ext.updatePagingToolBar = function(paingToolbar){
	paingToolbar.updateInfo();
	paingToolbar.afterTextItem.setText(String.format(paingToolbar.afterPageText,1));
	paingToolbar.inputItem.setValue(1);
    paingToolbar.first.disable();
    paingToolbar.prev.disable();
    paingToolbar.next.disable();
    paingToolbar.last.disable();
}

/**
 * 取得今天开始计算 其他日期 -2：前天、-1：昨天、0：今天、1：明天、2：后天
 * 
 * @param AddDayCount
 * @author 613413
 * @returns {Date}
 */
function GetDateStr(AddDayCount) {
	var myDate = new Date();
	myDate.setDate(myDate.getDate() + AddDayCount);
	return myDate;
}
function checkDept(field) {
	var deptCode = field.getValue().trim().toUpperCase();
	field.setValue(deptCode);
	if (deptCode.length > 0) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("post",
			'../cdpmgmt/commonAction!checkDeptCodeAndFindLevel.action?deptCode='
				+ deptCode, false
		);
		conn.send(null);
		var text = conn.responseText;
		if (text.indexOf("html") != -1) {
			top.location = "../loginmgmt/index.action";
		} else {
			var result = Ext.util.JSON.decode(text);
			if (!result.flag) {
				if (result.status == '1') {
					field.markInvalid('您输入的网点不正确,请重新输入!');
					Ext.MessageBox.alert(prompt,
					field.activeError, function() {
						// field.focus(true,false);
					}, this
					);
				} else if (result.status == '2') {
					field.markInvalid('您没有该网点权限,请重新输入!');
					Ext.MessageBox.alert(prompt,
					field.activeError, function() {
						// field.focus(true,false);
					}, this
					);
				}
				return false;
			} else {
				if (result.typeLevel > 2) {
					field.markInvalid('只能输入区部及以上层级的网点,请重新输入!');
					Ext.MessageBox.alert(prompt,
					field.activeError, function() {
						// field.focus(true,false);
					}, this
					);
					return false;
				} else {
					field.clearInvalid();
					return result;
				}
			}
		}
	}
	return true;
}

function checkDeptTwo(field) {
	var deptCode = field.getValue().trim().toUpperCase();
	field.setValue(deptCode);
	if (deptCode.length > 0) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("post",
			'../cdpmgmt/commonAction!checkDeptCodeAndFindLevel.action?deptCode='
				+ deptCode, false
		);
		conn.send(null);
		var text = conn.responseText;
		if (text.indexOf("html") != -1) {
			top.location = "../loginmgmt/index.action";
		} else {
			var result = Ext.util.JSON.decode(text);
			if (!result.flag) {
				if (result.status == '1') {
					field.markInvalid('您输入的网点不正确,请重新输入!');
					Ext.MessageBox.alert("提示", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '2') {
					field.markInvalid('您没有该网点权限,请重新输入!');
					Ext.MessageBox.alert("提示", field.activeError, function() {
						field.focus(true, false);
					}, this);
				}
				return false;
			}
			return result;
		}
	}
	return true;
}

function checkDeptTwoAndCity(field, maxDept) {
	var deptCode = field.getValue().trim().toUpperCase();
	field.setValue(deptCode);
	if (deptCode.length > 0) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("post",
			'../cdpmgmt/commonAction!checkSHTLDeptCodeAndFindLevel.action?power=true&maxDept='+maxDept+'&deptCode='
				+ deptCode, false
		);
		conn.send(null);
		var text = conn.responseText;
		if (text.indexOf("html") != -1) {
			top.location = "../loginmgmt/index.action";
		} else {
			var result = Ext.util.JSON.decode(text);
			if (!result.flag) {
				if (result.status == '1') {
					field.markInvalid('您输入的网点不正确,请重新输入!');
					Ext.MessageBox.alert("提示", field.activeError,
					function() {
						field.focus(true, false);
					}, this
					);
				} else if (result.status == '2') {
					field.markInvalid('您没有该网点权限,请重新输入!');
					Ext.MessageBox.alert("提示", field.activeError,
					function() {
						field.focus(true, false);
					}, this
					);
				}
				return false;
			}
			return result;
		}
	}
	return true;
}

function checkMoreDeptCode(field, maxDept) {
	var deptCode = field.getValue().trim().toUpperCase().replace("，",",");
	field.setValue(deptCode);

	if (deptCode.length > 0) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("post",
			'../cdpmgmt/commonAction!checkMoreDeptCodeAndFindLevel.action?deptCode='
				+ deptCode + '&maxDept=' + maxDept, false
		);
		conn.send(null);
		var text = conn.responseText;
		if (text.indexOf("html") != -1) {
			top.location = "../loginmgmt/index.action";
		} else {
			var result = Ext.util.JSON.decode(text);
			if (!result.flag) {
				if (result.status == '1') {
					field.markInvalid('您输入的网点  ' + result.depts
						+ '  不正确或无网点权限,请检查后重新输入!');
					Ext.MessageBox.alert("提示", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '2') {
					field.markInvalid('您输入的网点不正确或无网点权限,请重新输入!');
					Ext.MessageBox.alert("提示", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '3') {
					field.markInvalid('您输入的网点权限层级不一致,请重新输入!');
					Ext.MessageBox.alert("提示", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '5') {
					field
						.markInvalid('网点代码过长或格式有问题，最多可输入6个网点代码，参考格式:755B,755D,755BF');
					Ext.MessageBox.alert("提示", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '6') {
					field.markInvalid('您输入了重复的网络代码, 请重新输入!');
					Ext.MessageBox.alert("提示", field.activeError, function() {
						field.focus(true, false);
					}, this);
				}
				return false;
			}
			return result;
		}
	}
	return true;
}

function checkDeptCode(field) {
	var deptCode = field.getValue().trim().toUpperCase();
	field.setValue(deptCode);
	if (deptCode.length > 0) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("post", '../cdpmgmt/commonAction!checkDept.action?deptCode='
				+ deptCode, false);
		conn.send(null);
		var text = conn.responseText;
		if (text.indexOf("html") != -1) {
			top.location = "../loginmgmt/index.action";
		} else {
			var result = Ext.util.JSON.decode(text);
			if (!result.flag) {
				field.markInvalid('您输入的网点不正确,请重新输入!');
				Ext.MessageBox.alert(prompt,
				field.activeError, function() {
					field.focus(true, false);
				}, this
				);
				return false;
			}
			return result;
		}
	}
	return true;
} 

function checkDeptCodeAndCity(field, maxDept) {
	var deptCode = field.getValue().trim().toUpperCase();
	field.setValue(deptCode);
	if (deptCode.length > 0) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("post",
				'../cdpmgmt/commonAction!checkSHTLDeptCodeAndFindLevel.action?power=false&maxDept='+maxDept+'&deptCode='
						+ deptCode, false);
		conn.send(null);
		var text = conn.responseText;
		if (text.indexOf("html") != -1) {
			top.location = "../loginmgmt/index.action";
		} else {
			var result = Ext.util.JSON.decode(text);
			if (!result.flag) {
				field.markInvalid('您输入的网点不正确,请重新输入!');
				Ext.MessageBox.alert(prompt,
						field.activeError, function() {
							field.focus(true, false);
						}, this);
				return false;
			}
			return result;
		}
	}
	return true;
} 

function checkEmpCode(field) {
	var empCode = field.getValue().trim().toUpperCase();
	field.setValue(empCode);
	if (empCode.length > 0) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		conn.open("post", '../cdpmgmt/commonAction!getEmpInfo.action?empCode='
				+ empCode, false);
		conn.send(null);
		var text = conn.responseText;
		if (text.indexOf("html") != -1) {
			top.location = "../loginmgmt/index.action";
		} else {
			var result = Ext.util.JSON.decode(text);
			if (!result.flag) {
				field.markInvalid('您输入的员工号不正确,请重新输入!');
				Ext.MessageBox.alert("提示", field.activeError, function() {
					field.focus(true, false);
				}, this);
				return false;
			}
			return result;
		}
	}
	return true;
}
/**
 * 网点代码验证包括全货机网点城市名称和城市代码校验
 * 
 * @param {}
 *            field
 * @return {Boolean}
 */
function checkDeptDepartDistCode(field) {
	var distCode = field.getValue().trim().toUpperCase();
	field.setValue(distCode);
	if (distCode.length > 0) {
		var conn = Ext.lib.Ajax.getConnectionObject().conn;
		var url = encodeURI(encodeURI('../gswmgmt/general_findDepartDists.action?distCN='
			+ distCode));
		conn.open("post", url, false);
		conn.send(null);
		var text = conn.responseText;
		if (text.indexOf("html") != -1) {
			top.location = "../loginmgmt/index.action";
		} else {
			var result = Ext.util.JSON.decode(text);
			var departDists = result.departDists;
			var deptCode = "";
			if (departDists.length != 0) {
				deptCode = departDists[0].departCode;
			} else {
				deptCode = distCode;
			}
			url = encodeURI(encodeURI('../cdpmgmt/commonAction!checkDeptCodeAndFindLevel.action?deptCode='
				+ deptCode));
			conn.open("post", url, false);
			conn.send(null);
			text = conn.responseText;

			result = Ext.util.JSON.decode(text);
			if (!result.flag) {
				if (result.status == '1') {
					field.markInvalid('您输入的网点或城市名称不正确,请重新输入!');
					Ext.MessageBox.alert(prompt,
					field.activeError, function() {
					}, this
					);
				} else if (result.status == '2') {
					field.markInvalid('您没有该网点权限,请重新输入!');
					Ext.MessageBox.alert(prompt,
					field.activeError, function() {
					}, this
					);
				}
				return false;
			} else {
				return result;
			}
		}
	}
	return null;
}


/**
 * 网点代码和城市通用验证器(支持多个网点代码的输入)
 * 失败返回校验信息，成功则返回true，失败则返回消息提示
 * 如果需要获取当前输入网点层级，则直接获取方法调用者的typeLevel属性
 * 如果需要用户最大网点层级，则直接获取方法调用者的userLevel属性
 * 如果需要获取当前输入网点的中文名称，则直接获取方法调用者的deptName属性
 * 获取当前输入网点的所有验证结果信息，则直接获取方法调用者的resultInfo属性
 * @param checkLevel  校验级别 0 - 4分别代表总部- 网点
 * @param supportCity   是否支持城市代码输入（支持中文输入）
 * @param errMsg 校验失败时的提示信息
 * @param isCache 是否缓存已查询的网点
 * @param onlyCheckCode 是否只校验网点正确性（不校验权限）
 * @param multiCode 是否支持多个网点代码的输入
 * @param multiCount  网点代码最大输入个数
 * @returns
 */
function getDeptCodeValidator(opts) {
	
	var c = {
		checkLevel : 4, // 校验级别 0 - 4分别代表总部- 网点 
		excludeLevel : [], // 不支持输入的网点层级
		supportCity : false, // 是否支持城市名称及代码输入
		supportTransport : false, // 是否支持中转场输入,仅在checkLevel == 3  或者 checkLevel == 4时有效
		chineseCityName : false, // 是否支持中文城市名称的查询
		errMsg : '您输入的网点不正确,请重新输入!', // 通用提示信息
		isCache : true,	// 是否缓存已查询的网点
		onlyCheckCode : false, // 是否只校验网点正确性（不校验权限）
		multiCode : false, // 是否支持多个网点代码的输入
		multiCount : 6 // 网点代码最大输入个数
	}
	
	Ext.apply(c,opts);
	
	var clearInfo = function(obj){
		// 清空当前网点层级
		obj.typeLevel = null;
		// 清空当前网点的中文名称
		obj.deptName = null;
		// 清空所有结果信息信息
		obj.resultInfo = null;
	}
	var setInfo = function(obj,result){
		// 记录当前网点的层级
		obj.typeLevel = result.typeLevel;
		// 记录当前网点的中文名称
		obj.deptName = result.deptName;
		// 记录所有结果信息信息
		obj.resultInfo = result;
	}
	
	return function(v){
		
		// 记录网点校验历史
		if(!this.validHistoryDept && c.isCache){
			this.validHistoryDept = {};
			this.validHistoryDeptNoAuth = {};
		}
		// 记录城市校验历史
		if(!this.validHistoryCity && c.isCache){
			this.validHistoryCity = {};
		}
		
		if(Ext.isEmpty(v)) {
			// 校验失败则清空对应的记录
			clearInfo(this);
			return true;
		}
		v = v.trim().replace(new RegExp('，','g'),',').toUpperCase();
		if(this.setValue && this.validator){
			var validator = this.validator;
			this.validator = null;
			if(c.multiCode){
				var vals = v.split(',');
				var temp = [];
				for ( var j = 0; j < vals.length; j++) {
					temp.push(vals[j].trim());
				}
				v = temp.join(',');
			}
			this.setValue(v);
			this.validator = validator;
		}
		
		var deptCodeList = [];
		
		if(c.multiCode){
			deptCodeList = v.split(',');
			if(deptCodeList.length > c.multiCount){
				clearInfo(this);
				return '最多可输'+c.multiCount+'个网点代码!';
			}
		}else{
			deptCodeList.push(v.toUpperCase());
		}
		var level;
		
		for ( var j = 0; j < deptCodeList.length; j++) {
			
			var deptCode = deptCodeList[j];
			
			var result = undefined; // 结果对象
			
			// 如果支持城市则优先作为城市代码校验
			if(c.supportCity){
				if(!c.chineseCityName && (/^[\u4e00-\u9fa5]+$/i.test(v))){
					clearInfo(this);
					return c.errMsg;
				}
				
				// 有缓存则查找缓存
				if(c.isCache){
					result = this.validHistoryCity[deptCode];
				}
				
				if(result === undefined){
					var conn = Ext.lib.Ajax.getConnectionObject().conn;
					var url = encodeURI(encodeURI('../gswmgmt/general_findDepartDists.action?distCN=' + deptCode));
					conn.open("get", url, false);
					conn.send(null);
					var city = Ext.util.JSON.decode(conn.responseText);
					// 如果存在则校验通过
					if(city.departDists.length > 0){
						result = {flag : true,typeLevel : 5,city : city.departDists[0]};
					}else{
						result = null;
					}
					if(c.isCache){// 保存缓存信息
						this.validHistoryCity[deptCode] = result;
					}
				}
			}
			
			if(Ext.isEmpty(result)){
				
				// 网点校验
				// 有缓存则查找缓存
				if(c.isCache){
					if(c.onlyCheckCode){
						result = this.validHistoryDeptNoAuth[deptCode];
					}else{
						result = this.validHistoryDept[deptCode];
					}
				}
			
				// 没有再到后台找
				if(Ext.isEmpty(result)){
					var conn = Ext.lib.Ajax.getConnectionObject().conn;
					
					if(c.onlyCheckCode){
						conn.open("get",'../cdpmgmt/commonAction!checkDept.action?deptCode='+ deptCode, false);
					}else{
						conn.open("get",'../cdpmgmt/commonAction!checkDeptCodeAndFindLevel.action?deptCode='+ deptCode, false);
					}
					
					conn.send(null);
					var text = conn.responseText;
					if (text.indexOf("html") != -1) {
						top.location = "../loginmgmt/index.action";
						return true;
					}
					result = Ext.util.JSON.decode(text);
					if(c.isCache){
						// 保存缓存信息
						if(c.onlyCheckCode){
							this.validHistoryDeptNoAuth[deptCode] = result;
						}else{
							this.validHistoryDept[deptCode] = result;
						}
					}
				}
			}
			
			if (!result.flag) {	
				// 校验失败则清空对应的记录
				clearInfo(this);
				if (result.status == '1') {
					// 校验规则修改，所有支持城市代码输入的控件不管输入什么只要不是网点则当作城市代码处理2015-1-14
					if(c.supportCity){
						setInfo(this, {typeLevel : 5});
						return true;
					}
					return c.errMsg;
				}else if (result.status == '2') {
					return '您没有该网点权限,请重新输入!';
				}else{
					// 校验规则修改，所有支持城市代码输入的控件不管输入什么只要不是网点则当作城市代码处理2015-1-14
					if(c.supportCity){
						setInfo(this, {typeLevel : 5});
						return true;
					}
					return c.errMsg;
				}
			}
			var levelName =['总部','经营本部','区部','分点部','网点']
			// 如果结果层级大于检测级别则校验失败
			if(result.typeLevel > c.checkLevel && result.typeLevel != 5){
				// 校验失败则清空对应的记录
				clearInfo(this);
				return '您输入的网点无效，请输入'+levelName[c.checkLevel]+'及'+levelName[c.checkLevel]+'以上层级的网点 ';
			}
			// 如果输入的层级是不支持的层级，则校验失败
			for ( var i = 0; i < c.excludeLevel.length; i++) {
				if(c.excludeLevel[i] == result.typeLevel){
					clearInfo(this);
					return '不支持' + levelName[result.typeLevel] + '层级代码输入!';
				}
			}
			
			//中转场校验，现有中转场级别有3,4
			if(c.supportTransport === true && (result.typeLevel == 3 || result.typeLevel == 4)){
				if(result.deptName.indexOf('中转场') == -1){
					clearInfo(this);
					return '您输入的中转场不正确,请重新输入!';
				}
			}
			
			// 判断多个网点是否在同一个层级
			if(level && level != result.typeLevel){
				// 校验失败则清空对应的记录
				clearInfo(this);
				return '输入的多个网点代码必须处于同一层级!';
			}else{
				level = result.typeLevel;
			}
			
			setInfo(this, result);
			
			// 记录用户的最大网点层级
			if(deptCode == '${deptCode}' || this.userDept == deptCode){
				this.userLevel = result.typeLevel;
			}
		}
		return true;
	}
}

function findModuleByCode(modules, moduleCode) {
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	var url = String
		.format(
			"../cdpmgmt/commonAction!findModuleByCode.action?modules={0}&moduleCode={1}",
			modules, moduleCode
		)
	conn.open("post", url, false);
	conn.send(null);
	var text = conn.responseText;
	var result = Ext.util.JSON.decode(text);
	var module = result.module;
	return module;
}

/**
 * 新增一个页签
 * @param namespace 命名空间 eg: gswmgmt
 * @param moduleCode 模块编号(Action名称) eg : xxx
 * @param condition 参数 eg:{a:'支持中文'}（推荐） 或者 a=xxx&b=b
 */
function createNewTab(namespac,moduleCode,condition){
	var getTopWin = function(win){
		if(win != win.top){
			return getTopWin(win.top)
		}
		return win;
	}
	var top = getTopWin(this);
	var module = findModuleByCode(namespac,moduleCode);
	var itemLabel,itemId,itemURL;
	if(module){
		itemLabel = module.name;
		itemId = module.id;
		itemURL= ".."+module.action+'?'+(typeof(condition) == 'object'? Ext.urlEncode(condition) : condition);
	}
	top.SF.Frame.Nav.gps(top.SF.Frame.Nav.getPath(itemId),true);
}

/**
 * 打开一个非系统页面的页签
 * @param itemId 页签的唯一标识
 * @param itemLabel 页签标题
 * @param itemURL 页签访问地址
 * @param condition 参数 eg:{a:'支持中文'}（推荐） 或者 a=xxx&b=b
 * @param  
 */
function createNewTabNotInSys(itemId,itemLabel,itemURL,condition){
	var getTopWin = function(win){
		if(win != win.top){
			return getTopWin(win.top)
		}
		return win;
	}
	var top = getTopWin(this);
	itemURL += '?'+(typeof(condition) == 'object'? Ext.urlEncode(condition) : condition);
	top.SF.Frame.Tabs.addNewTab(itemId,itemLabel,itemURL);
}


/**
 * 获取浏览器地址参数,有则返回JSON对象，没有则返回NULL值
 * @returns
 */
function getURLParams(){
	if(!window.location.search) return null;
	return Ext.urlDecode(window.location.search.substr(1));
}

getTplType = function() {
	var tplType = Ext.getCmp('_ImportWin').tplType;
	window.location = "download!downloadTpl.action?tplType=" + tplType;
};
/**
 * 导入文件
 */
SF.ImportForm = Ext.extend(Ext.form.FormPanel, {
	constructor : function(config) {
		config = Ext.apply({
			height : 50,
			frame : true,
			labelWidth : 70,
			labelAlign : 'left',
			fileUpload : true,
			items : [{
				layout : 'column',
				items : [{
					columnWidth : .8,
					layout : 'form',
					items : {
						xtype : 'textfield',
						inputType : "file",
						name : "uploadFile",
						fieldLabel : "选择文件",
						anchor : '95%',
						listeners : {
							change : function(field, newValue, oldValue) {
								var index = newValue.lastIndexOf('.');
								if (index != -1) {
									var allow = newValue.substring(index + 1);
									if (allow != 'xls') {
										Ext.MessageBox.alert("提示",
											"请选择符合下列扩展名的文件（大小写不限）：.xls"
										);
										field.setValue('');
									}
								}
							}
						}
					}
				}, {
					columnWidth : .2,
					items : {
						html : "&nbsp;&nbsp;<a style=\"text-decoration:none;color:#11439C;line-height:23px;font-size:12;padding-top:2px;height:25px;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;\"  onmouseover=\"this.style.color=\'red\';this.style.cursor=\'hand\'\" onmouseout=\"this.style.color=\'#11439C\'\" href=\'#\' onClick=\"getTplType()\"><img src=\"../images/authorization/download.gif\" /><span>导入模板.xls</span></a>"
					}
				}, {
					columnWidth : 1,
					items : {
						frame : true,
						html : "<span>请先下载模板，根据模板填写相关信息。填写完成后请上传该文件。 （注：本系统支持OFFICE2003,为了保证系统性能，尽量上传2M以内的文件）。</span>"
					}
				}]
			}]
		}, config
		);
		SF.ImportForm.superclass.constructor.call(this, config);
	}
}
);
Ext.reg('SF.ImportForm', SF.ImportForm);

/**
 * 导入窗口
 */
SF.ImportWindow = Ext.extend(Ext.Window, {
	constructor : function(config) {
		this.tplType = config ? config.tplType : null;
		this.afterLoadGrid = config ? config.afterLoadGrid : null;
		this.successCallback = config ? config.successCallback : null;
		config = Ext.apply({
			id : '_ImportWin',
			title : '导入',
			closeAction : 'hide',
			modal : true,
			plain : true,
			resizable : false,
			width : 600,
			height : 140,
			layout : 'border',
			items : [{
				region : 'center',
				xtype : 'SF.ImportForm'
			}],
			tbar : [{
				text : "保存",
				icon : '${images}/save.gif',
				handler : this.importHandler.createDelegate(this)
			}]
		}, config);
		SF.ImportWindow.superclass.constructor.call(this, config);
	},
	importHandler : function() {
		var form = this.items.get(0).getForm();

		var filePath = form.findField('uploadFile').getValue();
		if (filePath == '') {
			Ext.MessageBox.alert("提示", "请先选择需要导入的文件!");
			return;
		}
		var index = filePath.lastIndexOf('.');
		if (index != -1) {
			var allow = filePath.substring(index + 1);
			if (allow != 'xls') {
				Ext.MessageBox.alert("提示", "请选择符合下列扩展名的文件（大小写不限）：.xls");
				return;
			}
		}

		form.submit({
			waitTitle : "提示",
			waitMsg : "正在导入,请稍后......",
			success : function(form, action) {
				var msg = action.result.msg;
				if (msg == null) {
					Ext.Msg.alert("提示", "导入成功!");
					if (this.afterLoadGrid) {
						Ext.getCmp(this.afterLoadGrid).getStore().reload();
					}
					if (this.successCallback) {
						this.successCallback(action);
					}
					this.hide();
				} else {
					Ext.Msg.alert("提示", msg);
				}
			}.createDelegate(this),
			failure : function(form, action) {
				var msg = action.result.msg;
				if (msg == null) {
					Ext.Msg.alert("提示", "导入失败!");
				} else {
					Ext.Msg.alert("提示", msg);
				}
			}
		});
	}
});
Ext.reg('SF.ImportWindow', SF.ImportWindow);

/**
 * 网点类型下拉框
 */
SF.DeptTypeComboBox = Ext.extend(Ext.form.ComboBox, {
	constructor : function(config) {
		var afterrender = null;
		var select = null;
		if(config.listeners){
			afterrender = config.listeners.afterrender;
			select = config.listeners.select;
		}else{
			config.listeners = {};
		}
		config.listeners.afterrender =  function(me){
				me.store.load({callback : function(){
					me.reset();
				}});
				if(afterrender) afterrender(me);
		};
		config.listeners.select = function(combo,r,i){
				if(combo.childDeptTypeBox){
					var childDeptBox = Ext.getCmp(combo.childDeptTypeBox);
					if(Ext.isEmpty(combo.getValue())){
						childDeptBox.store.proxy.setUrl(childDeptBox.url);
					}else{
						childDeptBox.store.proxy.setUrl("general_findAllChildDeptClassByClassCode.action?classCode=" + combo.getValue());	
					}
					childDeptBox.store.load();
				}
				if(select) select(combo,r,i);
		};
		config = Ext.apply({
			fieldLabel:"网点类型",
			triggerAction:"all",
			lazyRender : true,
			editable: false,
			mode:"local",
			displayField:"className",
			valueField:"classCode",
			value : null,
			store : new Ext.data.JsonStore({
				url:'general_findAllDeptClassTransitNo.action',
				root:'deptTypes',
				fields:['className','classCode'],
				listeners : {
					load : function(store){
						if(store.getCount()>0 && store.getAt(0).get('className') != "全部"){
							store.insert(0,new store.recordType({className:"全部",classCode:null}));
						}
					}
				}
			}),
			reset : function(){
				this.setValue(null);
				this.fireEvent('select',this);
			}
		}, config);
		if(config.url){
			config.store.proxy.setUrl(config.url);
		};
		SF.DeptTypeComboBox.superclass.constructor.call(this, config);
	}
});
Ext.reg('SF.DeptTypeComboBox', SF.DeptTypeComboBox);

/**
 * 网点子类型单选下拉框
 */
SF.DeptChildTypeComboBox = Ext.extend(Ext.form.ComboBox, {
	constructor : function(config) {
		var me = this;
		var afterrender = null;
		if(config.listeners){
			afterrender = config.listeners.afterrender;
		}else{
			config.listeners = {};
		}
		config.listeners.afterrender = function(me){
			me.store.load({callback : function(){
				me.setValue(null);
			}});
			if(afterrender) afterrender(me);
		};
		config = Ext.apply({
			fieldLabel:"网点子类型",
			triggerAction:"all",
			lazyRender : true,
			editable: false,
			mode:"local",
			displayField:"typeName",
			valueField:"typeCode",
			value : null,
			store : new Ext.data.JsonStore({
				url:'general_findAllChildDeptClassNotTransit.action',
				root:'deptTypes',
				fields:['typeName','typeCode'],
				listeners : {
					load : function(store){
						store.insert(0,new store.recordType({typeName:"全部",typeCode:null}));
						
						me.setValue(null);
						
						//store加载完毕执行方法
						if(Ext.isFunction(config.storeLoadCallback)){
							config.storeLoadCallback.call();
						}
					}
				}
			})
		}, config);
		if(config.url){
			config.store.proxy.setUrl(config.url);
		}else{
			config.url = config.store.proxy.url; // 记录原始的请求路径
		}
		
		SF.DeptChildTypeComboBox.superclass.constructor.call(this, config);
	}
});
Ext.reg('SF.DeptChildTypeComboBox', SF.DeptChildTypeComboBox);


/**
 * 网点子类型多选下拉框
 */
try {
	SF.DeptChildTypeLovCombo = Ext.extend(Ext.ux.form.LovCombo, {
		constructor : function(config) {
			var me = this;
			config = Ext.apply({
				fieldLabel:"网点子类型",
				triggerAction:"all",
				lazyRender : true,
				editable: false,
				mode:"local",
				displayField:"typeName",
				valueField:"typeCode",
				showSelectAll:true,
				emptyText: '全部',
				value : null,
				store : new Ext.data.JsonStore({
					url:'general_findAllChildDeptClassNotTransit.action',
					root:'deptTypes',
					fields:['typeName','typeCode'],
					listeners : {
						load : function(store){
							// 把全选按钮勾去掉
							me.selectAll();
							me.deselectAll();
							var div = Ext.select('div.ux-combo-selectall-icon-checked');
							if(div.elements.length > 0){
								div.removeClass("ux-combo-selectall-icon-checked");
								div.addClass("ux-combo-selectall-icon-unchecked");
							}
							//store加载完毕执行方法
							if(Ext.isFunction(config.storeLoadCallback)){
								config.storeLoadCallback.call();
							}
						}
					}
				})
			}, config);
			
			if(config.url){
				config.store.proxy.setUrl(config.url);
			}else{
				config.url = config.store.proxy.url; // 记录原始的请求路径
			}
			SF.DeptChildTypeComboBox.superclass.constructor.call(this, config);
		}
	});
	Ext.reg('SF.DeptChildTypeLovCombo', SF.DeptChildTypeLovCombo);  
}catch(exception){}



SF.TreePanel = Ext.extend(Ext.tree.TreePanel, {
	constructor : function(config) {
		var deptLoader = new Ext.tree.TreeLoader({
			dataUrl : "../authorization/showUserDeptList.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=",
			url : "../authorization/showUserDeptList.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
		});
		var deptRoot = new Ext.tree.AsyncTreeNode({
			id : "0",
//			deptCode:"001",
			text : "顺丰速运(集团)有限公司"
		});
		config = Ext.apply({
			region : "west",
			title : "网点信息",
			autoScroll : true,
			split : true,
			collapsible : true,
			checkModel:"multiple",
			width : 250,
			loader : deptLoader,
			root : deptRoot
		}, config);
		SF.TreePanel.superclass.constructor.call(this, config);

	},
	onInitHandler : function(loader) {
		this.fireEvent('click', this.getRootNode());
	}
});

Ext.reg('SF.TreePanel', SF.TreePanel);

SF.TreePanelWin = Ext.extend(Ext.Window, {
	showCheckedTreeAction:"../authorization/deptTreeList.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=",
	treeAction:"../authorization/showUserDeptList.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=",
	constructor : function(config) {
		this.config = config;
		if(config.checkTree){
			this.url = this.showCheckedTreeAction;
			this.baseAttributes = {uiProvider:Ext.tree.TreeCheckNodeUI};
			this.checkModel = "multiple";
		}else{
			this.url = this.treeAction;
			this.baseAttributes = null;
			this.checkModel = null;
		}
		var deptLoader = new Ext.tree.TreeLoader({
			baseAttrs:this.baseAttributes,
			dataUrl : this.url,
			url : this.url,
			listeners : {
				beforeload : function(treeLoader,node){
					this.baseParams.exceptionId = node.id;
					this.baseParams.onlyShowNonoperate = "yes";
				}
			}
		});
		var deptRoot = new Ext.tree.AsyncTreeNode({
			text : "顺丰速运(集团)有限公司",
			id : "0"
		});
		
		config = Ext.apply({
			title : '请选择适用地区',
			closeAction : 'hide',
			modal : true,
			plain : true,
			resizable : false,
			width : 600,
			height : 450,
			layout:'fit',
			items:[{
				xtype:'treepanel',
				checkModel:this.checkModel,
				onlyLeafCheckable:false,
				autoScroll : true,
				loader : deptLoader,
				root : deptRoot,
				selectedNode:null,
				listeners : {
					click: function(node){
						this.selectedNode = node;
					},
					dblclick : this.onNodeDBLClickHandler.createDelegate(this)
				}
			}],
			tbar : [{
				text : "选择",
				
				handler : this.select.createDelegate(this)
			},{
				text : "取消",
				handler : function(){
					this.hide();
				},
				scope:this
			}],
			listeners : {
				show: function(){
//					if(config.checkTree){
////						alert(config.nodeIds);
//					}
//					if(!config.checkTree){
//						var node = this.loadForm.getForm().findField('nonoperaMailconfig.exceptiontypeid');
//						if(node){
//							var nodeId = node.getValue();
//							if(nodeId && nodeId != ''){
//								var ids = [];
//								var path = "";
//								var record = Ext.getCmp('nonoperaMailconfigGrid').getSelectionModel().getSelected();
//								for(var i=0;i<4;i++){
//									ids.push(record.get('levelid0'+i));
//									path += '/' + record.get('levelid0'+i);
//								}
//								ids.push(record.get('exceptiontypeid'));
//								path += '/' + record.get('exceptiontypeid');
//								
//								var tree = this.items.get(0);
//								tree.collapseAll();
//								
//								var expandSelf = function(i){
//									if(!tree.getNodeById(ids[i]).leaf && i<=ids.length-1){
//										tree.getNodeById(ids[i]).expand(false,true,function(){
//											expandSelf(i+1);
//										});
//									}else{
//										tree.selectPath(path);
//									}
//								}
//								expandSelf(0);
//							}
//						}
//					}
//					var tree = this.items.get(0);
//					tree.collapseAll();
//					var path = "/0/1/3";
//					tree.selectPath(path,function(bSuccess, oLastNode){
////						if(bSuccess){
//	alert(1);
//							oLastNode.getUI().checkbox.checked = true;
////						}
//					});
//					tree.getNodeById('3').expand(false,true,function(){
////						expandSelf(i+1);
//					});
					
					//node.getUI().checkbox.checked = true;
				}
			}
		}, config);
		SF.TreePanelWin.superclass.constructor.call(this, config);
	},
	setLoadForm : function(form) {
		this.loadForm = form;
	},
	setData : function(data){
		this.loadForm.setData(data);
		this.hide();
	},
	select : function() {
		if(this.config.checkTree){
			var nodes = this.items.get(0).getChecked();
			var arrayId = [];
			var arrayDeptCode = [];
			var arrayDeptName = [];
			for(var i=0; i<nodes.length; i++){
//				if(nodes[i].attributes.typeLevel == 4){
					arrayId.push(nodes[i].attributes.id)
					var text = nodes[i].attributes.text;
					arrayDeptCode.push(text.split('/')[0])
					arrayDeptName.push(text.split('/')[1])
//				}
			}
			var obj = {};
			obj.id = arrayId;
			obj.deptCode = arrayDeptCode;
			obj.deptName = arrayDeptName;
			
//			this.setData(obj);
			
			if(Ext.isFunction(this.config.callback)){
				this.config.callback(obj);
			}
//			if(this.config.resultId){
////				alert(arrayText);
//				Ext.getCmp(this.config.resultId).setValue(arrayText);
//			}
			
		}else{
			this.onNodeDBLClickHandler(this.items.get(0).selectedNode);
		}
		
		this.hide();
	},
	onNodeDBLClickHandler : function(node) {
		if(!this.config.checkTree){
			if(node == null || !node.attributes){
				return ;
			}
			var obj = {};
			obj.id = node.attributes.id;
			obj.text = node.attributes.text;
			this.setData(obj);
		}
	}
});



SF.CityTreePanelWin = Ext.extend(Ext.Window, {
	showCheckedTreeAction:"../authorization/districtTreeList.action?textField=distName&idField=distId&leafField=&clsField=&childrenField=",
	constructor : function(config) {
		this.config = config;
		this.showCheckedTreeAction=this.showCheckedTreeAction+"&distCodes="+this.config.distCodes;
		this.url = this.showCheckedTreeAction;
		this.baseAttributes = {uiProvider:Ext.tree.TreeCheckNodeUI};
		this.checkModel = "multiple";
		 
		var deptLoader = new Ext.tree.TreeLoader({
			baseAttrs:this.baseAttributes,
			dataUrl : this.url,
			url : this.url,
			listeners : {
				beforeload : function(treeLoader,node){
					this.baseParams.exceptionId = node.id;
					this.baseParams.onlyShowNonoperate = "yes";
				} 
			}
		});
		var deptRoot = new Ext.tree.AsyncTreeNode({
			text : "世界",
			id : "0"
		});
		
		config = Ext.apply({
			title : '请选择城市',
			closeAction : 'hide',
			modal : true,
			plain : true,
			resizable : false,
			width : 600,
			height : 450,
			layout:'fit',
			items:[{
				xtype:'treepanel',
				id:'cityTreeId',
				checkModel:this.checkModel,
				onlyLeafCheckable:false,
				autoScroll : true,
				loader : deptLoader,
				root : deptRoot,
				selectedNode:null,
				listeners : {
					click: function(node){
						this.selectedNode = node;
					},
					dblclick : this.onNodeDBLClickHandler.createDelegate(this)
				}
			}],
			tbar : [{
				text : "选择",
				
				handler : this.select.createDelegate(this)
			},{
				text : "取消",
				handler : function(){
					this.hide();
				},
				scope:this
			}],
			listeners : {
				show: function(){
					expandSelectNode();
				}
			}
		}, config);
		SF.TreePanelWin.superclass.constructor.call(this, config);
	},
	setLoadForm : function(form) {
		this.loadForm = form;
	},
	setData : function(data){
		this.loadForm.setData(data);
		this.hide();
	},
	select : function() {
		if(this.config.checkTree){
			var nodes = this.items.get(0).getChecked();
			var arrayId = [];
			var arrayDeptCode = [];
			var arrayDeptName = [];
			for(var i=0; i<nodes.length; i++){
					arrayId.push(nodes[i].attributes.id)
					var text = nodes[i].attributes.text;
					arrayDeptCode.push(text.split('/')[0])
					arrayDeptName.push(text.split('/')[1])
			}
			var obj = {};
			obj.id = arrayId;
			obj.code = arrayDeptCode;
			obj.name = arrayDeptName;
			
			
			if(Ext.isFunction(this.config.callback)){
				this.config.callback(obj);
			}
			
		}else{
			this.onNodeDBLClickHandler(this.items.get(0).selectedNode);
		}
		
		this.hide();
	},
	onNodeDBLClickHandler : function(node) {
		if(!this.config.checkTree){
			if(node == null || !node.attributes){
				return ;
			}
			var obj = {};
			obj.id = node.attributes.id;
			obj.text = node.attributes.text;
			this.setData(obj);
		}
	}
});