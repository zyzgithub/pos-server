package test;

import com.dianba.pos.common.util.DateUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/28 0028.
 */
public class Test {

    public static void main(String[] args) {

        BigDecimal bd=new BigDecimal(5);
        BigDecimal bb=new BigDecimal(2);
        System.out.println((bd.divide(bb,0).intValue()));
    }
}
