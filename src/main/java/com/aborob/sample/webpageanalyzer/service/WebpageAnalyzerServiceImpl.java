package com.aborob.sample.webpageanalyzer.service;

import com.aborob.sample.webpageanalyzer.entity.AnalysisSingleResult;
import com.aborob.sample.webpageanalyzer.exception.WebpageAnalyzerException;
import com.aborob.sample.webpageanalyzer.helper.HtmlDocumentParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("webpageAnalyzerService")
public class WebpageAnalyzerServiceImpl implements WebpageAnalyzerService {

	private static final Logger log = LoggerFactory.getLogger(WebpageAnalyzerServiceImpl.class);

	@Autowired
	private HtmlDocumentParser htmlDocumentParser;

	@Override
	public Optional<LinkedHashSet<AnalysisSingleResult>> analyseWebpage(
			String url, List<Function<Document, AnalysisSingleResult>> criteria) throws WebpageAnalyzerException {

		Optional<Document> documentOptional = htmlDocumentParser.getRemoteDocument(url);

		if (!documentOptional.isPresent()) {
			throw new WebpageAnalyzerException("Wrong URL, timeout, malformed document, or something went wrong!");
		}
		Document document = documentOptional.get();

		return Optional.ofNullable(this.applyCriteria(document, criteria));
	}

	/**
	 * Apply analysis criteria on a given document and return the results.
	 *
	 * @param document
	 * @param criteria
	 * @return
	 */
	protected LinkedHashSet<AnalysisSingleResult> applyCriteria(
			Document document, List<Function<Document, AnalysisSingleResult>> criteria) {

		LinkedHashSet<AnalysisSingleResult> analysisSingleResults = null;
		try {

			analysisSingleResults = criteria
					.parallelStream()
					.map(function -> function.apply(document))
					.collect(Collectors.toCollection(LinkedHashSet::new));

		} catch (Exception e) {
			log.error("Something went wrong or data not found");
		}
		return analysisSingleResults;
	}

	/**
	 * This is the required analysis criteria.
	 * We could have created a component that generates criteria like Hibernate's one
	 * or even in generic way. Here for time and convenience,
	 * I just placed it here as static simple function.
	 */
	public static final List<Function<Document, AnalysisSingleResult>> customCriteria(String url) {

		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		final String domain = uri == null ? url : uri.getHost();

		return Arrays.asList(

				// HTML Version
				(Document doc) -> {
					List<Node> nodes = doc.childNodes();
					String version = nodes.stream()
							.filter(node -> node instanceof DocumentType)
							.map(node -> {
								DocumentType documentType = (DocumentType) node;
								String htmlVersion = documentType.attr("publicid");
								return htmlVersion.isEmpty() ? "HTML-5" : htmlVersion;
							})
							.collect(Collectors.joining());
					return new AnalysisSingleResult("HTML Version", version);
				},

				// Page Title
				(Document doc) -> {
					return new AnalysisSingleResult("Page Title", doc.title());
				},

				// Grouped Numbers of Headings
				(Document doc) -> {
					Elements hTags = doc.select("h1, h2, h3");
					int h1 = 0, h2 = 0, h3 = 0;
					for (Element element : hTags) {
						switch (element.tagName()) {
							case "h1":
								h1++;
								break;
							case "h2":
								h2++;
								break;
							case "h3":
								h3++;
								break;
						}
					}
					return new AnalysisSingleResult(
							"Headings", "h1:" + h1 + ",h2:" + h2 + ",h3:" + h3);
				},

				// Grouped Numbers of external/
				(Document doc) -> {
					int internal = 0;
					int external = 0;
					Elements links = doc.select("link[href]");
					if (links.size() > 0) {
						internal = Math.toIntExact(links.stream().filter(link -> {
							String absHref = link.attr("abs:href");
							return absHref.contains(domain);
						}).count());
						external = Math.toIntExact(links.size() - internal);
					}
					return new AnalysisSingleResult("Number of links",
							"Local:" + internal + ",External:" + external);
				},

				// Is login page (Not production ready strategy!)
				(Document doc) -> {
					Elements forms = doc.select("form");
					boolean isLogin = forms.stream().anyMatch(form -> {
						Elements inputElements =
								form.getElementsByAttributeValueContaining("name", "password");
						return inputElements.size() > 0 ? true : false;
					});
					return new AnalysisSingleResult("Is Login", (isLogin ? "Yes" : "No"));
				}
		);
	}
}
