$(document).ready(function() {
    $("#searchBtn").on("click", function(e) {
        e.preventDefault();
    });
});

function page(page) {
    var searchDateType = $("#searchDateType").val();
    var searchSellStatus = $("#searchSellStatus").val();
    var searchBy = $("#searchBy").val();
    var searchQuery = $("#searchQuery").val();

    location.href="/admin/products/" + page
        + "?dType=" + searchDateType
        + "&status=" + searchSellStatus
        + "&by=" + searchBy
        + "&query=" + searchQuery;
}