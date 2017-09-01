$(document).ready(function () {
    var window_width = $(window).width();

    if (window_width >= 992) {
        big_image = $('.wrapper > .header');
        $(window).on('scroll', materialKitDemo.checkScrollForParallax);
    }

    var tasks = [];
    $.addTask = function (task_data) {
        var template = $('.template').clone(true);
        template.removeClass('template');
        template.css('top', '100px');
        template.css('opacity', '0');
        $('.wrapper').append(template);

        template.find('.task_id > span').html(task_data[$._info_fields[0]['field']]);
        template.find('.subject > li > a').append(task_data[$._info_fields[1]['field']]);
        template.find('.title').html(task_data[$._info_fields[2]['field']]);
        template.find('.content > p').html(task_data[$._info_fields[3]['field']]);
        template.find('.deadline > .date').html(task_data[$._info_fields[4]['field']].substr(0, 10));
        var label = template.find('.deadline > .label');
        template.find('.submitFileName > form > [type = "file"]')
            .on('change', function () {
                template.find('.submit-button').removeAttr("disabled");
                template.find('.submitFileName > a')
                    .html(
                        this.value.substr(
                            this.value.lastIndexOf('\\') + 1
                        )
                    );
            });
        if (
            task_data[$._info_fields[6]['field']] === undefined ||
            task_data[$._info_fields[6]['field']] === null ||
            task_data[$._info_fields[6]['field']] === ""
        ) {
            label.addClass('label-danger');
            label.html('未提交');
        } else {
            label.addClass('label-info');
            label.html('已提交');
            var file = template.find('.submitFileName > a');
            file.html(task_data[$._info_fields[6]['field']]);
        }
        var deadline = new Date(
            task_data[$._info_fields[4]['field']]
        );
        var now = new Date();
        if (now > deadline)
            label.before("<span class='label label-default'>已截止</span>");

        template.find('.submit_div > .submit-button')
            .bind("click", function (event) {
                var uploadForm = template.find('.submitFileName > form');
                $.addSubmit(
                    uploadForm,
                    task_data[$._info_fields[0]['field']],
                    label
                );
                event.stopPropagation();
            });

        tasks.unshift(template);
    };

    $.showBuffTask = function () {
        showNode(tasks.pop());
    };

    function showNode(node) {
        if (node === null || node === undefined)
            return;
        node.animate({
            top: 0,
            opacity: 1.0
        }, 700, "swing", function () {
            showNode(tasks.pop());
        })
    }

    var loadingFlag = false;
    $(window).scroll(function () {
        var scrollBottom = $(document).height() - $(window).height() - $(window).scrollTop();
        if (scrollBottom < 15 && loadingFlag === false) {
            $('#task').trigger('click');
        }
    });

    $.setLoadingFlag = function (val) {
        if (val)
            $('#spinnerHolder').append('<div class="spinner"></div>');
        else
            $('.spinner').remove();
        loadingFlag = val;
    };

    var right_list_icon = $('.right-list > span > i');
    var right_list_icon_flag = false;
    right_list_icon.click(function () {
        if (right_list_icon_flag) {
            $('.right-list').animate({
                right: '-18%'
            }, 1000, "swing");
            right_list_icon.rotate({animateTo: 0});
        } else {
            $('.right-list').animate({
                right: '0'
            }, 1000, "swing");
            right_list_icon.rotate({animateTo: 180});
        }
        right_list_icon_flag = !right_list_icon_flag;
    });

    var download_icon = $('.download_file_div > span > i');
    var download_icon_flag = false;
    download_icon.click(function () {
        if (download_icon_flag) {
            $('.download_file_div').animate({
                left: '-18%'
            }, 1000, "swing");
            download_icon.rotate({animateTo: 0});
        } else {
            $('.download_file_div').animate({
                left: '0'
            }, 1000, "swing");
            download_icon.rotate({animateTo: -180});
        }
        download_icon_flag = !download_icon_flag
    });
});
