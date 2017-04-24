package com.wm.service.impl.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.base.enums.AppTypeConstants;
import com.jpush.JPush;
import com.jpush.SoundFile;

import jodd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.PrintFEUtils;
import org.jeecgframework.core.util.PrintKLLUtils;
import org.jeecgframework.core.util.TestPostPrintTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.base.enums.PayEnum;
import com.base.schedule.ScheduledUtil;
import com.courier_mana.common.Constants;
import com.wm.controller.open_api.ValidUtil;
import com.wm.entity.dineorder.DineOrderEntity;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.systemconfig.SystemconfigEntity;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;
import com.wm.service.pos.PosOrderModifyMoneyDetailService;
import com.wm.service.user.WUserServiceI;

@Service("printService")
public class PrintServiceImpl extends CommonServiceImpl implements PrintServiceI {

    private static final Logger logger = LoggerFactory.getLogger(PrintServiceImpl.class);

    // 打印联数
    private static final int printTimes = 3;

    private static final String getOrderMenu = "select a.menu_id as menuId, b.name,a.quantity,a.price,(a.price*a.quantity) total_price,promotion_money as promotionMoney,"
            + " sales_promotion as salesPromotion from order_menu a left join menu b on a.menu_id=b.id where a.order_id=?";

    @Autowired
    private WUserServiceI wUserService;
    @Autowired
    private OrderServiceI orderService;
    @Autowired
    private MerchantServiceI merchantService;
    @Autowired
    private PosOrderModifyMoneyDetailService posOrderModifyMoneyDetailService;

