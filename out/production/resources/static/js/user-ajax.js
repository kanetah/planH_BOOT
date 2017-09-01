$(document).ready(function () {

    $.ajaxPlanH({
        url: '/username',
        success: function (data) {
            $('#username').html(data.username);
        }
    });

    $.ajaxPlanH({
        const_url: '/info/task',
        success: function (info_fields) {
            $._info_fields = info_fields;
        }
    });

    $.task_index = 0;

    $('#task').click(function () {

        if ($.task_index === null)
            return;
        $.setLoadingFlag(true);

        $.ajaxPlanH({
            url: '/task/fetch',
            data: {
                from: $.task_index,
                to: $.task_index + 5
            },
            success: function (data) {
                if (data.length === 0) {
                    $.task_index = null;
                    $.setLoadingFlag(false);
                    $('#task').attr({"disabled": "disabled"});
                    $('.wrapper').append(
                        '<br/>---&nbsp;&nbsp;没有了&nbsp;&nbsp;---'
                    );
                    return;
                }
                $.task_index += 5;
                $.each(data, function (idx, elem) {
                    $.addTask(elem);
                });
                $.showBuffTask();
                $.setLoadingFlag(false);
            }
        })
    });


    $.addSubmit = function (form_data, task_id, label) {

        var right_list_icon = $('.right-list > span > i');
        var right_list = $('.right-list');
        alert(right_list.css('right'));
        if(right_list.css('right') !== '0px')
            right_list_icon.click();

        var template = $('.submit-template').clone(true);
        template.removeClass('submit-template');
        $('.loading-box').append(template);
        template.find('.loading-id').html(task_id);
        var progress_bar = template.find('> .progress > .progress-bar');

        $.ajaxPlanH({
            url: '/task/patch/' + task_id,
            data: new FormData(form_data[0]),
            cache: false,
            beforeSend: function () {
                progress_bar.css('width', '30%');
            },
            success: function () {
                progress_bar.css('width', '100%');
                label.removeClass('label-danger');
                label.addClass('label-info');
                label.html('已提交');
            },
            error: function () {
                progress_bar.css('background-color', 'red');
                label.removeClass('label-info');
                label.addClass('label-danger');
                label.html('提交失败');
            },
            complete: function () {
                progress_bar.removeClass('active');
            }
        });
    };

    $.ajaxPlanH({
        const_url: '/download/fileNames/get',
        success: function (data) {
            var download_box = $('.download-box');
            $.each(data, function (index) {
                download_box.append(
                    '<a href="/download/' + data[index] + '">' + data[index] + '</a>'
                );
            });
        }
    });
});