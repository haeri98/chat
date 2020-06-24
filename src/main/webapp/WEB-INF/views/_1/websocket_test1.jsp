<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
* {
	margin: 0 auto;
	text-align: center;
}

input {
	margin-top: 10%;
	border-radius: 10px;
	width: 60%;
	height: 30px;
}

button {
	width: 50px;
	height: 50px;
	border-radius: 50%;
	border: none;
	margin: 5% 5% 0 5%;
}

button:nth-child(1) {
	background: #FF3232
}

button:nth-child(2) {
	background: #FFC31E
}

button:nth-child(3) {
	background: #00BFFF
}
</style>
<title>Insert title here</title>
</head>
<body>
	<div>
		<input type="text" id="sender" value="${name }" style="display: none">
		<input type="text" id="messageinput">
	</div>
	<div>
		<button type="button">open</button>
		<button type="button">send</button>
		<button type="button">close</button>
	</div>
	<div id="messages"></div>
	<script src="resources/js/jquery-3.5.0.js"></script>
	<script>
		var ws;
		$('button:eq(1)').prop('disabled', true);
		$('button:eq(2)').prop('disabled', true);

		//open 클릭 한 경우
		$('button:eq(0)').click(function() {
			if (ws !== undefined && ws.readyState !== WebSocket.CLOSED) {
				writeResponse("WebSocket is already open");
				return;
			}

			//웹 소켓이 동작하기 위해 제일 먼저 서버와 연결 되어야 한다.
			//HTML5 에서 제공하는 WebSocket 객체를 통해 서버 연결을 수행한다.
			//프로토콜은 ws 를 사용한다.
			//웹 소켓 객체를 만드는 코드
			//ws = new WebSocket("ws://localhost/mychat/echo.do");

			var url = "ws://${url}/echo.do"

			ws = new WebSocket(url);
			$(this).prop('disabled', true); //open 버튼 활성화 
			$('button:eq(1)').prop('disabled', false); //send 버튼 활성화
			$('button:eq(2)').prop('disabled', false); //close 버튼 활성화 
			//웹 소켓이 연결 되었을 때 호출 되는 이벤트 
			ws.onopen = function(event) {
				if (event.data === undefined)
					return;
				writeResponse(event.data);
			};

			//서버에서  전송하는 데이터를 받으려면 message 이벤트를 구현하면 된다.
			//웹 소켓에서 메세지가 날라왔을 때 호출 되는 이벤트 
			ws.onmessage = function(event) {
				//console.log(event.data);
				writeResponse(event.data);

			};

			//웹 소켓이 닫혔을 때 호출 되는 이벤트 
			ws.onclose = function(event) {
				writeResponse("Connection closed");
				$('button:eq(0)').prop('disabled', false);
				$('button:eq(1)').prop('disabled', true);
				$('button:eq(2)').prop('disabled', true);
			};
		});
		//close 클릭한 경우
		$('button:eq(2)').click(function() {
			ws.close(); //웹 소켓을 닫습니다.
		});

		//send 클릭한 경우
		$('button:eq(1)').click(send);
		function send() {
			if ($('#messageinput').val() == "") {
				alert("메시지를 입력하세요");
				$("#messageinput").focus();
				return false;
			}
			var text = $("#messageinput").val() + "," + $("#sender").val();
			// 서버와 연결이되면 이제부터 데이터를 주고 받을 수 있습니다.
			// send메서드를 이용해서 데이터를 서버로 보낼 수 있습니다.
			ws.send(text); // 웹 소켓으로 text를 보냅니다. 보내는 형식(내용, 보낸사람)
			$("#messageinput").val('');
		}

		function writeResponse(text) {
			messages.innerHTML += "<br/>"+"<div>";
			messages.innerText += text;
			messages.innerHTML += "</div>";
		}
	</script>
</body>
</html>