package org.example.mapper;

import org.example.domain.Image;
import org.example.domain.Value;
import org.example.domain.dto.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "contentUrl", source = "image", qualifiedByName = "imageToContentUrl")
    Article valueToArticle(Value value);

    @Named("imageToContentUrl")
    static String imageToContentUrl(Image image) {
        if(image!=null){
            return image.getContentUrl();
        }
        return null;
    }

}
