package com.smallbusiness.crm.service;

import com.smallbusiness.crm.entity.Activity;
import com.smallbusiness.crm.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {
    private final ActivityRepository activityRepository;

    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    public List<Activity> getActivitiesByContact(Long contactId) {
        return activityRepository.findByContactIdOrderByCreatedAtDesc(contactId);
    }

    public List<Activity> getActivitiesByDeal(Long dealId) {
        return activityRepository.findByDealIdOrderByCreatedAtDesc(dealId);
    }
}
