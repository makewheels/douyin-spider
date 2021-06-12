package com.eg.douyinspider;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Down {

    public JSONObject getVideos(String sec_uid, String _signature,
                                long min_cursor, long max_cursor, int count) {
        String json = HttpUtil.get("https://www.iesdouyin.com/web/api/v2/aweme/post/?"
                + "sec_uid=" + sec_uid
                + "&_signature=" + _signature
                + "&min_cursor=" + min_cursor
                + "&max_cursor=" + max_cursor
                + "&count=" + count);
        return JSONObject.parseObject(json);
    }

    public static void main(String[] args) {
        Down down = new Down();

        String sec_uid = "MS4wLjABAAAApU6s3fSOZzifZTMkjGWjcLiAq75Z-dvsR51FM79WBOM";
        String _signature = "Pm-tNQAAXrDZGFUzJWb8bD5vrS";
                          1623469751000
        long min_cursor = 1622899045000L;
        long max_cursor = 1623504985407L;
                          1622971916000
        int count = 21;
        JSONObject videoListResponse = down.getVideos(sec_uid, _signature, min_cursor, max_cursor, count);
        System.out.println(videoListResponse);
        JSONArray aweme_list = videoListResponse.getJSONArray("aweme_list");
        System.out.println(aweme_list);
    }

}
