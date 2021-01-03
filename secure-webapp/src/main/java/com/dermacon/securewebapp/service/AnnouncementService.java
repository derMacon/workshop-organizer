package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.controller.ManagerController;
import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.AnnouncementRepository;
import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.form_input.FormAnnouncementInfo;
import com.dermacon.securewebapp.exception.AnnouncementNonExistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import org.apache.log4j.Logger;

@Service
public class AnnouncementService {

    private static Logger log = Logger.getLogger(ManagerController.class);

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private MailService mailService;

    /* ---------- announcements ---------- */

    public void createNewAnnouncement(Course course, FormAnnouncementInfo info) {

        Announcement announcement = new Announcement(
                info.getTitle(),
                info.getContent(),
                new Date(System.currentTimeMillis()),
                course
        );

        // save in database
        log.info("save announcement: " + announcement.getAnnouncementId());
        announcementRepository.save(announcement);

        mailService.sendAnnouncement(course.getParticipants(), announcement);
    }

    public void deleteAnnouncements(Set<Announcement> announcements) {
        announcements.stream().forEach(this::deleteAnnouncement);
    }

    public void deleteAnnouncement(long id) throws AnnouncementNonExistentException {
        Optional<Announcement> announcementOpt = announcementRepository.findById(id);
        if (!announcementOpt.isPresent()) {
            throw new AnnouncementNonExistentException();
        }
        deleteAnnouncement(announcementOpt.get());
    }

    public void deleteAnnouncement(Announcement announcement) {
        announcement.setCourse(null);
        announcementRepository.delete(announcement);
    }

}
