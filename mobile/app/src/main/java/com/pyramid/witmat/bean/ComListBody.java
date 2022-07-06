package com.pyramid.witmat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Create by lvxinlong  229608356@qq.com
 * desc
 * on
 **/
public class ComListBody<T> implements Serializable {


    public int count;
    public String next;
    public String previous;
    public List<T> results;
}
