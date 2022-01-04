package com.example.movieapp.Dataclasses;

import java.io.Serializable;


public class Actor implements Serializable {


    private String actorName;
    private String characterName;
    private String imgUrl;
    public Actor(String actorName, String characterName, String imgUrl){
        this.actorName = actorName;
        this.characterName = characterName;
        this.imgUrl = imgUrl;

    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
