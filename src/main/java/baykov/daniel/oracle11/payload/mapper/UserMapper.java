package baykov.daniel.oracle11.payload.mapper;

import baykov.daniel.oracle11.entity.User;
import baykov.daniel.oracle11.payload.request.UserRequestDTO;
import baykov.daniel.oracle11.payload.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    UserResponseDTO entityToDTO(User user);

    List<UserResponseDTO> entityToDTO(Iterable<User> users);

    User dtoToEntity(UserRequestDTO userRequestDTO);

    List<User> dtoToEntity(Iterable<UserRequestDTO> userRequestDTOS);
}
