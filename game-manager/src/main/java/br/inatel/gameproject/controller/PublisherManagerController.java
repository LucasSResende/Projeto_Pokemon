package br.inatel.gameproject.controller;

import br.inatel.gameproject.service.PublisherManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("publishermanagercache")//[Adauto] - With this request mapping your cache will never be cleared, please check documentation
public class PublisherManagerController {

    private final PublisherManagerService publisherManagerService;

    @Autowired
    public PublisherManagerController(PublisherManagerService publisherManagerService) {
        this.publisherManagerService = publisherManagerService;
    }

    @DeleteMapping
    public void deletePublisherManagerCache() {
        publisherManagerService.deletePublisherCache();
    }
}