$(function () {
    function onCode() {
        var num = 60;
        var timer01 = null;
        var phone = $('.phone').val();
        if (phone == "") {
            return false;
        }
        timer01 = setInterval(function () {
            num--;
            if (num < 0) {
                $('.codeLabel').html("点击验证码");
                $('.codeLabel').removeAttr("disabled", "disabled");
                return false;
            }
            $('.codeLabel').attr("disabled", "disabled");
            $(".codeLabel").html("验证码" + num + "s");
        }, 1000);
        $.ajax({
            type: 'post',
            url: "/box/service/posBoxAccount/sendSms",
            data: {
                phoneNumber: phone
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == 0) {
                    //clearInterval(timer01);
                    $('.codeLabel').html("点击验证码");
                    $('.codeLabel').removeAttr("disabled", "disabled");
                } else {
                    alert(data.msg);
                }
            },
            error: function (data) {
                alert(data.msg);
            }
        });
    }

    function register() {
        var smsCode = $(".codeInput").val();
        var openId = [[${openId}]];
        var phone = $('.phone').val();
        var passportId = [[${passportId}]];
        $.ajax({
            type: 'post',
            url: "/box/service/posBoxAccount/register",
            data: {
                phoneNumber: phone
                , openId: openId
                , smsCode: smsCode
                , passportId: passportId
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == 0) {
                    alert(data.msg)
                } else {
                    alert(data.msg);
                }
            },
            error: function (data) {
                alert(data.msg);
            }
        });
    }
})

