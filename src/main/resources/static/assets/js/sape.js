$(document).ready(function () {
    var pathname = window.location.pathname;
    $('.nav-item > a[href="' + pathname + '"]').parent().addClass('active');
});

$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});