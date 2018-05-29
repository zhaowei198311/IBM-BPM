<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<form name="searchform" id="searchform" class="layui-form layui-form-pane" method="post" action="">
	<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label" >定时器名称</label>
				<div class="layui-input-inline">
					<input type="text" name="jobName"   autocomplete="off" class="layui-input" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label" >所属组别</label>
				<div class="layui-input-inline">
					<select name="jobGroup"  id="jobGroup">
					</select>
				</div>
			</div>
		</div>
	<button class="layui-btn" id="btnSearch" type="button">立即查询</button>
	<button type="reset" id="btnRetSet" type="button" class="layui-btn layui-btn-primary">重置</button>
</form>
<script>
layui.use(['form', 'layedit', 'laydate','jquery'], function(){
  /* var form = layui.form; */
  /* var $ = layui.jquery;
  
  //初始化所属组别下拉框
  pbInitCombox($,form,'dictTypeCd=quarz_job_group','jobGroup');
	 */
  });
 </script>