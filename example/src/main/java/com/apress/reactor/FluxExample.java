package com.apress.reactor;

import com.apress.reactor.domain.ToDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Configuration
@Slf4j
public class FluxExample {

    @Bean
    public CommandLineRunner runFluxExample() {
        return args -> {
            EmitterProcessor<ToDo> stream = EmitterProcessor.create();
            Mono<List<ToDo>> promise = stream.filter(s->s.isCompleted())
                    .doOnNext(s-> log.info("FLUX >>> ToDo: {}", s.getDescription()))
                    .collectList()
                    .subscribeOn(Schedulers.single());

        stream.onNext(new ToDo("Read a book", true));
        stream.onNext(new ToDo("Listen Classical Music", true));
        stream.onNext(new ToDo("Workout in the morning"));
            stream.onNext(new ToDo("Organize my room", true));
            stream.onNext(new ToDo("Go to the Car Wash", true));
            stream.onNext(new ToDo("SP1 2018 is coming", true));

            stream.onComplete();

            promise.block();
        };
    }
}
