package com.aborob.sample.webpageanalyzer;

import com.aborob.sample.webpageanalyzer.entity.AnalysisSingleResult;
import com.aborob.sample.webpageanalyzer.exception.WebpageAnalyzerException;
import com.aborob.sample.webpageanalyzer.helper.HtmlDocumentParser;
import com.aborob.sample.webpageanalyzer.service.WebpageAnalyzerService;
import com.aborob.sample.webpageanalyzer.service.WebpageAnalyzerServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.LinkedHashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebpageAnalyzerServiceTest {

	@Autowired
	private WebpageAnalyzerService webpageAnalyzerService;

	@MockBean
	private HtmlDocumentParser htmlDocumentParser;

	/**
	 * This is a basic testing function that tests WebpageAnalyzer Service.
	 * It mocks the remote call so the service is provided with pre parsed document
	 * based on the HTML string var below.
	 * Based on the returned analysis, test will verify information against what
	 * expected from the provided HTML.
	 *
	 * @throws Exception
	 */
	@Test
	public void htmlDocumentAnalyzingTest() throws Exception {

		// Parse test HTML
		Document document = Jsoup.parse(TEST_HTML);
		assertThat(document).isNotNull();

		// Mock htmlDocumentParser
		Mockito.when(this.htmlDocumentParser.getRemoteDocument(any(String.class)))
				.thenReturn(Optional.of(document));

		// Call service and check the result set
		Optional<LinkedHashSet<AnalysisSingleResult>> analysisSingleResults = null;
		try {
			analysisSingleResults =
					this.webpageAnalyzerService.analyseWebpage(
							TEST_URL, WebpageAnalyzerServiceImpl.customCriteria(TEST_URL));
		} catch (WebpageAnalyzerException e) {
			e.printStackTrace();
		}
		assertThat(analysisSingleResults).isNotNull();
		assertThat(analysisSingleResults.isPresent()).isTrue();

		// Verify results set data based on the provided HTML
		LinkedHashSet<AnalysisSingleResult> analysisSingleResultsSet = analysisSingleResults.get();

		assertThat(analysisSingleResultsSet.size()).isEqualTo(5);

		assertThat(analysisSingleResultsSet.contains(
				new AnalysisSingleResult("HTML Version", "HTML-5")))
				.isTrue();
		assertThat(analysisSingleResultsSet.contains(
				new AnalysisSingleResult("Page Title", "My test")))
				.isTrue();
		assertThat(analysisSingleResultsSet.contains(
				new AnalysisSingleResult("Headings", "h1:1,h2:1,h3:1")))
				.isTrue();
		assertThat(analysisSingleResultsSet.contains(
				new AnalysisSingleResult("Number of links", "Local:2,External:3")))
				.isTrue();
		assertThat(analysisSingleResultsSet.contains(
				new AnalysisSingleResult("Is Login", "Yes")))
				.isTrue();
	}

	private static final String TEST_URL = "http://www.aborob.com/fake";

	private static final String TEST_HTML = "\n" +
			"<!DOCTYPE html>\n" +
			"<html lang=\"en\">\n" +
			"  <head>\n" +
			"    <meta charset=\"utf-8\">\n" +
			"  <link rel=\"dns-prefetch\" href=\"https://xxzztt.com\">\n" +
			"  <link rel=\"dns-prefetch\" href=\"http://www.aborob.com/fake/anotherFake\">\n" +
			"  <link rel=\"dns-prefetch\" href=\"http://www.aborob.com/anotherFake\">\n" +
			"  <link rel=\"dns-prefetch\" href=\"https://avatars1.fakedomain.com\">\n" +
			"  <link rel=\"dns-prefetch\" href=\"https://user-images.ccccdddd.com/\">\n" +
			"  <title>My test</title>\n" +
			"  </head>\n" +
			"\n" +
			"  <body>\n" +
			"    \n" +
			"\n" +
			"<form accept-charset=\"UTF-8\" action=\"/session\" method=\"post\">" +
			"   <div>\n" +
			"        <h1>h1 head</h1>\n" +
			"   </div>\n" +
			"\n" +
			"   <div>\n" +
			"        <h2>h2 head</h2>\n" +
			"   </div>\n" +
			"\n" +
			"\n" +
			"      <div>\n" +
			"\n" +
			"        <label for=\"login_field\">\n" +
			"          Username or email address\n" +
			"        </label>\n" +
			"        <input id=\"login_field\" name=\"login\" type=\"text\" />\n" +
			"\n" +
			"        <input id=\"password\" name=\"password\" type=\"password\" />\n" +
			"\n" +
			"        <input name=\"commit\" type=\"submit\" value=\"Sign in\" />\n" +
			"      </div>\n" +
			"</form>\n" +
			"\n" +
			"      <p class=\"create-account-callout mt-3\">\n" +
			"        <h3>h3 head</h3>\n" +
			"      </p>\n" +
			"\n" +
			"  </body>\n" +
			"</html>\n" +
			"\n";
}
