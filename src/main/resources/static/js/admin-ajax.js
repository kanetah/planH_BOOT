$(document).ready(function () {

    $('#task').click(function () {
        $.ajaxPlanH({
            url: '/admin/task/create',
            data: {
                subject: $('#subject').val(),
                content: $('#content').val(),
                date: $('#date').val()
            },
            success: function (data) {
                alert(data)
            }
        })
    });

    $('#user').click(function () {
        $.ajaxPlanH({
            url: 'admin/user/create',
            data: {
                code: $('#code').val(),
                name: $('#name').val()
            },
            success: function () {
                alert("su")
            }
        })
    });
});
