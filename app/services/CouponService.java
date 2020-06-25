package services;

import com.google.inject.Inject;
import models.CouponRequest;
import models.CouponResponse;
import models.Item;
import org.apache.commons.lang3.tuple.Pair;
import org.paukov.combinatorics3.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CouponService {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    private final MLAService mlaService;

    @Inject
    public CouponService(MLAService mlaService) {
        this.mlaService = mlaService;
    }

    /**
     * @param resource
     * @return
     */
    public CompletionStage<CouponResponse> maximizeCoupon(CouponRequest resource) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> idsWithoutDuplicates = resource.getItem_ids().stream().distinct().collect(Collectors.toList());
            List<Item> items = mlaService.getItems(idsWithoutDuplicates);
            Map<String, Float> parameters = items.stream().collect(Collectors.toMap(Item::getId, Item::getPrice));
            List<String> calculate = calculate(parameters, resource.getAmount());

            AtomicReference<Float> total = new AtomicReference<>(0f);

            calculate.forEach(id -> {
                total.updateAndGet(v -> new Float((float) (v + parameters.getOrDefault(id, 0f))));
            });

            return new CouponResponse(total.get(), calculate);
        });
    }

    /**
     * @param items
     * @param amount
     * @return
     */
    public List<String> calculate(Map<String, Float> items, Float amount) {

        List<Map.Entry<String, Float>> parametersFilter = filterItems(items, amount);

        List<Pair<Float, List<Map.Entry<String, Float>>>> subsets = generateSubSets(amount, parametersFilter);

        List<Pair<Float, List<Map.Entry<String, Float>>>> sorted = subsets.stream()
                .sorted(Comparator.comparing(p -> p.getLeft()))
                .collect(Collectors.toList());

        sorted.stream().forEach(System.out::println);

        return sorted.get(sorted.size() - 1).getRight().stream().map(e -> e.getKey()).collect(Collectors.toList());
    }

    /**
     * @param items
     * @param amount
     * @return
     */
    private List<Map.Entry<String, Float>> filterItems(Map<String, Float> items, Float amount) {
        return items.entrySet()
                .stream()
                .filter(es -> es.getValue() <= amount)
                .collect(Collectors.toList());
    }

    /**
     * @param amount
     * @param parametersFilter
     * @return
     */
    private List<Pair<Float, List<Map.Entry<String, Float>>>> generateSubSets(Float amount, List<Map.Entry<String, Float>> parametersFilter) {
        return Generator
                .subset(parametersFilter)
                .simple()
                .stream()
                .map(entry -> Pair.of(entry.stream().map(e -> e.getValue()).reduce(0f, Float::sum), entry))
                .filter(p -> p.getLeft() <= amount)
                .collect(Collectors.toList());
    }


}
