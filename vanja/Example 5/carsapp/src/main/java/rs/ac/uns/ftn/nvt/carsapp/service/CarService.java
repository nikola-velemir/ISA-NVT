package rs.ac.uns.ftn.nvt.carsapp.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.nvt.carsapp.model.Car;
import rs.ac.uns.ftn.nvt.carsapp.repository.ICarRepository;

import java.util.List;

@Service
public class CarService {

    private final ICarRepository carRepository;

    public CarService(ICarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }
}
