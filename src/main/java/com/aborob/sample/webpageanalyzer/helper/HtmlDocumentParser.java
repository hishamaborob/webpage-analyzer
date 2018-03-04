package com.aborob.sample.webpageanalyzer.helper;

import org.jsoup.nodes.Document;

import java.util.Optional;

public interface HtmlDocumentParser {

    /**
     * Use parser to get remote html page as an object.
     *
     * @param url
     * @return Optional parsed document object
     */
    public Optional<Document> getRemoteDocument(String url);
}
