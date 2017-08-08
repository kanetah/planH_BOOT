$(document).ready(function () {

    $('#add').click(function () {
        $.ajaxPlanH({
            url: '/admin/task/post',
            data: {
                subject: "Test",
                content: "do something"
            },
            success: function (data) {
                alert(data.r)
            }
        })
    });

    // $('#add').click(function () {
    //     $.ajax({
    //         url: '/admin/task/post',
    //         type: 'POST',
    //         data: {
    //             _csrf: $("meta[name='_csrf']").attr("content"),
    //             subject: "Test",
    //             content: "do something"
    //         },
    //         dataType: "json",
    //         success: function (data) {
    //             alert(data.r)
    //         },
    //         error: function (XMLHttpRequest, textStatus, errorThrown) {
    //             alert(
    //                 "XMLHttpRequest: " + XMLHttpRequest +
    //                 "\ntextStatus: " + textStatus +
    //                 "\nerrorThrown: " + errorThrown
    //             )
    //         }
    //     })
    // })

});
