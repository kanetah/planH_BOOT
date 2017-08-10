$(document).ready(function () {

    $('#task').click(function () {
        $.ajaxPlanH({
            url: '/task/fetch',
            data: {
                from: $('#from').val(),
                to: $('#to').val()
            },
            success: function (data) {
                var body = $('body');

                $.each(data, function (idx, elem) {
                    body.append('task:' + idx);
                    body.append('<hr>');
                    body.append('taskId: ' + elem.taskId);
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
                    body.append('name: ' + elem.name);
                    body.append('<hr>');
                    body.append(
                        '<button class = "submit" id = "task' + idx +
                        '" name = "' + elem.taskId + '">提交</button>');
                    body.append('<hr>');
                });

                $('.submit').click(function () {
                    var uploadForm = $('#upload');
                    uploadForm.find('> [name = "taskId"]').val(this.name);
                    var formData = new FormData(uploadForm[0]);

                    $.ajaxPlanH({
                        url: "/task/patch",
                        data: formData,
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function (date) {
                            alert(date.status)
                        }
                    })
                });
            }
        })
    });
});