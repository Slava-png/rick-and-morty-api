package springboot.rickandmorty.service;

import springboot.rickandmorty.model.Location;

import java.util.List;

public interface LocationService {
    void syncExternalLocations();

    Location findById(Long id);

    List<Location> findAll();
}
