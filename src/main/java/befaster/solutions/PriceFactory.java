package befaster.solutions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.io.LineReader;

import checkout_idjq01.PricesAndOffers.Deduct;
import checkout_idjq01.PricesAndOffers.MultiBuy;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.empty;

public class PriceFactory {
	
	public static PricesAndOffers obtain() {
		try {
			return obtainWrapped();
		} catch(IOException ioe) {
			throw new RuntimeException("Java smells", ioe);
		}
	}
	
	public static PricesAndOffers obtainWrapped() throws IOException{
		// java sucks
		LineReader reader = new LineReader(new BufferedReader(new InputStreamReader(PriceFactory.class.getResourceAsStream("table.txt"))));
		
		reader.readLine(); // first line, header
		reader.readLine(); // second line, more header
		reader.readLine(); // third line, moremore header
		
		Map<String, Integer> prices = new HashMap<>();
		Map<String, List<MultiBuy>> multiBuys = new HashMap<>();
		List<Deduct> deductions = new ArrayList();
		String line;
		while((line = reader.readLine()) != null && line.matches((".*[A-Z].*"))) {
			String[] split = line.split("\\|");
			
			String item = split[1].trim();
			int cost = Integer.valueOf(split[2].trim());
			String offerString = split[3].trim();
			
			// TODO: split up
			List<MultiBuy> multi = new ArrayList<>();
			if(!offerString.isEmpty()) {
				String[] offers = offerString.split(",");
				
				for(String offer: offers) {
					offer = offer.trim();
					if(offer.isEmpty()) {
						continue;
					}
					asMultiBuy(item, offer).ifPresent(multi::add);
					Optional<Deduct> deduct = asDeduct(offer);
					if(deduct.isPresent()) {
						Deduct d = deduct.get();
						if(d.isMultiBuyReally()) {
							int multiAmount = d.number + 1;
							multi.add(new MultiBuy(multiAmount, cost * d.number));
						} else {
							deductions.add(d);
						}
					}
				}
			}
			
			if(!multi.isEmpty()) {
				Collections.sort(multi, (a,b) -> b.quant - a.quant);
				multiBuys.put(item, multi);
			}
			
			prices.put(item, cost);
		}
		
		return new PricesAndOffers(prices, multiBuys, deductions);
	}
	
	private static Optional<MultiBuy> asMultiBuy(String item, String offer) {
		// 3A for 130
		String[] split = offer.split(" for ");
		if(split.length < 2) {
			return empty();
		}
		
		String countStr = split[0].replace(item, "");
		return Optional.of(new MultiBuy(Integer.valueOf(countStr), Integer.valueOf(split[1])));
	}
	
	private static Optional<Deduct> asDeduct(String offer) {
		// 2E get one B free
		String[] split = offer.split(" get one " );
		if(split.length < 2) {
			return empty();
		}
		
		String countStr = split[0].replaceAll("[A-Z]", "");
		String itemStr = split[0].replaceFirst(countStr, "");
		String freeStr = split[1].replaceAll("free", "").trim();
		return Optional.of(new Deduct(itemStr, Integer.valueOf(countStr), freeStr));
	}
}
