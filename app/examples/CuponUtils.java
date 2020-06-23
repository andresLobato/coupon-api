package examples;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CuponUtils {

	public List<String> calculate(Map<String, Float> items, Float amount){
		if(items.isEmpty())
			return new ArrayList<String>();
		Map<Float, List<String>> results = new LinkedHashMap<Float, List<String>>();
		if(items.size() > 1)
			nextIteration(items, amount, results);
		else
			resolveUniqueElement(items, amount, results);
		List<Float> partialResults = new ArrayList<Float>(results.keySet());
		Collections.sort(partialResults);
		System.out.println("Mejor resultado -> Valor maximo: "+amount+"\nMejor valor obtenido: "+(partialResults.isEmpty()? "0": partialResults.get(partialResults.size()-1))+", Elementos: "+(partialResults.isEmpty()?"[]":results.get(partialResults.get(partialResults.size()-1))));
		System.out.println("================================================================================================");
		return partialResults.size() > 0 ? results.get(partialResults.get(partialResults.size()-1)) : new ArrayList<String>();
	}

	private void resolveUniqueElement(Map<String, Float> items, Float amount, Map<Float, List<String>> results) {
		String elem = items.keySet().iterator().next();
		Float actualValue = items.get(elem);
		if(actualValue <= amount) {
			results.put(actualValue, Arrays.asList(elem));
		}
	}

	private void nextIteration(Map<String, Float> items, Float amount, Map<Float, List<String>> results) {
		if(items.isEmpty())
			return;
		String elem = items.keySet().iterator().next();
		Float actualValue = items.get(elem);
		items.remove(elem);
		calculateElementIteration(new LinkedHashMap<String, Float>(items), amount, results, elem, actualValue);
		nextIteration(items, amount, results);
	}

	private void calculateElementIteration(Map<String, Float> items, Float amount, Map<Float, List<String>> results, String elem, Float actualValue) {
		if(items.isEmpty())
			return;
		Entry<Float, List<String>> pair = calculateIteration(items, actualValue, elem, amount);
		results.put(pair.getKey(), pair.getValue());
		System.out.println("suma "+pair.getKey()+", Elementos: "+pair.getValue());
		String elemToRemove = items.keySet().iterator().next();
		items.remove(elemToRemove);
		calculateElementIteration(items, amount, results, elem, actualValue);
	}

	private Entry<Float, List<String>> calculateIteration(Map<String, Float> items, Float previousValue, String previousKey, Float amount) {
		Float partialSum = previousValue;
		List<String> maxItems = new ArrayList<String>();
		maxItems.add(previousKey);
		for (String elem : items.keySet()) {
			if(partialSum + items.get(elem) <= amount) {
				partialSum += items.get(elem);
				maxItems.add(elem);
			}else
				break;
		}
		return new AbstractMap.SimpleEntry<Float, List<String>>(partialSum, maxItems);
	}

}
