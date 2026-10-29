package br.inatel.gameproject.controller;

import br.inatel.gameproject.service.PublisherManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/publishermanagercache", "/publishercache"})
public class PublisherManagerController {

    private final PublisherManagerService publisherManagerService;

    @Autowired
    public PublisherManagerController(PublisherManagerService publisherManagerService) {
        this.publisherManagerService = publisherManagerService;
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePublisherManagerCache() {
        publisherManagerService.deletePublisherCache();
        return ResponseEntity.noContent().build();
    }
}
