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
                    'deadline',
                    'fileFormat'
                ];
                $.each(ths, function (i) {
                    var  new_td = document.createElement("td");
                    new_td.innerHTML = elem[ths[i]];
                    new_tr.append(new_td);
                });
                task_table.append(new_tr);
            });
        }
    });

    // $('#task').click(function () {
    //     $.ajaxPlanH({
    //         url: '/task/create',
    //         data: {
    //             subject: $('#subject').val(),
    //             title: $('#title').val(),
    //             content: $('#content').val(),
    //             format: $('#format').val(),
    //             deadline: $('#date').val()
    //         },
    //         success: function () {
    //             $('#subject').val("");
    //             $('#title').val("");
    //             $('#content').val("");
    //             $('#format').val("");
    //             $('#date').val("");
    //         }
    //     })
    // });
    //
    // $('#user').click(function () {
    //     $.ajaxPlanH({
    //         url: '/user/create',
    //         data: {
    //             code: $('#code').val(),
    //             name: $('#name').val()
    //         },
    //         success: function () {
    //             $('#code').val("");
    //             $('#name').val("");
    //         }
    //     })
    // });
    //
    // $('#batch_user').click(function () {
    //     var form_data = $('#batch_user_form');
    //     $.ajaxPlanH({
    //         url: '/user/batch',
    //         data: new FormData(form_data[0]),
    //         cache: false,
    //         success: function () {
    //             alert('成功');
    //         },
    //         error: function () {
    //             alert('失败');
    //         }
    //     });
    // });
});
