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

import com.google.common.io.LineReader;


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
		LineReader reader = new LineReader(new BufferedReader(new InputStreamReader(PriceFactory.class.getResourceAsStream("/befaster/solutions/table.txt"))));
		
		reader.readLine(); // first line, header
		reader.readLine(); // second line, more header
		reader.readLine(); // third line, moremore header
		
		Map<String, Integer> prices = new HashMap<>();
		Map<String, List<PricesAndOffers.MultiBuy>> multiBuys = new HashMap<>();
		List<PricesAndOffers.Deduct> deductions = new ArrayList();
		String line;
		while((line = reader.readLine()) != null && line.matches((".*[A-Z].*"))) {
			String[] split = line.split("\\|");
			
			String item = split[1].trim();
			int cost = Integer.valueOf(split[2].trim());
			String offerString = split[3].trim();
			
			// TODO: split up
			List<PricesAndOffers.MultiBuy> multi = new ArrayList<>();
			if(!offerString.isEmpty()) {
				String[] offers = offerString.split(",");
				
				for(String offer: offers) {
					offer = offer.trim();
					if(offer.isEmpty()) {
						continue;
					}
					asMultiBuy(item, offer).ifPresent(multi::add);
					Optional<PricesAndOffers.Deduct> deduct = asDeduct(offer);
					if(deduct.isPresent()) {
						PricesAndOffers.Deduct d = deduct.get();
						if(d.isMultiBuyReally()) {
							int multiAmount = d.number + 1;
							multi.add(new PricesAndOffers.MultiBuy(multiAmount, cost * d.number));
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
	
	private static Optional<PricesAndOffers.MultiBuy> asMultiBuy(String item, String offer) {
		// 3A for 130
		String[] split = offer.split(" for ");
		if(split.length < 2) {
			return empty();
		}
		
		String countStr = split[0].replace(item, "");
		return Optional.of(new PricesAndOffers.MultiBuy(Integer.valueOf(countStr), Integer.valueOf(split[1])));
	}
	
	private static Optional<PricesAndOffers.Deduct> asDeduct(String offer) {
		// 2E get one B free
		String[] split = offer.split(" get one " );
		if(split.length < 2) {
			return empty();
		}
		
		String countStr = split[0].replaceAll("[A-Z]", "");
		String itemStr = split[0].replaceFirst(countStr, "");
		String freeStr = split[1].replaceAll("free", "").trim();
		return Optional.of(new PricesAndOffers.Deduct(itemStr, Integer.valueOf(countStr), freeStr));
	}
}