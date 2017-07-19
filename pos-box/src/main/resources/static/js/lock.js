$(function () {
    /*请求开锁成功后，执行以下方法*/
//			$(".lock").hide();
//			$(".lockSuccess").show();
//			var timer01 = null;
//			setTimeout(function(){
//				$(".lockSuccess").hide();
//				$(".time").show();
//				onTime();
//			},3000);


})

function onLock(target) {
    var num = $(target).attr("data-num");
    if (num == 1) {
        $(".openBtn").hide();
        $(".noOpen").show();
        $(".title").html("开锁中...");
    }

}

function onTime(val) {
    var time = $(".count").html();
    timer01 = setInterval(function () {
        time--;
        if (time < 0) {
            $(".time").hide();
            $(".lock").show();
            return false;
        }
        $(".count").html(time);
    }, 1000);
}