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
            success: function () {
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

    $('#batch_user').click(function () {
        var form_data = $('#batch_user_form');
        $.ajaxPlanH({
            url: '/user/batch',
            data: new FormData(form_data[0]),
            cache: false,
            success: function () {
                alert('成功');
            },
            error: function () {
                alert('失败');
            }
        });
    })
});
