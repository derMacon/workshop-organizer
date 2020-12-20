package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public void removeAll(Set<Announcement> announcements) {
        // todo exception when non existent
        announcementRepository.deleteAll(announcements);
    }

}
