package com.softserve.sprint14.repository;

import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint,Long> {
    List<Sprint> getAllSprintsByMarathonId(Long id);
    Optional<Sprint> findFirstByTitleAndMarathon(String title, Marathon marathon);

}
