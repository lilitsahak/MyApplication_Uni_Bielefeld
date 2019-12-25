package com.bielefeld.uni_bielefeld.model;

import java.io.Serializable;
import java.util.Date;

public class Topic implements Serializable {
    public String id;
    public String title;
    public String askedBy;
    public Date postedOn;

    public Topic () {}

    public Topic(String id, String title, String askedBy, Date postedOn) {
        this.id = id;
        this.title = title;
        this.askedBy = askedBy;
        this.postedOn = postedOn;
    }
}
