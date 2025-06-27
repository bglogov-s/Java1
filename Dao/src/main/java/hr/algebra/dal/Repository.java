/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Article;
import hr.algebra.model.Users;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author daniel.bele
 */
public interface Repository {

    int createArticle(Article article) throws Exception;

    void createArticles(List<Article> articles) throws Exception;

    void updateArticle(int id, Article data) throws Exception;

    void deleteArticle(int id) throws Exception;

    Optional<Article> selectArticle(int id) throws Exception;

    List<Article> selectArticles() throws Exception;

    Optional<Users> logIn(String username, String password);

    Optional<Integer> registerUser(Users users);

    void updateUserRole(int userId, boolean isAdmin) throws Exception;

    List<Users> selectUsers() throws Exception;



}
