package com.github.RuSichPT.Vacationpaycalculator.client.impl;

import com.github.RuSichPT.Vacationpaycalculator.client.IsDayOffClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@Slf4j
@Component
public class IsDayOffClientImpl implements IsDayOffClient {
    private final String ROOT = "https://isdayoff.ru/";
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    @Override
    public HttpResponse<String> sendGet(URI uri) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Service isdayoff.ru is unavailable", e);
        }
    }

    @Override
    public boolean isDayOff(LocalDate date) {
        URI uri = URI.create(ROOT + date);
        HttpResponse<String> response = sendGet(uri);
        log.info(date + " " + date.getDayOfWeek() + " is day off = " + response.body());
        return response.body().equals("1");
    }
}
