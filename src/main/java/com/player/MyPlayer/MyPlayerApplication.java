package com.player.MyPlayer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyPlayerApplication implements CommandLineRunner {

	@Autowired
	PlayerService playerService;

	public static void main(String[] args) {
		SpringApplication.run(MyPlayerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		playerService.initDB();
		playerService.load_videos_in_db("tmkoc");
	}
}
