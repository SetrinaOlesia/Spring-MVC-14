package com.softserve.sprint14.repository;

import com.softserve.sprint14.entity.Marathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarathonRepository extends JpaRepository<Marathon,Long> {

}
