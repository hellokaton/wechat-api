package io.github.biezhi.wechat.api.constant;

public interface ContentTypes {

    String PHOTO_MIME_TYPE = "image/jpeg";
    String AUDIO_MIME_TYPE = "audio/mpeg";
    String DOC_MIME_TYPE   = "text/plain";
    String VIDEO_MIME_TYPE = "video/mp4";
    String VOICE_MIME_TYPE = "audio/ogg";

    String PHOTO_FILE_NAME = "file.jpg";
    String AUDIO_FILE_NAME = "file.mp3";
    String DOC_FILE_NAME   = "file.txt";
    String VIDEO_FILE_NAME = "file.mp4";
    String VOICE_FILE_NAME = "file.ogg";

    String GENERAL_MIME_TYPE = "application/x-www-form-urlencoded";
    String GENERAL_JSON_TYPE = "application/json; charset=UTF-8";
    String GENERAL_FILE_NAME = DOC_FILE_NAME;

}