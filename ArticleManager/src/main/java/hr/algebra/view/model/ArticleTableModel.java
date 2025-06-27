/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.view.model;

import hr.algebra.model.Article;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author daniel.bele
 */
public class ArticleTableModel extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"Id", "Title", "Link", "Description", "Picture path", "Published date", "Category", "Author"};
    
    private List<Article> articles;

    public ArticleTableModel(List<Article> articles) {
        this.articles = articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return articles.size();
    }

    @Override
    public int getColumnCount() {
        //return Article.class.getDeclaredFields().length - 1;
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return articles.get(rowIndex).getId();
            case 1:
                return articles.get(rowIndex).getTitle();
            case 2:
                return articles.get(rowIndex).getLink();
            case 3:
                return articles.get(rowIndex).getArticleDescription();
            case 4:
                return articles.get(rowIndex).getPicturePath();
            case 5:
                return articles.get(rowIndex).getPublishedDate().format(Article.DATE_FORMATTER);
            case 6:
                return articles.get(rowIndex).getCategoryName();
            case 7:
                return articles.get(rowIndex).getAuthor();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }


    // important for the id ordering
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex); 
    }
 
    
}