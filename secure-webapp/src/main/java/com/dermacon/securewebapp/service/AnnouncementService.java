package com.dermacon.securewebapp.service;

import com.dermacon.securewebapp.data.Announcement;
import com.dermacon.securewebapp.data.AnnouncementRepository;
import com.dermacon.securewebapp.data.Course;
import com.dermacon.securewebapp.data.FormAnnouncementInfo;
import com.dermacon.securewebapp.data.Person;
import com.dermacon.securewebapp.exception.AnnouncementNonExistentException;
import com.dermacon.securewebapp.exception.NonExistentCourseException;
import com.dermacon.securewebapp.logger.LoggerSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static com.dermacon.securewebapp.data.UserRole.ROLE_ADMIN;
import static com.dermacon.securewebapp.data.UserRole.ROLE_MANAGER;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

//    public void removeAll(Set<Announcement> announcements) {
//        // todo exception when non existent
//        announcementRepository.deleteAll(announcements);
//    }


    /* ---------- announcements ---------- */

    public void createNewAnnouncement(Course course, FormAnnouncementInfo info) {

        Announcement announcement = new Announcement(
                info.getTitle(),
                info.getContent(),
                new Date(System.currentTimeMillis()),
                course
        );

        // save in database
        LoggerSingleton.getInstance().info("save announcement: " + announcement.getAnnouncementId());
        announcementRepository.save(announcement);

//        mailService.sendAnnouncement(course.getParticipants(), announcement);
    }

    public void deleteAnnouncements(Set<Announcement> announcements) {
        for (Announcement announcement : announcements) {
            announcement.setCourse(null);
            announcementRepository.delete(announcement);
        }
    }

    public void deleteAnnouncement(long id) throws AnnouncementNonExistentException {
        Optional<Announcement> announcement_opt = announcementRepository.findById(id);
        if (!announcement_opt.isPresent()) {
            throw new AnnouncementNonExistentException();
        }
        Announcement announcement = announcement_opt.get();
        announcement.setCourse(null);
        announcementRepository.delete(announcement);
    }

}
