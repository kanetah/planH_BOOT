$(() => {
    document.login_form.password.focus();
    $('[name="login_form"]').submit(function () {
        let userCode = $('[name="password"]');
        if (userCode.val().indexOf($.cookie("userCodePrefix")) === 0)
            userCode.val(userCode.val().replace($.cookie("userCodePrefix"), ""));
        return true;
    });

    let username_div = $("#user-name");
    let password_div = $('#pwd-div');
    let username = $('[name="username"]');
    let password = $('[name="password"]');
    const hideUserInput = () => {
        password.val("");
        password_div.find('div > span').html("口令");
        username_div.slideUp("slow", () => {
            username.val("admin");
            password.attr("type", "password");
            password.focus();
        });
    };
    const hideAdminInput = () => {
        password.val("");
        username.val("");
        password_div.find('div > span').html("学号");
        username_div.slideDown("slow", function () {
            password.attr("type", "text");
        });
    };
    const drag = (obj) => {
        obj.bind("mousedown", start);
        let gapX;
        let gapY;

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

    let switch_role = () => {
        let flag = true;
        return () => {
            if (flag)
                hideUserInput();
            else
                hideAdminInput();
            flag = !flag;
        }
    };

    if ($.cookie("checkMobile") === "true") {
        $('#content').css('width', '100%');
        $('form').css('width', '90%');
        $("#switch-btn").click(switch_role());
        $('#switch').slideDown("slow");
    }
});
