package rs.ac.uns.ftn.nwt.influxdbexample.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import rs.ac.uns.ftn.nwt.influxdbexample.dto.Measurement;
import rs.ac.uns.ftn.nwt.influxdbexample.service.MeasurementService;

import java.util.*;

@EnableScheduling
@Configuration
public class ScheduledTask {

    private final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    private final Random random = new Random();
    private final List<String> measurements = Arrays.asList("Temperature", "Irradiance",
            "Humidity", "Precipitation");
    private final List<String> endpoints = Arrays.asList("10", "last",
            "aggregated", "deviation");

    private final MeasurementService measurementService;
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper;

    public ScheduledTask(MeasurementService measurementService,
                         SimpMessagingTemplate template) {
        this.measurementService = measurementService;
        this.template = template;
        this.mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }

    @Scheduled(fixedRate = 2000, initialDelay = 5000)
    public void generateMeasurement() {
        this.log.debug("Generating measurement");
        int randomIndex = this.random.nextInt(measurements.size());
        String randomElement = this.measurements.get(randomIndex);
        float randomValue = this.random.nextFloat() * (100);
        Map<String, String> tags = Map.of(
                "Type", "Weather",
                "Location", "Outside"
        );
        this.measurementService.save(randomElement, randomValue, new Date(), tags);
        this.log.debug("Measurement generated");
    }

    @Scheduled(fixedRate = 3000, initialDelay = 2000)
    public void streamData() throws JsonProcessingException {
        this.log.debug("Publishing measurement data");
        for (String measurement : this.measurements) {
            for (String endpoint : this.endpoints) {
                List<Measurement> data = switch (endpoint) {
                    case "10" -> this.measurementService.findLastTenByName(measurement);
                    case "last" -> this.measurementService.findLastByName(measurement);
                    case "aggregated" -> this.measurementService.findAggregatedDataByName(measurement);
                    default -> this.measurementService.findDeviationFromMeanByName(measurement);
                };
                Map<String, Object> message = Map.of("data", data);
                this.template.convertAndSend(
                        String.format("/data/%s/%s", endpoint, measurement),
                        this.mapper.writeValueAsString(message)
                );
            }
        }
        this.log.debug("Measurement data published");
    }
}
