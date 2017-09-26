package com.retrofit.rx.bean;

import java.util.List;

/**
 * Created by sunxx on 2016/6/20.
 */
public class baseBean {
    String error;
    public List<Pic> results;

    public class Pic {
        public String _id;
        public String url;
        public String who;
    }
}
