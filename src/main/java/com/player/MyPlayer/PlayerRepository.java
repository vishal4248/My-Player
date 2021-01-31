package com.player.MyPlayer;

import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepository {

    @Autowired
    MongoOperations mongoOperations;

    public void saveVideo(Video video) {

        System.out.println("Saving........ video");

        try {
            mongoOperations.save(video);
        } catch (DuplicateKeyException e) {
            System.out.println("Duplicate key occurred.....");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveAllVideos(List<Video> videoList) {

        System.out.println("Saving.........list of video");

        try {
            mongoOperations.insertAll(videoList);
        } catch (DuplicateKeyException e) {
            System.out.println("Duplicate key occurred.....");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Video> getVideos(String keyword,int pageNo,int pageSize) {

        System.out.println("fetching....... videos");

        Criteria criteria = Criteria.where("tag").is(keyword);
        Query query = new Query();
        query.addCriteria(criteria).skip((pageNo-1)*pageSize).limit(pageSize);

        return mongoOperations.find(query,Video.class);
    }

}
