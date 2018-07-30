<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人脸注册</title>
<script type="text/javascript" src="js/jquery-1.9.1.min.js"></script>
<style>
video {
	border: 1px solid #ccc;
	display: block;
	margin: 0 0 20px 0;
	float:left;
}
#canvas {
	margin-top: 20px;
	border: 1px solid #ccc;
	display: block;
}
</style>
</head>
<body>
	<video id="video" width="500" height="400" autoplay></video>
	<canvas id="canvas" width="500" height="400"></canvas>
	<button id="snap">拍照</button>
	<button id="insert">注册</button>
	<button id="login">登录</button>
	<button id="getAuth">获取授权</button>
	<script type="text/javascript">
		var context = canvas.getContext("2d");
		//当DOM树构建完成的时候就会执行DOMContentLoaded事件
		window.addEventListener("DOMContentLoaded", function() {
			//获得Canvas对象
			var canvas = document.getElementById("canvas");
			//获得video摄像头区域
			var video = document.getElementById("video");
			var videoObj = {
				"video" : true
			};
			var errBack = function(error) {
				console.log("Video capture error: ", error.code);
			};
			//获得摄像头并显示到video区域
			if (navigator.getUserMedia) { // Standard
				navigator.getUserMedia(videoObj, function(stream) {
					video.src = stream;
					video.play();
				}, errBack);
			} else if (navigator.webkitGetUserMedia) { // WebKit-prefixed
				navigator.webkitGetUserMedia(videoObj, function(stream) {
					video.src = window.webkitURL.createObjectURL(stream);
					video.play();
				}, errBack);
			} else if (navigator.mozGetUserMedia) { // Firefox-prefixed
				navigator.mozGetUserMedia(videoObj, function(stream) {
					video.src = window.URL.createObjectURL(stream);
					video.play();
				}, errBack);
			}
		}, false);
		// 触发拍照动作
		document.getElementById("snap").addEventListener("click", function() {
			context.drawImage(video, 0, 0, 640, 480);
		});
		
		document.getElementById("insert").addEventListener("click", function() {
		
			//实际运用可不写，测试代 ， 为单击拍照按钮就获取了当前图像，有其他用途    
			           var canvans = document.getElementById("canvas");
			      
			//获取浏览器页面的画布对象   
			alert("insert");
			   //以下开始编 数据   
		   var imgData = canvans.toDataURL(); 
			//将图像转换为base64数据
			      var base64Data = imgData.substr(22); 
			//在前端截取22位之后的字符串作为图像数据 
			 var userPhone=document.getElementById("userPhone");
			                            //开始异步上             
			   $.post("insertFace.do", {"img": base64Data,"userPhone":userPhone.value}, function (data) { 
			      				if (data == "OK") { 
			      				alert("hhh");
			      				}
			      				else
			      					{
			      					alert('wwww');
			      					}
			});
		});
		
		document.getElementById("login").addEventListener("click", function() {
			
			//实际运用可不写，测试代 ， 为单击拍照按钮就获取了当前图像，有其他用途    
			    var canvans = document.getElementById("canvas");
			      
			//获取浏览器页面的画布对象   
			alert("login");
			   //以下开始编 数据   
		   var imgData = canvans.toDataURL(); 
			//将图像转换为base64数据
			      var base64Data = imgData.substr(22); 
			//在前端截取22位之后的字符串作为图像数据       
			                            //开始异步上             
			   $.post("faceLogin.do", {"img": base64Data}, function (data) { 
			      				if (data == "OK") { 
			      				alert("hhh");
			      				}
			      				else
			      					{
			      					alert('wwww');
			      					}
			});
		});
		
		document.getElementById("getAuth").addEventListener("click", function() {
			alert("getAuth");
			       
			   $.post("getAuth.do", null, function (data) { 
			      				if (data == "OK") { 
			      				alert("hhh");
			      				}
			      				else
			      					{
			      					alert('wwww');
			      					}
			});
		});
	</script>
</body>
</html>