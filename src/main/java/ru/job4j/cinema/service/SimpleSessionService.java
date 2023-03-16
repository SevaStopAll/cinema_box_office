package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleSessionService implements SessionService {

    private final SessionRepository sessionRepository;

    public SimpleSessionService(SessionRepository sql2oSessionRepository) {
        this.sessionRepository = sql2oSessionRepository;
    }

    @Override
    public Collection<Session> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    public Optional<Session> findById(int id) {
        return Optional.empty();
    }
}
