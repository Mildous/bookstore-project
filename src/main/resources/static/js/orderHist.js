function cancelOrder(orderCode) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var url = "/order/" + orderCode + "/cancel";
    var paramData = {
        orderCode : orderCode,
    };

    var param = JSON.stringify(paramData);
    
    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: param,
        beforeSend: function (xhr) {
            // 데이터 전송 전 헤더에 csrf 값 설정
            xhr.setRequestHeader(header, token);
        },
        dataType: "json",
        cache: false,
        success  : function(result, status){
            alert("주문이 정상적으로 취소되었습니다.");
            location.href='/orders/' + page;
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