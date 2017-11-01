package befaster.solutions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

public class App {
	public static int checkout(String items) {
		Map<String, Integer> counts = countProducts(items);
		
		PricesAndOffers prices = PriceFactory.obtain();
		
		if(!prices.hasAll(counts.keySet())) {
			return -1;
		}
		
		prices.deductions().forEach((d) -> d.deduct(counts));
		
		int sum = 0;
		sum += doMultiBuying(counts);
		
		for(Map.Entry<String, Integer>entry : counts.entrySet()) {
			String item = entry.getKey();
			int count = entry.getValue();
			
			sum += count * prices.price(item);
		}
		return sum;
	}
	
	private static class MultiBuyResult {
		final int cost;
		final List<Change> changes;
		
		public MultiBuyResult(int cost, List<Change> changes) {
			this.cost = cost;
			this.changes = changes;
		}
		
		public static MultiBuyResult empty() {
			return new MultiBuyResult(0, emptyList());
		}
	}
	
	private static final class Change {
		final String item;
		final int rem;
		
		public Change(String item, int rem) {
			this.item = item;
			this.rem = rem;
		}
	}
	
	private static int doMultiBuying(Map<String, Integer> productCounts) {
		
		List<PricesAndOffers.MultiBuy> found = PriceFactory.obtain().offers();
		
		int sum = 0;
		for(PricesAndOffers.MultiBuy buy: found) {
			MultiBuyResult result;
			do {
				result = doMultiBuy(productCounts, buy);
				sum += result.cost;
				for(Change change: result.changes) {
					productCounts.put(change.item, change.rem); // TODO: this in doMultiBuy ?
				}
			} while(result.changes.size() > 0);
		}
		return sum;
	}
	
	private static MultiBuyResult doMultiBuy(Map<String, Integer> count, PricesAndOffers.MultiBuy offer) {
		List<Map.Entry<String, Integer>> valid = count.entrySet().stream().filter((entry) -> offer.items.contains(entry.getKey())).collect(toList());
		int available = valid.stream().map(Map.Entry::getValue).collect(summingInt(Integer::intValue));
		if(available < offer.quant) {
			return MultiBuyResult.empty();
		}
		
		List<Change> changes = new ArrayList<>();
		valid.sort((a, b) -> b.getValue() - a.getValue());
		Iterator<Map.Entry<String, Integer>> items = valid.iterator();
		int found = 0;
		Map.Entry<String, Integer> item = items.next();
		while(found < offer.quant) {
			int ofThese = Math.min(item.getValue(), offer.quant - found);
			changes.add(new Change(item.getKey(), item.getValue() - ofThese));
			found += ofThese;
			
			if(items.hasNext()) {
				item = items.next();
			} else if(found < offer.quant) {
				return MultiBuyResult.empty();
			}
		}
		
		int cost = offer.cost;
		return new MultiBuyResult(cost, changes);
	}
	
	private static Map<String, Integer> countProducts(String items) {
		// this sucks, can't we do this with streams ?
		Map<String, Integer> counts = new HashMap<>();
		char[] chars = items.toCharArray();
		for(int i = 0; i< chars.length; i++) {
			String c = String.valueOf(chars[i]);
			if(counts.containsKey(c)) {
				counts.put(c, counts.get(c) + 1);
			} else {
				counts.put(c, 1);
			}
		}
		return counts;
	}
}
