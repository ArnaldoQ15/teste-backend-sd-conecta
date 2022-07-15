package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService implements INotificationService {

    public void notify(User user, String message) {
        log.info("Notification to " + user.getEmail() + ", message: " + message);
    }

}
