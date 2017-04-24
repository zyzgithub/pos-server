/* 
* @Author: anchen
* @Date:   2015-08-05 14:27:30
* @Last Modified by:   anchen
* @Last Modified time: 2015-08-21 16:43:22
*/

$(document).ready(function(){

    /*--------------------点击展开消失--------------------*/
    $('.shadow_div').click(function() {
        /* Act on the event */
        $(this).hide();
        $('.menu').hide();
        $('.filter_tabs').children('li').removeClass('selected');
    });

     /*-----------------点击切换 点菜- 评价- 店铺------------------*/
      $('.menu-bar>li').click(function() {
        $(this).children('span').addClass('active');
        $(this).siblings().children('span').removeClass('active'); 
        $('.menu_lists>div').eq($(this).index()).siblings().css('display','none'); 
        $('.menu_lists>div').eq($(this).index()).css('display','block');          
         /*---------------评价和店铺显示的时候购物车不显示-----------*/
        if( $('.menu_lists>div:nth-child(2)').is(":visible") || $('.menu_lists>div:nth-child(3)').is(":visible")){
            $('.gwc_out').hide();
            $('.shopmenu-cart').hide();
         }else{
            $('.gwc_out').show();
            $('.shopmenu-cart').show();
         }

         if($('.menu_lists>div:nth-child(2)').is(":visible") ) {
            $('body,html').css('backgroundColor','#fff');
         }else{
            $('body,html').css('backgroundColor','#ececec');
         } 
     }); 

})



