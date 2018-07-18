<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
    <meta http-equiv="content-Type" content="text/html;charset=utf-8"/>
    <style type="text/css">
    #login
    {
        display:none;
        border:10px solid #3366FF;
        height:30%;
        width:50%;
        position:absolute;/*让节点脱离文档流,我的理解就是,从页面上浮出来,不再按照文档其它内容布局*/
        top:24%;/*节点脱离了文档流,如果设置位置需要用top和left,right,bottom定位*/
        left:24%;
        z-index:2;/*个人理解为层级关系,由于这个节点要在顶部显示,所以这个值比其余节点的都大*/
        background: white;
    }
    #over
    {
        width: 100%;
        height: 100%;
        opacity:0.8;/*设置背景色透明度,1为完全不透明,IE需要使用filter:alpha(opacity=80);*/
        filter:alpha(opacity=80);
        display: none;
        position:absolute;
        top:0;
        left:0;
        z-index:1;
        background: silver;
    }
    #title
    {
        background:greenyellow;
        width:100%;
        height:1.5em;
    }
    #title a 
    {
        float:right;
    }
    </style>
</head>
<body>
  <a href="javascript:show()">弹出</a>
  <div id="login">
      <div id="title" style="cursor:move">
      <a href="javascript:hide()">关闭</a></div>
      <div>这里是关闭弹窗的内容</div>
      <div>点击标题栏进行拖动</div>
  </div>
  <div id="over"></div>
</body>
</html>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/desmartsystem/scripts/js/jquery-3.3.1.js"></script>
<script type="text/javascript">
    var x_max = $(window).width();
    var y_max = $(window).height();
    var div_width = $("#login").width() + 20;//20是边框
    var div_height = $("#login").height() + 20;
    var _x_max = x_max - div_width;//最大水平位置
    var _y_max = y_max - div_height;//最大垂直位置
    function show()
    {
        var x = (x_max - div_width) / 2;//水平居中
        var y = (y_max - div_height) / 2;//垂直居中
        $("#login").css({"left": x + 'px',"top": y + 'px'});//设置初始位置,防止移动后关闭再打开位置在关闭时的位置
        $("#login").css("display","block");
        $("#over").css("display","block");
    }
    function hide()
    {
        $("#login").css("display","none");
        $("#over").css("display","none");
    }
    $(document).ready(function(){
            $("#title").mousedown(function(title){//title代表鼠标按下事件
                var point_x = title.pageX;//鼠标横坐标,有资料说pageX和pageY是FF独有,不过经过测试chrome和IE8是可以支持的,其余的浏览器没有装,没测
                var point_y = title.pageY;//鼠标纵坐标
                var title_x = $(this).offset().left;//标题横坐标
                var title_y = $(this).offset().top;//标题纵坐标
                $(document).bind("mousemove",function(move){
                    $(this).css("cursor","move");
                    var _point_x = move.pageX;//鼠标移动后的横坐标
                    var _point_y = move.pageY;//鼠标移动后的纵坐标
                    var _x = _point_x - point_x;//移动的水平距离
                    var _y = _point_y - point_y;//移动的纵向距离
                    // console.debug('水平位移: ' + _x + '垂直位移: ' + _y);
                    var __x = _x + title_x;//窗口移动后的位置
                    var __y = _y + title_y;//窗口移动后的位置
                    __x > _x_max ? __x = _x_max : __x = __x;//水平位置最大为651像素
                    __y > _y_max ?__y = _y_max : __y = __y;//垂直位置最大为300像素
                    __x < 0 ? __x = 0 : __x = __x;//水平位置最小为0像素
                    __y < 0 ?__y = 0 : __y = __y;//垂直位置最小为0像素
                    // console.debug('标题X:' + title_x + '标题Y:' + title_y);
                    $("#login").css({"left":__x,"top":__y});
                });//绑定鼠标移动事件,这里绑定的是标题,但是如果移动到区域外的话会导致事件不触发
                $(document).mouseup(function(){
                $(this).unbind("mousemove");//鼠标抬起,释放绑定,防止松开鼠标后,指针移动窗口跟着移动
                });
            });
        });
		</script>