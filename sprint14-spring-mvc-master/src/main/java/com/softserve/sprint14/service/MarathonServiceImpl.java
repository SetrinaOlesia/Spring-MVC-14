package com.softserve.sprint14.service;

import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.exception.EntityNotFoundByIdException;
import com.softserve.sprint14.repository.MarathonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MarathonServiceImpl implements MarathonService {

    @Autowired
    MarathonRepository marathonRepository;

    @Override
    public List<Marathon> getAll() {
        List<Marathon> marathons = marathonRepository.findAll();
        return marathons.isEmpty() ? new ArrayList<>() : marathons;
    }

    @Override
    public Marathon getMarathonById(Long id) {
        return marathonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundByIdException("No marathon exists for id " + id));
    }

    @Override
    public Marathon createOrUpdateMarathon(Marathon marathon) {
        if (marathon.getId() != null) {
            Optional<Marathon> marathonToUpdate = marathonRepository.findById(marathon.getId());
            if (marathonToUpdate.isPresent()) {
                Marathon newMarathon = marathonToUpdate.get();
                newMarathon.setTitle(marathon.getTitle());
                return marathonRepository.save(newMarathon);
            }
        }
        return marathonRepository.save(marathon);
    }

    @Override
    public void closeMarathonById(Long id) {
        Optional<Marathon> marathon = marathonRepository.findById(id);
        if (marathon.isPresent()){
            Marathon newMarathon = marathon.get();
            newMarathon.setClosed(true);
            marathonRepository.save(newMarathon);
        }
        else throw new EntityNotFoundByIdException("No marathon exists for given id");
    }

    @Override
    public void openMarathonById(Long id) {
        Optional<Marathon> marathon = marathonRepository.findById(id);
        if (marathon.isPresent()){
            Marathon newMarathon = marathon.get();
            newMarathon.setClosed(false);
            marathonRepository.save(newMarathon);
        }
        else throw new EntityNotFoundByIdException("No marathon exists for given id");
    }

    @Override
    public void deleteMarathonById(Long id) {
        Optional<Marathon> marathon = marathonRepository.findById(id);
        if (marathon.isPresent())
            marathonRepository.deleteById(id);
        else throw new EntityNotFoundByIdException("No marathon exists for given id");
    }
}
