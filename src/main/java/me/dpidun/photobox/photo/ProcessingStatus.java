package me.dpidun.photobox.photo;

public enum ProcessingStatus {
    CREATED,
    PROCESSING_STEP_WATERMARK,
    PROCESSING_STEP_THUMBNAIL,
    PROCESSING_STEP_FINISHING,
    FINISHED,
    FAILED;
}