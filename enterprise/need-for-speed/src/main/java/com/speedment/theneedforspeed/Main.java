/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.speedment.theneedforspeed;

import com.company.sakila.SakilaApplication;
import com.company.sakila.SakilaApplicationBuilder;
import com.company.sakila.db0.sakila.film.Film;
import com.company.sakila.db0.sakila.film.FilmManager;
import com.speedment.common.benchmark.Stopwatch;
import com.speedment.enterprise.datastore.runtime.DataStoreBundle;
import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import com.speedment.enterprise.datastore.runtime.util.StatisticsUtil;
import com.speedment.enterprise.plugins.json.JsonBundle;
import com.speedment.enterprise.plugins.json.JsonCollector;
import com.speedment.enterprise.plugins.json.JsonCollectors;
import com.speedment.enterprise.plugins.json.JsonComponent;
import com.speedment.enterprise.plugins.json.JsonEncoder;
import com.speedment.enterprise.virtualcolumn.runtime.VirtualColumnBundle;
import com.speedment.runtime.core.ApplicationBuilder.LogType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Per Minborg
 */
public class Main {

    public static void main(String... param) {
        SakilaApplication app = new SakilaApplicationBuilder()
            .withPassword("sakila-password")
//            .withBundle(VirtualColumnBundle.class)
            .withBundle(DataStoreBundle.class)
            .withBundle(JsonBundle.class)
            //.withLogging(LogType.STREAM)
            .withLogging(LogType.APPLICATION_BUILDER)
            .build();

        final JsonComponent json = app.getOrThrow(JsonComponent.class);
        final JsonEncoder<Film> filmEncoder = json.<Film>emptyEncoder()
            .put(Film.TITLE)
            .put(Film.RATING)
            .build();

        // Load data from database into materialized view
        app.get(DataStoreComponent.class).ifPresent(ds -> {
            ds.load();
            StatisticsUtil.prettyPrint(ds.getStatistics()).forEach(System.out::println);
        });

        // You are ready to go!
        final FilmManager films = app.getOrThrow(FilmManager.class);

        JsonCollector<Film, ?> filmCollector = json.collector(Film.class)
            .put(
                "maxLength",
                JsonCollectors.max(
                    Film.LENGTH,
                    i -> String.format("\"%d hours and %d minutes\"", i / 60, i % 60)
                )
            )
            .build();

        System.out.println(
            films.stream().collect(filmCollector)
        );

        List<Film> secondPage = films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .sorted(Film.TITLE.comparator())
            .skip(50)
            .limit(50)
            .collect(Collectors.toList());

        secondPage.stream().forEach(System.out::println);

        long count = films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .count();

        System.out.println(count);

        bench("page", () -> {
            int len = page(films, 1).size();
        });

        bench("count", () -> {
            long cnt = count(films);
        });

        bench("pageJsonSerialize", () -> {
            String jsonResult = pageJson(films, 1);
        });

        bench("pageJsonInPlaceSerialize", () -> {
            String jsonResult = pageJson(films, 1, filmEncoder);
        });

        String jsonFilmsSequential = films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .collect(JsonCollectors.toList(filmEncoder));

        String pg13Films = films.stream()
            .parallel()
            .filter(Film.RATING.equal("PG-13"))
            .collect(JsonCollectors.toList(filmEncoder));

        System.out.println(jsonFilmsSequential);
        System.out.println(pg13Films);

        System.out.println(jsonFilmsSequential.equals(pg13Films));

    }

    enum State {
        WARMUP, WARMUP2, BENCHMARK;
    }

    private static void bench(String name, Runnable r) {
        for (State state : State.values()) {
            for (int j = 0; j < 20; j++) {
                final Stopwatch sw = Stopwatch.createStarted();
                IntStream.rangeClosed(0, 1000).parallel().forEach(i -> r.run());
                sw.stop();
                System.out.format("State %s (%d), Test %s completed in %s ms%n", state, j, name, sw.elapsedMillis());
            }

        }
    }

    private static List<Film> page(FilmManager films, int pageNo) {
        List<Film> page = films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .sorted(Film.TITLE.comparator())
            .skip(50 * pageNo)
            .limit(50)
            .collect(Collectors.toList());
        return page;
    }

    private static String pageJson(FilmManager films, int pageNo) {
        return films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .sorted(Film.TITLE.comparator())
            .map(f -> String.format("{\"rating\":\"%s\",\"title\":\"%s\",\"length\":%d}", f.getRating().orElse("unknown"), f.getTitle(), f.getLength().orElse(0)))
            .collect(Collectors.joining(",", "[", "]"));
    }

    private static String pageJson(FilmManager films, int pageNo, final JsonEncoder<Film> filmEncoder) {
        return films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .sorted(Film.TITLE.comparator())
            .collect(JsonCollectors.toList(filmEncoder));
    }

    private static long count(FilmManager films) {
        long count = films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .count();
        return count;
    }

    private Stream<Film> page(int page) {
        return null;
    }

}
