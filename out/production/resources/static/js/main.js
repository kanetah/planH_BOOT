$(function () {
    const global = {};
    global.path_prefix_for_role = "";

    window.jQuery || document.getElementById("body").appendChild(document.createTextNode('<script src="js/vendor/jquery.min.js"><\/script>'));
    global.body = $('body');
    global.body.append('<script type="text/javascript" src="js/vendor/jquery.cookie.js"></script>');

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
            args.error = function (XMLHttpRequest) {
                if (navigator.userAgent.match(/mobile/i))
                    location.reload();
                else
                    alert(
                        "未预见的ajax请求错误，请联系管理员或重载网页\n" +
                        "XMLHttpRequestStatus: " + XMLHttpRequest.status + "\n" +
                        "XMLHttpRequestReadyStatus: " + XMLHttpRequest.readyState
                    )
            };

        const token = $("meta[name='_csrf']").attr("content");
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
        success: function (roles) {
            var body = global.body;
            global.role = roles[0];

            if (global.role === 'ADMIN') {
                global.path_prefix_for_role = '/admin';
                body.append('<script src="js/admin.js"><\/script>');
                body.append('<script src="js/admin-ajax.js"><\/script>');
            } else if (global.role === 'USER') {
                global.path_prefix_for_role = '/user/' + $.cookie('userCode');
                body.append('<script src="js/user.js"><\/script>');
                body.append('<script src="js/user-ajax.js"><\/script>');
                body.append('<script src="js/vendor/jquery.rotate.min.js"><\/script>');
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
