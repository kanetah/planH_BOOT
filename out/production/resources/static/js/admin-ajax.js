$(() => {
    $.ajaxPlanH({
        url: '/task/get',
        success: (date) => {
            const ths = [
                'id',
                'subject',
                'title',
                'content',
                'saveFormat',
                'fileFormat',
                'saveProcessor',
                'deadline'
            ];
            let task_table = $('#task_table');
            $.each(date, (idx, elem) => {
                let new_tr = document.createElement("tr");
                $.each(ths, (i) => {
                    let new_td = document.createElement("td");
                    new_td.innerHTML = elem[ths[i]];
                    new_tr.append(new_td);
                    $(new_tr).attr(ths[i], elem[ths[i]]);
                });
                $(new_tr).bind('click', function (event) {
                    let modal = $('#update_task_Modal');
                    let task_id = $(this).attr('id');
                    let body = modal.find('div > div > .modal-body');
                    body.find('#update_id').html(task_id);
                    let inputs = body.find('div');
                    inputs.find('#update_subject').val($(this).attr('subject'));
                    inputs.find('#update_title').val($(this).attr('title'));
                    inputs.find('#update_content').val($(this).attr('content'));
                    inputs.find('#update_type').val($(this).attr('fileFormat'));
                    inputs.find('#update_format').val($(this).attr('saveFormat'));
                    inputs.find('#update_date').val($(this).attr('deadline'));
                    inputs.find('#update_processor').val($(this).attr('saveProcessor'));
                    modal.modal('show');
                    updateSubmitTable(task_id);
                    event.stopPropagation();
                });
                task_table.append(new_tr);
            });
        }
    });

    let updateSubmitTable = (id) => {
        $.ajaxPlanH({
            url: "/task/submit",
            data: {
                taskId: id
            },
            success: (data) => {
                let submitTable = $('#submit_table');
                $('tr').remove('.submit_table_tr');
                $.each(data, (idx, elem) => {
                    let new_tr = document.createElement("tr");
                    $(new_tr).attr('class', 'submit_table_tr');
                    let new_td = document.createElement("td");
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
    };

    const option = (elem) => `<option>${elem}</option>`;

    $.ajaxPlanH({
        const_url: "/subject/names",
        success: (data) => {
            $.each(data, (idx, elem) => {
                $('#update_subject').append(option(elem));
                $('#subject').append(option(elem));
            });
        }
    });

    $.ajaxPlanH({
        const_url: "/processor/values",
        success: (data) => {
            $.each(data, (idx, elem) => {
                $('#update_processor').append(option(elem));
                $('#processor').append(option(elem));
            });
        }
    });

    $('#create_task').click(() => {
        let inputs = $('#create_task_Modal').find('div > div > #addTask > div');
        $.ajaxPlanH({
            url: '/task/create',
            data: {
                subject: inputs.find('#subject').val(),
                title: inputs.find('#title').val(),
                content: inputs.find('#content').val(),
                type: inputs.find('#type').val(),
                format: inputs.find('#format').val(),
                deadline: inputs.find('#date').val(),
                processor: inputs.find('#processor').val()
            },
            success: () => {
                location.reload(true);
            }
        });
    });

    $('#update_task').click(() => {
        let task_id = $('#update_id').html();
        let inputs = $('#update_task_Modal').find('div > div > .modal-body > div');
        $.ajaxPlanH({
            url: '/task/update',
            data: {
                id: task_id,
                subject: inputs.find('#update_subject').val(),
                title: inputs.find('#update_title').val(),
                content: inputs.find('#update_content').val(),
                type: inputs.find('#update_type').val(),
                format: inputs.find('#update_format').val(),
                deadline: inputs.find('#update_date').val(),
                processor: inputs.find('#update_processor').val()
            },
            success: () => {
                location.reload(true);
            }
        });
    });

    $('#create_user').click(() => {
        $.ajaxPlanH({
            url: '/user/create',
            data: {
                code: $('#code').val(),
                name: $('#name').val()
            },
            success: () => {
                $('#cancel_create_user').trigger('click');
            }
        })
    });

    $('#batch_user').click(() => {
        let form_data = $('#batch_user_form');
        $.ajaxPlanH({
            url: '/user/batch',
            data: new FormData(form_data[0]),
            cache: false,
            success: () => {
                alert('成功');
            },
            error: () => {
                alert('失败');
            }
        });
    });

    $('#shutdown').click(() => {
        $.ajaxPlanH({
            const_url: '/shutdown',
            error: () => {
            }
        });
        $('.navbar-brand').append('（服务已停止）');
        $('#clear_shutdown').trigger('click');
    });
});
