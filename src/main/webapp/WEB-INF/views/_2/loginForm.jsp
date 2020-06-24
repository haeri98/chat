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
   <form name="myform" action="loginProcess" method="post">
   	  <h1>로그인</h1><hr>
      <b>UserName</b> 
      <input type="text" name="id" id="id" placeholder="Enter Name" required>
      <b>Password</b> 
      <input type="password" name="password" id="password" placeholder="Enter Password" required>
      <div class="clearfix">
      	<input type="submit" class="submitbtn submit" value="LOGIN">
      	<button type="button" class="cancelbtn submit" onclick="javascript:location.href='join'">JOIN</button>
      </div>
   </form>
</body>
</html>