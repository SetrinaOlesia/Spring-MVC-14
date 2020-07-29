package com.softserve.sprint14.service;

import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.entity.User;
import com.softserve.sprint14.repository.MarathonRepository;
import com.softserve.sprint14.repository.ProgressRepository;
import com.softserve.sprint14.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ProgressRepository progressRepository;

    private final MarathonRepository marathonRepository;

    private final MarathonService marathonService;

    public UserServiceImpl(UserRepository userRepository, ProgressRepository progressRepository,
                           MarathonRepository marathonRepository, MarathonService marathonService) {
        this.userRepository = userRepository;
        this.progressRepository = progressRepository;
        this.marathonRepository = marathonRepository;
        this.marathonService = marathonService;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(("No existing user found with id "+id)));
    }

    @Override
    public User createOrUpdateUser(User user) {
//        if (user.getId() != null) {
//            Optional<User> userToUpdate = userRepository.findById(user.getId());
//            if (userToUpdate.isPresent()) {
//                User newUser = userToUpdate.get();
//                newUser.setEmail(user.getEmail());
//                newUser.setFirstName(user.getFirstName());
//                newUser.setLastName(user.getLastName());
//                newUser.setPassword(user.getPassword());
//                newUser.setRole(user.getRole());
//                newUser = userRepository.save(newUser);
//                return newUser;
//            }
//        }
//        user = userRepository.save(user);
//        return user;
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(User user) {
        Long id = user.getId();
        if (id != null) {
            userRepository.deleteById(id);
            return user;
        }
        return null;
    }

    @Override
    public boolean removeUserFromMarathon(User user, Marathon marathon) {
        User userEntity = userRepository.getOne(user.getId());
        Marathon marathonEntity = marathonRepository.getOne(marathon.getId());
        marathonEntity.getUsers().remove(userEntity);
        return marathonRepository.save(marathonEntity) != null;
    }

    @Override
    public List<User> getAllByRole(String role) {
        return userRepository.getAllByRole(User.Role.valueOf(role.toUpperCase()));
//        List<User> users = userRepository.findAll()
//                .stream()
//                .filter(user -> user.getRole().toString().equals(role))
//                .collect(Collectors.toList());
//        return users.isEmpty() ? new ArrayList<>() : users;
    }

    @Override
    public boolean addUserToMarathon(User user, Marathon marathon) {
        User userEntity = userRepository.getOne(user.getId());
        Marathon marathonEntity = marathonRepository.getOne(marathon.getId());
        marathonEntity.getUsers().add(userEntity);
        return marathonRepository.save(marathonEntity) != null;
//        User userEntity = getUserById(user.getId());
//        Marathon marathonEntity = marathonService.getMarathonById(marathon.getId());
//        marathonEntity.getUsers().add(userEntity);
//        userEntity.getMarathons().add(marathonEntity);
//        user.getMarathons().add(marathon);
//        marathon.getUsers().add(user);
//        createOrUpdateUser(user);
//        marathonService.createOrUpdateMarathon(marathon);
//        return marathonRepository.save(marathonEntity) != null
//                && userRepository.save(userEntity) != null;
    }
}
