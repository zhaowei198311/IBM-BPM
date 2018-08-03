<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML>
<html>

<head>
<base href="<%=basePath%>">
<title>任务调度</title>
 
<jsp:include page="../common/inc.jsp"></jsp:include>
<style type="text/css">
	.layui-icon{cursor: pointer;}
</style>
</head>
<body>
<div class="layui-container" style="margin-top: 20px; width: 100%;">
	<div class="search_area">
		<div class="layui-row layui-form">
			<form action="quarz/datagrid.do" method="post" class="layui-form"  id="searchform">
				<div class="layui-col-md2">
					<input type="text" placeholder="任务名称"  class="layui-input" name="jobName">
				</div>
				<div class="layui-col-md2">
					<select>
						<option>1</option>
						<option>2</option>
						<option>3</option>
					</select>
				</div>
				<div class="layui-col-md2" style="text-align: right;">
					<button class="layui-btn" id="btnSearch" type="button">查询</button>
					<button class="layui-btn" type="button"  data-type="btnAdd" id="openAdd" >新增</button>
				</div>
			</form>
		</div>
	</div>
</div>
<table class="layui-hide layui-table" id="table" lay-filter="table" ></table>
<script>
	
	
layui.use(['laydate', 'laypage', 'layer', 'table', 'carousel', 'upload', 'element'], function(){
	  var layer = layui.layer; //弹层
	  var laydate = layui.laydate; //日期
	  var laypage = layui.laypage; //分页
	  var table = layui.table; //表格
	  var carousel = layui.carousel; //轮播
	  var upload = layui.upload; //上传
	  var element = layui.element; //元素操作
	  var index = layer.load(1);//开启进度条
	  
	  //绑定table
	  table.render({
		  elem: '#table' ,//table id
		  url: '<%=request.getContextPath()%>/quarz/datagrid.do',
				method : 'POST', //方式
				page: true,//是否开启分页
				id : 'searchID',
				done: function(res, curr, count){
					//加载后回调
					layer.close(index);//关闭   
				},
				cols : [ [
				{
					field : 'jobName',
					title : '任务名称',
					align : 'center',
					//width : '200'
				},
				{
					field : 'triggerGroupName',
					title : '类型',
					align : 'center',
					//width : '200'
				},{
					field : 'cronExpr',
					title : '表达式',
					align : 'center',
					//width : '300'
				} ,{
					field : 'jobStatus',
					title : '状态',
					align : 'center',
					//width : '100',
					templet: '#operStTpl'
				} , {
					fixed : 'right',
					title : '操作',
					align : 'center',
					toolbar : '#toobar',
					//width : '250'
				}] ]
			});

			//监听工具条
			table.on('tool(table)', function(obj) { //注：tool是工具条事件名，table是table原始容器的属性 lay-filter="对应的值"
				var data = obj.data //获得当前行数据
				, layEvent = obj.event; //获得 lay-event 对应的值
				if (layEvent === 'detail') {
					openDetail(data);
				}else if(layEvent === 'update'){
					openUpdate(data);
				}else if(layEvent === 'del'){
					openDelete(data.jobName,data.jobGroupName,data.triggerName,data.triggerGroupName);
				}else if(layEvent === 'pauseJob'){//暂停任务
					pauseJob(data.jobName,data.jobGroupName);
				}else if(layEvent === 'resumeJob'){
					resumeJob(data.jobName,data.jobGroupName);
				}
			});

			//打开新增按钮
			function openAdd() {
					layer.open({
						type : 2,
						title : '新增',
						shift:'0',
						shadeClose : false,//点击遮罩关闭
						shade : 0.3,
						btnAlign : 'c',
						area: ['665px', '310px'],
						closeBtn: 0,
						content : [ 'quarz/quarzAdd', 'yes'],
						success : function(layero, lockIndex) {
							var body = layer.getChildFrame('body', lockIndex);
							//绑定解锁按钮的点击事件
							body.find('button#close').on('click', function() {
								layer.close(lockIndex);
								location.reload();//刷新
							});
						}
					});
			}
			
			//打开查看按钮
			function openDetail(data) {
				layer.open({
					type : 2,
					title : '查看',
					shadeClose : false,//点击遮罩关闭
					shade : 0.3,
					shift:'0',
					btnAlign : 'c',
					area: ['665px', '310px'],
					closeBtn: 0,
					content : [ 'quarz/quarzDetail?obj='+encodeURIComponent(JSON.stringify(data)), 'no'],
					success : function(layero, lockIndex) {
						var body = layer.getChildFrame('body', lockIndex);
						//绑定解锁按钮的点击事件
						body.find('button#close').on('click', function() {
							layer.close(lockIndex);
						});
						pubUtil.load(body, data);//填充表单
						body.find("input").attr("readonly", "readonly");  
					}
				});
			}
			
			//修改按钮
			function openUpdate(data){
				layer.open({
				      type: 2,
				      title: '修改',
				      shift:'0',
				      shadeClose: false,//点击遮罩关闭
				      anim: public_anim,
				      btnAlign: 'c',
				      shade : 0.3,
				      area: ['665px', '310px'],
				      closeBtn: 0,
					  content: ['quarz/quarzEdit?obj='+encodeURIComponent(JSON.stringify(data)), 'yes'],
					  success: function(layero, lockIndex) {
						var body = layer.getChildFrame('body', lockIndex);
						//绑定解锁按钮的点击事件
						body.find('button#close').on('click', function() {
							layer.close(lockIndex);
							llocation.reload();//刷新
						});
						body.find("input[name=nid]").val(data.nid);
						data.oldjobName=data.jobName;
						data.oldjobGroupName=data.jobGroupName;
						data.oldtriggerName=data.triggerName;
						data.oldtriggerGroup=data.triggerGroupName;
						var select=body.find("select[name='jobGroupName'] option");
						pubUtil.load(body, data);//填充表单
						body.find(".form-control").val(data.cronExpr);
					  }
				    });
				}
			
			
			//暂停
			function pauseJob(jobName,jobGroupName){
				layer.open({
			        title: '提示' //显示标题栏
			        ,closeBtn: false
			        ,area: '300px;'
			        ,shade: 0.3
			        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			        ,btn: ['确定', '关闭']
			        ,content: '确定要暂停?'
			        ,success: function(layero){
			          var btn = layero.find('.layui-layer-btn');
			          btn.css('text-align', 'center');//居中
			          btn.find('.layui-layer-btn0').on('click', function() {
			        	  var loadindex = layer.load(1);//开启进度条
			        	  $.ajax({
								url : '<%=request.getContextPath()%>/quarz/pauseJob.do',
								data : {
									jobName : jobName,
									jobGroupName : jobGroupName,
								},
								type:'POST',//默认以get提交，以get提交如果是中文后台会出现乱码
								dataType : 'json',
								success : function(r) {
									layer.close(loadindex);//关闭进程对话框
									if (r.success) {
										pubUtil.msg(r.msg,layer,1,function(){
											location.reload();//刷新
										});
									} else {
										pubUtil.msg(r.msg,layer,2,function(){
											
										});
									}
								}
							});
			          });
			        }
			      });
			}
			
			//启动
			function resumeJob(jobName,jobGroupName){
				layer.open({
			        title: '提示' //显示标题栏
			        ,closeBtn: false
			        ,area: '300px;'
			        ,shade: 0.3
			        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			        ,btn: ['确定', '关闭']
			        ,content: '确定启动?'
			        ,success: function(layero){
			          var btn = layero.find('.layui-layer-btn');
			          btn.css('text-align', 'center');//居中
			          btn.find('.layui-layer-btn0').on('click', function() {
			        	  var loadindex = layer.load(1);//开启进度条
			        	  $.ajax({
								url : '<%=request.getContextPath()%>/quarz/resumeJob.do',
								data : {
									jobName : jobName,
									jobGroupName : jobGroupName,
								},
								type:'POST',//默认以get提交，以get提交如果是中文后台会出现乱码
								dataType : 'json',
								success : function(r) {
									layer.close(loadindex);//关闭进程对话框
									if (r.success) {
										pubUtil.msg(r.msg,layer,1,function(){
											location.reload();//刷新
										});
									} else {
										pubUtil.msg(r.msg,layer,2,function(){
											
										});
									}
								}
							});
			          });
			        }
			      });
			}
			
			//删除按钮
			function openDelete(jobName,jobGroupName,triggerName,triggerGroupName){
				layer.open({
			        title: '确认删除' //显示标题栏
			        ,closeBtn: false
			        //,area: '300px;'
			        ,shade: 0.3
			        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
			        ,btn: ['确定', '取消']
			        ,content: '您是否要删除当前选中的记录？'
			        ,success: function(layero){
			          var btn = layero.find('.layui-layer-btn');
			          btn.css('text-align', 'center');//居中
			          btn.find('.layui-layer-btn0').on('click', function() {
			        	  var loadindex = layer.load(1);//开启进度条
			        	  $.ajax({
								url : '<%=request.getContextPath()%>/quarz/remove.do',
								data : {
									jobName : jobName,
									jobGroupName : jobGroupName,
									triggerName : triggerName,
									triggerGroupName : triggerGroupName
								},
								type:'POST',//默认以get提交，以get提交如果是中文后台会出现乱码
								dataType : 'json',
								success : function(r) {
									layer.close(loadindex);//关闭进程对话框
									if (r.success) {
										pubUtil.msg(r.msg,layer,1,function(){
											location.reload();//刷新
										});
									} else {
										pubUtil.msg(r.msg,layer,2,function(){
											
										});
									}
								}
							});
			          });
			        }
			      });
			}
			
			//查询按钮
			$('#btnSearch').on('click', function() {
				index = layer.load(1);//开启进度条
				var searchform = pubUtil.serializeObject($("#searchform"));//查询页面表单ID
				//alert(JSON.stringify(searchform));
				table.reload('searchID', {
					where : searchform
				});
			});

			//重置按钮 
			$('#btnRetSet').on('click', function() {
				index = layer.load(1);//开启进度条
				table.reload('searchID', {
					where : ""
				});
			});
			
		   var active = {
			btnAdd: function(){ //新增操作
		    	openAdd();
		    }
		  };
		  
		  $('#openAdd').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
		  });
			
		});
	</script>
<script type="text/html" id="toobar">


<i class="layui-icon" layui-btn-primary layui-btn-xs" lay-event="detail" >&#xe60a;</i>
<i class="layui-icon" layui-btn-primary layui-btn-xs" lay-event="update" >&#xe642;</i>
<a class="layui-btn layui-btn-xs"  {{ d.jobStatus == 'NORMAL' ? 'lay-event="pauseJob"' : 'lay-event="resumeJob"' }}  >{{d.jobStatus == 'PAUSED' ? '启动' : '暂停'}}</a>
<i class="layui-icon" layui-btn-primary layui-btn-xs" lay-event="del" >&#xe640;</i>
</script>
<script type="text/html" id="operStTpl">
	<span class="layui-badge  {{ d.jobStatus == 'NORMAL' ? 'layui-bg-green' : '' }} ">{{d.jobStatus == 'NORMAL' ? '正常运行'  :'暂停状态'}}</span>
</script>
</body>
</html>