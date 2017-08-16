$(document).ready(function () {
    var window_width = $(window).width();

    if (window_width >= 992) {
        big_image = $('.wrapper > .header');

        $(window).on('scroll', materialKitDemo.checkScrollForParallax);
    }

    var tasks = [];
    $.addTask = function (task_data, field_name) {

        var template = $('.template').clone(true);
        template.removeClass('template');
        template.css('top', '50px');
        template.css('opacity', '0');
        $('.wrapper').append(template);

        template.find('.task_id > span').html(task_data[field_name[0]['field']]);
        template.find('.subject > li > a').append(task_data[field_name[1]['field']]);
        template.find('.title').html(task_data[field_name[2]['field']]);
        template.find('.content > p').html(task_data[field_name[3]['field']]);
        template.find('.deadline > .date').html(task_data[field_name[4]['field']].substr(0, 10));
        var label = template.find('.deadline > .label');
        if (
            task_data[field_name[6]['field']] === undefined ||
            task_data[field_name[6]['field']] === null ||
            task_data[field_name[6]['field']] === ""
        ) {
            label.addClass('label-danger');
            label.html('未提交');
        } else {
            label.addClass('label-info');
            label.html('已提交');
            var file = template.find('.submitFileName');
            file.html(task_data[field_name[6]['field']]);
            file.attr('title', '已提交于：' + task_data[field_name[5]['field']]);
        }

        tasks.unshift(template);
    };

    $.showBuffTask = function () {
        showTask(tasks.pop());
    };

    function showTask(node) {
        if (node === null) {
            return;
        }

        node.animate({
            top: 0,
            opacity: 1.0
        }, 1000, "swing", function () {
            showTask(tasks.pop());
        })
    }

    var loadingFlag = false;
    $(window).scroll(function () {
        var scrollBottom = $(document).height() - $(window).height() - $(window).scrollTop();
        if(scrollBottom < 15 && loadingFlag === false){
            $.setLoadingFlag(true);
            $('#task').trigger('click');
        }
    });

    $.setLoadingFlag = function (val) {
        if(val)
            $('#spinnerHolder').append('<div class="spinner"></div>');
        else
            $('.spinner').remove();
        loadingFlag = val;
    }
});
