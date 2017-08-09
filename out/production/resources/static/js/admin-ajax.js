$(document).ready(function () {

    $('#task').click(function () {
        $.ajaxPlanH({
            url: '/task/create',
            data: {
                subject: $('#subject').val(),
                title: $('#title').val(),
                content: $('#content').val(),
                deadline: $('#date').val()
            },
            success: function (data) {
                $('#subject').val("");
                $('#title').val("");
                $('#content').val("");
                $('#date').val("");
            }
        })
    });

    $('#user').click(function () {
        $.ajaxPlanH({
            url: '/user/create',
            data: {
                code: $('#code').val(),
                name: $('#name').val()
            },
            success: function () {
                $('#code').val("");
                $('#name').val("");
            }
        })
    });
});
