$(document).ready(function () {

    window.jQuery || document.getElementById("body").appendChild(document.createTextNode('<script src="js/vendor/jquery.min.js"><\/script>'));
    $('body').append('<script type="text/javascript" src="js/vendor/jquery.cookie.js"></script>');

    var global = {};
    global.path_prefix_for_role = "";

    $.ajaxPlanH = function (args) {

        if (args.url === undefined)
            args.url = '/';
        if (args.type === undefined)
            args.type = 'POST';
        if (args.async === undefined)
            args.async = true;
        if (args.data === undefined)
            args.data = {
                'default': 'default'
            };
        if (args.dataType === undefined)
            args.dataType = "json";
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

        args.url = global.path_prefix_for_role + args.url;

        var token = $("meta[name='_csrf']").attr("content");
        if (args.data instanceof FormData)
            args.data.append("_csrf", token);
        else
            args.data._csrf = token;

        $.ajax(args);
    };

    $.ajaxPlanH({
        url: '/role/get',
        success: function (data) {
            window.role = data.role;

            if (window.role === 'ADMIN') {
                $('body').append('<script src="js/admin-ajax.js"><\/script>');
                global.path_prefix_for_role = '/admin';
            }
            else if (window.role === 'USER') {
                $('body').append('<script src="js/user-ajax.js"><\/script>');
                global.path_prefix_for_role = '/user/' + $.cookie('userCode');
            }
            else
                alert('Role Error')
        }
    });
});
