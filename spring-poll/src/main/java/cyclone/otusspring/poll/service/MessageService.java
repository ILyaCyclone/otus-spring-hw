package cyclone.otusspring.poll.service;

public interface MessageService {
    String getMessage(String key);

    String getMessage(String key, String... parameters);
}
