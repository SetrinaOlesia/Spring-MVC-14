package com.softserve.sprint14.service;

import com.softserve.sprint14.entity.Progress;
import com.softserve.sprint14.entity.Task;
import com.softserve.sprint14.entity.User;
import com.softserve.sprint14.exception.EntityNotFoundByIdException;
import com.softserve.sprint14.repository.ProgressRepository;
import com.softserve.sprint14.repository.SprintRepository;
import com.softserve.sprint14.repository.TaskRepository;
import com.softserve.sprint14.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    ProgressRepository progressRepository;

    TaskRepository taskRepository;

    SprintRepository sprintRepository;

    UserRepository userRepository;

    @Override
    public Progress getProgressById(Long id) {
        return progressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(("No Progress exist for given id")));
    }

    @Override
    public boolean addTaskForStudent(Task task, User user) {
        if (user.getRole() == User.Role.TRAINEE){
            Progress newProgress = new Progress();
            newProgress.setTask(task);
            newProgress.setTrainee(user);
            return progressRepository.save(newProgress) != null;
        }
        return false;
    }

    @Override
    public Progress createOrUpdateProgress(Progress progress) {
        if (progress.getId() != null) {
            Optional<Progress> progressToUpdate = progressRepository.findById(progress.getId());
            if (progressToUpdate.isPresent()) {
                Progress newProgress = progressToUpdate.get();
                newProgress.setStatus(progress.getStatus());
                newProgress.setTask(progress.getTask());
                newProgress.setTrainee(progress.getTrainee());
                return progressRepository.save(progress);
            }
        }
        return progressRepository.save(progress);
    }

    @Override
    public boolean setStatus(Progress.TaskStatus taskStatus, Progress progress) {
        Optional<Progress> progressEntityOpt = progressRepository.findById(progress.getId());
        if(progressEntityOpt.isPresent()) {
            Progress progressEntity = progressEntityOpt.get();
            if (progressEntity.getStatus() != taskStatus) {
                progressEntity.setStatus(taskStatus);
                return true;
            }
        }
        return progressRepository.save(progress)!=null;
    }

    @Override
    public List<Progress> allProgressByUserIdAndMarathonId(Long userId, Long marathonId) {
        return progressRepository.findAllByTraineeIdAndTaskSprintMarathonId(userId,marathonId);
    }

    @Override
    public List<Progress> allProgressByUserIdAndSprintId(Long userId, Long sprintId) {
        return progressRepository.findAllByTraineeIdAndTaskSprintId(userId,sprintId);
    }

    @Override
    public List<Progress> getAll() {
        return progressRepository.findAll();
    }

    @Override
    public List<Progress> getAllProgressesOfTask(Task task) {
        return progressRepository.findAll()
                .stream()
                .filter(progress -> progress.getTask().equals(task))
                .collect(Collectors.toList());
    }
    
    @Override
    public Progress deleteProgress(Progress progress) {
        Long id = progress.getId();
        if(id != null) {
            progressRepository.deleteById(id);
            return progress;
        }
        return null;
    }
}
