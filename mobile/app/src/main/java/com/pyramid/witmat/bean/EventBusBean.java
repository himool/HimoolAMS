package com.pyramid.witmat.bean;
/**全局eventbus广播对象**/
public class EventBusBean {
    private String content;

    public EventBusBean(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
