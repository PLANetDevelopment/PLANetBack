package com.planet.develop.Service;

import com.planet.develop.DTO.UserDTO;
import com.planet.develop.Entity.User;
import com.planet.develop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String register(UserDTO dto) {
        User user = dtoToEntity(dto);
        Optional<User> findId = userRepository.findById(user.getUserId());
        if (findId.isPresent()) {
            log.info(findId.get().getUserId() + " is already exist...");
            return findId.get().getUserId();
        }
        userRepository.save(user);
        log.info(user.getUserId() + " is saved...");
        return user.getUserId();
    }

}
