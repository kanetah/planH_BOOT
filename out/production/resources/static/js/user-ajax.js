$(document).ready(function () {

    $('#task').click(function () {
        $.ajaxPlanH({
            url: '/task/fetch',
            data: {
                from: 0,
                to: 5
            },
            success: function (data) {
                var body = $('body');
                $.ajaxPlanH({
                    const_url: '/info/task',
                    success: function (info_fields) {
                        $.each(data, function (idx, elem) {
                            body.append('task:' + idx);
                            body.append('<hr>');
                            $.each(info_fields, function (i, e) {
                                body.append(e['field'] + ': ' + elem[e['field']]);
                                body.append('<br>');
                            });
                            body.append(
                                '<button class = "submit" id = "task' + idx +
                                '" name = "' + elem.id + '">提交</button>');
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
                                success: function (date) {
                                    alert(date.status)
                                }
                            })
                        });
                    }
                });
            }
        })
    });
});