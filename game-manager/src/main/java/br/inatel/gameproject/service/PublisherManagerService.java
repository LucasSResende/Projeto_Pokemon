package br.inatel.gameproject.service;

import br.inatel.gameproject.handler.exceptions.PublisherManagerConnectionException;
import br.inatel.gameproject.model.dto.NotificationDTO;
import br.inatel.gameproject.model.dto.PublisherDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class PublisherManagerService {

    @Value("${publisher.manager.host}")
    private String publisherManagerHost;

    @Value("${publisher.manager.port}")
    private String publisherManagerPort;

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    private WebClient webClient;

    @Cacheable(cacheNames = "cache")
    public List<PublisherDTO> getAllPublishers() {
        try {
            PublisherDTO[] publishers = WebClient.create("http://"
                            + this.publisherManagerHost + ":"
                            + this.publisherManagerPort)
                    .get()
                    .uri("/publisher")
                    .retrieve()
                    .bodyToMono(PublisherDTO[].class)
                    .block();
            return Arrays.asList(publishers);
        } catch (WebClientException e) {
            throw new PublisherManagerConnectionException();
        }
    }

    @CacheEvict(cacheNames = "cache")
    public void deletePublisherCache() {
        log.info("Deleted cache");
    }

    public NotificationDTO[] sendPublisherManagerNotification() {
        try {
            NotificationDTO notificationDTO = NotificationDTO.builder()
                    .host(this.host)
                    .port(this.port).build();
            webClient = WebClient
                    .builder()
                    .baseUrl("http://"
                            + this.publisherManagerHost + ":"
                            + this.publisherManagerPort).build();

            return this.webClient.post()
                    .uri("/notification")
                    .body(BodyInserters.fromValue(notificationDTO))
                    .retrieve()
                    .bodyToMono(NotificationDTO[].class)
                    .block();
        } catch (WebClientException e) {
            throw new PublisherManagerConnectionException();
        }
    }


}