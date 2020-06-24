<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="resources/css/join.css" rel="stylesheet" type="text/css">
<script src="resources/js/jquery-3.5.0.js"></script>
<script src="resources/js/write.js"></script>
<style>
#upfile{display: none}
img{width: 213px; height: 213px;}
#filevalue{display: none}

</style>
</head>
<body>
	<form class="modal-content animate" action="joinProcess" method="post" enctype="multipart/form-data" >
		<div class="imgcontainer">
			<label for="upfile">
				<img src="resources/img/img_avatar2.png" alt="Avatar" class="avatar" id="img">
			</label>
			<input type="file" id="upfile" name="uploadfile" accept="image/gif, image/jpeg, image/png">
			<span id="filevalue"></span>
		</div>

		<div class="container">
			<label for="id"><b>Username</b></label> <span id="message"></span> <input type="text"
				placeholder="Enter Username" name="id" id="id" required> <label
				for="password"><b>Password</b></label> <input type="password"
				placeholder="Enter Password" name="password" id="paswword" required>

			<button type="submit">Join</button>
		</div>
	</form>
</body>
</html>
