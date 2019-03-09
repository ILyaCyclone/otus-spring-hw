package cyclone.otusspring.hw01.service;

public interface MessageService {
    String getMessage(String key);

    String getMessage(String key, String... parameters);
}
