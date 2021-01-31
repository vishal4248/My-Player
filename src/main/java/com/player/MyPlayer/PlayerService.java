package com.player.MyPlayer;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {


//    @Value("${app.youtube.api-key}")
//    private String API_KEY;

    private String API_KEY="AIzaSyB78drU8prtmLU7SflU9AWvFCQYCNX203c";

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    MongoOperations mongoOperations;

    private YouTube youTube;

    public PlayerService() {

        System.out.println("Loading.......... databases = "+API_KEY);

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        this.youTube = new YouTube.Builder(httpTransport, jsonFactory, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) {

            }
        }).setApplicationName("My Player").build();

    }


    public void load_videos_in_db(String keyword) {

        /*

        try {

            YouTube.Search.List searchList = youTube.search().list("id,snippet");
            searchList.setKey(API_KEY);
            searchList.setQ(keyword);
            searchList.setMaxResults(10L);

            System.out.println("SearchList:"+searchList);

            SearchListResponse response = searchList.execute();


            List<SearchResult> items = response.getItems();

            System.out.println("In search responses items..........");

            for(SearchResult val : items) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println(val);

                if(!val.getId().getKind().equals("youtube#video")) {
                    System.out.println("++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("i.e. not a video :- "+ val);
                }


                SearchResultSnippet searchResultSnippet = val.getSnippet();

                JSONObject jsonThumbnails = new JSONObject();
                jsonThumbnails.put("default",searchResultSnippet.getThumbnails().getDefault().getUrl());
                jsonThumbnails.put("high",searchResultSnippet.getThumbnails().getHigh().getUrl());
                jsonThumbnails.put("medium",searchResultSnippet.getThumbnails().getMedium().getUrl());

//                JSONObject jsonThumbnails = (JSONObject) JSONValue.parseWithException(searchResultSnippet.getThumbnails().toString());

                Video video = Video.builder()
                        .videoId(val.getId().getVideoId())
                        .channelId(searchResultSnippet.getChannelId())
                        .channelTitle(searchResultSnippet.getChannelTitle())
                        .title(searchResultSnippet.getTitle())
                        .description(searchResultSnippet.getDescription())
                        .thumbnailUrl(jsonThumbnails)
                        .publishedAt(searchResultSnippet.getPublishedAt().toString())
                        .tag(keyword)
                        .build();

                playerRepository.save(video);

           }
            System.out.println("At end of fetch videos");

        } catch (Exception e) {
            System.out.println("Caught the Exception");
            e.printStackTrace();
        }

         */

        VideoThread videoThread = new VideoThread(API_KEY,keyword,youTube,playerRepository);
        videoThread.start();

    }

    public void initDB() {

        System.out.println("Index getting created on videoId field..............");
        mongoOperations
                .indexOps(Video.class)
                .ensureIndex(new Index()
                        .on("videoId", Sort.Direction.ASC)
                        .unique()
                        .background()
                );

    }

}
