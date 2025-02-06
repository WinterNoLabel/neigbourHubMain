package com.neighbourhub.community.firebase;


import com.neighbourhub.community.entity.Community;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CommunityFirebaseChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "community_id")
    Community community;
    String chatId;

}
