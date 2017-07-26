$(function () {
})

function onCode(target) {
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
        url: "/box/service/account/sendSms",
        data: {
            phoneNumber: phone
        },
        dataType: 'json',
        success: function (data) {
            if (data.code != 0) {
                clearInterval(timer01);
                $('.codeLabel').html("点击验证码");
                $('.codeLabel').removeAttr("disabled", "disabled");
                alert(data.msg);
            }
        },
        error: function (data) {
            alert(data.msg);
        }
    });
}

function boxRegister(target) {
    var smsCode = $(".codeInput").val();
    var openId = $(".openId").val();
    var phone = $('.phone').val();
    var passportId = $(".passportId").val();
    $.ajax({
        type: 'post',
        url: "/box/service/account/register",
        data: {
            phoneNumber: phone
            , openId: openId
            , smsCode: smsCode
            , passportId: passportId
        },
        dataType: 'json',
        success: function (data) {
            if (data.code == 0) {
                window.location.href = "/box/service/account/position?passportId=" + passportId + "&openId=" + openId;
            } else {
                alert(data.msg);
            }
        },
        error: function (data) {
            alert(data.msg);
        }
    });
}
