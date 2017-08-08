$(document).ready(function () {

    window.jQuery || document.write('<script src="js/vendor/jquery-1.12.0.min.js"><\/script>');

    $.ajaxPlanH = function (args) {

        if (args.url === undefined)
            args.url = '/';
        if (args.data === undefined)
            args.data = {
                'default': 'default'
            };
        if (args.success === undefined)
            args.success = function (data) {
                alert(data)
            };
        if (args.error === undefined)
            args.error = function (XMLHttpRequest, textStatus, errorThrown) {
                alert(
                    "XMLHttpRequest: " + XMLHttpRequest + "\n" +
                    "textStatus: " + textStatus + "\n" +
                    "errorThrown: " + errorThrown
                )
            };

        args.data._csrf = $("meta[name='_csrf']").attr("content");

        $.ajax({
            url: args.url,
            type: 'POST',
            data: args.data,
            dataType: "json",
            success: args.success,
            error: args.error
        })
    };

    $.ajaxPlanH({
        url: '/role/get',
        success: function (data) {
            window.role = data.role;

            if (window.role === 'ADMIN')
                $('body').append('<script src="js/admin-ajax.js"><\/script>');
            else if (window.role === 'USER')
                $('body').append('<script src="js/user-ajax.js"><\/script>');
            else
                alert("Role Error")
        }
    });
});
