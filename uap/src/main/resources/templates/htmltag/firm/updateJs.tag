<script type="text/javascript">
var firmId = '${firm.id!}';

function saveOrUpdate() {
	$.messager.progress({
				msg : '正在加载数据......'
			});
	if (!$('#_form').form("validate")) {
		$.messager.progress('close');
		return;
	}
	$.messager.progress('close');
	if ($('#failureTime').datebox('getValue') < $('#effectTime').datebox('getValue')) {
		swal('错误', '生效日期不能大于失效日期', 'error');
		return;
	}
	var _formData = $("#_form").serializeObject();
	var _url = null;
	// 没有id就新增
	if (_formData.id == null || _formData.id == "") {
		_url = "${contextPath}/firm/insert.action";
	} else {// 有id就修改
		_url = "${contextPath}/firm/update.action";
	}
	$.ajax({
				type : "POST",
				url : _url,
				data : _formData,
				processData : true,
				dataType : "json",
				async : true,
				success : function(data) {
					if (data.code == "200") {
						window.location.href = '${contextPath!}/firm/index.html';
					} else {
						swal('错误', data.result, 'error');
						$.messager.progress('close');
					}
				},
				error : function() {
					swal('错误', '远程访问失败', 'error');
					$.messager.progress('close');
				}
			});
}

function onRegProvinceChange(n, v) {
	$('#registeredCityId').combobox('disable');
	$('#registeredDistrictId').combobox('disable');
	$('#registeredCityId').combobox('setValue', '');
	$('#registeredCityId').combobox('setText', '');
	$('#registeredCityId').combobox('enable');
	$('#registeredCityId').combobox('reload', '${contextPath!}/firm/cityList.action?parentId=' + n);
}

function onRegCityChange(n, v) {
	if (!n) {
		$('#registeredDistrictId').combobox('setValue', '');
		$('#registeredDistrictId').combobox('setText', '');
		return;
	}
	$('#registeredDistrictId').combobox('setValue', '');
	$('#registeredDistrictId').combobox('setText', '');
	$('#registeredDistrictId').combobox('enable');
	$('#registeredDistrictId').combobox('reload', '${contextPath!}/firm/districtList.action?parentId=' + n);
}

function onActProvinceChange(n, v) {
	$('#actualCityId').combobox('disable');
	$('#actualDistrictId').combobox('disable');
	$('#actualCityId').combobox('setValue', '');
	$('#actualCityId').combobox('setText', '');
	$('#actualCityId').combobox('enable');
	$('#actualCityId').combobox('reload', '${contextPath!}/firm/cityList.action?parentId=' + n);
}

function onActCityChange(n, v) {
	if (!n) {
		$('#actualDistrictId').combobox('setValue', '');
		$('#actualDistrictId').combobox('setText', '');
		return;
	}
	$('#actualDistrictId').combobox('setValue', '');
	$('#actualDistrictId').combobox('setText', '');
	$('#actualDistrictId').combobox('enable');
	$('#actualDistrictId').combobox('reload', '${contextPath!}/firm/districtList.action?parentId=' + n);
}

function onLongTermEffictiveChange(n, v) {
	if (n) {
		$('#certificateValidityPeriod').datebox('disable');
	} else {
		$('#certificateValidityPeriod').datebox('enable');
	}
}

function onBankChange(n, v) {
	$('#bankProvinceId').combobox('enable');
	$('#bankProvinceId').combobox('reload', '${contextPath!}/firm/provinceList.action?parentId=' + n);
	reloadBankUnionInfo();
}

function reloadBankUnionInfo() {
	$('#depositBankUnionInfoId').combobox('setValue', '');
	$('#depositBankUnionInfoId').combobox('enable');
	var n = $('#depositBank').combobox('getValue');
	var cityId = $('#bankCityId').combobox('getValue');
	if (cityId) {
		$('#depositBankUnionInfoId').combobox('reload', '${contextPath!}/firm/bankUnionInfoList.action?cityId=' + cityId + '&bankId=' + n);
	}
}

function onBankProvinceChange(n, v) {
	$('#bankCityId').combobox('disable');
	$('#bankCityId').combobox('setValue', '');
	$('#bankCityId').combobox('setText', '');
	$('#bankCityId').combobox('enable');
	$('#bankCityId').combobox('reload', '${contextPath!}/firm/cityList.action?parentId=' + n);
	$('#depositBankUnionInfoId').combobox('loadData', {});
	reloadBankUnionInfo();
}

function onBankCityChange(n, v) {

	var bankId = $('#depositBank').combobox('getValue');
	if (bankId) {
		$('#depositBankUnionInfoId').combobox('setValue', '');
		$('#depositBankUnionInfoId').combobox('reload', '${contextPath!}/firm/bankUnionInfoList.action?cityId=' + n + '&bankId=' + bankId);
		$('#depositBankUnionInfoId').combobox('enable');
	}
	reloadBankUnionInfo();
}

function onBankDistrictChange(n, v) {
	$('#depositBankUnionInfoId').combobox('setValue', '');
	$('#depositBankUnionInfoId').combobox('enable');
	$('#depositBankUnionInfoId').combobox('reload', '${contextPath!}/firm/bankUnionInfoList.action?districtId=' + n);
	// reloadBankUnionInfo();
}

// 清空表单
function clearForm() {
	$('#form').form('clear');
}

/**
 * 绑定页面回车事件，以及初始化页面时的光标定位
 * 
 * @formId 表单ID
 * @elementName 光标定位在指点表单元素的name属性的值
 * @submitFun 表单提交需执行的任务
 */
$(function() {
			$.messager.progress({
						msg : '正在加载数据......'
					});
			var longTermEffictive = '${firm.longTermEffictive!}';
			if (longTermEffictive == 'true') {
				$('#longTermEffictive').combobox('select', 1);
			} else {
				$('#longTermEffictive').combobox('select', 0);
			}
			if (firmId) {
				$('#industry').combobox('select', '${firm.industry!}');
				$('#certificateType').combobox('select', '${firm.certificateType!}');
				$('#registeredProvinceId').combobox('select', '${firm.registeredProvinceId!}');
				$('#registeredCityId').combobox('select', '${firm.registeredCityId!}');
				$('#registeredDistrictId').combobox('select', '${firm.registeredDistrictId!}');
				$('#actualProvinceId').combobox('select', '${firm.actualProvinceId!}');
				$('#actualCityId').combobox('select', '${firm.actualCityId!}');
				$('#actualDistrictId').combobox('select', '${firm.actualDistrictId!}');
				$('#legalPersonCertificateType').combobox('select', '${firm.legalPersonCertificateType!}');
				$('#depositBank').combobox('select', '${firm.depositBank!}');
				$('#bankProvinceId').combobox('select', '${bankUnionInfo.provinceId!}');
				$('#bankCityId').combobox('select', '${bankUnionInfo.cityId!}');
				$('#depositBankUnionInfoId').combobox('select', '${firm.depositBankUnionInfoId!}');
			}
			$.messager.progress('close');
		});
</script>