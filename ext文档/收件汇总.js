
Ext.namespace('SF');
 
Ext.QuickTips.init();
 
Ext.pageSize = 25;
Ext.base = "/cdp";
Ext.HOME = "../ext-3.4.0/";
Ext.BLANK_IMAGE_URL = Ext.HOME + "resources/images/default/s.gif";
/** ������Чʱ��ʮ����� */
Ext.Ajax.timeout = 15 * 60 * 1000;
Ext.Msg.minWidth = 160;
 
var prompt = "��ʾ";
Ext.form.Field.prototype.msgTarget = "qtip"; // side|qtip
 
 
/**��ȡ��������ʵʱʱ�� SF.SYSTEM_DATE �Ǿ�̬�ģ�SF.DYNAMIC_SYSTEM_DATE �Ƕ�̬��,��Ҫ�������Action����д���� getSystemTime() �Żظ�ʽΪ"Y-m-d H:i:s" ���ַ���**/
/*try {
if(""){
	SF.SYSTEM_DATE = Date.parseDate("","Y-m-d H:i:s");
	SF.DYNAMIC_SYSTEM_DATE = Date.parseDate("","Y-m-d H:i:s");
	window.setInterval(function(){
		SF.DYNAMIC_SYSTEM_DATE = SF.DYNAMIC_SYSTEM_DATE.add(Date.SECOND,1);
	},1000)
}
} catch (e) {}*/
 
/**
 * XML��ʽ���ݸ�ʽ��
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
 * gridˢ�º������λ�ò���
 */
Ext.override(Ext.grid.GridView, {
    scrollToTop : function() {
        var dom = this.scroller.dom;
        
        dom.scrollTop  = 0;
//        dom.scrollLeft = 0;
    }
});
 
 
/**
 * ��дģ���ȡֵ��ʽ
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
 * gridˢ�º������λ�ò���
 */
 
Ext.getComboTpl = function(displayField){
	return '<tpl for="."><div class="x-combo-list-item" ext:qtip="{'+displayField+'}">{'+displayField+'}</div></tpl>';
}
 
Ext.apply(Ext.form.BasicForm.prototype, {
	showErrorMsg : true
});
 
/** extjs��gridpanel���������ݲ��ܸ��ƽ���취. */
if (!Ext.grid.GridView.prototype.templates) {   
    Ext.grid.GridView.prototype.templates = {};   
}   
Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(   
     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,   
     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,   
     '</td>'   
);
 
// �޸��취���ȸ��������,table�ĵ�Ԫ��ʵ�ʿ��=ָ�����+padding������ֻҪ��дgridview���һ�����������£�
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
 
