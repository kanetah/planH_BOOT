$(function () {
    $.ajaxPlanH({
        url: '/username',
        success: function (username) {
            $('#username').html(username);
        }
    });

    $.task_index = 0;

    const task_btn = $('#task');
    task_btn.click(function () {
        const icon = task_btn.find('> i');
        var historyTaskFlag = false;
        return function () {
            if (task_btn.attr("disabled") === "disabled") return;
            $.setLoadingFlag(true);
            $.ajaxPlanH({
                url: '/task/fetch',
                data: {
                    from: $.task_index,
                    to: $.task_index + 5
                },
                beforeSend: function () {
                    icon.rotate({
                        animateTo: 720, duration: 2000,
                        callback: function () {
                            icon.rotate(0);
                        }
                    });
                },
                success: function (data) {
                    if (data.length === 0) {
                        $.task_index = null;
                        $.setLoadingFlag(false);
                        task_btn.attr({"disabled": "disabled"});
                        $('.wrapper').append('<br/>---&nbsp;&nbsp;没有了&nbsp;&nbsp;---');
                        return;
                    }
                    $.task_index += 5;
                    $.each(data, function (idx, elem) {
                        var flag = new Date() > new Date(elem['deadline']);
                        if (!historyTaskFlag && flag) {
                            historyTaskFlag = true;
                            $('.wrapper').append('<hr/>');
                        }
                        $.addTask(elem, flag);
                    });
                    $.showBuffTask();
                    $.setLoadingFlag(false);
                }
            })
        }
    }());

    $.addSubmit = function (form_data, task_id, task_title, label) {

        var right_list_icon = $('.right-list > span > i');
        var right_list = $('.right-list');
        if (right_list.css('right') !== '0px')
            right_list_icon.click();

        var template = $('.submit-template').clone(true);
        template.removeClass('submit-template');
        $('.loading-box').append(template);
        var title = template.find('.loading-id');
        title.html(task_title);
        var progress_bar = template.find('> .progress > .progress-bar');
        var per;

        $.ajaxPlanH({
            url: '/task/patch/' + task_id,
            data: new FormData(form_data[0]),
            cache: false,
            beforeSend: function () {
                progress_bar.css('width', '0');
            },
            xhr: function () {
                var xhr = $.ajaxSettings.xhr();
                if (xhr.upload) {
                    xhr.upload.addEventListener("progress", function (evt) {
                        per = Math.floor(100 * evt.loaded / evt.total);
                        progress_bar.css("width", per + "%");
                    }, false);
                    return xhr;
                }
            },
            success: function () {
                label.removeClass('label-danger');
                label.addClass('label-info');
                label.html('已提交');
                progress_bar.removeClass('progress-bar-striped');
                progress_bar.text('完成');
            },
            error: function (xhr) {
                progress_bar.css('background-color', 'red');
                label.removeClass('label-info');
                label.addClass('label-danger');
                label.html('提交失败');
                progress_bar.text(xhr.responseJSON.message);
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
                    '<a href="/download/' + data[index] + '">'
                    + data[index] +
                    '</a>'
                );
            });
        }
    });
});
