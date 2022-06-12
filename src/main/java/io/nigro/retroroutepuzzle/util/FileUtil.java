package io.nigro.retroroutepuzzle.util;

import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.apache.logging.log4j.util.Chars.TAB;

public class FileUtil {
    public static ByteArrayInputStream routeEventToCSV(List<RouteEvent> routeEvents) {
        final CSVFormat format = CSVFormat.Builder.create()
                .setDelimiter(TAB)
                .setQuoteMode(QuoteMode.ALL)
                .setQuote(' ')
                .build();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            csvPrinter.printRecord("--------------------------------------------------------------------");
            csvPrinter.printRecord(List.of(
                    StringUtils.rightPad("ID", 4),
                    StringUtils.rightPad("Room", 11),
                    StringUtils.rightPad("Object collected", 20)));
            csvPrinter.printRecord("--------------------------------------------------------------------");

            for (RouteEvent routeEvent : routeEvents) {
                List<String> data = Arrays.asList(
                        StringUtils.rightPad(String.valueOf(routeEvent.getId()), 4),
                        StringUtils.rightPad(routeEvent.getRoom(), 14),
                        StringUtils.rightPad(routeEvent.getObjectCollected(), 20)
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
