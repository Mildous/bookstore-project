$(document).ready(function() {
    $("#searchBtn").on("click", function(e) {
        e.preventDefault();
        page(0);
    });
});

function page(page) {
    var dateType = $("#date").val();
    var sellStatus = $("#status").val();
    var searchBy = $("#by").val();
    var searchQuery = $("#query").val();

    location.href="/admin/products/" + page
        + "?date=" + dateType
        + "&status=" + sellStatus
        + "&by=" + searchBy
        + "&query=" + searchQuery;
}