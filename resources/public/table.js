$(document).ready(function () {
    var header = $("#table-header");
    $("#table-body").scroll(function ()
    {
        header.prop("scrollLeft", this.scrollLeft);
    });
});
