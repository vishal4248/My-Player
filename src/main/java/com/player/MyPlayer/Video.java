package com.player.MyPlayer;

import com.google.api.client.util.DateTime;
import lombok.*;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * id :- kind : youtube#video
 *       videoId:  **********
 *
 * snippet :-  title :
 *             channelId :
 *             channelTitle :
 *             description :
 *             publishedAt :
 *             thumbnails :- default ? high ? medium
 */

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Video {


    private String videoId;
    private String title;
    private String channelId;
    private String channelTitle;
    private String description;
    private JSONObject thumbnailUrl;
    private String publishedAt;
    private String tag;

}
