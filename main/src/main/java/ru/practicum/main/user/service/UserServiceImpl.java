package ru.practicum.main.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.exception.SomethingWentWrongException;
import ru.practicum.main.exception.UserNotFoundException;
import ru.practicum.main.user.dto.NewUserRequest;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.mapper.UserMapper;
import ru.practicum.main.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        if (userRepository.existsByName(newUserRequest.getName()))
            throw new SomethingWentWrongException("Невозможно создать пользователя с уже существующим именем.");
        return UserMapper.toDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll(List<Long> userIds, int from, int size) {
        if (userIds != null) {
            return userRepository.findUsersByIdIn(userIds, PageRequest.of(from / size, size)).stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(PageRequest.of(from / size, size)).stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}
