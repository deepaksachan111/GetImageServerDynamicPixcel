package com.example.qserver.getimageserverdynamicpixcel.Model;

/**
 * Created by QServer on 7/8/2016.
 */
public class Data {
    private String id;
    private String video_id;
    private String video_duration;
    private String video_title;
    private String video_desc;

    public Data(String id, String video_id, String video_duration, String video_title, String video_desc) {
        this.id = id;
        this.video_id = video_id;
        this.video_duration = video_duration;
        this.video_title = video_title;
        this.video_desc = video_desc;
    }

    public String getId() {
        return id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_duration() {
        return video_duration;
    }

    public String getVideo_title() {
        return video_title;
    }

    public String getVideo_desc() {
        return video_desc;
    }
}
