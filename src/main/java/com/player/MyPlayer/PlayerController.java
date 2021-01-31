package com.player.MyPlayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerRepository playerRepository;

    @GetMapping("/home")
    public String getHome() {
        return "Welcome to My-Player";
    }

    @GetMapping("/getVideos")
    public List<Video> get_videos(@RequestParam("query") String query,
                                  @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        playerService.load_videos_in_db("tmkoc");

        return playerRepository.getVideos(query, pageNo, pageSize);
    }

}