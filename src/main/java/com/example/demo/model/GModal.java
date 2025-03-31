package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GModal {
    String idd;

    public static GModal toModalMapper(String idd){
        GModal game = new GModal();
        game.seIdd(idd);
        return game;

    }


    public void seIdd (String idd) {

        this.idd = idd;
    }

}
