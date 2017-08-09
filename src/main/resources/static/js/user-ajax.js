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
                $.each(data, function (idx, elem) {
                    body.append('task:' + idx);
                    body.append('<hr>');
                    body.append('subject: ' + elem.subject);
                    body.append('<br>');
                    body.append('title: ' + elem.title);
                    body.append('<br>');
                    body.append('content: ' + elem.content);
                    body.append('<br>');
                    body.append('deadline: ' + elem.deadline);
                    body.append('<br>');
                    body.append('submit: ' + elem.submit);
                    body.append('<br>');
                    body.append('path: ' + elem.path);
                    body.append('<hr>');
                });
            }
        })
    });
});