package com.softserve.sprint14.service;

import com.softserve.sprint14.entity.Marathon;
import com.softserve.sprint14.entity.Progress;
import com.softserve.sprint14.entity.User;

import java.util.List;

public interface UserService {

    public List<User> getAll();

    public User getUserById(Long id);

    public User createOrUpdateUser(User user);

    boolean removeUserFromMarathon(User user, Marathon marathon);

    public List<User> getAllByRole(String role);

    public boolean addUserToMarathon(User user, Marathon marathon);

    public User deleteUser(User user);
}
