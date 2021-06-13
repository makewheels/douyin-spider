package com.eg.douyinspider;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

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

    public JSONObject getUserInfo(String sec_uid) {
        String json = HttpUtil.get("https://www.iesdouyin.com/web/api/v2/user/info/?" +
                "sec_uid=" + sec_uid);
        return JSONObject.parseObject(json);
    }

    private void downloadVideos(JSONArray aweme_list) {
        for (int i = 0; i < aweme_list.size(); i++) {
            JSONObject aweme = aweme_list.getJSONObject(i);
            String aweme_id = aweme.getString("aweme_id");
            String vid = aweme.getJSONObject("video").getString("vid");
            String desc = aweme.getString("desc");
            desc = desc.replace("/", "");

            System.out.println(aweme_id + " " + desc);
            String url = "https://aweme.snssdk.com/aweme/v1/play/?video_id=" + vid;
            File folder = new File("D:\\2345Downloads\\douyin");
            if (!folder.exists())
                folder.mkdirs();
            File file = new File(folder,
                    aweme.getJSONObject("author").getString("nickname")
                            + "/" + aweme_id + " " + desc + ".mp4");
            HttpUtil.downloadFile(url, file);
        }
    }

    public static void main(String[] args) {
        Down down = new Down();
        String sec_uid = "MS4wLjABAAAAPNBZbXCGObTxkTKRccqSSm5NFNUPlL0duDuEglTYxJY";
        String _signature = "xS1tvwAApfIiWpW5D6wbU8Utba";

        JSONObject userInfo = down.getUserInfo(sec_uid);
        int aweme_count = userInfo.getJSONObject("user_info").getInteger("aweme_count");
        int downloadCount = 0;

        DateTime time = DateUtil.tomorrow();
        time.setField(DateField.HOUR_OF_DAY, 0);
        time.setField(DateField.MINUTE, 0);
        time.setField(DateField.SECOND, 0);
        time.setField(DateField.MILLISECOND, 0);

        //时间间隔为一天
        long oneDayInMillis = TimeUnit.DAYS.toMillis(1);
        long max_cursor = time.getTime();
        long min_cursor = max_cursor - oneDayInMillis;

        JSONArray aweme_list;
        do {
            JSONObject response = down.getVideos(
                    sec_uid, _signature, min_cursor, max_cursor, 200);
            aweme_list = response.getJSONArray("aweme_list");
            //时间区间往前移动一天
            min_cursor -= oneDayInMillis;
            max_cursor -= oneDayInMillis;

            //如果他这天没发东西就跳过
            if (aweme_list == null || aweme_list.isEmpty()) {
                continue;
            }

            //下载
            downloadCount += aweme_list.size();
            String percent = new DecimalFormat("#0.00%").format(
                    downloadCount * 1.0 / aweme_count);
            System.out.println(downloadCount + " / " + aweme_count + "  " + percent + " ");
            down.downloadVideos(aweme_list);

        } while (downloadCount < aweme_count);

    }


}
