$(document).ready(function (){
    calculateTotalPrice();

    $("#count").change( function (){
       calculateTotalPrice();
    });
});

function calculateTotalPrice(){
    var count = $("#count").val();
    var price = $("#productPrice").val();
    var totalPrice = price * count;
    $("#totalPrice").html(totalPrice + '원');
}

function order() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/order";
    var paramData = {
        productCode : $("#productCode").val(),
        count : $("#count").val()
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: param,
        beforeSend: function(xhr){
            // 데이터 전송 전 헤더에 csrf 값 설정
            xhr.setRequestHeader(header, token);
        },
        dataType: "json",
        cache: false,
        success: function (result, status){
            alert("주문이 정상적으로 처리되었습니다.");
            location.href='/';
        },
        error: function (jqXHR, status, error) {
            if(jqXHR.status == '401') {
                alert('로그인 후 이용바랍니다.');
                location.href='/members/login';
            } else {
                alert(jqXHR.responseText);
            }
        }
    });
}

function addCart() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    
    var url = "/cart";
    var paramData = {
        productCode: $("#productCode").val(),
        count: $("#count").val()
    };
    
    var param = JSON.stringify(paramData);
    
    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: param,
        beforeSend: function(xhr) {
            // 데이터 전송 전 헤더에 csrf 값 설정
            xhr.setRequestHeader(header, token);
        },
        dataType: "json",
        cache: false,
        success: function(result, status) {
            alert("장바구니에 상품을 담았습니다.");
        },
        error: function(jqXHR, status, error) {
            if(jqXHR.status == '401') {
                alert("로그인 후 이용바랍니다.");
                location.href='/members/login';
            } else {
                alert(jqXHR.responseText);
            }
        }
    });
}
