package cyclone.otusspring.library.controller.rest;

import cyclone.otusspring.library.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityRestController {

    @GetMapping
    public UserDto identity() {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return new UserDto(currentUserName);
    }

}
