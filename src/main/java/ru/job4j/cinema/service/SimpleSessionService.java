package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleSessionService implements SessionService {

    private final SessionRepository sessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public SimpleSessionService(SessionRepository sql2oSessionRepository, FilmRepository sql2oFilmRepository, HallRepository sql2oHallRepository) {
        this.sessionRepository = sql2oSessionRepository;
        this.filmRepository = sql2oFilmRepository;
        this.hallRepository = sql2oHallRepository;

    }

    @Override
    public Optional<Session> findById(int id) {
        return sessionRepository.findById(id);
    }


    @Override
    public Collection<SessionDto> findAll() {
        List<Session> list = (List<Session>) sessionRepository.findAll();
        List<SessionDto> listOfSessionDTO = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listOfSessionDTO.add(new SessionDto(list.get(i).getId(),
                    filmRepository.findById(list.get(i).getFilmId()).get().getName(),
                    hallRepository.findById(list.get(i).getHallId()).get().getName(),
                    list.get(i).getStartTime(),
                    list.get(i).getEndTime(),
                    list.get(i).getPrice()
            ));
        }
        return listOfSessionDTO;
    }
}
