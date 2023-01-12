$(document).ready(function(){
    if(errorMessage != null){
        alert(errorMessage);
    }

    bindDomEvent();

});

function bindDomEvent(){
    $(".custom-file-input").on("change", function() {
        var fileName = $(this).val().split("\\").pop();  //이미지 파일명
        var fileExt = fileName.substring(fileName.lastIndexOf(".")+1); // 확장자 추출
        fileExt = fileExt.toLowerCase(); //소문자 변환

        if(fileExt != "jpg" && fileExt != "jpeg" && fileExt != "gif" && fileExt != "png" && fileExt != "bmp"){
            alert("이미지 파일만 등록이 가능합니다.");
            return;
        }

        $(this).siblings(".custom-file-label").html(fileName);
    });
}
