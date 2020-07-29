package com.softserve.sprint14.service;

import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.entity.Sprint;
import com.softserve.sprint14.exception.EntityNotFoundByIdException;
import com.softserve.sprint14.repository.MarathonRepository;
import com.softserve.sprint14.repository.SprintRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;

    private final MarathonRepository marathonRepository;

    private final MarathonService marathonService;

    @Override
    public List<Sprint> getSprintByMarathonId(Long id) {
        return sprintRepository.getAllSprintsByMarathonId(id);
    }

    @Override
    public boolean addSprintToMarathon(Sprint sprint, Marathon marathon) {
        if (sprint.getId() == null) {
            Marathon marathonEntity = marathonRepository.getOne(marathon.getId());
            if (!sprintRepository.findFirstByTitleAndMarathon(sprint.getTitle(), marathonEntity).isPresent()) {
                sprint.setMarathon(marathonEntity);
                sprintRepository.save(sprint);
                marathonEntity.getSprints().add(sprint);
                return marathonRepository.save(marathonEntity) != null;
            }
        }
        return false;
    }


    @Override
    public Sprint createOrUpdateSprint(Sprint sprint) {
        if (sprint.getId() != null) {
            Optional<Sprint> sprintToUpdate = sprintRepository.findById(sprint.getId());
            if (sprintToUpdate.isPresent()) {
                Sprint newSprint = sprintToUpdate.get();
                newSprint.setTitle(sprint.getTitle());
                newSprint.setStartDate(sprint.getStartDate());
                newSprint.setFinishDate(sprint.getFinishDate());
                newSprint.setMarathon(sprint.getMarathon());
                return sprintRepository.save(newSprint);
            }
        }
        return sprintRepository.save(sprint);
    }

    @Override
    public Sprint getSprintById(Long id) {
        Optional<Sprint> sprint = sprintRepository.findById(id);
        if (sprint.isPresent())
            return sprint.get();
        else throw new EntityNotFoundByIdException("No sprint for given id");
    }

    @Override
    public Sprint deleteSprint(Sprint sprint) {
        Long id = sprint.getId();
        if (id != null) {
            sprintRepository.deleteById(id);
            return sprint;
        }
        return null;
    }
}
