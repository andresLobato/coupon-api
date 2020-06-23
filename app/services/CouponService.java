package services;

import com.google.inject.Inject;
import models.CouponRequest;
import models.CouponResponse;
import models.Item;
import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Service;
import play.libs.concurrent.HttpExecutionContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

@Service
public class CouponService {

    private final HttpExecutionContext ec;
    private final MLAService mlaService;

    @Inject
    public CouponService(HttpExecutionContext ec, MLAService mlaService) {
        this.ec = ec;
        this.mlaService = mlaService;
    }

    public CompletionStage<CouponResponse> maximizeCoupon(CouponRequest resource) {
        return  CompletableFuture.supplyAsync(() -> {
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

    public List<String> calculate(Map<String, Float> items, Float amount){

        List<Map.Entry<String, Float>> parametersFilter = items.entrySet()
                .stream()
                .filter(es -> es.getValue() <= amount)
                .collect(Collectors.toList());

        List<Pair<Float, List<Map.Entry<String, Float>>>> subsets = Generator
                .subset(parametersFilter)
                .simple()
                .stream()
                .map(entry -> Pair.of(entry.stream().map(e -> e.getValue()).reduce(0f, Float::sum), entry))
                .filter(p -> p.getLeft() <= amount)
                .collect(Collectors.<Pair<Float, List<Map.Entry<String, Float>>>>toList());

        List<Pair<Float, List<Map.Entry<String, Float>>>> sorted = subsets.stream()
                .sorted(Comparator.comparing(p -> p.getLeft()))
                .collect(Collectors.toList());

        sorted.stream().forEach(System.out::println);

        return sorted.get(sorted.size()-1).getRight().stream().map(e -> e.getKey()).collect(Collectors.toList());
    }


}
