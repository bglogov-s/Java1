/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author daniel.bele
 */
public final class Article {
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private int id;
    private String title;
    private String link;
    private Author author;
    private LocalDateTime publishedDate;
    private String categoryName;
    private String articleDescription;
    private String picturePath;
    

    public Article() {
    }

    public Article(String title, String link, Author author, LocalDateTime publishedDate, String categoryName, String articleDescription, String picturePath) {
        this.title = title;
        this.link = link;
        this.author = author;
        this.publishedDate = publishedDate;
        this.categoryName = categoryName;
        this.articleDescription = articleDescription;
        this.picturePath = picturePath;
    }
    
    
    
    public Article(int id, String title, String link, Author author, LocalDateTime publishedDate, String categoryName, String articleDescription, String picturePath) {
        this(title, link, author, publishedDate, categoryName, articleDescription, picturePath);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Article other = (Article) obj;
        return this.id == other.id;
    }

    
    //Nisam znao napisati toString u javi pa sam pitao ChatGPT :(
    @Override
public String toString() {
    return String.format(
        "Article - ID: %d | Title: %s | Link: %s | Author: %s %s | DoP: %s | Category: %s | Description: %s",
        id,
        title,
        link,
        author.getFirstName(),
        author.getLastName(),
        publishedDate.format(DATE_FORMATTER),
        categoryName,
        articleDescription
    );
}


    
    
}

