package com.natsu.blog.service.schedule.task;

public interface JobRunner extends Runnable {

    void setParam(String param);

}
