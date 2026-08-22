package com.biy.social.curvydolphin.service;

import com.biy.social.curvydolphin.constants.VideoInteractionEventType;
import com.biy.social.curvydolphin.entity.UserEntity;
import com.biy.social.curvydolphin.entity.VideoEntity;
import com.biy.social.curvydolphin.entity.VideoInteractionEntity;
import com.biy.social.curvydolphin.model.User;
import com.biy.social.curvydolphin.model.Video;
import com.biy.social.curvydolphin.model.VideoInteraction;
import com.biy.social.curvydolphin.repository.UserRepository;
import com.biy.social.curvydolphin.repository.VideoInteractionsRepository;
import com.biy.social.curvydolphin.repository.VideoRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SeedClass {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    VideoInteractionsRepository videoInteractionsRepository;

    int interaction_count = 0;
    int flow_a_count = 0;
    int flow_b_count = 0;

    int[] flow_a_type_count = new int[10];
    List<VideoInteractionEntity> interactions = new ArrayList<>();

    public void seed(){

        List<UserEntity> users = userRepository.findAll();
        List<VideoEntity> videos = videoRepository.findAll();


        for (UserEntity user : users){
            // for each user need to create interactions for each video they didn't post
            for (VideoEntity video : videos){
                // if video isn't theirs create interaction data
                if (video.getUser().getUserId() != user.getUserId()){
                    log.info("user [{}] needs interaction type for video [{}]", user.getUserId(), video.getId());
                    interaction_count++;

                    // need to get 1 of 2 main interaction flows
                    getInteractions(user, video);
                }

            }

        }

        log.info("interaction summary");
        log.info("users {}", users.size());
        log.info("videos {}", videos.size());
        log.info("total interactions {}", interaction_count);
        log.info("flow a count: {}", flow_a_count);
        log.info("flow a sub count distribution {}", Arrays.toString(flow_a_type_count));
        log.info("flow b count: {}", flow_b_count);
        log.info("interactions {}", interactions.size());

        videoInteractionsRepository.saveAll(interactions);

    }

    private void getInteractions(UserEntity user , VideoEntity video){
        List<VideoInteractionEventType> types = new ArrayList<>();

        types.add(VideoInteractionEventType.IMPRESSION);
        types.add(VideoInteractionEventType.PLAY);
        types.add(VideoInteractionEventType.WATCH_25);
        types.add(VideoInteractionEventType.WATCH_50);
        types.add(VideoInteractionEventType.WATCH_75);
        types.add(VideoInteractionEventType.WATCH_COMPLETE);
        types.add(VideoInteractionEventType.LIKE);
        types.add(VideoInteractionEventType.COMMENT);
        types.add(VideoInteractionEventType.SHARE);

        // decide whether flow a or b
        int decider = (int) (Math.random() * 100);
        int end = 0;

        if (decider % 2 == 0){
            int sub_decider = (int) (Math.random() * 100) / 10;
            // do flow a
            // of flow a there are several sub flows
            // IMPRESSION -> PLAY -> WATCH 25 -> WATCH 50 -> WATCH 75 -> WATCH COMPLETE -> LIKE -> COMMENT -> SHARE
            // IMPRESSION -> PLAY -> WATCH 25 -> WATCH 50 -> WATCH 75 -> WATCH COMPLETE -> LIKE -> COMMENT
            // IMPRESSION -> PLAY -> WATCH 25 -> WATCH 50 -> WATCH 75 -> WATCH COMPLETE -> LIKE
            // IMPRESSION -> PLAY -> WATCH 25 -> WATCH 50 -> WATCH 75 -> WATCH COMPLETE
            // IMPRESSION -> PLAY -> WATCH 25 -> WATCH 50 -> WATCH 75
            // IMPRESSION -> PLAY -> WATCH 25 -> WATCH 50
            // IMPRESSION -> PLAY -> WATCH 25

            switch (sub_decider){
                case 0:
                    end = 8;
                    break;
                case 1:
                    end = 7;
                    break;
                case 2:
                    end = 6;
                    break;
                case 3:
                    end = 5;
                    break;
                case 4:
                    end = 4;
                    break;
                case 5:
                    end = 3;
                    break;
                case 6:
                    end = 2;
                    break;
                case 7:
                    end = 6;
                    break;
                case 8:
                    end = 5;
                    break;
                case 9:
                    end = 4;
                    break;
                default:
                    end = 3;
                    break;
            }

            flow_a_type_count[end] = flow_a_type_count[end] + 1;

            for (int start = 0; start <= end; start++){
                VideoInteractionEntity interaction = new VideoInteractionEntity();
                interaction.setUserId(user.getUserId());
                interaction.setVideoId(video.getId());
                interaction.setEventType(types.get(start));
                interaction.setWatchDuration(null);
                interaction.setVideoDuration(null);
                interaction.setCreatedAt(LocalDateTime.now());
                interaction.setLatitude(video.getLatitude());
                interaction.setLongitude(video.getLongitude());

                interactions.add(interaction);
            }

            flow_a_count++;
        } else {
            // do flow b -> impression then skip
            flow_b_count++;

            VideoInteractionEntity interactionImpression = new VideoInteractionEntity();
            interactionImpression.setUserId(user.getUserId());
            interactionImpression.setVideoId(video.getId());
            interactionImpression.setEventType(VideoInteractionEventType.IMPRESSION);
            interactionImpression.setWatchDuration(null);
            interactionImpression.setVideoDuration(null);
            interactionImpression.setCreatedAt(LocalDateTime.now());
            interactionImpression.setLatitude(video.getLatitude());
            interactionImpression.setLongitude(video.getLongitude());

            VideoInteractionEntity interactionSkip = new VideoInteractionEntity();
            interactionSkip.setUserId(user.getUserId());
            interactionSkip.setVideoId(video.getId());
            interactionSkip.setEventType(VideoInteractionEventType.SKIP);
            interactionSkip.setWatchDuration(null);
            interactionSkip.setVideoDuration(null);
            interactionSkip.setCreatedAt(LocalDateTime.now());
            interactionSkip.setLatitude(video.getLatitude());
            interactionSkip.setLongitude(video.getLongitude());

            interactions.add(interactionImpression);
            interactions.add(interactionSkip);

        }

    }

}
