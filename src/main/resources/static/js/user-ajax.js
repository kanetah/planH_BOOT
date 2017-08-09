$(document).ready(function () {

    $('#task').click(function () {
        $.ajaxPlanH({
            url: '/user/213/task/get',
            data: {
                from: $('#from').val(),
                to: $('#to').val()
            },
            success: function (data) {
                var body = $('body');
                for (var task in data) {
                    body.append('<hr>');
                    body.append(task.subject);
                    body.append('<br>');
                    body.append(task.title);
                    body.append('<br>');
                    body.append(task.content);
                    body.append('<br>');
                    body.append(task.deadline);
                    body.append('<hr>');
                }
            }
        })
    });

});