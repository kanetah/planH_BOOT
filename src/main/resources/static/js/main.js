$(document).ready(function () {

    window.jQuery || document.write('<script src="js/vendor/jquery-1.12.0.min.js"><\/script>');

    $.ajaxPlanH = function (args) {

        if (args.ajaxUrl === undefined)
            args.ajaxUrl = '/';
        if (args.ajaxData === undefined)
            args.ajaxData = null;
        if (args.onSuccess === undefined)
            args.onSuccess = function (data) {
                alert(data)
            };
        if (args.onError === undefined)
            args.onError = function (XMLHttpRequest, textStatus, errorThrown) {
                alert(
                    "XMLHttpRequest: " + XMLHttpRequest + "\n" +
                    "textStatus: " + textStatus + "\n" +
                    "errorThrown: " + errorThrown
                )
            };

        $.ajax({
            url: args.ajaxUrl,
            type: 'POST',
            data: args.ajaxData,
            dataType: "json",
            success: args.onSuccess,
            error: args.onError
        })
    }


});
