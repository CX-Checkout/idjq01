package befaster.solutions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class App {	
	public static int checkout(String items) {
		Map<String, Integer> counts = countProducts(items);
		
		PricesAndOffers prices = PriceFactory.obtain();
		
		if(!prices.hasAll(counts.keySet())) {
			return -1;
		}
		
		prices.deductions().forEach((d) -> d.deduct(counts));
		
		int sum = 0;
		for(Map.Entry<String, Integer>entry : counts.entrySet()) {
			String item = entry.getKey();
			int count = entry.getValue();
			MultiBuyResult result = doMultiBuying(item, count);
			sum += result.cost;
			count = result.remainder;
			
			sum += count * prices.price(item);
		}
		return sum;
	}
	
	private static class MultiBuyResult {
		final int cost;
		final int remainder;
		
		public MultiBuyResult(int cost, int rem) {
			this.cost = cost;
			this.remainder = rem;
		}
		
		public static MultiBuyResult empty(int rem) {
			return new MultiBuyResult(0, rem);
		}
	}
	
	private static MultiBuyResult doMultiBuying(String item, int count) {
		
		List<PricesAndOffers.MultiBuy> found = PriceFactory.obtain().offers(item);
		if(found == null) {
			return MultiBuyResult.empty(count);
		}
		
		int sum = 0;
		for(PricesAndOffers.MultiBuy buy: found) {
			MultiBuyResult result = doMultiBuy(count, buy);
			sum += result.cost;
			count = result.remainder;
		}
		
		return new MultiBuyResult(sum, count);
	}
	
	private static MultiBuyResult doMultiBuy(int count, PricesAndOffers.MultiBuy offer) {
		if(offer.quant > count) {
			return MultiBuyResult.empty(count);
		}
		
		int cost = offer.cost * (count / offer.quant);
		int rem = count % offer.quant;
		return new MultiBuyResult(cost, rem);
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
