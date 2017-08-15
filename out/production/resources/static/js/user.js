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
        template.css('display', 'none');
        $('.wrapper').append(template);

        template.find('.task_id > span').html(task_data[field_name[0]['field']]);
        template.find('.subject > li > a').append(task_data[field_name[1]['field']]);
        template.find('.title').html(task_data[field_name[2]['field']]);
        template.find('.content > p').html(task_data[field_name[3]['field']]);
        template.find('.deadline > .date').html(task_data[field_name[4]['field']].substr(0, 10));
        var label = template.find('.deadline > .label');
        if(
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
        if(node === null)
            return;
        node.fadeIn(700, function () {
            showTask(tasks.pop());
        });
    }
});
