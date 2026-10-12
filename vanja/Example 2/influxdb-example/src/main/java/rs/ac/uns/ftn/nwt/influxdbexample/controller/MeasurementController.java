package rs.ac.uns.ftn.nwt.influxdbexample.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.nwt.influxdbexample.dto.Measurement;
import rs.ac.uns.ftn.nwt.influxdbexample.service.MeasurementService;

import java.util.List;

@RestController
@RequestMapping(value = "measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @RequestMapping(value = "10", method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getLastTenByName(String name) {
        return new ResponseEntity<>(this.measurementService.findLastTenByName(name), HttpStatus.OK);
    }

    @RequestMapping(value = "last", method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getLastByName(String name) {
        return new ResponseEntity<>(this.measurementService.findLastByName(name), HttpStatus.OK);
    }

    @RequestMapping(value = "aggregated", method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getAggregatedDataByName(String name) {
        return new ResponseEntity<>(this.measurementService.findAggregatedDataByName(name), HttpStatus.OK);
    }

    @RequestMapping(value = "deviation", method = RequestMethod.GET)
    public ResponseEntity<List<Measurement>> getDeviationFromMeanByName(String name) {
        return new ResponseEntity<>(this.measurementService.findDeviationFromMeanByName(name), HttpStatus.OK);
    }
}
