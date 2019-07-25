package cyclone.otusspring.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    /**
     * @return username of authenticated user
     * @throws AuthenticationException if could not authenticate
     */
    @Override
    public String authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }


    @Override
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
