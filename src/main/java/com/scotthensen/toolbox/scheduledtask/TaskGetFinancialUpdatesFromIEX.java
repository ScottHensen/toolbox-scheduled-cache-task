package com.scotthensen.toolbox.scheduledtask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scotthensen.toolbox.scheduledtask.cache.Quote;
import com.scotthensen.toolbox.scheduledtask.domain.CompanyInfo;
import com.scotthensen.toolbox.scheduledtask.domain.StockQuote;
import com.scotthensen.toolbox.scheduledtask.repository.QuoteRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskGetFinancialUpdatesFromIEX<K,V> {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS Z");
	private static final String IEX_URL        = "https://api.iextrading.com/1.0";
	private static final String TESLA_QUOTE    = "/stock/tsla/quote";
	private static final String TESLA_COMPANY  = "/stock/tsla/company";
	
	@Autowired
	private RedisTemplate<String, Object> template;
	
	@Autowired
	private QuoteRepository quoteRepository;
	
	@Scheduled(fixedDelay = 10000)
	public void getStockQuote() throws JsonProcessingException {
		log.info("The time is now {}", dateFormat.format(new Date()), " ... start getStockQuote");
		
		RestTemplate restTemplate = new RestTemplate();
		
		StockQuote quote = restTemplate.getForObject(IEX_URL+TESLA_QUOTE, StockQuote.class);
		log.info("\n>>> quote= " + quote.toString());

		quoteRepository.save(new Quote(quote));
		
		Optional<Quote> cachedQuote = quoteRepository.findBySymbol(quote.getSymbol());
		cachedQuote.ifPresent(q -> log.info("\n>>> cached quote= " + q.toString()));
		
		log.info("The time is now {}", dateFormat.format(new Date()), " ... stop  getStockQuote");		
	}

	@Scheduled(cron = "0/30 * * * * *")
	public void getCompanyInfo() {
		log.info("The time is now {}", dateFormat.format(new Date()), " ... start getStockQuote");
		
		RestTemplate restTemplate = new RestTemplate();
		CompanyInfo company = restTemplate.getForObject(IEX_URL+TESLA_COMPANY, CompanyInfo.class);
		log.info(company.toString());
		
		log.info("The time is now {}", dateFormat.format(new Date()), " ... stop  getStockQuote");		
	}

}
