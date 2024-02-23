package baykov.daniel.oracle11.service;

import baykov.daniel.oracle11.entity.User;
import baykov.daniel.oracle11.payload.mapper.UserMapper;
import baykov.daniel.oracle11.payload.request.UserRequestDTO;
import baykov.daniel.oracle11.payload.response.UserResponseDTO;
import baykov.daniel.oracle11.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = UserMapper.INSTANCE.dtoToEntity(userRequestDTO);
        userRepository.save(user);
        return UserMapper.INSTANCE.entityToDTO(user);
    }
}
