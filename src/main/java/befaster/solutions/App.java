package befaster.solutions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class App {
	private static class MultiBuy {
		public final int quant;
		public final int cost;
		
		public MultiBuy(int quant, int cost) {
			this.quant = quant;
			this.cost = cost;
		}
	}
	
	private static final Map<String, List<MultiBuy>> offers = new HashMap<>();
	static {
		// offers ordered from highest quant to lowest
		offers.put("A", asList(new MultiBuy(5, 200), new MultiBuy(3, 130)));
		offers.put("B", singletonList(new MultiBuy(2, 45)));
	}
	
	private static final Map<String, Integer> prices = new HashMap<>();
	static {
		prices.put("A", 50);
		prices.put("B", 30);
		prices.put("C", 20);
		prices.put("D", 15);
		prices.put("E", 40);
	}
	
	private static final class Deduct {
		private final String item;
		private final int number;
		private final String freeItem;
		// assume one item free
		private final int freeCount = 1;
		
		public Deduct(String item, int number, String free) {
			this.item = item;
			this.number = number;
			this.freeItem = free;
		}
		
		public void deduct(Map<String, Integer> items) {
			Integer found = items.get(item);
			if(found == null || found < number) {
				return;
			}
			
			Integer overpaid = items.get(freeItem);
			if(overpaid == null) {
				return;
			}
			int won = found / number;
			
			Integer toPay = Math.max((overpaid - won), 0);
			items.put(freeItem, toPay);
		}
	}
	
	private static final List<Deduct> deductions = asList(new Deduct("E", 2, "B"));
	
	public static int checkout(String items) {
		Map<String, Integer> counts = countProducts(items);
		
		if(!prices.keySet().containsAll(counts.keySet())) {
			return -1;
		}
		
		deductions.stream().forEach((d) -> d.deduct(counts));
		
		int sum = 0;
		for(Map.Entry<String, Integer>entry : counts.entrySet()) {
			String item = entry.getKey();
			int count = entry.getValue();
			MultiBuyResult result = doMultiBuying(item, count);
			sum += result.cost;
			count = result.remainder;
			
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
		
		public static MultiBuyResult empty(int rem) {
			return new MultiBuyResult(0, rem);
		}
	}
	
	private static MultiBuyResult doMultiBuying(String item, int count) {
		List<MultiBuy> found = offers.get(item);
		if(found == null) {
			return MultiBuyResult.empty(count);
		}
		
		int sum = 0;
		for(MultiBuy buy: found) {
			MultiBuyResult result = doMultiBuy(count, buy);
			sum += result.cost;
			count = result.remainder;
		}
		
		return new MultiBuyResult(sum, count);
	}
	
	private static MultiBuyResult doMultiBuy(int count, MultiBuy offer) {
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
