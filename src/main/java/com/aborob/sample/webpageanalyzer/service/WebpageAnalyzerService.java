package com.aborob.sample.webpageanalyzer.service;

import com.aborob.sample.webpageanalyzer.entity.AnalysisSingleResult;
import com.aborob.sample.webpageanalyzer.exception.WebpageAnalyzerException;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.function.Function;

public interface WebpageAnalyzerService {

	/**
	 * Parse document and return simple analysis based on provided criteria.
	 * Based on Opened-Closed principle, I like to keep this function generic
	 * and provide it with list of Lambdas,
	 * so we can add/remove analysis in the future without modifying this service.
	 *
	 * @param url
	 * @param criteria
	 * @return
	 * @throws WebpageAnalyzerException validation, timeout, or other error.
	 */
	public Optional<LinkedHashSet<AnalysisSingleResult>> analyseWebpage(
			String url, List<Function<Document, AnalysisSingleResult>> criteria) throws WebpageAnalyzerException;
}
