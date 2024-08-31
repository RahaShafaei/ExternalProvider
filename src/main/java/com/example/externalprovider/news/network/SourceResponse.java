package com.example.externalprovider.news.network;

import com.example.externalprovider.news.models.Article;
import com.example.externalprovider.news.models.Source;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class SourceResponse  extends ResponseType{
    private List<Source> sources;
}
