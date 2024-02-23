package baykov.daniel.oracle11.controller;

import baykov.daniel.oracle11.payload.request.UserRequestDTO;
import baykov.daniel.oracle11.payload.response.UserResponseDTO;
import baykov.daniel.oracle11.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {

        UserResponseDTO createdUser = userService.createUser(userRequestDTO);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
