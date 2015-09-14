package com.konifar.nearbychat.models.pojo;

public class MessageModel extends Model {

    private String text;

    public MessageModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
