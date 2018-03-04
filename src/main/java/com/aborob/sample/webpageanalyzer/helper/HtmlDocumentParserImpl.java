package com.aborob.sample.webpageanalyzer.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("htmlDocumentParser")
public class HtmlDocumentParserImpl implements HtmlDocumentParser {

	private static final Logger log = LoggerFactory.getLogger(HtmlDocumentParserImpl.class);

	private static final int TIMEOUT_MILL = 5000;

	@Override
	public Optional<Document> getRemoteDocument(String url) {

		Document document = null;

		try {
			document = Jsoup.connect(url).timeout(TIMEOUT_MILL).get();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return Optional.ofNullable(document);
	}
}
