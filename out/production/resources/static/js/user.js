$(document).ready(function () {
    var window_width = $(window).width();

    if (window_width >= 992) {
        big_image = $('.wrapper > .header');

        $(window).on('scroll', materialKitDemo.checkScrollForParallax);
    }

     $.addTask = function (task_data) {
        alert(task_data.id);
        var template = $('.template').clone(true);
        template.removeClass('template');
        template.css('display', 'none');
        var nodes = template.childNodes;
        $.each(nodes, function (idx, e) {
            if(e.class.contains('task_id'))
                e.html(e.html() + task_data.id);
        });
        $('.wrapper').appendChild(template);
        template.fadeIn(9000);
     }
});
