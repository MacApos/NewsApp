package org.example.mapper;

import org.example.domain.Answer;
import org.example.domain.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface CityMapper {
    @Mapping(target = "articles", source = "value")
    @Mapping(target = "name", source = "queryContext", qualifiedByName = "queryToName")
    City answerToCity(Answer answer);

    @Named("queryToName")
    static String customAnswerToCity(Map<String, String> queryContext) {
        return queryContext.entrySet().stream().filter(e->e.getKey().equals("originalQuery")).findFirst()
                .orElseThrow(RuntimeException::new).getValue();
    }

}
