package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleHallService implements HallService {

    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository sql2oHallRepository) {
        this.hallRepository = sql2oHallRepository;
    }

    @Override
    public Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    }

    @Override
    public Collection<Integer> getRowCountByHallId(int hallId) {
        Collection<Integer> listOfRows = new ArrayList<>();
        int rowCount = findById(hallId).get().getRowCount();
        for (int i = 1; i < rowCount + 1; i++) {
            listOfRows.add(i);
        }
        return listOfRows;
    }

    @Override
    public Collection<Integer> getPlaceCountByHallId(int hallId) {
        Collection<Integer> listOfPlaces = new ArrayList<>();
        int placeCount = findById(hallId).get().getPlaceCount();
        for (int i = 1; i < placeCount + 1; i++) {
            listOfPlaces.add(i);
        }
        return listOfPlaces;
    }
}

