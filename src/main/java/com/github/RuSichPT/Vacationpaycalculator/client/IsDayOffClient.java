package com.github.RuSichPT.Vacationpaycalculator.client;

import java.net.URI;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public interface IsDayOffClient {
    HttpResponse<String> sendGet(URI uri);

    boolean isDayOff(LocalDate date);
}
