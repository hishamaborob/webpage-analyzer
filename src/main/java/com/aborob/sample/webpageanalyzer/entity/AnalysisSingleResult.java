package com.aborob.sample.webpageanalyzer.entity;

/**
 * Immutable entity used to store unique analysis.
 */
public class AnalysisSingleResult {

    private final String analysis;
    private final String results;


    public AnalysisSingleResult(String analysis, String results) {
        this.analysis = analysis;
        this.results = results;
    }

    public String getAnalysis() {
        return analysis;
    }

    public String getResults() {
        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalysisSingleResult that = (AnalysisSingleResult) o;

        if (analysis != null ? !analysis.equals(that.analysis) : that.analysis != null) return false;
        return results != null ? results.equals(that.results) : that.results == null;
    }

    @Override
    public int hashCode() {
        int result = analysis != null ? analysis.hashCode() : 0;
        result = 31 * result + (results != null ? results.hashCode() : 0);
        return result;
    }
}