/** ��д����֤����,������֤������Ϣ */
Ext.override(Ext.form.BasicForm, {
	isValid : function() {
		var valid = true;
		for (var i = 0; i < this.items.length; i++) {
			var f = this.items.get(i);
			if (!f.validate()) {
				valid = false;
				if (this.showErrorMsg) {
					Ext.Msg.alert("��ʾ", f.fieldLabel + ":" + f.activeError,
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
 
/** ��󳤶ȼ��㺺�ִ����� */
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
		return String.format(maxLenText + "��ǰ������" + len + "��.");
	}
	return true;
};
 
/** ������֤,��ʼ���ڲ��ܴ��ڽ������� */
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
	    	 
	    	 // ������Сʱ��
	         endDateField.setMinValue(date);
	         
	         // �������ʱ��
	         if(field.dateLimit){
		         var maxDate = new Date(date.getFullYear(),date.getMonth(),date.getDate() + (field.dateLimit - 1));
		         endDateField.setMaxValue(maxDate);	         
//		         if(!window.maxText) window.maxText = endDateField.maxText;
//     			 var maxText = String.format(window.maxText, endDateField.formatDate(maxDate));
//     			 $('td.x-date-disabled').attr('title',maxText);
		         
	         }
 
	         if(!Ext.isDate(endDateField.getValue())) return true;
	    	 // ��ȡ����ʱ��
	         var end = endDateField.getValue();
	         
	         if(end.getTime() < date.getTime()){
	        	 endDateField.setValue(date);
	         }
	         
	         if(field.dateLimit){
		         // ���ʱ���������������
		         if(end.getTime() - date.getTime() >= (field.dateLimit * 3600 * 24 * 1000)){
		        	 endDateField.setValue(maxDate);
		         }
	         }
 
	     }
	     return true;
	}, // ����ֵ����ֻ��ʾ
	dateLimit2 : function(val, field) {
		
		 var date = field.parseDate(val);
		 
		 if(!Ext.isDate(date)) return false;
 
	     if (field.endDateField) {
	         
	    	 // ��ȡ����ʱ��
	    	 var endDateField = Ext.getCmp(field.endDateField);
	    	 
	         if(!Ext.isDate(endDateField.getValue())) return false;
	         
	    	 // ��ȡʱ��
	         var end = endDateField.getValue();
	    	 
	         if(end.getTime() < date.getTime()){
	        	 field.vtypeText = field.fieldLabel + '���ܴ���' + endDateField.fieldLabel;
	        	 return false;
	         }
	        
	         if(field.dateLimit){
		         // ���ʱ���������������
		         if(end.getTime() - date.getTime() >= (field.dateLimit * 3600 * 24 * 1000)){
		        	 field.vtypeText = field.fieldLabel + '��' + endDateField.fieldLabel +'�ļ�����ܳ���' + field.dateLimit +'��!';
		        	 return false;
		         }
	         }
	         endDateField.clearInvalid();
	     }else if(field.startDateField){
	    	 // ��ȡ����ʱ��
	    	 var startDateField = Ext.getCmp(field.startDateField);
	    	 
	         if(!Ext.isDate(startDateField.getValue())) return false;
	         
	    	 // ��ȡʱ��
	         var start = startDateField.getValue();
	    	 
	         if(start.getTime() > date.getTime()){
	        	 field.vtypeText = field.fieldLabel + '����С��' + startDateField.fieldLabel;
	        	 return false;
	         }
	        
	         if(field.dateLimit){
		         // ���ʱ���������������
		         if( date.getTime() - start.getTime() >= (field.dateLimit * 3600 * 24 * 1000)){
		        	 field.vtypeText = startDateField.fieldLabel + '��' + field.fieldLabel +'�ļ�����ܳ���' + field.dateLimit +'��!';
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
 * ��дGridPanel �����б�ͷ��ͷ�������˵�
 */
Ext.override(Ext.grid.GridPanel, {
	enableHdMenu : false
});
 
/**
 * ��дExt.grid.RowNumberer ʹGRID��ҳʱʵ���������
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
 
//��ʽǧ��λ
Ext.formatThousands = function(value){
	if(value != null){
		return (parseInt(value) + '').replace(/\d{1,3}(?=(\d{3})+$)/g, '$&,');
	}
    return value;
}
 
/**
 * ����չʾΪͼƬ
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
			value='��';
		}
		return value;
	}
	return '��';
}
 
/**
 * ����������ΪreadOnly�������ɫ��ʾ
 */
Ext.override(Ext.form.Field, {
	getAutoCreate : function() {
		var cfg = Ext.isObject(this.autoCreate) ? this.autoCreate : Ext.apply(
		{}, this.defaultAutoCreate
		);
		// �ļ���ֻ��
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
 * ����������ΪreadOnly�������ɫ��ʾ
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
 * ��дGrid.Column.renderer ÿ����ͣʱ��ʾ������Ϣ
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
 * ��дExt.QuickTip ���������ݱ���
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
 
 
 
 
 
// �����ļ��ϴ���
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
 * ����grid��ҳ������
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
 * ȡ�ý��쿪ʼ���� �������� -2��ǰ�졢-1�����졢0�����졢1�����졢2������
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
					field.markInvalid('����������㲻��ȷ,����������!');
					Ext.MessageBox.alert(prompt,
					field.activeError, function() {
						// field.focus(true,false);
					}, this
					);
				} else if (result.status == '2') {
					field.markInvalid('��û�и�����Ȩ��,����������!');
					Ext.MessageBox.alert(prompt,
					field.activeError, function() {
						// field.focus(true,false);
					}, this
					);
				}
				return false;
			} else {
				if (result.typeLevel > 2) {
					field.markInvalid('ֻ���������������ϲ㼶������,����������!');
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
					field.markInvalid('����������㲻��ȷ,����������!');
					Ext.MessageBox.alert("��ʾ", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '2') {
					field.markInvalid('��û�и�����Ȩ��,����������!');
					Ext.MessageBox.alert("��ʾ", field.activeError, function() {
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
					field.markInvalid('����������㲻��ȷ,����������!');
					Ext.MessageBox.alert("��ʾ", field.activeError,
					function() {
						field.focus(true, false);
					}, this
					);
				} else if (result.status == '2') {
					field.markInvalid('��û�и�����Ȩ��,����������!');
					Ext.MessageBox.alert("��ʾ", field.activeError,
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
	var deptCode = field.getValue().trim().toUpperCase().replace("��",",");
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
					field.markInvalid('�����������  ' + result.depts
						+ '  ����ȷ��������Ȩ��,�������������!');
					Ext.MessageBox.alert("��ʾ", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '2') {
					field.markInvalid('����������㲻��ȷ��������Ȩ��,����������!');
					Ext.MessageBox.alert("��ʾ", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '3') {
					field.markInvalid('�����������Ȩ�޲㼶��һ��,����������!');
					Ext.MessageBox.alert("��ʾ", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '5') {
					field
						.markInvalid('�������������ʽ�����⣬��������6��������룬�ο���ʽ:755B,755D,755BF');
					Ext.MessageBox.alert("��ʾ", field.activeError, function() {
						field.focus(true, false);
					}, this);
				} else if (result.status == '6') {
					field.markInvalid('���������ظ����������, ����������!');
					Ext.MessageBox.alert("��ʾ", field.activeError, function() {
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
				field.markInvalid('����������㲻��ȷ,����������!');
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
				field.markInvalid('����������㲻��ȷ,����������!');
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
				field.markInvalid('�������Ա���Ų���ȷ,����������!');
				Ext.MessageBox.alert("��ʾ", field.activeError, function() {
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
 * ���������֤����ȫ��������������ƺͳ��д���У��
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
					field.markInvalid('������������������Ʋ���ȷ,����������!');
					Ext.MessageBox.alert(prompt,
					field.activeError, function() {
					}, this
					);
				} else if (result.status == '2') {
					field.markInvalid('��û�и�����Ȩ��,����������!');
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
 * �������ͳ���ͨ����֤��(֧�ֶ��������������)
 * ʧ�ܷ���У����Ϣ���ɹ��򷵻�true��ʧ���򷵻���Ϣ��ʾ
 * �����Ҫ��ȡ��ǰ��������㼶����ֱ�ӻ�ȡ���������ߵ�typeLevel����
 * �����Ҫ�û��������㼶����ֱ�ӻ�ȡ���������ߵ�userLevel����
 * �����Ҫ��ȡ��ǰ����������������ƣ���ֱ�ӻ�ȡ���������ߵ�deptName����
 * ��ȡ��ǰ���������������֤�����Ϣ����ֱ�ӻ�ȡ���������ߵ�resultInfo����
 * @param checkLevel  У�鼶�� 0 - 4�ֱ�����ܲ�- ����
 * @param supportCity   �Ƿ�֧�ֳ��д������루֧���������룩
 * @param errMsg У��ʧ��ʱ����ʾ��Ϣ
 * @param isCache �Ƿ񻺴��Ѳ�ѯ������
 * @param onlyCheckCode �Ƿ�ֻУ��������ȷ�ԣ���У��Ȩ�ޣ�
 * @param multiCode �Ƿ�֧�ֶ��������������
 * @param multiCount  �����������������
 * @returns
 */
function getDeptCodeValidator(opts) {
	
	var c = {
		checkLevel : 4, // У�鼶�� 0 - 4�ֱ�����ܲ�- ���� 
		excludeLevel : [], // ��֧�����������㼶
		supportCity : false, // �Ƿ�֧�ֳ������Ƽ���������
		supportTransport : false, // �Ƿ�֧����ת������,����checkLevel == 3  ���� checkLevel == 4ʱ��Ч
		chineseCityName : false, // �Ƿ�֧�����ĳ������ƵĲ�ѯ
		errMsg : '����������㲻��ȷ,����������!', // ͨ����ʾ��Ϣ
		isCache : true,	// �Ƿ񻺴��Ѳ�ѯ������
		onlyCheckCode : false, // �Ƿ�ֻУ��������ȷ�ԣ���У��Ȩ�ޣ�
		multiCode : false, // �Ƿ�֧�ֶ��������������
		multiCount : 6 // �����������������
	}
	
	Ext.apply(c,opts);
	
	var clearInfo = function(obj){
		// ��յ�ǰ����㼶
		obj.typeLevel = null;
		// ��յ�ǰ�������������
		obj.deptName = null;
		// ������н����Ϣ��Ϣ
		obj.resultInfo = null;
	}
	var setInfo = function(obj,result){
		// ��¼��ǰ����Ĳ㼶
		obj.typeLevel = result.typeLevel;
		// ��¼��ǰ�������������
		obj.deptName = result.deptName;
		// ��¼���н����Ϣ��Ϣ
		obj.resultInfo = result;
	}
	
	return function(v){
		
		// ��¼����У����ʷ
		if(!this.validHistoryDept && c.isCache){
			this.validHistoryDept = {};
			this.validHistoryDeptNoAuth = {};
		}
		// ��¼����У����ʷ
		if(!this.validHistoryCity && c.isCache){
			this.validHistoryCity = {};
		}
		
		if(Ext.isEmpty(v)) {
			// У��ʧ������ն�Ӧ�ļ�¼
			clearInfo(this);
			return true;
		}
		v = v.trim().replace(new RegExp('��','g'),',').toUpperCase();
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
				return '������'+c.multiCount+'���������!';
			}
		}else{
			deptCodeList.push(v.toUpperCase());
		}
		var level;
		
		for ( var j = 0; j < deptCodeList.length; j++) {
			
			var deptCode = deptCodeList[j];
			
			var result = undefined; // �������
			
			// ���֧�ֳ�����������Ϊ���д���У��
			if(c.supportCity){
				if(!c.chineseCityName && (/^[\u4e00-\u9fa5]+$/i.test(v))){
					clearInfo(this);
					return c.errMsg;
				}
				
				// �л�������һ���
				if(c.isCache){
					result = this.validHistoryCity[deptCode];
				}
				
				if(result === undefined){
					var conn = Ext.lib.Ajax.getConnectionObject().conn;
					var url = encodeURI(encodeURI('../gswmgmt/general_findDepartDists.action?distCN=' + deptCode));
					conn.open("get", url, false);
					conn.send(null);
					var city = Ext.util.JSON.decode(conn.responseText);
					// ���������У��ͨ��
					if(city.departDists.length > 0){
						result = {flag : true,typeLevel : 5,city : city.departDists[0]};
					}else{
						result = null;
					}
					if(c.isCache){// ���滺����Ϣ
						this.validHistoryCity[deptCode] = result;
					}
				}
			}
			
			if(Ext.isEmpty(result)){
				
				// ����У��
				// �л�������һ���
				if(c.isCache){
					if(c.onlyCheckCode){
						result = this.validHistoryDeptNoAuth[deptCode];
					}else{
						result = this.validHistoryDept[deptCode];
					}
				}
			
				// û���ٵ���̨��
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
						// ���滺����Ϣ
						if(c.onlyCheckCode){
							this.validHistoryDeptNoAuth[deptCode] = result;
						}else{
							this.validHistoryDept[deptCode] = result;
						}
					}
				}
			}
			
			if (!result.flag) {	
				// У��ʧ������ն�Ӧ�ļ�¼
				clearInfo(this);
				if (result.status == '1') {
					// У������޸ģ�����֧�ֳ��д�������Ŀؼ���������ʲôֻҪ���������������д��봦��2015-1-14
					if(c.supportCity){
						setInfo(this, {typeLevel : 5});
						return true;
					}
					return c.errMsg;
				}else if (result.status == '2') {
					return '��û�и�����Ȩ��,����������!';
				}else{
					// У������޸ģ�����֧�ֳ��д�������Ŀؼ���������ʲôֻҪ���������������д��봦��2015-1-14
					if(c.supportCity){
						setInfo(this, {typeLevel : 5});
						return true;
					}
					return c.errMsg;
				}
			}
			var levelName =['�ܲ�','��Ӫ����','����','�ֵ㲿','����']
			// �������㼶���ڼ�⼶����У��ʧ��
			if(result.typeLevel > c.checkLevel && result.typeLevel != 5){
				// У��ʧ������ն�Ӧ�ļ�¼
				clearInfo(this);
				return '�������������Ч��������'+levelName[c.checkLevel]+'��'+levelName[c.checkLevel]+'���ϲ㼶������ ';
			}
			// �������Ĳ㼶�ǲ�֧�ֵĲ㼶����У��ʧ��
			for ( var i = 0; i < c.excludeLevel.length; i++) {
				if(c.excludeLevel[i] == result.typeLevel){
					clearInfo(this);
					return '��֧��' + levelName[result.typeLevel] + '�㼶��������!';
				}
			}
			
			//��ת��У�飬������ת��������3,4
			if(c.supportTransport === true && (result.typeLevel == 3 || result.typeLevel == 4)){
				if(result.deptName.indexOf('��ת��') == -1){
					clearInfo(this);
					return '���������ת������ȷ,����������!';
				}
			}
			
			// �ж϶�������Ƿ���ͬһ���㼶
			if(level && level != result.typeLevel){
				// У��ʧ������ն�Ӧ�ļ�¼
				clearInfo(this);
				return '����Ķ�����������봦��ͬһ�㼶!';
			}else{
				level = result.typeLevel;
			}
			
			setInfo(this, result);
			
			// ��¼�û����������㼶
			if(deptCode == '' || this.userDept == deptCode){
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
 * ����һ��ҳǩ
 * @param namespace �����ռ� eg: gswmgmt
 * @param moduleCode ģ����(Action����) eg : xxx
 * @param condition ���� eg:{a:'֧������'}���Ƽ��� ���� a=xxx&b=b
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
	top.SF.Frame.Tabs.addNewTab(itemId,itemLabel,itemURL);
}
 
/**
 * ��һ����ϵͳҳ���ҳǩ
 * @param itemId ҳǩ��Ψһ��ʶ
 * @param itemLabel ҳǩ����
 * @param itemURL ҳǩ���ʵ�ַ
 * @param condition ���� eg:{a:'֧������'}���Ƽ��� ���� a=xxx&b=b
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
 * ��ȡ�������ַ����,���򷵻�JSON����û���򷵻�NULLֵ
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
 * �����ļ�
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
						fieldLabel : "ѡ���ļ�",
						anchor : '95%',
						listeners : {
							change : function(field, newValue, oldValue) {
								var index = newValue.lastIndexOf('.');
								if (index != -1) {
									var allow = newValue.substring(index + 1);
									if (allow != 'xls') {
										Ext.MessageBox.alert("��ʾ",
											"��ѡ�����������չ�����ļ�����Сд���ޣ���.xls"
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
						html : "&nbsp;&nbsp;<a style=\"text-decoration:none;color:#11439C;line-height:23px;font-size:12;padding-top:2px;height:25px;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;\"  onmouseover=\"this.style.color=\'red\';this.style.cursor=\'hand\'\" onmouseout=\"this.style.color=\'#11439C\'\" href=\'#\' onClick=\"getTplType()\"><img src=\"../images/authorization/download.gif\" /><span>����ģ��.xls</span></a>"
					}
				}, {
					columnWidth : 1,
					items : {
						frame : true,
						html : "<span>��������ģ�壬����ģ����д�����Ϣ����д��ɺ����ϴ����ļ��� ��ע����ϵͳ֧��OFFICE2003,Ϊ�˱�֤ϵͳ���ܣ������ϴ�2M���ڵ��ļ�����</span>"
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
 * ���봰��
 */
SF.ImportWindow = Ext.extend(Ext.Window, {
	constructor : function(config) {
		this.tplType = config ? config.tplType : null;
		this.afterLoadGrid = config ? config.afterLoadGrid : null;
		this.successCallback = config ? config.successCallback : null;
		config = Ext.apply({
			id : '_ImportWin',
			title : '����',
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
				text : "����",
				icon : '../images/gswmgmt/save.gif',
				handler : this.importHandler.createDelegate(this)
			}]
		}, config);
		SF.ImportWindow.superclass.constructor.call(this, config);
	},
	importHandler : function() {
		var form = this.items.get(0).getForm();
 
		var filePath = form.findField('uploadFile').getValue();
		if (filePath == '') {
			Ext.MessageBox.alert("��ʾ", "����ѡ����Ҫ������ļ�!");
			return;
		}
		var index = filePath.lastIndexOf('.');
		if (index != -1) {
			var allow = filePath.substring(index + 1);
			if (allow != 'xls') {
				Ext.MessageBox.alert("��ʾ", "��ѡ�����������չ�����ļ�����Сд���ޣ���.xls");
				return;
			}
		}
 
		form.submit({
			waitTitle : "��ʾ",
			waitMsg : "���ڵ���,���Ժ�......",
			success : function(form, action) {
				var msg = action.result.msg;
				if (msg == null) {
					Ext.Msg.alert("��ʾ", "����ɹ�!");
					if (this.afterLoadGrid) {
						Ext.getCmp(this.afterLoadGrid).getStore().reload();
					}
					if (this.successCallback) {
						this.successCallback(action);
					}
					this.hide();
				} else {
					Ext.Msg.alert("��ʾ", msg);
				}
			}.createDelegate(this),
			failure : function(form, action) {
				var msg = action.result.msg;
				if (msg == null) {
					Ext.Msg.alert("��ʾ", "����ʧ��!");
				} else {
					Ext.Msg.alert("��ʾ", msg);
				}
			}
		});
	}
});
Ext.reg('SF.ImportWindow', SF.ImportWindow);
 
/**
 * ��������������
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
			fieldLabel:"��������",
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
						if(store.getCount()>0 && store.getAt(0).get('className') != "ȫ��"){
							store.insert(0,new store.recordType({className:"ȫ��",classCode:null}));
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
 * ���������͵�ѡ������
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
			fieldLabel:"����������",
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
						store.insert(0,new store.recordType({typeName:"ȫ��",typeCode:null}));
						
						me.setValue(null);
						
						//store�������ִ�з���
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
			config.url = config.store.proxy.url; // ��¼ԭʼ������·��
		}
		
		SF.DeptChildTypeComboBox.superclass.constructor.call(this, config);
	}
});
Ext.reg('SF.DeptChildTypeComboBox', SF.DeptChildTypeComboBox);
 
 
/**
 * ���������Ͷ�ѡ������
 */
try {
	SF.DeptChildTypeLovCombo = Ext.extend(Ext.ux.form.LovCombo, {
		constructor : function(config) {
			var me = this;
			config = Ext.apply({
				fieldLabel:"����������",
				triggerAction:"all",
				lazyRender : true,
				editable: false,
				mode:"local",
				displayField:"typeName",
				valueField:"typeCode",
				showSelectAll:true,
				emptyText: 'ȫ��',
				value : null,
				store : new Ext.data.JsonStore({
					url:'general_findAllChildDeptClassNotTransit.action',
					root:'deptTypes',
					fields:['typeName','typeCode'],
					listeners : {
						load : function(store){
							// ��ȫѡ��ť��ȥ��
							me.selectAll();
							me.deselectAll();
							var div = Ext.select('div.ux-combo-selectall-icon-checked');
							if(div.elements.length > 0){
								div.removeClass("ux-combo-selectall-icon-checked");
								div.addClass("ux-combo-selectall-icon-unchecked");
							}
							//store�������ִ�з���
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
				config.url = config.store.proxy.url; // ��¼ԭʼ������·��
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
			text : "˳������(����)���޹�˾"
		});
		config = Ext.apply({
			region : "west",
			title : "������Ϣ",
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
			text : "˳������(����)���޹�˾",
			id : "0"
		});
		
		config = Ext.apply({
			title : '��ѡ�����õ���',
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
				text : "ѡ��",
				
				handler : this.select.createDelegate(this)
			},{
				text : "ȡ��",
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
			text : "����",
			id : "0"
		});
		
		config = Ext.apply({
			title : '��ѡ�����',
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
				text : "ѡ��",
				
				handler : this.select.createDelegate(this)
			},{
				text : "ȡ��",
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
 
/*
 * ���ַ���תΪ�������ͣ���ʽ��strDate='2015-10-10'��
 */
function stringParseDate(strDate){
    var dependedVal=strDate;
    //���������ַ���ת��������
    var regEx = new RegExp("\\-","gi");
    dependedVal=dependedVal.replace(regEx,"/");
    var milliseconds=Date.parse(dependedVal); 
    var dependedDate=new Date();
    dependedDate.setTime(milliseconds);
    return dependedDate
}
 
 
 
//
 
Ext.ns('SF.mdp.receiveDetail');
 
var tmTypes={
	8 : "00:00-08:00",
	9 : "08:00-09:00",
	10 : "09:00-10:00",
	11 : "10:00-11:00",
	12 : "11:00-12:00",
	13 : "12:00-13:00",
	14 : "13:00-14:00",
	15 : "14:00-15:00",
	16 : "15:00-16:00",
	17 : "16:00-17:00",
	18 : "17:00-18:00",
	19 : "18:00-19:00",
	20 : "19:00-20:00",
	21 : "20:00-21:00",
	22 : "21:00-22:00",
	23 : "22:00-23:00",
	24 : "23:00-24:00"
};
//��Ʒ����
var productStore = new Ext.data.JsonStore({
	url:'receivedDetail_findAllProductType.action',
	root:'productTypes',
	fields:['id','typeName','typeCode']
});
 
var flowTypeStore = new Ext.data.ArrayStore({
	fields:['id','name'],
	data:[
		['1',"ͬ��"],
		['2',"����"],
		['3',"ʡ��"],
		['4',"��������ʡ"],
		['5',"�籾����ʡ"],
		['6',"�����ڿ�ʡ"],
		['7',"�籾��"],
		['8',"����"]
	]
});
 
 
var countTypeStore = new Ext.data.ArrayStore({
	fields:['id','name'],
	data:[
		['1',"���ļ�����"],
		['2',"���ļ�ʱ��"],
		['3',"������Ŀ�ĵ�"],
		['4',"���ĳ�����"]]
});
 
//(�ļ�����չʾ��ʽstore����Դ)1-4Ϊͳ�Ʒ�ʽid
 
var receiveDeptResource = {
		'1':{'receive' : [ ['0',"ȫ��"],['1','�ļ�BG'],['2','�ļ���Ӫ����'],['3','�ļ���������'],['4','�ļ�����'],['5','�ļ�����+����'],['6','�ļ�����'],['7','�ļ��ֲ�'],['8','�ļ�����']]},
		'2':{'receive' : [['0',"ȫ��"],['1','�ļ�BG'],['2','�ļ���Ӫ����'],['3','�ļ���������'],['4','�ļ�����'],['5','�ļ�����+����'],['6','�ļ�����']]},
		'3':{'dest' : [['0','ȫ��'],['1','Ŀ��BG'],['2','Ŀ�ľ�Ӫ����'],['3','Ŀ�Ķ�������'],['4','Ŀ�ĵ���'],['5','Ŀ�ĵ���+����'],['6','Ŀ�ĳ���'],['7','Ŀ�ķֲ�'],['8','Ŀ������']]},
		'4':{'receive' : [ ['0',"ȫ��"],['1','�ļ�BG'],['2','�ļ���Ӫ����'],['3','�ļ���������'],['4','�ļ�����'],['5','�ļ�����+����'],['6','�ļ�����']],
			'dest' : [['0','ȫ��'],['1','Ŀ��BG'],['2','Ŀ�ľ�Ӫ����'],['3','Ŀ�Ķ�������'],['4','Ŀ�ĵ���'],['5','Ŀ�ĵ���+����'],['6','Ŀ�ĳ���']]}
};
 
 
//�ļ�����չʾ��ʽ
var receiveDeptShowTypeStore = new Ext.data.ArrayStore({
	fields:['id','name'],
	data:[[0,'ȫ��'],[1,'�ļ�BG'],[2,'�ļ���Ӫ����'],[3,'�ļ���������'],[4,'�ļ�����'],[5,'�ļ�����+����'],[6,'�ļ�����'],[7,'�ļ��ֲ�'],[8,'�ļ�����']]
//	data:[[1,"�ļ���Ӫ����"],[2,"�ļ�����"],[3,"�ļ��ֲ�"],[4,"�ļ�����"],[5,"�ļ�����"]]
});
 
//Ŀ������չʾ��ʽ
var destDeptShowType = new Ext.data.ArrayStore({
	fields:['id','name'],
	data:[[0,"ȫ��"],[1,"Ŀ��BG"],[2,"Ŀ�ľ�Ӫ����"],[3,"Ŀ�Ķ�������"],[4,"Ŀ�ĵ���"],[5,"Ŀ�ĵ���+����"],[6,"Ŀ�ĳ���"],[7,"Ŀ�ķֲ�"],[8,"Ŀ������"]]
});
 
//  �������ϲ㼶����У��
var validDeptCode = getDeptCodeValidator({onlyCheckCode:true});
 
var validDeptCodeToArea = getDeptCodeValidator({onlyCheckCode:true,checkLevel : 2});
 
 
 
Ext.onReady(function(){
	new Ext.Viewport({
		layout:'border',
		items:[{
			region:'center',
			layout:'border',
			xtype:'SF.mdp.receiveDetail.MainPanel'
		}],
		listeners:{
			afterrender:afterStoreLoad
		}
	});
});
 
/**
 * ����ҳ������ѯ����
 */
function afterStoreLoad(){
	
	//�������������û�ͳ�Ʒ�ʽֻչʾ���ļ�����
	if('1' > 2){
		var store = Ext.getCmp('statisticsWay').getStore();
		store.removeAll();
		store.insert(0,new store.recordType({id:1,name:"���ļ�����"}));
	}
	
	//����ͳ�Ʒ�ʽ�¼���Ⱦ
//	Ext.getCmp('receiveDeptShowType').fireEvent('select',receiveDeptShowType);
	//����ͳ�Ʒ�ʽ�¼���Ⱦ
	var combo = Ext.getCmp('statisticsWay');
	combo.fireEvent('select',combo);
	
	var cbCmp = Ext.getCmp('productTypeCheckbox');
	cbCmp.fireEvent('check',cbCmp);
 
	var c = null;
	var condition = '';
	
	if (condition) {
		condition = "{" + condition + "}";
		c = Ext.util.JSON.decode(condition);
	} else if(getURLParams()){
		c = {};
		c.params = getURLParams();
	}
	if(c){
		for (var unit in c.params) {
			var unitField = Ext.getCmp(unit);
			if(unitField){
				unitField.setValue(c.params[unit]);
				if(unit == "statisticsWay"){
					unitField.fireEvent('select', unitField);
				}else if(unit == "receivedDeptCode"){
					unitField.fireEvent('change', unitField);
				}else if(unit == "receiveDeptShowType"){
					unitField.fireEvent('select', unitField);
				}else if(unit == "destDeptCode"){
					unitField.fireEvent('change', unitField);
				}else if(unit == "destDeptShowType"){
					unitField.fireEvent('select', unitField);
				}
			}
		}
		Ext.getCmp("mainPanel").searchHandler();
	}
};
 
/**
* ������
*/
SF.mdp.receiveDetail.MainPanel = Ext.extend(Ext.Panel,{
	constructor : function(config){
		config = Ext.apply({
			id:'mainPanel',
			//frame:true,
			tbar : new Ext.Toolbar({
				items:[{
						text:"��ѯ",
						id:'searchBtn',
						iconCls:'search',
						handler : this.searchHandler.createDelegate(this)
					},{
						text:"����",
						id:'exportBtn',
						iconCls:'export',
						handler : this.exportHandler.createDelegate(this)
					}, {
						text : "����",
						id : 'resetBtn',
						iconCls : 'reset',
						handler : this.resetHandler.createDelegate(this)
					}]
			}),
			items:[{
				region:'north',
				xtype:'SF.mdp.receiveDetail.QueryPanel'
			},{
				region:'center',
				xtype:'SF.mdp.receiveDetail.GridPanel'
			}]
		},config);
		SF.mdp.receiveDetail.MainPanel.superclass.constructor.call(this,config);
	},
	resetHandler : function() {
		this.items.get(0).getForm().reset();
		var combo = Ext.getCmp('receiveDeptShowType');
		var store = combo.getStore();
		store.removeAll();
		
		Ext.getCmp('statisticsWay').fireEvent('select',Ext.getCmp('theTotal'),0);
		
		var receivedDeptCode = Ext.getCmp('receivedDeptCode');
		receivedDeptCode.setValue('CN01');
		receivedDeptCode.fireEvent('change',receivedDeptCode);
		
		var productTypeCmp = Ext.getCmp('productType');
		changeStatisticsWay(1);
		productTypeCmp.selectAll();
		productTypeCmp.disable();
		Ext.getCmp('theTotal').fireEvent('select',Ext.getCmp('theTotal'),1);//1��ʾ����
	},
	searchHandler : function(){
		//	data:[[1,'�ļ���Ӫ����'],[2,'�ļ�����'],[3,'�ļ��ֲ�'],[4,'�ļ�����']]
		//	��ѯ���������а汾����һ�£�ͳ�Ʒ�ʽ�����ļ����㡱�����ļ�ʱ�Ρ������ļ�����ʱ�ļ�����չʾ��ʽ���ӡ��ļ����С�ѡ��							
		var store = this.items.get(1).getStore();
		var form = this.items.get(0).getForm();
		
		if(form.isValid()){
			Ext.getCmp('mainPanel').getTopToolbar().disable();
			store.load({
				callback : function(r,option,success){
					Ext.getCmp('mainPanel').getTopToolbar().enable();
					if(!success){
						store.removeAll();
						if(this.reader && this.reader.jsonData && this.reader.jsonData.msg){
							Ext.Msg.alert("��ʾ", this.reader.jsonData.msg);
						}else{
							Ext.Msg.alert("��ʾ", "��ѯʧ��!");
						}
					}
				}
			});
 
		}
	},
	exportHandler : function(){
		
		var store = this.items.get(1).getStore();
		
		if(!store.baseParams.limit){
			Ext.Msg.alert('��ʾ', '���Ȳ�ѯ��Ҫ����������!');
			return;
		}
		if(store.totalLength > 100000){
			Ext.Msg.alert('��ʾ', '����������ܳ���100000������ɸѡ���ݺ�����!');
			return;
		}
		
		//��ȡ�����ֶ�
		var cm = Ext.getCmp('receiveDetailGrid').getColumnModel();
		var hideCols=[];
		// ����/��ʾ�ֶ�
		for(var i=0; i<cm.getColumnCount(); i++){
			if(cm.isHidden(i)){
				hideCols.push(i-1);
			}
		}
		
		Ext.Msg.confirm("��ʾ", "�Ƿ�ȷ������EXCEL?", function(btn, text) {
			if (btn == 'yes') {
				var params = store.baseParams;
				params['query.hideCols'] =  "|" + hideCols.join("|") + "|";
				var myMask = new Ext.LoadMask(Ext.getBody(), {msg:"���ڵ���,���Ժ�......"});
				myMask.show();
			    Ext.Ajax.request({
			    	method : 'POST',
				    url: 'receivedDetail_export.action',
				    success: function(response,opts){
				    	myMask.hide();
				    	var result = Ext.decode(response.responseText);
				    	if(!result.success){
				    		Ext.Msg.alert("��ʾ",result.msg);
				    	}else{
				    		var url=encodeURI(encodeURI(result.fileName));
							window.location = 'download!downloadFile.action?fileName=' + url;
				    	}
				    },
				    failure: function(response,opts){
				    	myMask.hide();
				    	var result = Ext.decode(response.responseText);
				    	Ext.Msg.alert("��ʾ",result.msg);
				    },	
				    params: params
			    });
			}
		})
	}
});
Ext.reg('SF.mdp.receiveDetail.MainPanel',SF.mdp.receiveDetail.MainPanel);
 
/**
 * ��ѯ��
 */
SF.mdp.receiveDetail.QueryPanel = Ext.extend(Ext.form.FormPanel,{
	constructor : function(config){
		config = Ext.apply({
			id:'receiveDetailQueryPanel',
			height:130,
			frame:true,
			labelAlign:'left',
			bodyStyle:'padding: 1px 5px 0px 1px',
			margins:'0 0 5 0',
			showErrorMsg : true,
			items:[{
				xtype:'fieldset',
				title:"��ѯ����",
			//	style:'padding-top: 2px',
				layout:'column',
				defaults:{
					labelWidth:110,
					defaults:{
						anchor:'95%'
					}
				},
				items:[{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'combo',
						fieldLabel:"ͳ�Ʒ�ʽ",
						triggerAction:"all",
						lazyRender : true,
						checkboxToggle: true,
						editable: false,
						mode:"local",
						displayField:"name",
						valueField:"id",
						id:'statisticsWay',
						hiddenName:'query.statisticsWay',
						store : countTypeStore,
						value:1,
						emptyText:"��ѡ��",
						allowBlank:false,
						listeners : {
							select : this.selectHandler.createDelegate(this)
						}
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'combo',
						fieldLabel:"���ܷ�ʽ",
						triggerAction:"all",
						lazyRender : true,
						checkboxToggle: true,
						editable: false,
						mode:"local",
						displayField:"name",
						valueField:"id",
						id:'theTotal',
						hiddenName:'query.theTotal',
						store : new Ext.data.ArrayStore({
							fields:['id','name'],
							data:[['1','����'],['2','�ۼ�']]
						}),
						value:'1',
						emptyText:"��ѡ��",
						allowBlank:false,
						listeners:{
							select:function(field,value,c){
								
								var startDate = Ext.getCmp('receivedDeliveredTm');//��ʼ����
								if(field.getValue()==1){
									Ext.apply(startDate,{dateLimit : 7});
									startDate.setValue(startDate.getValue()); 
								}else if(field.getValue()==2){
									Ext.apply(startDate,{dateLimit : 31});
									startDate.setValue(startDate.getValue());
								}
								
								var grid = Ext.getCmp('receiveDetailGrid');
								grid.init();
								//��Ʒ����
								var cbCmp = Ext.getCmp('productTypeCheckbox');
								cbCmp.fireEvent('check',cbCmp);
							}
						}
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						id : 'receivedDeliveredTm',
						xtype : 'datefield',
						name : 'query.receivedDeliveredTm',
						format : 'Y-m-d',
						fieldLabel : "��ʼ����",
						value : new Date(),
						editable : false,
						frame:true,
						allowBlank:false,
						endDateField: 'endTm',//Ŀ�����ڿؼ�ID
						dateLimit : 7,//������ڼ��(��λ:��)
						vtype : 'dateLimit'//ʹ��dateLimitУ�����   
						
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
				        id : 'endTm',
						xtype : 'datefield',
						name : 'query.endTm',
						format : 'Y-m-d',
						fieldLabel : "��������",
						value : new Date(),
						editable : false
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'textfield',
						id:'receivedDeptCode',
						name:'query.receivedDeptCode',
						fieldLabel:"�ļ�����",
						validateOnBlur : false,
						validationEvent : false,
						validator : validDeptCode,
						allowBlank:false,
						statisticsWay : '1,2,4',
						listeners : {
							change : this.onReceivedBlurHandler.createDelegate(this)
						},
						userDept : 'CN01',
						value : 'CN01'
						
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'combo',
						id:'receiveDeptShowType',
						fieldLabel:"�ļ�����չʾ��ʽ",
						triggerAction:"all",
						lazyRender : true,
						editable: false,
						mode:"local",
						displayField:"name",
						valueField:"id",
						hiddenName:'query.receiveDeptShowType',
						store : receiveDeptShowTypeStore,
						value : 0,
						emptyText:"��ѡ��",
						allowBlank:false,
						statisticsWay : '1,2,4',
						listeners : {
							select : this.selectReceiveDeptHandler.createDelegate(this)
						}
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						id:'sendZdCodeId',
						xtype:'textfield',
						name:'query.sendZdCode',
//						allowBlank:false,
						fieldLabel:'�ļ���������',
						statisticsWay : '1,2,4',
						listeners : {
							'change' : function(f,v){
								this.setValue(v.toUpperCase());
							}
						}
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						id:'cityCodeId',
						xtype:'textfield',
						name:'query.cityCode',
						statisticsWay : '1,2,4',
						fieldLabel:'�ļ�����'
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'textfield',
						id:'destDeptCode',
						name:'query.destDeptCode',
						fieldLabel:"Ŀ�ĵ�",
						allowBlank:false,
						statisticsWay : '3,4',
						validateOnBlur : false,
						validationEvent : false,
						validator : validDeptCode,
						listeners : {
							change : this.onDestBlurHandler.createDelegate(this)
						},
						userDept : 'CN01',
						value : 'CN01'
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'combo',
						fieldLabel:"Ŀ�ĵ�չʾ��ʽ",
						id:'destDeptShowType',
						triggerAction:"all",
						lazyRender : true,
						editable: false,
						mode:"local",
						displayField:"name",
						valueField:"id",
						hiddenName:'query.destDeptShowType',
						store : destDeptShowType,
						value : 0,
						emptyText:"��ѡ��",
						allowBlank:false,
						statisticsWay : '3,4',
						listeners : {
							select : this.selectDestDeptHandler.createDelegate(this)
						}
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'textfield',
						id:'destZdCodeId',
						name:'query.destZdCode',
						fieldLabel:"Ŀ�Ķ�������",
						statisticsWay : '3,4',
						listeners : {
							'change' : function(f,v){
								this.setValue(v.toUpperCase());
							}
						}
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'textfield',
						id:'destCityCode',
						name:'query.destCityCode',
						fieldLabel:"Ŀ�ĳ���",
						statisticsWay : '3,4',
						validator : validtorCity
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						xtype:'SF.DeptTypeComboBox',
						id : 'destDeptType',
						hiddenName : 'query.destDeptType',
						statisticsWay : '3',
						childDeptTypeBox : 'destChildDeptType'
					}
				},{
					columnWidth:.25,
					layout:'form',
					items:{
						id : 'destChildDeptType',
						hiddenName : 'query.destChildDeptType',
						statisticsWay : '3',
						xtype : 'SF.DeptChildTypeComboBox'
					}
				},{
					columnWidth:.225,
					layout:'form',
					id : 'productTypePanel',
				    items: {
				    	xtype:'lovcombo',
						id:'productType',
						frame:true,
						triggerAction:"all",
						lazyRender : true,
						allowBlank:true,
						editable: false,
						fieldLabel:"��Ʒ����",
						mode:"local",
						displayField:"typeName",
						valueField:"typeCode",
						hiddenName:'query.productType',
						store : productStore,
						showSelectAll:true,
						emptyText:"ȫ��",
                        listeners:{
                            afterrender : this.setProductTypeDisable.createDelegate(this)
                        }
				    }
				},{
					columnWidth:.025,
					layout:'form',
					id : 'productTypeCheckboxPanel',
				    items: {
				    	id : 'productTypeCheckbox',
			    		xtype:'checkbox',
			    		name : 'query.productTypeCheckbox',
			    		hideLabel : true,
			        	checked:false,
			        	targetField : 'productType',
			        	listeners:{
			        		check:checkBoxFun
			        	}
				    }
				},{
					columnWidth:.225,
					layout:'form',
					id : 'flowTypePanel',
				    items: {
				    	id : 'flowType',
				    	xtype:'lovcombo',
						frame:true,
						triggerAction:"all",
						lazyRender : true,
						allowBlank:true,
						editable: false,
						fieldLabel:"��������",
						mode:"local",
						displayField:"name",
						valueField:"id",
						hiddenName:'query.flowType',
						store : flowTypeStore,
						showSelectAll:true,
						statisticsWay : '4',
						emptyText:"ȫ��",
						value : '1,2,3,4,5,6,7,8'
				    }
				},{
					columnWidth:.025,
					layout:'form',
					id : 'flowTypeCheckboxPanel',
				    items: {
				    	id : 'flowTypeCheckbox',
			    		xtype:'checkbox',
			    		hideLabel : true,
			        	checked:false,
			        	name : 'query.flowTypeCheckbox',
			        	statisticsWay : '4',
			        	targetField : 'flowType',
			        	listeners:{
			        		check:checkBoxFun
			        	}
				    }
				},{
					xtype:'hidden',
					id:'receivedDeptLevel',
					name:'query.receivedDeptLevel'
				},{
					xtype:'hidden',
					id:'destDeptLevel',
					name:'query.destDeptLevel'
				}]
			}]
		},config);
		SF.mdp.receiveDetail.QueryPanel.superclass.constructor.call(this,config);
	},	
	setProductTypeDisable :function(field){
        productStore.load({callback : function(){
        	field.selectAll();
        }});
	},
	selectHandler : function(combo){
		var type = combo.getValue();
		changeStatisticsWay(type);				//��ʾ�����ز�ѯ����
		//���ݼļ�����չʾ��ʽ�¼���Ⱦ 
		var receivedDeptCode = Ext.getCmp('receivedDeptCode');
		var destDeptCode = Ext.getCmp('destDeptCode');
		
		if(type==1 || type==2){
 
			receivedDeptCode.setValue('CN01');
			receivedDeptCode.fireEvent('change',receivedDeptCode);
			receivedDeptCode.allowBlank = false;
			receivedDeptCode.label.dom.innerHTML = "�ļ�����" + "<font color='red'>*</font>:";
			this.setProductTypeDisable(Ext.getCmp('productType'));
			//��������Ⱦչʾ��ʽ
			var receiveDeptShowType = Ext.getCmp('receiveDeptShowType');
			receiveDeptShowType.getStore().removeAll();
			receiveDeptShowType.getStore().loadData(receiveDeptResource[combo.getValue()].receive);
			receiveDeptShowType.setValue(receiveDeptShowType.getStore().getAt(0).data.id);
			Ext.getCmp('destDeptShowType').setValue(0);
			
			Ext.getCmp('receiveDetailQueryPanel').setHeight(130);
		}else if(type == 3){
			destDeptCode.isCheckRelation = false; // �Ƿ�������У��
			destDeptCode.allowBlank = false;
			destDeptCode.label.dom.innerHTML = "Ŀ�ĵ�" + "<font color='red'>*</font>:";
			destDeptCode.setValue('CN01');
			destDeptCode.fireEvent('change',destDeptCode);
			
			//��������Ⱦչʾ��ʽ
			var destDeptShowType = Ext.getCmp('destDeptShowType');
			destDeptShowType.getStore().removeAll();
			destDeptShowType.getStore().loadData(receiveDeptResource[combo.getValue()].dest);
			destDeptShowType.setValue(destDeptShowType.getStore().getAt(0).data.id);
			Ext.getCmp('receiveDeptShowType').setValue(0);;
			
			Ext.getCmp('receiveDetailQueryPanel').setHeight(130);
		}else if(type==4){
			
			receivedDeptCode.isCheckRelation = true; // �Ƿ�������У��
			receivedDeptCode.allowBlank = true;
			receivedDeptCode.label.dom.innerHTML = "�ļ�����:";
			receivedDeptCode.setValue('');
			receivedDeptCode.fireEvent('change',receivedDeptCode);
			destDeptCode.isCheckRelation = true; // �Ƿ�������У��
			destDeptCode.allowBlank = true;
			destDeptCode.label.dom.innerHTML = "Ŀ�ĵ�:";
			destDeptCode.setValue('');
			destDeptCode.fireEvent('change',destDeptCode);
			
			this.setProductTypeDisable(Ext.getCmp('productType'));
			
				//��������Ⱦչʾ��ʽ
			var receiveDeptShowType = Ext.getCmp('receiveDeptShowType');
			receiveDeptShowType.getStore().removeAll();
			receiveDeptShowType.getStore().loadData(receiveDeptResource[combo.getValue()].receive);
			receiveDeptShowType.setValue(receiveDeptShowType.getStore().getAt(0).data.id);
			//��������Ⱦչʾ��ʽ
			var destDeptShowType = Ext.getCmp('destDeptShowType');
			destDeptShowType.getStore().removeAll();
			destDeptShowType.getStore().loadData(receiveDeptResource[combo.getValue()].dest);
			destDeptShowType.setValue(destDeptShowType.getStore().getAt(0).data.id);
 
			Ext.getCmp('receiveDetailQueryPanel').setHeight(160);
		}
		
		if(type==1 || type==3){
			receivedDeptCode.validator = validDeptCode;
			destDeptCode.validator = validDeptCode;
		}else if(type==2 || type==4){
			receivedDeptCode.validator = validDeptCodeToArea;
			destDeptCode.validator = validDeptCodeToArea;
		}
		receivedDeptCode.validate();
		destDeptCode.validate();
		
		//��ʾ������
		var grid = Ext.getCmp('receiveDetailGrid');
		grid.init();
		
		Ext.getCmp('mainPanel').doLayout();
	},
	
	onReceivedBlurHandler : function(field){
		Ext.getCmp('receivedDeptLevel').setValue(field.typeLevel);
	},
	
	onDestBlurHandler : function(field){
		Ext.getCmp('destDeptLevel').setValue(field.typeLevel);
	},
	
	selectReceiveDeptHandler : function(combo,record,index){
//		var type = combo.getValue();
		var grid = Ext.getCmp('receiveDetailGrid');
		grid.init();
	},
	
	selectDestDeptHandler : function(combo,record,index){
//		var type = combo.getValue();
		//�л���ͬ��չʾ��ʽgrid��������ʾ
		var grid = Ext.getCmp('receiveDetailGrid');
		grid.init();
	}
	
});
Ext.reg('SF.mdp.receiveDetail.QueryPanel',SF.mdp.receiveDetail.QueryPanel);
 
/**
 * �б���ʾ
 */
SF.mdp.receiveDetail.GridPanel = Ext.extend(Ext.grid.GridPanel,{
	constructor : function(config){
		var store = new Ext.data.JsonStore({
			url:'receivedDetail_list.action',
			root:'data',
			totalProperty:'total',
			fields:['statDt','statDate','endDate','sendBgCode','destBgCode','hqCode','sendZdCode','destZdCode','areaCode','cityCode','divisionCode','deptCode','deptCodeName','destHqCode','destAreaCode','destCityCode','destDivisionCode',
			'destDeptCode','destDeptName','destClassName','destDeptTypeName','tmType'
			,'deptType','deptName','childDeptName','productType','picNum','tdayPs','tmrPs','atmrPs','otherPs','delNum',
			'tdayPsRate','tmrPsRate','atmrPsRate','otherPsRate','delNumRate','noDelNum','flowType','tmr1030Ps','tmr1030PsRate'],
			listeners : {
				beforeload : this.beforeReceiveListStoreLoad.createDelegate(this)
			}
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel();
		var cm = new Ext.grid.ColumnModel({
			
			defaults: {
	        	sortable: false,
	        	align : 'center',
	            menuDisabled: true,
	            width : 70,
	            showTip:true
	        },
			rows:[
			      [{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{
				header : "��������",
				colspan : 5,
				align : 'center'
			},{
				header : "������",
				colspan : 5,
				align : 'center'
			}]
			
			],
			columns:[new Ext.grid.RowNumberer(),
			         {
					id:'statDt',
					header:"�ռ���ʼ����",
					dataIndex:'statDate',
					theTotal:'2',
					hidden:'true',
					width:100,
					renderer : Ext.renderDate
				},{
					id:'statDt',
					header:"�ռ���������",
					dataIndex:'endDate',
					theTotal:'2',
					hidden:'true',
					width:100,
					renderer : Ext.renderDate
				},{
					id:'statDt',
					header:"��ѯ����",
					dataIndex:'statDt',
					theTotal:'1',
					width:100,
					renderer : Ext.renderDate
				},{
					id:'sendBgCode',
					header:"�ļ�BG",
					dataIndex:'sendBgCode',
					receiveDeptShowType:'1,2,4,5,7,8',
					width:90
				},{
					id:'hqCode',
					header:"�ļ���Ӫ����",
					dataIndex:'hqCode',
					receiveDeptShowType:'2,4,5,7,8',
					width:100
				},{
					id:'sendZdCode',
					header:"�ļ���������",
					dataIndex:'sendZdCode',
					receiveDeptShowType:'3,5,6,7,8',
					width:100
				},{
					id:'areaCode',
					header:"�ļ�����",
					dataIndex:'areaCode',
					receiveDeptShowType:'4,5,7,8'
				},{
					id:'cityCode',
					header:"�ļ�����",
					dataIndex:'cityCode',
					receiveDeptShowType:'5,6,7,8'
				},{
					id:'divisionCode',
					header:"�ļ��ֲ�",
					dataIndex:'divisionCode',
					receiveDeptShowType:'7,8'
				},{
					id:'deptCode',
					header:"�ļ�����",
					dataIndex:'deptCode',
					changeStatisticsWay:'1',
					receiveDeptShowType:'8'
				},{
					id:'deptCodeName',
					header:"�ļ���������",
					width:100,
					dataIndex:'deptCodeName',
					receiveDeptShowType:'8'
				},{
					id:'deptType',
					header:"��������",
					dataIndex:'deptName',
					receiveDeptShowType:'8'
				},{
					id:'deptType',
					header:"����������",
					width:80,
					dataIndex:'childDeptName',
					receiveDeptShowType:'8'
				},{
					id:'destBgCode',
					header:"Ŀ�ĵ�BG",
					destDeptShowType: '1,2,4,5,7,8',
					dataIndex:'destBgCode',
					width:90
				},{
					id:'destHqCode',
					header:"Ŀ�ĵؾ�Ӫ����",
					destDeptShowType: '2,4,5,7,8',
					dataIndex:'destHqCode',
					width:120
				},{
					id:'destZdCode',
					header:"Ŀ�ĵض�������",
					destDeptShowType: '3,5,6,7,8',
					dataIndex:'destZdCode',
					width:100
				},{
					id:'destAreaCode',
					header:"Ŀ�ĵ���",
					destDeptShowType: '4,5,7,8',
					dataIndex:'destAreaCode'
				},{
					id:'destCityCode',
					header:"Ŀ�ĳ���",
					destDeptShowType: '5,6,7,8',
					dataIndex:'destCityCode'
				}
				,{
					id:'destDivisionCode',
					header:"Ŀ�ķֲ�",
					destDeptShowType:'7,8',
					dataIndex:'destDivisionCode'
				},{
					id:'destDeptCode',
					header:"Ŀ������",
					destDeptShowType:'8',
					dataIndex:'destDeptCode'
				},{
					id:'destDeptName',
					header:"Ŀ����������",
					destDeptShowType:'8',
					width:100,
					dataIndex:'destDeptName'
				},{
					id:'destClassName',
					header:"��������",
					dataIndex:'destClassName',
					destDeptShowType:'8'
				},{
					id:'destDeptType',
					header:"����������",
					destDeptShowType:'8',
					dataIndex:'destDeptTypeName'
				},{
					id:'tmType',
					header:"ʱ��",
					statisticsWay : '2',
					dataIndex:'tmType',
					width:85,
					renderer : this.formatTmType.createDelegate(this)
				},{
					id:'productTypeCol',
					header:"��Ʒ����",
					dataIndex:'productType',
					renderer : function(value){
						if(value == null || value == ''){
							return "";
						}
						return value;
					}
				},{
					id:'flowTypeCol',
					header:"��������",
					dataIndex:'flowType',
					statisticsWay:'4',
					hidden:true,
					renderer : function(value){
						//1��ͬ�� 2������ 3��ʡ�� 4����������ʡ 5���籾����ʡ 6�������ڿ�ʡ 7���籾�� 8������
						var values ={
								1:'ͬ��',
								2:'����',
								3:'ʡ��',
								4:'��������ʡ',
								5:'�籾����ʡ',
								6:'�����ڿ�ʡ',
								7:'�籾��',
								8:'����'
						};
						if(value == null || value == ''){
							return "";
						}else{
							return values[value];
						}
					}
				},{
					id:'picNum',
					header:"�ռ���",
					dataIndex:'picNum'
				},{
					id:'picNum',
					header:"���ɼ���",
					dataIndex:'delNum'
				},{
					id:'noDelNum',
					header:"δ�ɼ���",
					dataIndex:'noDelNum'
				},{
					id:'picNum',
					header:"��������",
					dataIndex:'delNumRate',
					renderer : renderRate
				},{
					header:"����������",
					dataIndex:'tdayPs'
				},{
					header:"����1030������",
					width:120,
					dataIndex:'tmr1030Ps'
				},{
					header:"����������",
					dataIndex:'tmrPs'
				},{
					header:"����������",
					dataIndex:'atmrPs'
				},{
					header:"��������������",
					dataIndex:'otherPs',
					width:120
				},{
					header:"����������",
					dataIndex:'tdayPsRate',
					renderer : renderRate
					
				},{
					header:"����1030������",
					width:120,
					dataIndex:'tmr1030PsRate',
					renderer : renderRate
					
				},{
					header:"����������",
					dataIndex:'tmrPsRate',
					renderer : renderRate
				},{
					header:"����������",
					dataIndex:'atmrPsRate',
					renderer : renderRate
				},{
					header:"��������������",
					width:120,
					dataIndex:'otherPsRate',
					renderer : renderRate
				}]
		});
		config = Ext.apply({
			id:'receiveDetailGrid',
			store:store,
			cm:cm,
			stripeRows: true,
			loadMask:true,
			init:function(){
				//ͳ�Ʒ�ʽ
				var statisticsWayValue = Ext.getCmp('statisticsWay').getValue();
				if(!statisticsWayValue) statisticsWayValue=0;
				//�ļ�����չʾ��ʽ
				var receiveDeptShowTypeValue = Ext.getCmp('receiveDeptShowType').getValue();
				if(!receiveDeptShowTypeValue) receiveDeptShowTypeValue=0;
				//Ŀ������չʾ��ʽ
				var destDeptShowTypeValue = Ext.getCmp('destDeptShowType').getValue();
				if(!destDeptShowTypeValue) destDeptShowTypeValue=0;
				//���ܷ�ʽ
				var theTotalValue = Ext.getCmp('theTotal').getValue();
				
				
				this.getStore().removeAll();
				var cm = this.getColumnModel();
			   //��ʾ����grid�б�
				for(var i=0; i<cm.getColumnCount(); i++){
						var receiveDeptShowType =  cm.getColumnAt(i).receiveDeptShowType;
						if(!receiveDeptShowType) continue;
						if(receiveDeptShowType.indexOf(receiveDeptShowTypeValue) >=0 ){
							cm.setHidden(i,false);
						}else{
							cm.setHidden(i,true);
						}
					}
				for(var i=0; i<cm.getColumnCount(); i++){
					var destDeptShowType =  cm.getColumnAt(i).destDeptShowType;
					if(!destDeptShowType) continue;
					if(destDeptShowType.indexOf(destDeptShowTypeValue) >=0 ){
						cm.setHidden(i,false);
					}else{
						cm.setHidden(i,true);
					}
				}
				for(var i=0; i<cm.getColumnCount(); i++){
					var theTotal =  cm.getColumnAt(i).theTotal;
					if(!theTotal) continue;
					if(theTotal.indexOf(theTotalValue) >=0 ){
						cm.setHidden(i,false);
					}else{
						cm.setHidden(i,true);
					}
				}
				
				for(var i=0; i<cm.getColumnCount(); i++){
					var statisticsWay =  cm.getColumnAt(i).statisticsWay;
					if(!statisticsWay) continue;
					if(statisticsWay.indexOf(statisticsWayValue) < 0){
						cm.setHidden(i,true);
					}else{
						cm.setHidden(i,false);
					}
				}
				
				Ext.getCmp('productTypeCheckbox').fireEvent('check',Ext.getCmp('productTypeCheckbox'));
				
				if(statisticsWayValue == 4){
					Ext.getCmp('flowTypeCheckbox').fireEvent('check',Ext.getCmp('flowTypeCheckbox'));
				}
				
				/*
				 * ��������������ʾ�߼�
				 * ͳ�Ʒ�ʽ���ĳ�����
				 * �ļ�����չʾ��ʽ��Ŀ������չʾ��ʽ�������������
				 */
//				if(statisticsWayValue == 4){
//					var column =  cm.getColumnById('flowTypeCol');
//					var index = cm.getIndexById('flowTypeCol');
//					if(column.destDeptShowType.indexOf(destDeptShowTypeValue)>-1
//							&& column.receiveDeptShowType.indexOf(receiveDeptShowTypeValue)>-1 ){
//						cm.setHidden(index,false);
//					}else{
//						cm.setHidden(index,true);
//					}
//				} 
			},
			tbar : new Ext.PagingToolbar({
				store : store,
				pageSize : Ext.pageSize,
				displayInfo : true
			}),
			plugins : [new Ext.ux.plugins.GroupHeaderGrid()]
		},config);
		SF.mdp.receiveDetail.GridPanel.superclass.constructor.call(this,config);
	},
	beforeReceiveListStoreLoad : function(store){
		if(!Ext.getCmp('receiveDetailQueryPanel').form.isValid()){
			return false;
		}
		
		store.baseParams = Ext.apply(Ext.getCmp('receiveDetailQueryPanel').getForm().getValues(),{
			start:0,
			limit:Ext.pageSize
		});
	},
	formatTmType : function(value){
		if(value>=0 && value<=8){
			value=8;
		}
		var result=tmTypes[value];
		if(!result){
			return value;
		}
		return result;
	}
});
Ext.reg('SF.mdp.receiveDetail.GridPanel',SF.mdp.receiveDetail.GridPanel);
 
function changeStatisticsWay(value){
	
	var form = Ext.getCmp('receiveDetailQueryPanel').getForm();
	// ��ʾ�����ؿؼ�
	var items = form.items.items;
	for(var i=0; i<items.length;i++){
		var field = items[i];
		if(!field.statisticsWay) continue;
		if( field.statisticsWay.indexOf(value) > -1){
			field.enable();
			field.show();
		}else{
			field.disable();
			field.hide();
		}
	}
};
 
//��Ʒ����״̬�޸�
function checkBoxFun(field){
	var cm = Ext.getCmp('receiveDetailGrid').getColumnModel();
	var targetField = Ext.getCmp(field.targetField);
	if(field.checked){
		targetField.enable();
		cm.setHidden(cm.getIndexById(field.targetField+'Col'),false);
	}else{
		targetField.disable();
		cm.setHidden(cm.getIndexById(field.targetField+'Col'),true);
	}
	var grid = Ext.getCmp('receiveDetailGrid');
	grid.getStore().removeAll();
	Ext.updatePagingToolBar(grid.getTopToolbar());
};
 
 
/**
 * @param v
 * ֧���������д��룬��У���ʽ��ֻУ�鶺��
 */
function validtorCity(v){
		if(!Ext.isEmpty(v)){
			v = v.trim().replace(new RegExp('��','g'),',').toUpperCase();
			var validator = this.validator;
			this.validator = null;
			this.setValue(v);
			this.validator = validator;
			if(v.split(',').length>6){
				return "���֧���������в�ѯ";
			}
		}
		
		return true;
};
 
renderRate = function(value) {
	if (value || value != null) {
		return new Number(value * 100).toFixed(2) + '%';
	}
	return "0.00%";
};
 
//function flowTypeControl(dispalyWay1,displayWay2){
//	var cm = Ext.getCmp('receiveDetailGrid').getColumnModel();
//	//ͳ�Ʒ�ʽΪ������,Ŀ�ĺ�ʼ����չʾ��ʽΪ���У�չʾ��������
//	if('4'==Ext.getCmp('statisticsWay').getValue()&&'5'==dispalyWay1&&'3'==displayWay2){
//		cm.setHidden(17,false);
//	}else{
//		cm.setHidden(17,true);	
//	}
//}

