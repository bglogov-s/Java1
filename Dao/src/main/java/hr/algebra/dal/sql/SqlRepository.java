/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Article;
import hr.algebra.model.Author;
import hr.algebra.model.Users;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class SqlRepository implements Repository {
    
    private final DataSource dataSource = DataSourceSingleton.getInstance();
    private static final String ArtcleID = "ArtcleID";
    private static final String TITLE = "Title";
    private static final String LINK = "Link";
    private static final String AUTHOR_FIRST_NAME = "AuthorFirstName";
    private static final String AUTHOR_LAST_NAME = "AuthorLastName";
    private static final String DESCRIPTION = "ArticleDescription";
    private static final String PUBLISHED_DATE = "PublishedDate";
    private static final String PICTURE_PATH = "PicturePath";
    private static final String CATEGORY = "CategoryName";

    private static final String CREATE_ARTICLE = "{ CALL sp_CreateArticle (?,?,?,?,?,?,?,?,?) }";
    private static final String UPDATE_ARTICLE = "{ CALL sp_UpdateArticle (?,?,?,?,?,?,?,?,?) }";
    private static final String DELETE_ARTICLE = "{ CALL sp_DeleteArticle (?) }";
    private static final String SELECT_ARTICLE = "{ CALL sp_SelectArticle (?) }";
    private static final String SELECT_ARTICLES = "{ CALL sp_GetAllArticles }";

    @Override
    public int createArticle(Article article) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_ARTICLE)) {

            stmt.setString(1, article.getTitle());
            stmt.setString(2, article.getLink());
            stmt.setString(3, article.getAuthor().getFirstName());
            stmt.setString(4, article.getAuthor().getLastName());
            stmt.setObject(5, article.getPublishedDate());
            stmt.setString(6, article.getCategoryName());
            stmt.setString(7, article.getArticleDescription());
            stmt.setString(8, article.getPicturePath());

            stmt.registerOutParameter(9, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(9);
        }
    }

    @Override
    public void createArticles(List<Article> articles) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_ARTICLE)) {

            for (Article article : articles) {
                stmt.setString(TITLE, article.getTitle());
                stmt.setString(LINK, article.getLink());
                stmt.setString(AUTHOR_FIRST_NAME, article.getAuthor().getFirstName());
                stmt.setString(AUTHOR_LAST_NAME, article.getAuthor().getLastName());
                stmt.setString(CATEGORY, article.getCategoryName());
                stmt.setString(DESCRIPTION, article.getArticleDescription());
                stmt.setString(PUBLISHED_DATE, article.getPublishedDate().format(Article.DATE_FORMATTER));
                stmt.setString(PICTURE_PATH, article.getPicturePath());

                stmt.registerOutParameter("NewArticleID", Types.INTEGER);
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public void updateArticle(int id, Article article) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_ARTICLE)) {

            stmt.setString(1, article.getTitle());
            stmt.setString(2, article.getLink());
            stmt.setString(3, article.getAuthor().getFirstName());
            stmt.setString(4, article.getAuthor().getLastName());
            stmt.setObject(5, article.getPublishedDate());
            stmt.setString(6, article.getCategoryName());
            stmt.setString(7, article.getArticleDescription());
            stmt.setString(8, article.getPicturePath());
            stmt.setInt(9, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteArticle(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_ARTICLE)) {

            stmt.setInt(ArtcleID, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Article> selectArticle(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ARTICLE)) {

            stmt.setInt(ArtcleID, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Author author = new Author(
                            rs.getString(AUTHOR_FIRST_NAME),
                            rs.getString(AUTHOR_LAST_NAME)
                    );

                    OffsetDateTime odt = rs.getObject(PUBLISHED_DATE, OffsetDateTime.class);
                    LocalDateTime ldt = odt.toLocalDateTime();

                    Article article = new Article(
                            rs.getInt(ArtcleID),
                            rs.getString(TITLE),
                            rs.getString(LINK),
                            author,
                            rs.getObject(PUBLISHED_DATE, OffsetDateTime.class).toLocalDateTime(),
                            rs.getString("CategoryName"),
                            rs.getString(DESCRIPTION),
                            rs.getString(PICTURE_PATH)
                    );

                    return Optional.of(article);
                }
            }
        }
        return Optional.empty();
    }
    private static final DateTimeFormatter OFFSET_DATE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS XXX");

    @Override
    public List<Article> selectArticles() throws Exception {
        List<Article> articles = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ARTICLES); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Author author = new Author(
                        rs.getString(AUTHOR_FIRST_NAME),
                        rs.getString(AUTHOR_LAST_NAME)
                );

                articles.add(new Article(
                        rs.getInt(ArtcleID),
                        rs.getString(TITLE),
                        rs.getString(LINK),
                        author,
                        OffsetDateTime.parse(rs.getString(PUBLISHED_DATE), OFFSET_DATE_TIME_FORMATTER).toLocalDateTime(),
                        rs.getString("CategoryName"),
                        rs.getString(DESCRIPTION),
                        rs.getString(PICTURE_PATH)
                ));
            }
        }
        return articles;
    }

    @Override
    public Optional<Users> logIn(String username, String password) {

        DataSource dataSource = DataSourceSingleton.getInstance();
        String loginProcedure = "{ CALL sp_LoginUser(?, ?) }";

        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(loginProcedure)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Users thisUser = new Users(
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("userName"),
                            rs.getString("email"),
                            rs.getBoolean("isAdmin"),
                            rs.getString("userPass")
                    );

                    return Optional.of(thisUser);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Integer> registerUser(Users user) {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall("{ CALL sp_RegisterUser(?, ?, ?, ?, ?, ?) }")) {

            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getEmail());
            stmt.registerOutParameter(6, Types.INTEGER);

            stmt.execute();

            int result = stmt.getInt(6);
            if (result == -1) {
                return Optional.empty(); // korisnik već postoji
            }

            return Optional.of(result); // vraća novi UserID

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void updateUserRole(int userId, boolean isAdmin) throws Exception {
        DataSource ds = DataSourceSingleton.getInstance();
        try (Connection con = ds.getConnection(); 
                PreparedStatement stmt = con.prepareStatement("UPDATE Users SET isAdmin = ? WHERE UserID = ?")) {
            stmt.setBoolean(1, isAdmin);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Users> selectUsers() throws Exception {
    List<Users> users = new ArrayList<>();

    try (Connection con = dataSource.getConnection();
         CallableStatement stmt = con.prepareCall("{ CALL sp_SelectUsers }");
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            users.add(new Users(
                rs.getInt("UserID"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getString("userName"),
                rs.getString("email"),
                rs.getBoolean("isAdmin"),
                rs.getString("userPass")
            ));
        }
    }
    return users;
}

}
