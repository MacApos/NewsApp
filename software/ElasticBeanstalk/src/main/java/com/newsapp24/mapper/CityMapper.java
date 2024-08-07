package com.newsapp24.mapper;

import com.newsapp24.domain.Answer;
import com.newsapp24.domain.dto.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(componentModel = "spring", uses = ArticleMapper.class)
public interface CityMapper {
    @Mapping(target = "name", source = "queryContext", qualifiedByName = "queryToName")
    @Mapping(target = "articles", source = "value")
    City answerToCity(Answer answer);

    @Named("queryToName")
    static String queryToName(Map<String, String> queryContext) {
        return queryContext.get("originalQuery");
    }
}
