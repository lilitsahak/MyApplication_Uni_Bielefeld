package com.bielefeld.uni_bielefeld.model;

import java.io.Serializable;
import java.util.Date;

public class Answer implements Serializable {
    public String id;
    public String text;
    public String postedBy;
    public String topicId;
    public Date postedOn;

    public Answer() {}

    public Answer(String id, String text, String postedBy, String topicId, Date postedOn) {
        this.id = id;
        this.text = text;
        this.postedBy = postedBy;
        this.postedOn = postedOn;
        this.topicId = topicId;
    }
}
