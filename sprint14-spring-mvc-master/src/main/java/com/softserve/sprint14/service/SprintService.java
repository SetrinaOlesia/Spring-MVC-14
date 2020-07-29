package com.softserve.sprint14.service;

import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.entity.Sprint;

import java.util.List;

public interface SprintService {
    public List<Sprint> getSprintByMarathonId(Long id);
    public boolean addSprintToMarathon(Sprint sprint, Marathon marathon);
    public Sprint createOrUpdateSprint(Sprint sprint);
    public Sprint getSprintById(Long id);
    public Sprint deleteSprint(Sprint sprint);
}
