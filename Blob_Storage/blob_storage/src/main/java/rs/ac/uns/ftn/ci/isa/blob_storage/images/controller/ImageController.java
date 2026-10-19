package rs.ac.uns.ftn.ci.isa.blob_storage.images.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.ci.isa.blob_storage.images.dto.GetPutUrlResponse;
import rs.ac.uns.ftn.ci.isa.blob_storage.images.service.ImageService;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("api/images")
@CrossOrigin(value = "*", exposedHeaders = "Location")
public class ImageController {

    private final ImageService service;

    public ImageController(ImageService service) {
        this.service = service;
    }
    /*
    Kako NE skiadati fajl
     */
    @GetMapping("/{keyName}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String keyName) {
        var response = service.download(keyName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Or parse s3Stream.response().contentType()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + keyName + "\"")
                .body(response);
    }
    /*
    Kako NE upload-ovati fajl
    */
    @PostMapping
    public ResponseEntity<Void> upload(
            @RequestParam("file")
            MultipartFile multipartFile) throws IOException {
        var route = service.upload(multipartFile);
        return ResponseEntity.created(URI.create(route)).build();
    }

    @GetMapping("url/{key}")
    public ResponseEntity<String> getGetUrl(@PathVariable(value = "key") String key) throws IOException {
        var response = service.generateGetUrl(key);
        return ResponseEntity.ok(response);
    }

    @PutMapping("url")
    public ResponseEntity<GetPutUrlResponse> getPutUrl() throws IOException {
        var response = service.generatePutUrl();
        return ResponseEntity.ok(response);
    }
}
