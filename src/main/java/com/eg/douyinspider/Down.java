package com.eg.douyinspider;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;

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

        String sec_uid = "MS4wLjABAAAAa0jfVFQtwOj1P7K51yUL4eOqCYglAnpToT_NY1k-qzI";
        String _signature = "FK7NLwAAdHDz2TUpbJVWmhSuzT";
        long min_cursor = 1522899045000L;
        long max_cursor = 1623504985407L;
        int count = 100;
        JSONObject videoListResponse = down.getVideos(sec_uid, _signature, min_cursor, max_cursor, count);
        JSONArray aweme_list = videoListResponse.getJSONArray("aweme_list");
        for (int i = 0; i < aweme_list.size(); i++) {
            JSONObject aweme = aweme_list.getJSONObject(i);
            String aweme_id = aweme.getString("aweme_id");
            String vid = aweme.getJSONObject("video").getString("vid");
            String desc = aweme.getString("desc");

            System.out.println(aweme_id + " " + vid + " " + desc);
            String url = "https://aweme.snssdk.com/aweme/v1/play/?video_id=" + vid;
            File folder = new File("D:\\2345Downloads\\douyin");
            if (!folder.exists())
                folder.mkdirs();
            File file = new File(folder, aweme_id + "-" + desc + ".mp4");
            HttpUtil.downloadFile(url, file);

        }
    }

}
