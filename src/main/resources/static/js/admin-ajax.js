$(document).ready(function () {

    $.ajaxPlanH({
        url: '/task/get',
        success: function (date) {
            var task_table = $('#task_table');
            $.each(date, function (idx, elem) {
                var new_tr = document.createElement("tr");
                var ths = [
                    'id',
                    'subject',
                    'title',
                    'content',
                    'saveFormat',
                    'fileFormat',
                    'deadline'
                ];
                $.each(ths, function (i) {
                    var new_td = document.createElement("td");
                    new_td.innerHTML = elem[ths[i]];
                    new_tr.append(new_td);
                    $(new_tr).attr(ths[i], elem[ths[i]]);
                });
                $(new_tr).bind('click', function (event) {
                    var modal = $('#update_task_Modal');
                    var task_id = $(this).attr('id');
                    var body = modal.find('div > div > .modal-body');
                    body.find('#update_id').html(task_id);
                    var inputs = body.find('div');
                    inputs.find('#update_subject').val($(this).attr('subject'));
                    inputs.find('#update_title').val($(this).attr('title'));
                    inputs.find('#update_content').val($(this).attr('content'));
                    inputs.find('#update_type').val($(this).attr('fileFormat'));
                    inputs.find('#update_format').val($(this).attr('saveFormat'));
                    inputs.find('#update_date').val($(this).attr('deadline'));
                    modal.modal('show');
                    updateSubmitTable(task_id);
                    event.stopPropagation();
                });
                task_table.append(new_tr);
            });
        }
    });

    function updateSubmitTable(id) {
        $.ajaxPlanH({
            url: "/task/submit",
            data: {
                taskId: id
            },
            success: function (data) {
                var submitTable = $('#submit_table');
                $('tr').remove('.submit_table_tr');
                $.each(data, function (idx, elem) {
                    var new_tr = document.createElement("tr");
                    $(new_tr).attr('class', 'submit_table_tr');
                    var new_td = document.createElement("td");
                    new_td.innerHTML = idx + 1;
                    new_tr.append(new_td);
                    new_td = document.createElement("td");
                    new_td.innerHTML = elem['fileName'];
                    new_tr.append(new_td);
                    new_td = document.createElement("td");
                    new_td.innerHTML = elem['submitDate'];
                    new_tr.append(new_td);
                    submitTable.append(new_tr);
                });
            }
        })
    }

    $.ajaxPlanH({
        const_url: "/subject/names",
        success: function (data) {
            $.each(data, function (idx, elem) {
                $('#update_subject').append(
                    '<option>' + elem + '</option>'
                );
                $('#subject').append(
                    '<option>' + elem + '</option>'
                );
            });
        }
    });

    $('#create_task').click(function () {
        var inputs = $('#create_task_Modal').find('div > div > #addTask > div');
        $.ajaxPlanH({
            url: '/task/create',
            data: {
                subject: inputs.find('#subject').val(),
                title: inputs.find('#title').val(),
                content: inputs.find('#content').val(),
                type: inputs.find('#type').val(),
                format: inputs.find('#format').val(),
                deadline: inputs.find('#date').val()
            },
            success: function () {
                location.reload(true);
            }
        });
    });

    $('#update_task').click(function () {
        var task_id = $('#update_id').html();
        var inputs = $('#update_task_Modal').find('div > div > .modal-body > div');
        $.ajaxPlanH({
            url: '/task/update',
            data: {
                id: task_id,
                subject: inputs.find('#update_subject').val(),
                title: inputs.find('#update_title').val(),
                content: inputs.find('#update_content').val(),
                type: inputs.find('#update_type').val(),
                format: inputs.find('#update_format').val(),
                deadline: inputs.find('#update_date').val()
            },
            success: function () {
                location.reload(true);
            }
        });
    });

    $('#create_user').click(function () {
        $.ajaxPlanH({
            url: '/user/create',
            data: {
                code: $('#code').val(),
                name: $('#name').val()
            },
            success: function () {
                $('#cancel_create_user').trigger('click');
            }
        })
    });

    $('#batch_user').click(function () {
        var form_data = $('#batch_user_form');
        $.ajaxPlanH({
            url: '/user/batch',
            data: new FormData(form_data[0]),
            cache: false,
            success: function () {
                alert('成功');
            },
            error: function () {
                alert('失败');
            }
        });
    });

    $('#shutdown').click(function () {
        $.ajaxPlanH({
            const_url: '/shutdown',
            error: function () {
            }
        });
        $('.navbar-brand').append('（服务已停止）');
        $('#clear_shutdown').trigger('click');
    });
});
