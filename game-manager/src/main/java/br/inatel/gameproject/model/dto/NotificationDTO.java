package br.inatel.gameproject.model.dto;

import lombok.Builder;

@Builder
public record NotificationDTO(String host, String port) {
}