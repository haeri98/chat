<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script
	src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<meta charset='UTF-8'>
<meta name="robots" content="noindex">
<link rel="shortcut icon" type="image/x-icon"
	href="//production-assets.codepen.io/assets/favicon/favicon-8ea04875e70c4b0bb41da869e81236e54394d63638a1ef12fa558a4a835f1164.ico" />
<link rel="mask-icon" type=""
	href="//production-assets.codepen.io/assets/favicon/logo-pin-f2d2b6d2c61838f7e76325261b7195c27224080bc099486ddd6dccb469b8e8e6.svg"
	color="#111" />
<link rel="canonical"
	href="https://codepen.io/emilcarlsson/pen/ZOQZaV?limit=all&page=74&q=contact+" />
<link
	href='https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600,700,300'
	rel='stylesheet' type='text/css'>



<link rel='stylesheet prefetch'
	href='https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css'>
<link rel='stylesheet prefetch'
	href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.2/css/font-awesome.min.css'>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/chat.css">
<style>
#frame .content {
	width: 100%
}

sup {
	/*  position: relative;
    top: -20px;
    right: 30px; */
	top: -10px;
	right: -97%;
	font-size: 3px;
}

.inout {
	text-align: center;
}
</style>
<title>
   boot.jsp
</title>
</head>
<body>
	<div id="frame">
		<div class="content">
			<div class="contact-profile">
				<%-- <img src="resources/upload${filename}" alt="" /> --%>
				<p>${name}</p>
				<div class="social-media">
					<i class="fa fa-facebook" aria-hidden="true"></i> <i
						class="fa fa-twitter" aria-hidden="true"></i> <i
						class="fa fa-instagram" aria-hidden="true"></i>
				</div>
			</div>
			<div class="messages">
				<ul>

				</ul>
			</div>
			<div class="message-input">
				<div class="wrap">
					<input type="text" id="write" placeholder="Write your message..." />
					<!-- <i class="fa fa-paperclip attachment" aria-hidden="true"></i>  -->
					<button class="exit">나가기</button>
				</div>
			</div>
		</div>
	</div>
	<script
		src='//production-assets.codepen.io/assets/common/stopExecutionOnTimeout-b2a7b3fe212eaa732349046d8416e00a9dec26eb7fd347590fbced3ab38af52e.js'></script>

	<script>
	$(window).on('keyup', function(e) {
		if (e.which == 13) { //엔터
				var message = $(".message-input input").val();
			if(message){ //메시지가 존재하는 경우
				output = '<li class="sent">'					
					+ '<p></p></li>'
			     $(output).appendTo($('.messages ul'));
				$('.messages>ul>li').last().find('p').text(message);
				$('.message-input input').val('');
			}   
			return false;
		}
	});
	
	function moveScroll() {
		$('.messages').scrollTop($('.messages')[0].scrollHeight)
	}
	</script>
</body>
</html>