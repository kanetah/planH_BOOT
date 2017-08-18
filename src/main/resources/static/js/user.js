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
        template.css('top', '100px');
        template.css('opacity', '0');
        $('.wrapper').append(template);

        template.find('.task_id > span').html(task_data[field_name[0]['field']]);
        template.find('.submit').attr("name", task_data[field_name[0]['field']]);
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
            template.find('.submitFileName > a').attr(
                'id',
                'task_a_' + task_data[field_name[0]['field']]
            );
            template.find('.submitFileName > form').attr(
                'id',
                'task_form_' + task_data[field_name[0]['field']]
            );
            template.find('.submitFileName > form > [type = "file"]').attr(
                'id',
                task_data[field_name[0]['field']]
            );
        } else {
            label.addClass('label-info');
            label.html('已提交');
            // template.find('.submitFileName > form').css('display', 'none');
            var file = template.find('.submitFileName > a');
            file.html(task_data[field_name[6]['field']]);
        }

        tasks.unshift(template);
    };

    $.showBuffTask = function () {
        showTask(tasks.pop());
    };

    function showTask(node) {

        if (node === null || node === undefined) {
            return;
        }

        node.animate({
            top: 0,
            opacity: 1.0
        }, 700, "swing", function () {
            showTask(tasks.pop());
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

    var firstChange = true;
    $('[type = "file"]').on('change', function () {
        if (firstChange) {
            $('[name = "' + this.id + '"]').removeAttr("disabled");
            firstChange = false;
        }
        $('#task_a_' + this.id)
            .html(
                this.value.substr(
                    this.value.lastIndexOf('\\') + 1
                )
            );
    });

    var arrow = $('.right-list-arrow');
    arrow.animate({
        right: 0,
        opacity: 0.5
    }, 1000, "swing");
    arrow.click(function () {
        $('.right-list').animate({
            right: 0
        }, 1000, "swing");
        arrow.animate({
            right: '-5%',
            opacity: 0.0
        }, 1000, "swing");
    });
    $('.right-list > span > i').click(function () {
        $('.right-list').animate({
            right: '-20%'
        }, 1000, "swing");
        arrow.animate({
            right: 0,
            opacity: 0.5
        }, 1500, "swing");
    });
});
