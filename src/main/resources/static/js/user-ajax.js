$(document).ready(function () {

    $.ajaxPlanH({
        url: '/username',
        success: function (data) {
            $('#username').html(data.username);
        }
    });

    $.task_index = 0;

    $('#task').click(function () {

        if($.task_index === null)
            return;
        $.setLoadingFlag(true);

        $.ajaxPlanH({
            url: '/task/fetch',
            data: {
                from: $.task_index,
                to: $.task_index + 5
            },
            success: function (data) {
                if(data.length === 0){
                    $.task_index = null;
                    $.setLoadingFlag(false);
                    $('#task').attr({"disabled":"disabled"});
                    $('.wrapper').append(
                        '<br/>---&nbsp;&nbsp;没有了&nbsp;&nbsp;---'
                    );
                    return;
                }
                $.task_index += 5;
                $.ajaxPlanH({
                    const_url: '/info/task',
                    success: function (info_fields) {
                        $.each(data, function (idx, elem) {
                            $.addTask(elem, info_fields);
                        });
                        $.showBuffTask();
                        $.setLoadingFlag(false);
                    }
                });
            }
        })
    });
});