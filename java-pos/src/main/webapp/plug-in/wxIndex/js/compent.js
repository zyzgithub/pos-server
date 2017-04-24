/* 
* @Author: anchen
* @Date:   2015-08-21 16:45:03
* @Last Modified by:   anchen
* @Last Modified time: 2015-08-21 16:45:58
*/

$(document).ready(function(){
    var elm = $('.dc_sort .lb');
    var startPos = $(elm).offset().top;
    $.event.add(window, "scroll", function() {
        var p = $(window).scrollTop();
        $(elm).css('position',((p) > startPos) ? 'fixed' : 'absolute');
        $(elm).css('top',((p) > startPos) ? '0px' : '');
    });
});