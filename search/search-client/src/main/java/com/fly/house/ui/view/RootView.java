package com.fly.house.ui.view;

import com.fly.house.authentication.Authorization;
import com.fly.house.ui.event.ChoosePathEvent;
import com.fly.house.ui.event.LoginEvent;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dimon on 3/6/14.
 */
@Component
public class RootView extends JFrame {

    @Autowired
    private Authorization authorization;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private ViewContainer container;

    @Autowired
    private TopMenu menu;

    public void run() {
        setConfig();
        setJMenuBar(menu.asMenuBar());
        fireEvent();
        addContainer();
        pack();
        setVisible(true);
    }

    private void fireEvent() {
        if (authorization.isAuthorized()) {
            eventBus.post(new ChoosePathEvent());
        } else {
            eventBus.post(new LoginEvent());
        }
    }

    private void addContainer() {
        container.setPreferredSize(new Dimension(500, 500));
        add(container);
    }

    private void setConfig() {
        setPreferredSize(new Dimension(500, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

}
