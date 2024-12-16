package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.entity.OrderCollection;
import vn.id.vuductrieu.tlcn_be.entity.document.ItemDocument;
import vn.id.vuductrieu.tlcn_be.repository.OrderRepo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SumaryMongoService {

    private final OrderRepo orderRepo;

    LocalTime startOfDay = LocalTime.of(0, 0, 0);
    LocalTime endOfDay = LocalTime.of(23, 59, 59);

    public Object getSales(LocalDate startDate, LocalDate endDate, String brand) {
        List<Long> result = new ArrayList<>();
        List<String> date = new ArrayList<>();
        Duration diff = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay());
        if (diff.toDays() < 10) {
            startDate = endDate.minusDays(10);
            diff = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay());
        }

        Long dateSeparate = diff.toDays() / 10;
        LocalDate baseDate = startDate.minusDays(1);

        for (int i = 0; i < 9; i++) {
            LocalDateTime from = baseDate.plusDays(dateSeparate * i + 1).atTime(startOfDay).plusHours(7);
            LocalDateTime to = baseDate.plusDays(dateSeparate * (i + 1)).atTime(endOfDay).plusHours(7);
            Long sum = sumarySales(from, to, brand);
            result.add(sum);
            date.add(convertDate(from.toLocalDate()) + " - " + convertDate(to.toLocalDate()));
        }
        Long sum = sumarySales(startDate.plusDays(dateSeparate * 9).atTime(startOfDay).plusHours(7),
            endDate.atTime(endOfDay).plusHours(7), brand);
        result.add(sum);
        date.add(convertDate(startDate.plusDays(dateSeparate * 9)) + " - " + convertDate(endDate));
        return Map.of("labels", date, "datasets", List.of(Map.of("label", "Sales", "data", result)));
    }

    public Object getRevenue(LocalDate startDate, LocalDate endDate, String brand) {
        List<Long> result = new ArrayList<>();
        List<String> date = new ArrayList<>();
        Duration diff = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay());
        if (diff.toDays() < 10) {
            startDate = endDate.minusDays(10);
            diff = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay());
        }

        Long dateSeparate = diff.toDays() / 10;
        LocalDate baseDate = startDate.minusDays(1);

        for (int i = 0; i < 9; i++) {
            LocalDateTime from = baseDate.plusDays(dateSeparate * i + 1).atTime(startOfDay).plusHours(7);
            LocalDateTime to = baseDate.plusDays(dateSeparate * (i + 1)).atTime(endOfDay).plusHours(7);
            Long sum = sumaryRevenue(from, to, brand);
            result.add(sum);
            date.add(convertDate(from.toLocalDate()) + " - " + convertDate(to.toLocalDate()));
        }
        Long sum = sumaryRevenue(startDate.plusDays(dateSeparate * 9).atTime(startOfDay).plusHours(7),
            endDate.atTime(endOfDay).plusHours(7), brand);
        result.add(sum);
        date.add(convertDate(startDate.plusDays(dateSeparate * 9)) + " - " + convertDate(endDate));
        return Map.of("labels", date, "datasets", List.of(Map.of("label", "Revenue", "data", result)));
    }

    // convert yyyy-MM-dd to dd/MM/yy
    public String convertDate(LocalDate date) {
        String[] dateArr = date.toString().split("-");
        return dateArr[2].substring(0, 2) + "/" + dateArr[1] + "/" + dateArr[0].substring(2);
    }

    private Long sumarySales(LocalDateTime from, LocalDateTime to, String brand) {
        Stream<OrderCollection> orderStream = orderRepo.findAll().stream();

        if (brand != null && !brand.equals("")) {
            String lowerCaseBrand = brand.toLowerCase();
            orderStream =
                orderStream.filter(order -> order.getItems().stream().anyMatch(item -> item.getProductName().toLowerCase().contains(lowerCaseBrand)));

            return orderStream.filter(order -> order.getCreatedAt().isAfter(from) && order.getCreatedAt().isBefore(to))
                .flatMap(order -> order.getItems().stream())
                .mapToLong(ItemDocument::getQuantity)
                .sum();
        }

        return orderStream.filter(order -> order.getCreatedAt().isAfter(from) && order.getCreatedAt().isBefore(to))
            .flatMap(order -> order.getItems().stream())
            .mapToLong(ItemDocument::getQuantity)
            .sum();
    }

    private Long sumaryRevenue(LocalDateTime from, LocalDateTime to, String brand) {
        Stream<OrderCollection> orderStream = orderRepo.findAll().stream();

        if (brand != null && !brand.equals("")) {
            String lowerCaseBrand = brand.toLowerCase();
            orderStream =
                orderStream.filter(order -> order.getItems().stream().anyMatch(item -> item.getProductName().toLowerCase().contains(lowerCaseBrand)));

            return orderStream.filter(order -> order.getCreatedAt().isAfter(from) && order.getCreatedAt().isBefore(to))
                .flatMap(order -> order.getItems().stream())
                .mapToLong(item -> (long) item.getQuantity() * item.getPrice())
                .sum();
        }

        return orderStream.filter(order -> order.getCreatedAt().isAfter(from) && order.getCreatedAt().isBefore(to))
            .flatMap(order -> order.getItems().stream())
            .mapToLong(item -> (long) item.getQuantity() * item.getPrice())
            .sum();
    }

}
