package br.sdconecta.testebackend.service;

import br.sdconecta.testebackend.model.User;

public interface INotificationService {

    void notify(User user, String message);

}
