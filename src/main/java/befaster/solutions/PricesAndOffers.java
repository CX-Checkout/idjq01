package befaster.solutions;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class PricesAndOffers {
	
	private final Map<String, Integer> prices;
	private final Map<String, List<MultiBuy>> offers;
	private final List<Deduct> deductions;
	
	public PricesAndOffers(Map<String, Integer> prices, Map<String, List<MultiBuy>> offers, List<Deduct> deductions) {
		this.prices = prices;
		this.offers = offers;
		this.deductions = deductions;
	}
	
	public Integer price(String item) {
		return prices.get(item);
	}
	
	public boolean hasAll(Set<String> items) {
		return prices.keySet().containsAll(items);
	}
	
	public Stream<Deduct> deductions() {
		return deductions.stream();
	}
	
	public List<MultiBuy> offers(String item) {
		return offers.get(item);
	}
	
	public static class MultiBuy {
		public final int quant;
		public final int cost;
		
		public MultiBuy(int quant, int cost) {
			this.quant = quant;
			this.cost = cost;
		}
	}
	
	public static final class Deduct {
		private final String item;
		public final int number;
		private final String freeItem;
		// assume one item free
		private final int freeCount = 1;
		
		public Deduct(String item, int number, String free) {
			this.item = item;
			this.number = number;
			this.freeItem = free;
		}
		
		public boolean isMultiBuyReally() {
			return item.equals(freeItem);
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
			int won = (found / number) * freeCount;
			
			Integer toPay = Math.max((overpaid - won), 0);
			items.put(freeItem, toPay);
		}
	}
	
}
