package vn.id.vuductrieu.tlcn_be.service.MongoService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.OrderRepo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SumaryMongoService {

    private final OrderRepo orderRepo;

    LocalTime startOfDay = LocalTime.of(0, 0, 0);
    LocalTime endOfDay = LocalTime.of(23, 59, 59);

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
            LocalDateTime from = baseDate.plusDays(dateSeparate * i + 1).atTime(startOfDay).plusHours(7);
            LocalDateTime to = baseDate.plusDays(dateSeparate * (i + 1)).atTime(endOfDay).plusHours(7);
            Long sum = orderRepo.findForSumarySales(from, to);
            if (sum == null) sum = 0L;
            result.add(sum);
            date.add(convertDate(from.toLocalDate()) + " - " + convertDate(to.toLocalDate()));
        }
        Long sum =
            orderRepo.findForSumarySales(startDate.plusDays(dateSeparate * 9).atTime(startOfDay).plusHours(7),
                endDate.atTime(endOfDay).plusHours(7));

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
            LocalDateTime from = baseDate.plusDays(dateSeparate * i + 1).atTime(startOfDay).plusHours(7);
            LocalDateTime to = baseDate.plusDays(dateSeparate * (i + 1)).atTime(endOfDay).plusHours(7);
            Long sum = orderRepo.findForSummaryRevenue(from, to);
            if (sum == null) sum = 0L;
            result.add(sum);
            date.add(convertDate(from.toLocalDate()) + " - " + convertDate(to.toLocalDate()));
        }
        Long sum = orderRepo.findForSummaryRevenue(startDate.plusDays(dateSeparate * 9).atTime(startOfDay).plusHours(7),
            endDate.atTime(endOfDay).plusHours(7));
        if (sum == null) sum = 0L;
        result.add(sum);
        date.add(convertDate(startDate.plusDays(dateSeparate * 9)) + " - " + convertDate(endDate));
        return Map.of("labels", date, "datasets", List.of(Map.of("label", "Revenue", "data", result)));
    }

    // convert yyyy-MM-dd to dd/MM/yy
    public String convertDate(LocalDate date) {
        String[] dateArr = date.toString().split("-");
        return dateArr[2].substring(0, 2) + "/" + dateArr[1] + "/" + dateArr[0].substring(2);
    }

}
