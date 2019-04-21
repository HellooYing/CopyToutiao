package com.toutiao.async;

public enum EventType {
    LIKE(0),DISLIKE(1),COMMENT(2),LOGIN(3),MAIL(4);
    private int value;
    EventType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}
