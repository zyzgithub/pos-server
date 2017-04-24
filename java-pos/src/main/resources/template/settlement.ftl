<CB>715有家超市（${merchantName}）</CB><BR>
------------------------------------------------<BR>
订单类型		订单数量		收入<BR>
<#list orders as order>
${order.orderType}		${order.quantity}		￥${order.income}<BR>
</#list>
-----------------------------------------------<BR>
合计		${totalQuantity}		￥${totalIncome}<BR>
清点现金				￥${cash}<BR>
-----------------------------------------------<BR>
营业员:${cashier}
统计时间:${beginTime}-${endTime}
