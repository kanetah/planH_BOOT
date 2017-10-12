$(() => {
    $.ajaxPlanH({
        url: '/username',
        success: (username) => {
            $('#username').html(username[0]);
        }
    });

    $.task_index = 0;

    $('#task').click(() => {
        let icon = $('#task').find('> i');
        if ($.task_index === null)
            return;
        $.setLoadingFlag(true);

        $.ajaxPlanH({
            url: '/task/fetch',
            data: {
                from: $.task_index,
                to: $.task_index + 5
            },
            beforeSend: () => {
                icon.rotate({
                    animateTo: 720, duration: 2000,
                    callback: () => {
                        icon.rotate(0);
                    }
                });
            },
            success: (data) => {
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
                $.each(data, (idx, elem) => {
                    $.addTask(elem);
                });
                $.showBuffTask();
                $.setLoadingFlag(false);
            }
        })
    });

    $.addSubmit = (form_data, task_id, task_title, label) => {

        let right_list_icon = $('.right-list > span > i');
        let right_list = $('.right-list');
        if (right_list.css('right') !== '0px')
            right_list_icon.click();

        let template = $('.submit-template').clone(true);
        template.removeClass('submit-template');
        $('.loading-box').append(template);
        let title = template.find('.loading-id');
        title.html(task_title);
        let progress_bar = template.find('> .progress > .progress-bar');
        let per;

        $.ajaxPlanH({
            url: '/task/patch/' + task_id,
            data: new FormData(form_data[0]),
            cache: false,
            beforeSend: () => {
                progress_bar.css('width', '0');
            },
            xhr: () => {
                let xhr = $.ajaxSettings.xhr();
                if (xhr.upload) {
                    xhr.upload.addEventListener("progress", (evt) => {
                        per = Math.floor(100 * evt.loaded / evt.total);
                        progress_bar.css("width", per + "%");
                    }, false);
                    return xhr;
                }
            },
            success: () => {
                label.removeClass('label-danger');
                label.addClass('label-info');
                label.html('已提交');
                progress_bar.removeClass('progress-bar-striped');
                progress_bar.text('完成');
            },
            error: (xhr) => {
                progress_bar.css('background-color', 'red');
                label.removeClass('label-info');
                label.addClass('label-danger');
                label.html('提交失败');
                progress_bar.text(xhr.responseJSON.message);
            },
            complete: () => {
                progress_bar.removeClass('active');
            }
        });
    };

    $.ajaxPlanH({
        const_url: '/download/fileNames/get',
        success: (data) => {
            let download_box = $('.download-box');
            $.each(data, (index) => {
                download_box.append(
                    '<a href="/download/' + data[index] + '">'
                    + data[index] +
                    '</a>'
                );
            });
        }
    });
});