package com.example.externalprovider.news.network;

import com.example.externalprovider.news.models.Article;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ArticleResponse extends ResponseType{
    private List<Article> articles;
}
