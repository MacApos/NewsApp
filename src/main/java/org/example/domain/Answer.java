package org.example.domain;

import java.util.List;
import java.util.Map;

public class Answer {
    private String _type;
    private String readLink;
    private int totalEstimatedMatches;
    private Map<String, String> queryContext;
    private List<Value> value;
    private Object sort;

    public Answer() {
    }

    public Answer(String _type, String readLink, int totalEstimatedMatches, Map<String, String> queryContext,
                  List<Value> value, Object sort) {
        this._type = _type;
        this.readLink = readLink;
        this.totalEstimatedMatches = totalEstimatedMatches;
        this.queryContext = queryContext;
        this.value = value;
        this.sort = sort;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String getReadLink() {
        return readLink;
    }

    public void setReadLink(String readLink) {
        this.readLink = readLink;
    }

    public int getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    public void setTotalEstimatedMatches(int totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }

    public Map<String, String> getQueryContext() {
        return queryContext;
    }

    public void setQueryContext(Map<String, String> queryContext) {
        this.queryContext = queryContext;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }
}
