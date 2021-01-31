package com.player.MyPlayer;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoThread extends Thread {

    private String api_key;
    private String keyword;
    private YouTube youTube;
    private PlayerRepository playerRepository;

    private String pageToken;
    private List<Video> videos;

    public VideoThread(String api_key,String keyword,YouTube youTube,PlayerRepository playerRepository) {
        this.api_key = api_key;
        this.keyword = keyword;
        this.youTube = youTube;
        this.playerRepository = playerRepository;
    }

    @Override
    public void run() {

        System.out.println("In VideoThread run()........");
        System.out.println("Running a thread........");

        try {

            while (true) {

                YouTube.Search.List searchList = youTube.search().list("id,snippet");
                searchList.setKey(this.api_key);
                searchList.setQ(this.keyword);
                searchList.setMaxResults(10L);
                searchList.setPageToken(this.pageToken);

                System.out.println("SearchList...................:-"+searchList);

                SearchListResponse response = searchList.execute();
                List<SearchResult> items = response.getItems();

                this.videos = new ArrayList<>();

                System.out.println("In search responses items..........");

                for(SearchResult val : items) {

                    if(!val.getId().getKind().equals("youtube#video")) {
                        System.out.println("**********************");
                        System.out.println("i.e. not a video :- "+ val);
                        continue;
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

                    this.videos.add(video);
                }

                playerRepository.saveAllVideos(this.videos);
                this.pageToken = response.getNextPageToken();

                System.out.println("saved videos in thread.......::"+currentThread().getName());

                Thread.sleep(10000);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
