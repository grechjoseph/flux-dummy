package com.jg.flux;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@RestController
@CrossOrigin("*")
public class FluxController {

    /*
        curl -i -X POST -H "Content-Type: application/json" -d "{\"iterations\":\"3\",\"interval\":\"500\"}" "http://localhost:8080"
        curl -i -X GET "http://localhost:8080?iterations=3&interval=500"
     */

    @GetMapping("/help")
    public Map<String, String> help() {
        return new HashMap<String, String>() {{
            put("Using GET", "curl -i -X GET \"http://localhost:8080?iterations=3&interval=500\"");
            put("Using POST", "curl -i -X POST -H \"Content-Type: application/json\" -d \"{\\\"iterations\\\":\\\"3\\\",\\\"interval\\\":\\\"500\\\"}\" \"http://localhost:8080\"");
        }};
    }

    @SneakyThrows
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<_ResponseBody> getFlux(@RequestParam(defaultValue = "0") final int iterations,
                                       @RequestParam(defaultValue = "0") final int interval) {
        log.info("Publishing for request: iterations={}, interval={}.", iterations, interval);
        if (iterations < 1) {
            return Flux.zip(
                    Flux.fromStream(Stream.generate(() -> new _ResponseBody(UUID.randomUUID()))),
                    Flux.interval(Duration.ofMillis(interval == 0 ? 1_000L : interval)),
                    (key, value) -> key
            );
        }

        return Flux.range(0, iterations)
                .map(i -> {
                    sleep(interval == 0 ? 1_000L : interval);
                    return new _ResponseBody(UUID.randomUUID());
                });
    }

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<_ResponseBody> postFlux(@RequestBody final _RequestBody request) {
        return getFlux(request.getIterations(), request.getInterval());
    }

    private void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class _RequestBody {

        private int iterations;
        private int interval;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class _ResponseBody {

        private UUID uuidValue;

    }

}
