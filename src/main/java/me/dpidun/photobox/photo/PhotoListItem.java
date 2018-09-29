package me.dpidun.photobox.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class PhotoListItem {

    public String uri;

    @JsonProperty("created_at")
    public Date createdAt;

}
