$(document).ready(function(){
	//등록 버튼 클릭할 때 이벤트 부분
	$("form").submit(function(){
		if($.trim($("input").eq(2).val())==""){
			alert("비밀번호를 입력하세요");
			$("input:eq(2)").focus();
			return false;
		}
		
		if($.trim($("input").eq(1).val())==""){
			alert("아이디를 입력하세요");
			$("input:eq(1)").focus();
			return false;
		}
	})
	
	$("#upfile").change(function(){
		var inputfile = $(this).val().split('\\');
		$('#filevalue').text(inputfile[inputfile.length-1]);
	})
	
	$("input:eq(1)").on('keyup',function(){
		$("#message").empty();
		var pattern = /^\w{5,12}$/;
		var id = $("input:eq(1)").val();
		if(!pattern.test(id)){
			$("#message").css('color','red').html("영문자 숫자_로 5~12자 가능합니다.");
			checkid=false;
			return
		}
		$.ajax({
			url: "idcheck",
			data: {"id":id},
			success: function(resp){
				if(resp == -1){
					$("#message").css('color','green').html("사용 가능한 아이디입니다.");
					checkid=true;
				} else {
					$("#message").css('color','blue').html("사용중인 아이디입니다.");
					checkid = false;
				}
			}
		})// ajax end
	})//id keyup end
	
	$("#upfile").on("change",handelImgFileSelect);
		
	function handelImgFileSelect(e) {
		var files = e.target.files;
		var filesArr = Array.prototype.slice.call(files);
		
		filesArr.forEach(function(f) {
			if(!f.type.match("image.*")) {
				alert("확장자는 이미지 확장자만 가능합니다.");
				return;
			}
			sel_file=f;
			var reader = new FileReader();
			reader.onload=function(e){
				$("#img").attr("src",e.target.result);
			}
			reader.readAsDataURL(f);
		});
	}
		
	function preview(e) {
		var file = e.target.files[0];
		
		if(!file.type.match('image.*')) {
			alert("확장자는 이미지 확장자만 가능합니다.");
			return;
		}
		
		var reader = new FileReader();
		reader.readAsDataURL(file);
		reader.onload=function(e) {
			$("img").attr('src',e.target.result);
		}
	}
})