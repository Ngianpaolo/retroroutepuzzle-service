package io.nigro.retroroutepuzzle.feature.routeresult;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import io.nigro.retroroutepuzzle.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class RouteResultRepository {

    private final String path;

    @Autowired
    public RouteResultRepository(@Value("${io.nigro.retroroutepuzzle.storage.routeresults:./storage/route_results/}") String path) {
        this.path = path;
    }

    public ByteArrayInputStream save(List<RouteEvent> routeEvents, String filename) {
        var routeEventBytes = FileUtil.routeEventToCSV(routeEvents).readAllBytes();
        try {
            IOUtils.copy(new ByteArrayInputStream(routeEventBytes), new FileOutputStream(path + "" + filename.toLowerCase() + ".txt"));
        } catch (IOException e) {
            log.error("Error: ", e);
        }
        return new ByteArrayInputStream(routeEventBytes);
    }

    public List<String> findAllRouteResultIds() {
        return getAllSavedTextFiles().stream()
                .filter(Objects::nonNull)
                .map(file -> file.getName().replace(".txt", ""))
                .collect(Collectors.toList());
    }

    public InputStream getLatestRouteResults(Integer size) {
        var files = getAllSavedTextFiles().stream()
                .map(file -> {
                    try {

                        return List.of(
                                new ByteArrayInputStream((" " + StringUtils.center("", 68, "-") + "\n").getBytes(StandardCharsets.UTF_8)),
                                new ByteArrayInputStream((" " + StringUtils.center(" " + file.getName().replace(".txt", "") + " ", 68, '-') + "\n").getBytes(StandardCharsets.UTF_8)),
                                new ByteArrayInputStream(FileUtils.readFileToByteArray(file)),
                                new ByteArrayInputStream("\n".getBytes(StandardCharsets.UTF_8)));
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(lastN(size))
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new SequenceInputStream(Collections.enumeration(files));
    }

    private List<File> getAllSavedTextFiles() {
        File dir = new File(path);
        File[] textList = dir.listFiles((dir1, name) -> name.endsWith(".txt"));
        return Arrays.asList(Objects.requireNonNull(textList));
    }

    public Optional<ByteArrayInputStream> findRouteResultById(String id) {
        File file = new File(String.format("%s%s.txt", path, id));
        if (!file.exists()) {
            return Optional.empty();
        }
        try {
            return Optional.of(new ByteArrayInputStream(FileUtils.readFileToByteArray(file)));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private static <T> Collector<T, ?, List<T>> lastN(int n) {
        return Collector.<T, Deque<T>, List<T>>of(ArrayDeque::new, (acc, t) -> {
            if (acc.size() == n)
                acc.pollFirst();
            acc.add(t);
        }, (acc1, acc2) -> {
            while (acc2.size() < n && !acc1.isEmpty()) {
                acc2.addFirst(acc1.pollLast());
            }
            return acc2;
        }, ArrayList::new);
    }
}
