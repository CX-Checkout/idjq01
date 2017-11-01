package befaster.solutions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;

public class App {
	private static class MultiBuy {
		public final int quant;
		public final int cost;
		
		public MultiBuy(int quant, int cost) {
			this.quant = quant;
			this.cost = cost;
		}
	}
	
	private static final Map<String, MultiBuy> offers = new HashMap<>();
	static {
		offers.put("A", new MultiBuy(3, 130));
		offers.put("B", new MultiBuy(2, 45));
	}
	
	private static final Map<String, Integer> prices = new HashMap<>();
	static {
		prices.put("A", 50);
		prices.put("B", 30);
		prices.put("C", 20);
		prices.put("D", 15);
	}
	
	public static int checkout(String items) {
		Map<String, Integer> counts = countProducts(items);
		
		if(!prices.keySet().containsAll(counts.keySet())) {
			return -1;
		}
		
		int sum = 0;
		for(Map.Entry<String, Integer>entry : counts.entrySet()) {
			String item = entry.getKey();
			int count = entry.getValue();
			Optional<MultiBuyResult> result = doMultiBuy(item, count);
			if(result.isPresent()) {
				MultiBuyResult r = result.get();
				sum += r.cost;
				count = r.remainder;
			}
			
			sum += count * prices.get(item);
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
	}
	
	private static Optional<MultiBuyResult> doMultiBuy(String item, int count) {
		MultiBuy offer = offers.get(item);
		if(offer == null) {
			return empty();
		}
		if(offer.quant > count) {
			return empty();
		}
		
		int cost = offer.cost * (count / offer.quant);
		int rem = count % offer.quant;
		return Optional.of(new MultiBuyResult(cost, rem));
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
