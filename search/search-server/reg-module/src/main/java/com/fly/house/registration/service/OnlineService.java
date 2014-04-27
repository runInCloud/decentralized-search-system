package com.fly.house.registration.service;

import java.net.InetAddress;

/**
 * Created by dimon on 4/27/14.
 */
public interface OnlineService {

    void online(String userName, InetAddress ipAddress);

    public void offline(String name);

    long size();

    boolean isOnline(String name);
}
