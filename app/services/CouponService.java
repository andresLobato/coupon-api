package services;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import models.CouponRequest;
import models.CouponResponse;
import models.Item;
import org.apache.commons.lang3.tuple.Pair;
import org.paukov.combinatorics3.Generator;
import org.springframework.stereotype.Service;
import play.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
public class CouponService {

    private final MLAService mlaService;

    @Inject
    public CouponService(MLAService mlaService) {
        this.mlaService = mlaService;
    }

    /**
     * @param resource
     * @return a Future of CouponResponse
     */
    public CompletionStage<CouponResponse> maximizeCoupon(CouponRequest resource) {
        return supplyAsync(() -> {
            List<Item> items = mlaService.getItems(resource.getIdsWithoutDuplicates());
            if(!items.isEmpty()){
                Item item = findingLuckyOne(resource.getAmount(), items);
                if (item != null) {
                    Logger.info("I found the lucky One item : " + item.getId() + " - " + item.getPrice());
                    return new CouponResponse(item.getPrice(), Lists.newArrayList(item.getId()));
                } else {
                    return processCombinations(resource.getAmount(), items);
                }
            }else{
                Logger.warn("from the api returned without Items");
                return new CouponResponse();
            }
        });
    }

    /**
     * @param amount
     * @param items
     * @return
     */
    private CouponResponse processCombinations(float amount, List<Item> items) {
        Logger.info("Start to process combinations...");
        Map<String, Float> parameters = items.stream()
                .filter(i -> i.getPrice() <= amount)
                .collect(Collectors.toMap(Item::getId, Item::getPrice));
        List<String> calculate = calculate(parameters, amount);
        Float total = getTotalOfItems(parameters, calculate);
        return new CouponResponse(total, calculate);
    }

    /**
     *
     * From the list of itemIds that meets the condition of maximizing the coupon,
     * I search the map that contains all the items, the prices necessary to show the total of the combination.
     *
     * @param parameters
     * @param calculate
     * @return sum of items price.
     */
    private Float getTotalOfItems(Map<String, Float> parameters, List<String> calculate) {
        AtomicReference<Float> total = new AtomicReference<>(0f);
        calculate.forEach(id -> {
            total.updateAndGet(v -> v + parameters.getOrDefault(id, 0f));
        });
        return total.get();
    }

    /**
     * I am looking to see if an item meets the total requested. Thus avoiding the process of
     * combining and searching for the closest value.
     *
     * @param amount
     * @param items
     * @return an item if I find it.
     * null otherwise.
     */
    private Item findingLuckyOne(float amount, List<Item> items) {
        return items.stream()
                .filter(item -> amount == item.getPrice())
                .findFirst()
                .orElse(null);
    }

    /**
     * @param items
     * @param amount
     * @return a List of ids with maximize the amount
     */
    public List<String> calculate(Map<String, Float> items, Float amount) {
        Logger.info("Start to calculate combinations");
        List<Pair<Float, List<Map.Entry<String, Float>>>> subsets = generateSubSets(amount, items.entrySet());
        List<Pair<Float, List<Map.Entry<String, Float>>>> sorted = subsets.stream()
                .sorted(Comparator.comparing(p -> p.getLeft()))
                .collect(Collectors.toList());
        //sorted.stream().forEach(System.out::println);
        return sorted.get(sorted.size() - 1).getRight().stream().map(e -> e.getKey()).collect(Collectors.toList());
    }

    /**
     * Is in charge of combining all the elements in the list.
     * It also adds the content of each of the combinations and filters those that exceed the amount.
     * <p>
     * Parameters:
     * amount: 200f
     * list: [(MLA1=80.0),(MLA2=90.0),(MLA3=100.0)]
     * <p>
     * Return:
     * (80.0,[MLA1=80.0])
     * (90.0,[MLA2=90.0])
     * (100.0,[MLA3=100.0])
     * (170.0,[MLA1=80.0, MLA2=90.0])
     * (180.0,[MLA1=80.0, MLA3=100.0])
     * (190.0,[MLA2=90.0, MLA3=100.0])
     *
     * @param amount
     * @param parametersFilter
     * @return a list of combinations.
     */
    private List<Pair<Float, List<Map.Entry<String, Float>>>> generateSubSets(Float amount, Set<Map.Entry<String, Float>> parametersFilter) {
        return Generator
                .subset(parametersFilter)
                .simple()
                .stream()
                .map(entry -> Pair.of(entry.stream().map(e -> e.getValue()).reduce(0f, Float::sum), entry))
                .filter(p -> p.getLeft() <= amount)
                .collect(Collectors.toList());
    }


}
