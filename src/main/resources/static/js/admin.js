$(function () {
    $('#cancel_create_task').click(function () {
        $('#title').val("");
        $('#content').val("");
        $('#format').val("");
        $('#date').val("");
    });

    $('#cancel_create_user').click(function () {
        $('#code').val("");
        $('#name').val("");
    });
});
