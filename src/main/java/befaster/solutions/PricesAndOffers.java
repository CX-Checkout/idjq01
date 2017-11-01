package befaster.solutions;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singleton;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;

public class PricesAndOffers {
	
	private final Map<String, Integer> prices;
	private final Set<MultiBuy> offers;
	private final List<Deduct> deductions;
	
	public PricesAndOffers(Map<String, Integer> prices, Set<MultiBuy> offers, List<Deduct> deductions) {
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
	
	public List<MultiBuy> offers() {
		ArrayList<MultiBuy> list = new ArrayList<>(offers.stream().collect(toList()));
		Collections.sort(list, (a, b) -> b.quant - a.quant);
		return list;
	}
	
	public static class MultiBuy {
		public final Set<String> items;
		public final int quant;
		public final int cost;
		
		public MultiBuy(String item, int quant, int cost) {
			this(singleton(item), quant, cost);
		}
		
		public MultiBuy(Set<String> items, int quant, int cost) {
			this.items = items;
			this.quant = quant;
			this.cost = cost;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) {
				return true;
			}
			if(o == null || getClass() != o.getClass()) {
				return false;
			}
			
			MultiBuy multiBuy = (MultiBuy) o;
			
			if(quant != multiBuy.quant) {
				return false;
			}
			return items != null ? items.equals(multiBuy.items) : multiBuy.items == null;
			
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
