$(function () {
    if (navigator.userAgent.match(/mobile/i)) {
        $('#content').css('width', '100%');
        $('form').css('width', '90%');
        $("#switch-btn").click(function () {
            var flag = true;
            return function () {
                if (flag)
                    hideUserInput();
                else
                    hideAdminInput();
                flag = !flag;
            }
        }());
        $('#switch').slideDown("slow");
    }

    document.login_form.password.focus();
    $('[name="login_form"]').submit(function () {
        var userCode = $('[name="password"]');
        if (userCode.val().indexOf($.cookie("userCodePrefix")) === 0)
            userCode.val(userCode.val().replace($.cookie("userCodePrefix"), ""));
        return true;
    });

    var username_div = $("#user-name");
    var password_div = $('#pwd-div');
    var username = $('[name="username"]');
    var password = $('[name="password"]');
    const hideUserInput = function () {
        password.val("");
        password_div.find('div > span').html("口令");
        username_div.slideUp("slow", function () {
            username.val("admin");
            password.attr("type", "password");
            password.focus();
        });
    };
    const hideAdminInput = function () {
        password.val("");
        username.val("");
        password_div.find('div > span').html("学号");
        username_div.slideDown("slow", function () {
            password.attr("type", "text");
        });
    };
    const drag = function (obj) {
        obj.bind("mousedown", start);
        var gapX;
        var gapY;

        function start(event) {
            if (event.button === 0) {
                gapX = event.clientX - obj.offset().left;
                gapY = event.clientY - obj.offset().top;
                $(document).bind("mouseup", stop);
            }
        }

        function stop(event) {
            if ((event.clientY - gapY) > 10)
                hideAdminInput();
            else if ((event.clientY - gapY) < -10)
                hideUserInput();
            $(document).unbind("mouseup", stop);
        }
    };
    drag($("main"));
});
