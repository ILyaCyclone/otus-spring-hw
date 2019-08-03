package cyclone.otusspring.library.service;

public interface AuthenticationService {

    String authenticate(String username, String password);

    String getCurrentUsername();

    void logout();

}
