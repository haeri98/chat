<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link href="resources/css/login.css" rel="stylesheet" type="text/css">
<script src="resources/js/jquery-3.5.0.js"></script>
<script>
	$(function(){
		$(".join").click(function(){
			location.href="join.net";
		});
	})
</script>
</head>

<body>
   <form name="myform" action="login_ok.do" method="post">
   	  <h1>로그인</h1><hr>
      <b>이름</b> 
      <input type="text" name="name" id="name" placeholder="Enter Name" required>
      <div class="clearfix">
      	<input type="submit" class="submitbtn submit" value="LOGIN">
      </div>
   </form>
</body>
</html>