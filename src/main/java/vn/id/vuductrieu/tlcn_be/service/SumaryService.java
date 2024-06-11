package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.repository.OrderItemRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SumaryService {

    private final OrderItemRepository orderItemRepository;

    public Object getSales(LocalDate startDate, LocalDate endDate) {
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
            LocalDate from = baseDate.plusDays(dateSeparate * i + 1);
            LocalDate to = baseDate.plusDays(dateSeparate * (i + 1));
            Long sum = orderItemRepository.findForSumarySales(from, to);
            if (sum == null) sum = 0L;
            result.add(sum);
            date.add(convertDate(from) + " - " + convertDate(to));
        }
        Long sum = orderItemRepository.findForSumarySales(startDate.plusDays(dateSeparate * 9), endDate);
        if (sum == null) sum = 0L;
        result.add(sum);
        date.add(convertDate(startDate.plusDays(dateSeparate * 9)) + " - " + convertDate(endDate));
        return Map.of("labels", date, "datasets", List.of(Map.of("label", "Sales", "data", result)));
    }

    public Object getRevenue(LocalDate startDate, LocalDate endDate) {
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
            LocalDate from = baseDate.plusDays(dateSeparate * i + 1);
            LocalDate to = baseDate.plusDays(dateSeparate * (i + 1));
            Long sum = orderItemRepository.findForSumaryRevenue(from, to);
            if (sum == null) sum = 0L;
            result.add(sum);
            date.add(convertDate(from) + " - " + convertDate(to));
        }
        Long sum = orderItemRepository.findForSumaryRevenue(startDate.plusDays(dateSeparate * 9), endDate);
        if (sum == null) sum = 0L;
        result.add(sum);
        date.add(convertDate(startDate.plusDays(dateSeparate * 9)) + " - " + convertDate(endDate));
        return Map.of("labels", date, "datasets", List.of(Map.of("label", "Revenue", "data", result)));
    }

    // convert yyyy-MM-dd to dd/MM/yy
    public String convertDate(LocalDate date) {
        String[] dateArr = date.toString().split("-");
        return dateArr[2] + "/" + dateArr[1] + "/" + dateArr[0].substring(2);
    }

}