    public static NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        // 设置double的精度
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
    }


    public void print(OrderEntity order, boolean realPrint) {
        logger.info("print orderId:{}", order.getId());
        PrintTask task = new PrintTask(order, realPrint);
        ScheduledUtil.runNodelayTask(task, ScheduledUtil.PRINT_POOL);
    }

    /**
     * 远程下单打印
     *
     * @param order
     * @param b
     */
    public boolean orderPrint(Integer orderId, boolean b) {
        OrderEntity order = orderService.getEntity(OrderEntity.class, orderId);
        if (order == null) {
            return false;
        }
        // 获取商家打印机编号
        MerchantEntity merchant = order.getMerchant();
        if (merchant == null) {
            return false;
        }
        String printCode = merchant.getPrintCode();

        if (null == printCode || "".equals(printCode)) {
            return false;
        }
        if ("sunmi".equalsIgnoreCase(printCode.trim()) && !b) {
            JPush.pushSunmiOrder(orderId, order.getMerchant().getId());
            return true;
        }
        logger.info("printCode is " + printCode + " indexOf is " + printCode.indexOf("|"));
        if (printCode.indexOf("|") != -1) {

            // 飞蛾打印机
            // 分隔行
            String separatorLine = "--------------------------------------------<BR>";

            String content = "超市订单";

            String userType = wUserService.getCustType(order.getWuser().getId());

            content += "　　　　　　" + userType + "　　　　　　排号："
                    + Integer.parseInt(order.getOrderNum().substring(8,
                    order.getOrderNum().length())) + "<BR>";
            content += "订单号：" + order.getPayId() + "<BR>" + "订单时间：";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date((long) order.getCreateTime() * 1000));
            content += time + "<BR>" + separatorLine;

            List<Map<String, Object>> list = this.findForJdbc(
                    "select b.name,a.quantity,a.price,(a.price*a.quantity) total_price from order_menu a left join menu b on a.menu_id=b.id where a.order_id=?",
                    order.getId());
            Integer sumQuantity = 0;

            // 设置空格多少
            String blank1_1 = ""; // 商品明细与数量之间的空格
            for (int j = 0; j < 12; j++) {
                blank1_1 += "　";
            }
            String blank1_2 = ""; // 换行后数量前的空格
            for (int j = 0; j < 16; j++) {
                blank1_2 += "　";
            }
            String blank2 = ""; // 数量与价格之间的空格
            for (int j = 0; j < 2; j++) {
                blank2 += "　";
            }

            if (list != null) {
                content += "商品明细" + blank1_1 + "数量" + blank2 + "价格<BR>";
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    String name = map.get("name").toString();
                    Integer quantity = Integer.parseInt(map.get("quantity").toString());
                    sumQuantity += quantity;
                    String totalPrice = map.get("total_price").toString();
                    String nameStr = name.replaceAll("[^\\x00-\\xff]", "**");
                    int length = nameStr.length();

                    if (length <= 32) {
                        int blank = 32 - length;
                        for (int j = 0; j < blank / 2; j++) {
                            name += "　";
                        }

                        content += name + " ";
                        content += quantity.toString() + blank2 + "　" + totalPrice + "<BR>";
                    } else {
                        content += name + "<BR>";
                        content += " " + blank1_2 + quantity.toString() + blank2 + "　" + totalPrice + "<BR>";
                    }
                }
            }

            content += "总计：　　　　　　　　　　 　　　" + sumQuantity.toString() + blank2 + "　"
                    + order.getOrigin() + "<BR>";
            content += separatorLine;
            content += "实际金额：　" + blank1_1 + blank2 + (order.getOrigin() - order.getScoreMoney() - order.getCard()) + "<BR>";
            content += separatorLine;
            content += "店铺：" + merchant.getTitle() + "　　"
                    + merchant.getMobile() + "<BR>" + "地址："
                    + merchant.getAddress() + "<BR>";
            content += separatorLine;
            content += "1号生活  客服热线：4008-945-917　　　　<BR>";
            content += "<BR><BR><BR>";
            PrintFEUtils.print(printCode, 1 + "", content);
            return true;
        } else {

            // 客来乐打印机
            // 分隔行
            String separatorLine = "--------------------------------------------\\r\\n";

            String content = "超市订单\\t";

            String userType = wUserService.getCustType(order.getWuser().getId());

            content += userType + "\\t\\t排号："
                    + Integer.parseInt(order.getOrderNum().substring(8,
                    order.getOrderNum().length())) + "\\r\\n";
            content += "订单号：" + order.getPayId() + "\\r\\n" + "订单时间：";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date((long) order.getCreateTime() * 1000));
            content += time + "\\r\\n" + separatorLine;

            List<Map<String, Object>> list = this.findForJdbc(
                    "select b.name,a.quantity,a.price,(a.price*a.quantity) total_price from order_menu a left join menu b on a.menu_id=b.id where a.order_id=?",
                    order.getId());
            Integer sumQuantity = 0;
            if (list != null) {
                content += "商品明细\\t\\t\\t数量\\t价格\\r\\n";
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    String name = map.get("name").toString();
                    Integer quantity = Integer.parseInt(map.get("quantity").toString());
                    sumQuantity += quantity;
                    String totalPrice = map.get("total_price").toString();
                    int count_abc = 0;
                    int count_num = 0;
                    int count_oth = 0;
                    int count_blank = 0;
                    int count_symbol = 0;
                    String regEx = "[\\u4e00-\\u9fa5]";
                    String tem = name.replaceAll(regEx, "aa");
                    count_oth = tem.length() - name.length();
                    char[] chars = name.toCharArray();
                    for (int k = 0; k < chars.length; k++) {
                        if ((chars[k] >= 65 && chars[k] <= 90) || (chars[k] >= 97 && chars[k] <= 122)) {
                            count_abc++;
                        } else if (chars[k] >= 48 && chars[k] <= 57) {
                            count_num++;
                        } else if (chars[k] == ' ') {
                            count_blank++;
                        }
                    }
                    count_symbol = chars.length - count_abc - count_num - count_blank - count_oth;
                    int nameLen = (count_abc + count_num + count_symbol + count_blank) / 2 + count_oth;
                    if (nameLen <= 16) {
                        int menuNameLen = (16 - nameLen) / 4;
                        int menuNameLen2 = (16 - nameLen) % 4 * 2;
                        for (int j = 0; j < menuNameLen; j++) {
                            name += "\\t";
                        }
                        for (int j = 0; j < menuNameLen2; j++) {
                            StringBuilder builder = new StringBuilder();
                            builder.append("　");
                            name += builder.toString();
                        }
                        if (count_oth < 4) {
                            int menuNameLen3 = (4 - count_oth) * 2;
                            for (int j = 0; j < menuNameLen3; j++) {
                                StringBuilder builder = new StringBuilder();
                                builder.append("　");
                                name += builder.toString();
                            }
                        }
                        content += name;
                        content += quantity.toString() + "\\t" + totalPrice + "\\r\\n";
                    } else {
                        content += name + "\\r\\n";
                        content += "\\t\\t\\t\\t" + quantity.toString() + "\\t" + totalPrice + "\\r\\n";
                    }
                }
            }

            content += "总计：\\t\\t\\t\\t" + sumQuantity.toString() + "\\t" + order.getOrigin() + "\\r\\n";
            content += separatorLine;
            content += "实际金额：\\t\\t\\t\\t" + (order.getOrigin() - order.getScoreMoney() - order.getCard()) + "\\r\\n";
            content += separatorLine;
            content += "店铺：" + merchant.getTitle() + "\\t"
                    + merchant.getMobile() + "\\r\\n" + "地址："
                    + merchant.getAddress() + "\\r\\n";
            content += separatorLine;
            content += "1号外卖  客服热线：4008-945-917\\t\\t\\r\\n";
            content += "\\r\\n\\r\\n\\r\\n";
            SystemconfigEntity systemconfigAppId = this.findUniqueByProperty(
                    SystemconfigEntity.class, "code", "appid");
            SystemconfigEntity systemconfigAppSecret = this.findUniqueByProperty(
                    SystemconfigEntity.class, "code", "appsecret");

            Boolean flag = PrintKLLUtils.orderPrint(printCode, content, systemconfigAppId.getValue(), systemconfigAppSecret.getValue(), order.getPayId(), order.getOrigin().toString());
            return flag;
        }
    }

    public boolean printDineOrder(DineOrderEntity dineOrder) {
        if (dineOrder == null) {
            logger.info("订单dine_order不存在，无法打印！");
            return false;
        }
        Boolean flag = false;
        //获取商家打印机编号
        MerchantEntity merchant = dineOrder.getMerchant();
        String printCode = merchant.getPrintCode();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String content = "";
        //飞蛾打印
        if (printCode.indexOf("|") != -1) {

            // 设置空格多少
            String blank1_1 = ""; // 商品明细与数量之间的空格
            for (int j = 0; j < 8; j++) {
                blank1_1 += "　";
            }
            String blank1_2 = ""; // 堂食与排号之间的空格,换行后数量前的空格
            for (int j = 0; j < 12; j++) {
                blank1_2 += "　";
            }
            String blank1_3 = "";//数量与价格之间的空格
            for (int j = 0; j < 6; j++) {
                blank1_3 += "　";
            }

            content += "堂食订单<BR>";
            content += "堂食" + blank1_2 + "排号：" + Integer.parseInt(dineOrder.getOrderNum().substring(8)) + "<BR>";
            content += "订单号：" + dineOrder.getPayId() + "<BR>" + "订单时间：" + sdf.format(new Date((long) dineOrder.getCreateTime() * 1000)) + "<BR>" +
                    "--------------------------------------------<BR>";
            if (null == printCode || "".equals(printCode)) {
                return false;
            }

            List<Map<String, Object>> list = this.findForJdbc("select b.name,a.quantity,a.price,(a.price*a.quantity) total_price from dine_order_menu a "
                    + " left join menu b on a.menu_id=b.id where a.order_id=?", dineOrder.getId());

            Double sumPrice = 0.00;
            if (list != null) {
                content += "商品明细" + blank1_1 + "数量" + blank1_3 + "价格<BR>";
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    String name = map.get("name").toString();
                    String quantity = map.get("quantity").toString();
                    String totalPrice = map.get("total_price").toString();
                    sumPrice += Double.valueOf(totalPrice.toString());

                    String nameStr = name.replaceAll("[^\\x00-\\xff]", "**");
                    nameStr = nameStr.replaceAll("[\\w\\s]", "*");
                    int length = nameStr.length();

                    if (length % 2 != 0) {
                        length -= 1;
                    }

                    if (length <= 24) {
                        int blank = 24 - length;
                        for (int j = 0; j < blank / 2; j++) {
                            name += "　";
                        }
                        content += name + " ";
                        content += quantity.toString() + blank1_3 + " " + totalPrice + "<BR>";
                    } else {
                        content += name + "<BR>";
                        content += "　" + blank1_2 + quantity.toString() + blank1_3 + " " + totalPrice + "<BR>";
                    }
                }
            }

            content += "总金额：" + blank1_1 + "　　" + blank1_3 + sumPrice + "<BR>";
            content += "--------------------------------------------<BR>";
            content += "店铺：" + dineOrder.getMerchant().getTitle() + blank1_1
                    + dineOrder.getMerchant().getMobile() + "<BR>"
                    + "地址：" + dineOrder.getMerchant().getAddress() + "<BR>";
            content += "--------------------------------------------<BR>";
            content += "<BR><BR>";
            //打印小票
            String printResult = PrintFEUtils.print(printCode, "1", content);
            if (printResult != null) {
                flag = true;
            }
        } else {
            //客莱乐打印
            content += "堂食订单\\r\\n";
            content += "堂食\\t\\t\\t排号：" + Integer.parseInt(dineOrder.getOrderNum().substring(8)) + "\\r\\n";
            content += "订单号：" + dineOrder.getPayId() + "\\r\\n" + "订单时间：" + sdf.format(new Date((long) dineOrder.getCreateTime() * 1000)) + "\\r\\n" +
                    "--------------------------------------------\\r\\n";
            if (null == printCode || "".equals(printCode)) {
                return false;
            }

            List<Map<String, Object>> list = this.findForJdbc("select b.name,a.quantity,a.price,(a.price*a.quantity) total_price from dine_order_menu a "
                    + " left join menu b on a.menu_id=b.id where a.order_id=?", dineOrder.getId());
            if (list != null) {
                content += "商品明细\\t\\t数量\\t\\t价格\\r\\n";
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    String name = map.get("name").toString();
                    String quantity = map.get("quantity").toString();
                    String totalPrice = map.get("total_price").toString();

                    content += name + "\\t\\t" + quantity + "\\t\\t" + totalPrice + "\\r\\n";
                }
            }


            content += "总金额：" + dineOrder.getOrigin() + "\\r\\n";
            content += "--------------------------------------------\\r\\n";
            content += "店铺：" + dineOrder.getMerchant().getTitle() + "\\t\\t"
                    + dineOrder.getMerchant().getMobile() + "\\r\\n"
                    + "地址：" + dineOrder.getMerchant().getAddress() + "\\r\\n";
            content += "--------------------------------------------\\r\\n";
            content += "\\r\\n\\r\\n";
            flag = PrintKLLUtils.print(printCode, content);
        }
        return flag;
    }

    public boolean mealPrint(Integer orderId) {
        OrderEntity order = orderService.get(OrderEntity.class, orderId);
        if (order == null) {
            return false;
        }
        // 获取商家打印机编号
        MerchantEntity merchant = order.getMerchant();
        if (merchant == null) {
            logger.error("merchant not exist !!! orderId:{}", orderId);
            return false;
        }
        String printCode = merchant.getPrintCode();
        if (null == printCode || "".equals(printCode)) {
            logger.error("printCode not exist !!! orderId:{}", orderId);
            return false;
        }

        String content = "";
        // 分隔行
        String separatorLine = "--------------------------------------------\\r\\n";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Object> findOneForJdbc = this
                .findOneForJdbc(
                        "select p.full_num,p.order_time,p.order_type,p.remark from 0085_dinein_order p where  p.`status`=2 and p.order_id=?",
                        orderId);
        if (findOneForJdbc != null && findOneForJdbc.size() > 0) {
            content += "请留意取餐区" + findOneForJdbc.get("order_type").toString()
                    + "屏\\r\\n" + separatorLine + "\\r\\n";
            content += "出餐排号：" + findOneForJdbc.get("full_num").toString()
                    + "\\r\\n";
            content += "下单时间:";
            String time = sdf.format(findOneForJdbc.get("order_time"));
            content += time + "\\r\\n" + separatorLine;
        } else {
            return false;
        }

        List<Map<String, Object>> list = this
                .findForJdbc(
                        "select  m.quantity,m.price,m.total_price,n.name from  order_menu m LEFT JOIN menu n on m.menu_id = n.id where m.order_id=?",
                        order.getId());
        Integer sumQuantity = 0; // 总数量
        double totalprice = 0; // 总价格

        if (list != null) {
            content += "商品明细\\t\\t\\t数量\\t价格\\r\\n";
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                String name = map.get("name").toString();
                Integer quantity = Integer.parseInt(map.get("quantity")
                        .toString());
                sumQuantity += quantity;
                String price = map.get("price").toString();
                double total_price = (double) map.get("total_price");
                totalprice += total_price;

                String s = name.replaceAll("[^\\x00-\\xff]", "**");
                int length = s.length();
                if (length <= 32) {
                    int blank = 32 - length;
                    if (blank % 8 != 0) {
                        name += "\\t";
                    }
                    for (int j = 0; j < blank / 8; j++) {
                        name += "\\t";
                    }
                    content += name;
                    content += quantity.toString() + "\\t" + price + "\\r\\n";
                } else {
                    content += name + "\\r\\n";
                    content += "\\t\\t\\t\\t" + quantity.toString() + "\\t"
                            + price + "\\r\\n";
                }
            }
        }

        content += "总计：\\t\\t\\t\\t" + sumQuantity.toString() + "\\t"
                + totalprice + "\\r\\n";

        if (findOneForJdbc.get("remark") != null
                && !findOneForJdbc.get("remark").equals("")) {
            content += "备注:" + findOneForJdbc.get("remark") + "\\r\\n";
        }

        content += separatorLine + "\\r\\n";
        content += "客户电话:400—241—8888\\t\\t\\r\\n";
        content += "\\r\\n\\r\\n\\r\\n";

        if (content.equals("")) {
            return false;
        }
        Boolean flag = PrintKLLUtils.print(printCode, content);
        if (flag == true) {
            return true;
        }
        return false;
    }

    public boolean dineOrderPrint(OrderEntity order, String printType, JSONArray extendParams) {
        if (order == null) {
            return false;
        }

        try {
            // 获取商家打印机编号
            MerchantEntity merchant = order.getMerchant();
            if (merchant == null) {
                return false;
            }
            String printCode = merchant.getPrintCode();
            if (StringUtils.isEmpty(printCode)) {
                return false;
            }
            logger.info("printCode is " + printCode + " indexOf is " + printCode.indexOf("|"));
            if (printCode.indexOf("|") != -1) {
                // 飞蛾打印机
                String separatorLine = "--------------------------------------------<BR>";
                String content = "";

                if (printType.equals(OrderEntity.PrintType.AFTERADDMENU.toString())) {
                    content += "                 <L><B>加菜单</B></L>          <BR><BR>";
                }

                content += "<L><B>门店</B></L>";

                String userType = wUserService.getCustType(order.getWuser().getId());
                content += userType;

                content += "　　　　　　　　<B>排号："
                        + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                        : order.getOrderNum().substring(8))
                        + "</B><BR><BR>";
                content += "订单号：" + order.getPayId();

                // 堂食新增打印项：座位号
                Map<String, Object> seatmap = orderService.getDineSeatDetail(order.getId());
                if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("seat_name"))) {
                    content += "           " + "<L>座位号：" + seatmap.get("seat_name") + "</L><BR>";
                }

                String time = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = sdf.format(new Date((long) order.getCreateTime() * 1000));
                content += "订单时间：" + time + "<BR>" + separatorLine;

                // 设置空格多少
                String blank1_1 = ""; // 商品明细与数量之间的空格
                for (int j = 0; j < 12; j++) {
                    blank1_1 += "　";
                }
                String blank1_2 = ""; // 换行后数量前的空格
                for (int j = 0; j < 16; j++) {
                    blank1_2 += "　";
                }
                String blank2 = ""; // 数量与价格之间的空格
                for (int j = 0; j < 4; j++) {
                    blank2 += "　";
                }

                List<Map<String, Object>> list = this
                        .findForJdbc(
                                "select m.name,om.quantity,om.price,om.menuRemarks from "
                                        + " order_menu om left join menu m on om.menu_id=m.id where om.order_id=?",
                                order.getId());

                content += "商品明细" + blank1_1 + blank2 + "数量<BR>";
                if (printType.equals(OrderEntity.PrintType.AFTERADDMENU.toString())) {
                    list = null;
                    for (int i = 0; i < extendParams.size(); i++) {
                        JSONObject jo = extendParams.getJSONObject(i);
                        Object menuId = jo.get("id");
                        String name = "";
                        if (menuId != null) {
                            Integer mi = Integer.valueOf(menuId.toString());
                            MenuEntity menu = this.get(MenuEntity.class, mi);
                            if (menu != null) {
                                name = menu.getName();
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                        Object quantity = jo.get("count");
                        String menuRemarks = jo.get("remarks").toString();
                        String nameStr = name.replaceAll("[^\\x00-\\xff]", "**");
                        int length = nameStr.length();

                        content += "<L>";
                        if (length <= 32) {
                            int blank = 32 - length;
                            for (int j = 0; j < blank / 2; j++) {
                                name += "　";
                            }

                            content += name + " ";
                            content += blank2 + quantity.toString() + "　<BR>";
                        } else {
                            content += name + "<BR>";
                            content += " " + blank1_2 + blank2 + quantity.toString() + "<BR>";
                        }
                        content += "</L>";
                        content += menuRemarks + "<BR><BR>";
                    }
                }

                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = list.get(i);
                        String name = map.get("name").toString();
                        Integer quantity = Integer.parseInt(map.get("quantity").toString());
                        String menuRemarks = "";
                        if (map.get("menuRemarks") != null) {
                            menuRemarks = map.get("menuRemarks").toString();
                        }
                        String nameStr = name.replaceAll("[^\\x00-\\xff]", "**");
                        int length = nameStr.length();

                        content += "<L>";
                        if (length <= 32) {
                            int blank = 32 - length;
                            for (int j = 0; j < blank / 2; j++) {
                                name += "　";
                            }

                            content += name + " ";
                            content += blank2 + quantity.toString() + "　<BR>";
                        } else {
                            content += name + "<BR>";
                            content += " " + blank1_2 + blank2 + quantity.toString() + "<BR>";
                        }
                        content += "</L>";
                        content += menuRemarks + "<BR><BR>";
                    }
                }

                content += separatorLine;
                content += "店铺：" + order.getMerchant().getTitle() + "　　　　" + order.getMerchant().getMobile() + "<BR>"
                        + "地址：" + order.getMerchant().getAddress() + "<BR>";

                content += separatorLine;
                content += "1号生活  客服热线：4008-945-917　　　　<BR>";
                content += "<BR><BR><BR>";

                int printTime = 2;

                PrintFEUtils.print(printCode, printTime + "", content);
                return true;
            } else {

                // 客来乐打印机
                // 分隔行
                String separatorLine = "--------------------------------------------\\r\\n";

                String content = "";

                String userType = wUserService.getCustType(order.getWuser().getId());

                content += "<L><B>门店</B></L>" + userType + "\\t\\t\\t\\t<L><B>排号："
                        + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                        : order.getOrderNum().substring(8))
                        + "</B></L>\\r\\n";

                content += "订单号：" + order.getPayId();

                Map<String, Object> seatmap = orderService.getDineSeatDetail(order.getId());
                if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("seat_name"))) {
                    content += "\\t\\t\\t\\t<L>座位号：" + seatmap.get("seat_name") + "</L>\\r\\n";
                }

                String time = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = sdf.format(new Date((long) order.getCreateTime() * 1000));
                content += "订单时间：" + time + "\\r\\n" + separatorLine;

                List<Map<String, Object>> list = this
                        .findForJdbc(
                                "select b.name,a.quantity,a.price,a.total_price,a.menuRemarks from "
                                        + " order_menu a left join menu b on a.menu_id=b.id where a.order_id=?",
                                order.getId());
                if (list != null) {
                    content += "商品明细\\t\\t\\t数量\\t价格\\r\\n";
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = list.get(i);
                        String name = map.get("name").toString();
                        String menuRemarks = "";
                        if (map.get("menuRemarks") != null) {
                            menuRemarks = map.get("menuRemarks").toString();
                        }
                        Integer quantity = Integer.parseInt(map.get("quantity").toString());
                        /**
                         * 肖斌修改打印排版
                         */
                        String nameStr = name.replaceAll("[^\\x00-\\xff]", "**");
                        int length = nameStr.length();
                        if (length <= 32) {
                            int blank = 32 - length;
                            if (blank % 8 != 0) {
                                name += "\\t";
                            }
                            for (int j = 0; j < blank / 8; j++) {
                                name += "\\t";
                            }
                            content += "<L>" + name + "</L>";
                            content += "\\t" + quantity.toString() + "\\r\\n";
                        } else {
                            content += "<L>" + name + "</L>" + "\\r\\n";
                            content += "\\t\\t\\t\\t\\t" + quantity.toString() + "\\r\\n";
                        }
                        content += menuRemarks + "\\r\\n\\r\\n";
                    }
                }

                content += separatorLine;
                content += "店铺：" + order.getMerchant().getTitle() + "\\t\\t" + order.getMerchant().getMobile()
                        + "\\r\\n" + "地址：" + order.getMerchant().getAddress() + "\\r\\n";

                content += separatorLine;
                content += "1号生活  客服热线：4008-945-917\\t\\t\\r\\n";
                content += "\\r\\n\\r\\n\\r\\n";

                int printTime = 2; //堂食默认为1
                boolean flag = true;
                for (int i = 0; i < printTime; i++) {
                    flag = PrintKLLUtils.print(printCode, content);
                    if (!flag) {
                        return false;
                    }
                }
                return flag;
            }
        } catch (Exception e) {
            logger.error("打印订单【" + order.getId() + "】小票失败！", e);
        }
        return false;
    }


    public class PrintTask implements Runnable {

        private OrderEntity order;
        private boolean realPrint;


        public PrintTask(OrderEntity order, boolean realPrint) {
            this.order = order;
            this.realPrint = realPrint;
        }

        public void run() {
            print();
        }

        private void print() {
            if (order == null) {
                logger.warn("<打印订单接口>订单为null");
                return;
            }

            // 获取商家打印机编号
            MerchantEntity merchant = order.getMerchant();
            if (merchant == null) {
                logger.warn("<打印订单接口>未找到商家信息");
                return;
            }
            String printCode = merchant.getPrintCode();
            if ("sunmi".equalsIgnoreCase(printCode)) {
                JPush.pushSunmiOrder(order.getId(), order.getMerchant().getId());
                return;
            }
            if (StringUtils.isEmpty(printCode)) {
                logger.warn("<打印订单接口>未绑定打印机");
                return;
            }
            logger.info("print orderId:{} printCode:{}", order.getId(), printCode);
            try {
                if (printCode.indexOf("|") != -1) {
                    fePrint(merchant, printCode);
                } else if (printCode.indexOf("&") != -1) {
                    jljPrint(merchant, printCode);
                } else {
                    kllPrint(merchant, printCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("打印订单【{}】小票失败！", order.getId());
            }
        }

        /**
         * 客来乐打印机
         *
         * @param merchant
         * @param printCode 打印机编码
         */
        private void kllPrint(MerchantEntity merchant, String printCode) {

            String separatorLine = "--------------------------------------------\\r\\n";

            String content = "";
            if (OrderEntity.OrderType.MOBILE.equals(order.getOrderType())) {
                content += "电话订单\\r\\n";
            }

            String userType = wUserService.getCustType(order.getWuser().getId());
            String merchantSource = merchantService.getMerchantSource(merchant.getId());
            String strSaleType = "外卖";
            if ("2".equals(merchantSource)) {
                if (order.getSaleType() == 2) {
                    content += "自提";
                }
            } else {
                if (order.getSaleType() == 2) {
                    content += "门店";
                }
            }
            content += strSaleType + "\\t\\t" + userType + "\\t\\t排号："
                    + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                    : order.getOrderNum().substring(8))
                    + "\\r\\n";

            content += "订单号：" + order.getPayId() + "\\r\\n" + "订单时间：";

            Integer payTime = order.getPayTime();
            if (OrderEntity.OrderType.MOBILE.equals(order.getOrderType())) {
                payTime = order.getCreateTime();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date((long) payTime * 1000));
            content += time + "\\r\\n" + separatorLine;
            if (StringUtils.isNotEmpty(order.getTimeRemark())) {
                content += "送达时间：" + order.getTimeRemark() + "\\r\\n";
            }

            Map<String, Object> seatmap = orderService.getDineSeatDetail(order.getId());
            if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("seat_name"))) {
                content += "<L>座位号：" + seatmap.get("seat_name") + "</L>\\r\\n";
            }

            if (StringUtils.isNotEmpty(order.getRemark())) {
                content += "备注：" + order.getRemark() + "\\r\\n";
            }
            if (StringUtils.isNotEmpty(order.getRemark()) || StringUtils.isNotEmpty(order.getTimeRemark())) {
                content += separatorLine;
            }

            List<Map<String, Object>> list = orderService.findForJdbc(getOrderMenu, order.getId());
            Integer sumQuantity = 0;
            if (list != null) {
                content += "商品明细\\t\\t\\t数量\\t价格\\r\\n";
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    String name = map.get("name").toString();
                    Integer quantity = Integer.parseInt(map.get("quantity").toString());
                    sumQuantity += quantity;
                    String salesPromotion = (String) map.get("salesPromotion");
                    String totalPrice = "";
                    if (StringUtil.isNotEmpty(salesPromotion) && "Y".equals(salesPromotion)) {
                        name = name + "(特价)";
                        totalPrice = map.get("promotionMoney").toString();
                    } else {
                        totalPrice = map.get("total_price").toString();
                    }
                    String nameStr = name.replaceAll("[^\\x00-\\xff]", "**");
                    int length = nameStr.length();
                    if (length <= 32) {
                        int blank = 32 - length;
                        if (blank % 8 != 0) {
                            name += "\\t";
                        }
                        for (int j = 0; j < blank / 8; j++) {
                            name += "\\t";
                        }
                        content += name;
                        content += quantity.toString() + "\\t" + totalPrice + "\\r\\n";
                    } else {
                        content += name + "\\r\\n";
                        content += "\\t\\t\\t\\t" + quantity.toString() + "\\t" + totalPrice + "\\r\\n";
                    }
                }
            }

            if (order.getCostLunchBox() != 0) {
                content += "打包费：\\t\\t\\t\\t" + order.getCostLunchBox() + "\\r\\n";
            }
            if (order.getDeliveryFee() != 0) {
                content += "配送费：\\t\\t\\t\\t" + order.getDeliveryFee() + "\\r\\n";
            }

            // 堂食新增打印项：餐位费
            if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("pmoney"))) {
                content += "餐位费：\\t\\t\\t\\t" + seatmap.get("pnumber") + "\\t" + seatmap.get("pmoney") + "\\r\\n";
                sumQuantity += (Integer) seatmap.get("pnumber");
            }

            content += "总计：\\t\\t\\t\\t" + sumQuantity.toString() + "\\t"
                    + numberFormat.format((order.getOrigin() + order.getCostLunchBox() + order.getDeliveryFee()))
                    + "\\r\\n";

            content += separatorLine;

            content += "优惠券抵扣：\\t\\t\\t\\t" + order.getCard() + "\\r\\n";
            content += "积分抵扣：\\t\\t\\t\\t" + order.getScoreMoney() + "\\r\\n";

            if (order.getMemberDiscountMoney() > 0) {
                content += "会员卡抵扣：\\t\\t\\t\\t" + order.getMemberDiscountMoney() + "\\r\\n";
            }
            if (order.getMerchantMemberDiscountMoney() > 0) {
                content += "会员卡抵扣：\\t\\t\\t\\t" + order.getMerchantMemberDiscountMoney() + "\\r\\n";
            }
            if (order.getDineInDiscountMoney() > 0) {
                content += "堂食优惠抵扣：\\t\\t\\t\\t" + order.getDineInDiscountMoney() + "\\r\\n";
            }

            content += separatorLine;

            content += "实际金额：\\t\\t\\t\\t" + orderService.getOrderRealMoney(order) + "\\r\\n";

            content += separatorLine;

            if (1 == order.getSaleType()) {
                content += "用户：" + order.getRealname() + "\\t\\t" + order.getMobile() + "\\r\\n";
                content += "地址：" + order.getAddress() + "\\r\\n";
                if (StringUtils.isNotEmpty(order.getInvoice())) {
                    content += "发票：" + order.getInvoice() + "\\r\\n";
                }
                content += separatorLine;
            }
            content += "店铺：" + merchant.getTitle() + "\\t\\t" + merchant.getMobile()
                    + "\\r\\n" + "地址：" + merchant.getAddress() + "\\r\\n";
            content += separatorLine;
            content += "1号生活  客服热线：4008-945-917\\t\\t\\r\\n";
            content += "\\r\\n\\r\\n\\r\\n";

            int printTime = 1; //堂食默认为1
            if (1 == order.getSaleType()) {
                printTime = printTimes;  //外卖默认为3
            }
            MerchantInfoEntity merchantinfo = merchantService.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchant.getId());
            if (merchantinfo != null) {
                if (1 == order.getSaleType()) {
                    if (!ValidUtil.anyEmpty(merchantinfo.getOutPrintPiece())) {
                        printTime = merchantinfo.getOutPrintPiece(); //外卖
                    }
                } else {
                    if (!ValidUtil.anyEmpty(merchantinfo.getOutPrintPiece())) {
                        printTime = merchantinfo.getInPrintPiece(); //堂食
                    }
                }
            }
            if (OrderEntity.OrderType.SCAN_ORDER.equals(order.getOrderType())) { //扫码定单写死1张
                printTime = 1;
            }
            boolean flag = true;
            for (int i = 0; i < printTime; i++) {
                flag = PrintKLLUtils.print(printCode, content);
                if (!flag) {
                    return;
                }
            }
        }

        /**
         * 金领结打印机
         *
         * @param merchant
         * @param printCode 打印机编码
         */
        private void jljPrint(MerchantEntity merchant, String printCode) {
            //
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String content = "";
            String time = "";
            if ("mobile".equals(order.getOrderType())) {
                content += "电话订单*";
            }
            String merchantSource = merchantService.getMerchantSource(merchant.getId());
            if (Constants.MERCHANT_SOURCE_PRIVATE.equals(merchantSource)) {
                if (order.getSaleType() == 2) {
                    content += "自提                                        排号："
                            + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                            : Integer.parseInt(order.getOrderNum().substring(8)))
                            + "*";
                } else {
                    content += "外卖                                        排号："
                            + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                            : Integer.parseInt(order.getOrderNum().substring(8)))
                            + "*";
                }
            } else {
                if (order.getSaleType() == 2) {
                    content += "门店                                        排号："
                            + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                            : Integer.parseInt(order.getOrderNum().substring(8)))
                            + "*";
                } else {
                    content += "外卖                                        排号："
                            + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                            : Integer.parseInt(order.getOrderNum().substring(8)))
                            + "*";
                }
            }

            content += "订单号：" + order.getPayId() + "*" + "订单时间：";

            if ("mobile".equals(order.getOrderType())) {
                time = sdf.format(new Date((long) order.getCreateTime() * 1000));
            } else {
                time = sdf.format(new Date((long) order.getPayTime() * 1000));
            }

            content += time + "*" + "----------------------------*";

            if (StringUtils.isNotEmpty(order.getTimeRemark())) {
                content += "送达时间：" + order.getTimeRemark() + "*";
            }

            if (StringUtils.isNotEmpty(order.getRemark())) {
                content += "备注：" + order.getRemark() + "*";
            }

            Map<String, Object> seatmap = orderService.getDineSeatDetail(order.getId());
            if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("seat_name"))) {
                content += "座位号：" + seatmap.get("seat_name") + "*";
            }

            if (StringUtils.isNotEmpty(order.getRemark()) || StringUtils.isNotEmpty(order.getTimeRemark())) {
                content += "----------------------------*";
            }

            List<Map<String, Object>> list = orderService.findForJdbc(getOrderMenu, order.getId());
            Integer sumQuantity = 0;
            if (list != null) {
                content += "商品明细                数量    价格" + "*";
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) list.get(i);
                    String name = map.get("name").toString();
                    String quantity = map.get("quantity").toString();
                    String totalPrice = map.get("total_price").toString();
                    String str = "";
                    str = name;
                    if (name.length() > 11) {
                        str = str.substring(0, 11);
                    } else {
                        int k = 11 - name.length();
                        for (int j = 0; j < k; j++) {
                            str = str + "  ";
                        }
                    }
                    content += str + "     " + quantity + "     " + totalPrice + "*";

                    Integer quantity1 = Integer.parseInt(quantity);
                    sumQuantity += quantity1;
                }
            }

            // 堂食新增打印项：餐位费
            if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("pmoney"))) {
                content += "餐位费：                 " + seatmap.get("pmoney") + "*";
            }

            content += "总计：                  " + sumQuantity.toString() + "  " + order.getOrigin() + "*";

            content += "总金额：" + order.getOrigin() + "*";
            content += "----------------------------*";
            content += "代金券抵扣：" + order.getCard() + "*";
            content += "积分抵扣：" + order.getScoreMoney() + "*";
            content += "----------------------------*";
            content += "实际金额：" + (order.getOrigin() - order.getScoreMoney() - order.getCard()) + "*";
            content += "----------------------------*";
            if (1 == order.getSaleType()) {

                content += "用户：" + order.getRealname() + "        " + order.getMobile() + "*" + "地址："
                        + order.getAddress() + "*";
                content += "----------------------------*";
            }
            content += "店铺：" + merchant.getTitle() + "       " + merchant.getMobile() + "*"
                    + "地址：" + merchant.getAddress() + "*";
            content += "----------------------------*";
            content += "**";

            Map<String, String> params = new HashMap<String, String>();
            params.put("printId", printCode);
            JSONArray contentArray = new JSONArray();
            JSONObject line1 = new JSONObject();
            line1.put("content", content);
            contentArray.add(line1);

            params.put("printContentList", contentArray.toString());
            String sign = TestPostPrintTask.createSign(params.get("printId"), params.get("printContentList"));

            params.put("sign", sign);

            String result;
            try {
                result = TestPostPrintTask.post("http://www.jljcn.com:8180/edingapi/print", "utf-8", params);
                logger.info(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 飞蛾打印机
         *
         * @param merchant
         * @param printCode 打印机编码
         */
        private void fePrint(MerchantEntity merchant, String printCode) {
            String separatorLine = "--------------------------------------------<BR>";
            String content = "";
            if (OrderEntity.OrderType.MOBILE.equals(order.getOrderType())) {
                content += "电话订单<BR>";
            }

            String userType = wUserService.getCustType(order.getWuser().getId());
            String merchantSource = merchantService.getMerchantSource(merchant.getId());
            String strSaleType = "<B>外卖</B>";
            if (Constants.MERCHANT_SOURCE_PRIVATE.equals(merchantSource)) {
                if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
                    strSaleType = "<B>自提</B>";
                }
            } else {
                if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
                    strSaleType = "<B>门店</B>";
                }
            }
            content += strSaleType + "　　　　" + userType + "　　　<B>排号："
                    + ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                    : order.getOrderNum().substring(8))
                    + "</B><BR>";

            content += "<BR>订单号：" + order.getPayId() + "<BR>";
            String payType = PayEnum.getCn(order.getPayType());
            content += "支付方式：" + payType + "<BR>订单时间：";

            Integer payTime = order.getPayTime();
            if (OrderEntity.OrderType.MOBILE.equals(order.getOrderType())) {
                payTime = order.getCreateTime();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date((long) payTime * 1000));
            content += time + "<BR>" + separatorLine;
            if (StringUtils.isNotEmpty(order.getTimeRemark())) {
                content += "送达时间：" + order.getTimeRemark() + "<BR>";
            }

            // 堂食新增打印项：座位号
            Map<String, Object> seatmap = orderService.getDineSeatDetail(order.getId());
            if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("seat_name"))) {
                content += "<L>座位号：" + seatmap.get("seat_name") + "</L><BR>";
            }

            if (StringUtils.isNotEmpty(order.getRemark())) {
                content += "备注：" + order.getRemark() + "<BR>";
            }
            if (StringUtils.isNotEmpty(order.getRemark()) || StringUtils.isNotEmpty(order.getTimeRemark())) {
                content += separatorLine;
            }

            Integer sumQuantity = 0;
            //促销优惠总金额
            BigDecimal promotionMoney = BigDecimal.ZERO;
            
            // 设置空格多少
            String blank1_1 = ""; // 商品明细与数量之间的空格
            for (int j = 0; j < 12; j++) {
                blank1_1 += "　";
            }
            String blank1_2 = ""; // 换行后数量前的空格
            for (int j = 0; j < 16; j++) {
                blank1_2 += "　";
            }
            String blank2 = ""; // 数量与价格之间的空格
            for (int j = 0; j < 2; j++) {
                blank2 += "　";
            }

            List<Map<String, Object>> list = orderService.findForJdbc(getOrderMenu, order.getId());
            if (CollectionUtils.isNotEmpty(list)) {
                content += "商品明细" + blank1_1 + "数量" + blank2 + "价格<BR>";
                content += "<L>";
                for (Map<String, Object> map : list) {
                    int menuId = Integer.parseInt(map.get("menuId").toString());
                    String name = "";
                    if (menuId == 0) {
                        name = "无码商品";
                    } else {
                        name = map.get("name").toString();
                    }
                    String salesPromotion = (String) map.get("salesPromotion");
                    Integer quantity = Integer.parseInt(map.get("quantity").toString());
                    sumQuantity += quantity;
                    String totalPrice = "";
                    if (StringUtil.isNotEmpty(salesPromotion) && "Y".equals(salesPromotion)) {
                        name = name + "(特价)";
                        totalPrice = map.get("promotionMoney").toString();
                        promotionMoney = promotionMoney.add(new BigDecimal(map.get("total_price").toString())
                        					.subtract(new BigDecimal(map.get("promotionMoney").toString())));
                    } else {
                        totalPrice = map.get("total_price").toString();
                    }
                    String nameStr = name.replaceAll("[^\\x00-\\xff]", "**");
                    int length = nameStr.length();

                    if (length <= 32) {
                        int blank = 32 - length;
                        for (int j = 0; j < blank / 2; j++) {
                            name += "　";
                        }
                        content += name + " ";
                        content += quantity.toString() + blank2 + "　" + totalPrice + "<BR>";
                    } else {
                        content += name + "<BR>";
                        content += " " + blank1_2 + quantity.toString() + blank2 + "　" + totalPrice + "<BR>";
                    }
                }
                content += "</L>";
            }
            
            if(promotionMoney.compareTo(BigDecimal.ZERO) == 1){
            	content += "促销优惠：　　　　　　　　　　　　　　　" + promotionMoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "<BR>";
            }
            //pos订单如果存在整单折扣
            Map<String, Object> orderModifyMap = posOrderModifyMoneyDetailService.getOrderModifyDetail(order.getId());
            if (orderModifyMap != null) {
                content += orderModifyMap.get("modifyType") + "：　　　　　　　　　　 　　　" + " " + blank2 + "  " + orderModifyMap.get("money") + "<BR>";
            }

            if (order.getCostLunchBox() > 0) {
                content += "打包费：　　　　　　　　　　 　　" + " " + blank2 + "　" + order.getCostLunchBox() + "<BR>";
            }
            if (order.getDeliveryFee() > 0) {
                content += "配送费：　　　　　　　　　　 　　" + " " + blank2 + "　" + order.getDeliveryFee() + "<BR>";
            }

            // 堂食新增打印项：餐位费
            if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("pmoney"))) {
                content += "餐位费：　　　　　　　　　　 　　" + seatmap.get("pnumber") + blank2 + "　" + seatmap.get("pmoney") + "<BR>";
                sumQuantity += (Integer) seatmap.get("pnumber");
            }

            content += "总计：　　　　　　　　　　 　　　" + sumQuantity.toString() + blank2 + "　"
                    + numberFormat.format((order.getOrigin() + order.getCostLunchBox() + order.getDeliveryFee()))
                    + "<BR>";

            content += separatorLine;
            content += "优惠券抵扣：　　　　　　　　　　　　　　" + order.getCard() + "<BR>";
            content += "积分抵扣：　　　　　　　　　　　　　　　" + order.getScoreMoney() + "<BR>";

            if (order.getMemberDiscountMoney() > 0) {
                content += "会员卡抵扣：　　　　　　　　　　　　　　" + order.getMemberDiscountMoney() + "<BR>";
            }
            if (order.getMerchantMemberDiscountMoney() > 0) {
                content += "会员卡抵扣：　　　　　　　　　　　　　　" + order.getMerchantMemberDiscountMoney() + "<BR>";
            }
            if (order.getDineInDiscountMoney() > 0) {
                content += "堂食优惠抵扣：　　　　　　　　　　　　　" + order.getDineInDiscountMoney() + "<BR>";
            }

            content += separatorLine;
            content += "实际金额：　　　　　　　　　　　　　　　" + orderService.getOrderRealMoney(order) + "<BR>";

            content += separatorLine;
            if (OrderEntity.SaleType.TAKEOUT.equals(order.getSaleType())) {
                content += "<L>";
                content += "用户：" + order.getRealname() + "　　　　" + order.getMobile() + "<BR>";
                content += "地址：" + order.getAddress() + "<BR>";
                content += "</L>";
                if (StringUtils.isNoneEmpty(order.getInvoice())) {
                    content += "发票：" + order.getInvoice() + "<BR>";
                }
                content += separatorLine;
            }
            content += "店铺：" + order.getMerchant().getTitle() + "　　　　" + merchant.getMobile() + "<BR>"
                    + "地址：" + merchant.getAddress() + "<BR>";
            content += separatorLine;
            content += "1号生活  客服热线：4008-945-917　　　　<BR>";
            if (OrderEntity.OrderType.SUPERMARKET.equals(order.getOrderType()) || OrderEntity.OrderType.SUPERMARKET_CODEFREE.equals(order.getOrderType())) {
                content += "         招商合作：4000-915-715　　　　<BR>";
            }
            content += "<BR><BR><BR>";

            int printTime = 1; //堂食默认为1
            if (OrderEntity.SaleType.TAKEOUT.equals(order.getSaleType())) {
                printTime = printTimes;  //外卖默认为3
            }
            MerchantInfoEntity merchantinfo = merchantService.findUniqueByProperty(MerchantInfoEntity.class, "merchantId", merchant.getId());
            if (merchantinfo != null) {
                if (OrderEntity.SaleType.TAKEOUT.equals(order.getSaleType())) {
                    if (!ValidUtil.anyEmpty(merchantinfo.getOutPrintPiece())) {
                        printTime = merchantinfo.getOutPrintPiece();
                    }
                } else {
                    if (!ValidUtil.anyEmpty(merchantinfo.getOutPrintPiece())) {
                        printTime = merchantinfo.getInPrintPiece();
                    }
                }
            }
            if (OrderEntity.OrderType.SCAN_ORDER.equals(order.getOrderType())) {
                printTime = 1;
            }
            PrintFEUtils.print(printCode, printTime + "", content);
        }
    }
}