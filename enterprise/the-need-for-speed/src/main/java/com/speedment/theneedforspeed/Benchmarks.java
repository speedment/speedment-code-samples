package com.speedment.theneedforspeed;

import com.company.sakila.SakilaApplication;
import com.company.sakila.SakilaApplicationBuilder;
import com.company.sakila.db0.sakila.film.Film;
import com.company.sakila.db0.sakila.film.FilmManager;
import com.speedment.enterprise.datastore.runtime.DataStoreBundle;
import com.speedment.enterprise.datastore.runtime.DataStoreComponent;
import com.speedment.enterprise.datastore.runtime.entitystore.EntityStore;
import com.speedment.enterprise.plugins.json.JsonBundle;
import com.speedment.enterprise.plugins.json.JsonCollector;
import com.speedment.enterprise.plugins.json.JsonCollectors;
import com.speedment.enterprise.plugins.json.JsonComponent;
import com.speedment.enterprise.plugins.json.JsonEncoder;
import com.speedment.enterprise.virtualcolumn.runtime.VirtualColumnBundle;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;

import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 *
 * @author Per Minborg
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Threads(Threads.MAX)
@Fork(2)
@State(Scope.Benchmark)
public class Benchmarks {

    @Param({
        "false",
        "true"
    })
    protected String useDataStore;

    private FilmManager films;
    private JsonEncoder<Film> filmEncoder;
    private JsonCollector<Film, ?> filmCollector;
    private JsonCollector<Film, AtomicInteger> sumLengthCollector;

    @Setup(/*Level.Iteration*/)
    public void setup() {
        
        
        
        SakilaApplicationBuilder builder = new SakilaApplicationBuilder()
            .withPassword("sakila-password")
            //.withBundle(VirtualColumnBundle.class)
            .withBundle(JsonBundle.class)
            ;
//            .withLogging(LogType.STREAM);
//            .withLogging(ApplicationBuilder.LogType.APPLICATION_BUILDER)

        if (Boolean.parseBoolean(useDataStore)) {
            builder.withBundle(DataStoreBundle.class);
        }

        SakilaApplication app = builder.build();

        final JsonComponent json = app.getOrThrow(JsonComponent.class);
        filmEncoder = json.<Film>emptyEncoder()
            .put(Film.TITLE)
            .put(Film.RATING)
            .put(Film.LENGTH)
            .build();

        sumLengthCollector = new JsonCollector<Film, AtomicInteger>() {
            @Override
            public Supplier<AtomicInteger> supplier() {
                return AtomicInteger::new;
            }

            @Override
            public BiConsumer<AtomicInteger, Film> accumulator() {
                return (a, f) -> a.getAndAdd(f.getLength().orElse(0));
            }

            @Override
            public ObjLongConsumer<AtomicInteger> referenceAccumulator(EntityStore<Film> store) {
                return (ai, ref)
                    -> ai.getAndAdd(store.deserializeReference(ref, Film.LENGTH));
            }

            @Override
            public BinaryOperator<AtomicInteger> combiner() {
                return (a, b) -> {
                    a.addAndGet(b.get());
                    return a;
                };
            }

            @Override
            public Function<AtomicInteger, String> finisher() {
                return AtomicInteger::toString;
            }

            @Override
            public Set<Collector.Characteristics> characteristics() {
                return Collections.singleton(Characteristics.UNORDERED);
            }

        };

        filmCollector = json.collector(Film.class)
            .put("totalLength", /*JsonCollectors.sum(Film.LENGTH)*/ sumLengthCollector)
            .build();

           

        // Load data from database into materialized view
        app.get(DataStoreComponent.class).ifPresent(ds -> {
            ds.load();
        //            StatisticsUtil.prettyPrint(ds.getStatistics()).forEach(System.out::println);
        });

        // You are ready to go!
        films = app.getOrThrow(FilmManager.class);
        
        
    }

    @Benchmark
    public long countAll() {
        return films.stream()
            .count();
    }

    @Benchmark
    public long countWithFilter() {
        return films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .count();
    }

    @Benchmark
    public String filtering() {
        return films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .collect(filmCollector);
    }

    @Benchmark
    public String sorting() {
        return films.stream()
            .sorted(Film.RATING.comparator())
            .collect(filmCollector);
    }

    @Benchmark
    public String paging() {
        return films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .skip(50)
            .limit(50)
            .collect(filmCollector);
    }

    @Benchmark
    public String aggregation() {
        return films.stream()
            .collect(sumLengthCollector);
    }

    @Benchmark
    public String aggregationWithFilter() {
        return films.stream()
            .filter(Film.RATING.equal("PG-13"))
            .collect(sumLengthCollector);
    }

    @Benchmark
    public void iterateOverAll() {
        films.stream().forEach(blackHole());
    }

    public static <T> Consumer<T> blackHole() {
        return t -> {
        };
    }

}
