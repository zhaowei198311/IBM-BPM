<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>新增</title>
<jsp:include page="../common/inc.jsp"></jsp:include>
</head>
<body>
	<form class="layui-form layui-form-pane" style="margin-top: 20px;" method="post" action="" id="quartzAddForm" >
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label" >任务名</label>
				<div class="layui-input-inline">
					<input type="text" name="jobName"  lay-verify="required" autocomplete="off" class="layui-input" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label" >类型</label>
				<div class="layui-input-inline">
					<select name="jobGroupName" lay-verify="required" id="jobGroupName">
        			</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
				<label class="layui-form-label" >类路径</label>
				<div class="layui-input-block" style="width: 78%;">
					<input type="text" name="clazz"  lay-verify="required" autocomplete="off" class="layui-input" />
				</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label" >表达式</label>
			<div class="layui-input-block">
				<span  onclick="generateCron();" style="color:red;float:right;margin-top:10px;margin-right:160px;cursor:pointer;" >在线Cron表达生成器</span>
				<input type="text" name="cronExpr"  style="width: 200px;"  lay-verify="required"  maxlength="50"  autocomplete="off" class="layui-input" />
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block" style="float: right;margin-right: 20px;">
				<button class="layui-btn" lay-submit="" lay-filter="btnSubmit">保存</button>
				<button class="layui-btn layui-btn-primary" id="close">关闭</button>
			</div>
		</div>
	</form>
<script>

function generateCron(){
	window.open('http://cron.qqe2.com/');
}

layui.use(['form', 'layedit', 'laydate','jquery'], function(){
  var form = layui.form;
  var $ = layui.jquery;
  //初始化所属组别下拉框
  pbInitCombox($,form,'dicUid=dicbea3b258-604c-4aca-8c02-83b2315edcf8','jobGroupName');
   //监听提交
   form.on('submit(btnSubmit)', function(data){
   //layer.msg(JSON.stringify(data.field));
   var index = layer.load(1);//开启进度条
     $.ajax({
		url : '<%=request.getContextPath()%>/quarz/add.do',
		data :$('#quartzAddForm').serialize(),
		type:'POST',//默认以get提交，以get提交如果是中文后台会出现乱码
		dataType : 'json',
		success : function(obj) {
			layer.close(index);//关闭   
			if (obj.success) {
				pubUtil.msg(obj.msg,layer,1,function(){
					$("#close").click();
				},500);
			} else {
				pubUtil.msg(obj.msg,layer,2,function(){
					
				},5*1000);
			}
		}
	});
    return false;
  });
    
});


//按钮的点击事件
$('button#treeSelect').on('click', function() {
	parent.layer.open({
	      type: 2,
	      title : '选择<span style="color:red">[请单击选中]</span>',
	      shadeClose: false,//点击遮罩关闭
	      anim: public_anim,
	      btnAlign: 'c',
	      shade : 0.3,//是否有遮罩，可以设置成false
	      maxmin: true, //开启最大化最小化按钮
	      area: ['600px', '300px'],
	      /* boolean:true, */
		  content: ['InsInfoTree.jsp', 'yes'], //iframe的url，no代表不显示滚动条
		  btn: ['选择', '关闭']
		  ,yes: function(index,layero) {
			  var body=$(layero).find("iframe")[0].contentWindow.document;
			  var pid = body.getElementById("id").value;
			  var pidNm = body.getElementById("name").value;
			  $("#parentInsCdNm").val(pidNm);
			  $("#parentInsCd").val(pid);
			  parent.layer.close(index); //关闭当前弹层
		  }
		  ,btn2: function(index, layero){
			  
		  }
	    });
});
</script>
</body>
</html>