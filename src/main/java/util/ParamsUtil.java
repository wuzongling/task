package util;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 15:52
 * @Description:
 */
public class ParamsUtil {

    public static String toParamsString(List params){
        return JSONObject.toJSONString(params);
    }

    public static String toResultString(Object result){
        return JSONObject.toJSONString(result);
    }
}
