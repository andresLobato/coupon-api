package examples;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.paukov.combinatorics3.Generator;


import java.util.*;
import java.util.stream.Collectors;


public class JavaApiTest {

    @Test
    public void testSubSets() {

        Float amount = 30.0f;

        Map<String, Float> parameters = new HashMap<String,Float>();
        parameters.put("MLA1",10.0f);
        parameters.put("MLA2",18.0f);
        parameters.put("MLA3",40.0f);
        parameters.put("MLA4",15.0f);


        List<Map.Entry<String, Float>> parametersFilter = parameters.entrySet()
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

    }



}
