$(document).ready(function () {

    window.jQuery || document.getElementById("body").appendChild(document.createTextNode('<script src="js/vendor/jquery.min.js"><\/script>'));
    $('body').append('<script type="text/javascript" src="js/vendor/jquery.cookie.js"></script>');

    var global = {};
    global.path_prefix_for_role = "";

    $.ajaxPlanH = function (args) {

        if (args.url === undefined)
            args.url = '/';
        if (args.const_url === undefined)
            args.url = global.path_prefix_for_role + args.url;
        else
            args.url = args.const_url;
        if (args.type === undefined)
            args.type = 'POST';
        if (args.async === undefined)
            args.async = true;
        if (args.data === undefined)
            args.data = {
                'data': 'undefined'
            };
        if (args.dataType === undefined)
            args.dataType = "json";
        if (args.error === undefined && args.complete === undefined)
            args.error = function (XMLHttpRequest, textStatus, errorThrown) {
                alert(
                    "XMLHttpRequest: " + XMLHttpRequest + "\n" +
                    "textStatus: " + textStatus + "\n" +
                    "errorThrown: " + errorThrown
                )
            };

        var token = $("meta[name='_csrf']").attr("content");
        if (args.data instanceof FormData) {
            args.data.append("_csrf", token);
            args.contentType = false;
            args.processData = false;
        }
        else
            args.data._csrf = token;

        $.ajax(args);
    };

    $.ajaxPlanH({
        const_url: '/role/get',
        success: function (data) {
            var body = $('body');
            global.role = data.role;

            if (global.role === 'ADMIN') {
                body.append('<script src="js/admin.js"><\/script>');
                body.append('<script src="js/admin-ajax.js"><\/script>');
                global.path_prefix_for_role = '/admin';
            } else if (global.role === 'USER') {
                body.append('<script src="js/user.js"><\/script>');
                body.append('<script src="js/user-ajax.js"><\/script>');
                global.path_prefix_for_role = '/user/' + $.cookie('userCode');
            } else
                alert('Role Error')
        }
    });

    $('#logout').click(function () {
        $.ajaxPlanH({
            const_url: '/logout',
            complete: function () {
                window.location.reload();
            }
        })
    });
});
