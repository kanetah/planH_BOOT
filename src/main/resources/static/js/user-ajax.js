$(document).ready(function () {

    $.ajaxPlanH({
        url: '/username',
        success: function (data) {
            $('#username').html('hi, ' + data.username);
        }
    });

    $('#task').click(function () {
        $.ajaxPlanH({
            url: '/task/fetch',
            data: {
                from: 0,
                to: 5
            },
            success: function (data) {
                $.ajaxPlanH({
                    const_url: '/info/task',
                    success: function (info_fields) {
                        $.each(data, function (idx, elem) {
                            $.addTask(elem, info_fields);
                        });
                        $.showBuffTask();

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

                        $.setLoadingFlag(false);
                    }
                });
            }
        })
    });
});