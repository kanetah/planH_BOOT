$(document).ready(function () {

    $('body').onload = document.login_form.password.focus();

    $('[name="login_form"]').submit(function () {
        var userCode = $('[name="password"]');
        if (userCode.val().indexOf($.cookie("userCodePrefix")) === 0)
            userCode.val(userCode.val().replace($.cookie("userCodePrefix"), ""));
        return true;
    });
});
