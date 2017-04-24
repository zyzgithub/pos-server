package courier.salary;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by mjorcen on 16/8/18.
 */
public class XAddDateUtil {

    public static void main(String[] ss) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date d = sdf.parse("2016-1-30");

        Date d2 = DateUtils.addMonths(d, 1);
        System.out.print(sdf.format(d2));
        String itemInfos = "{\n" +
                "    \"123\": \"123;xxsfsadfa\"\n" +
                "}";
        Map<String, String> map = JSONObject.parseObject(itemInfos, Map.class);
        System.out.println(map);
    }
}
