package com.demo.forest;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.DataParam;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.http.ForestResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * @author mifei
 * @create 2020-08-18 10:18
 **/
public interface AmapClient {

    @Request(
            url = "http://ditu.amap.com/service/regeo",
            dataType = "json"
    )
    Map getLocation(@DataParam("longitude") String longitude, @DataParam("latitude") String latitude);

    @Request(
            url = "${basetUrl}/admin/API/accounts/authorize",
            type = "post",
            dataType = "json",
            contentType = "application/json"
    )
    ForestResponse<String> login(@DataObject Map input);
}