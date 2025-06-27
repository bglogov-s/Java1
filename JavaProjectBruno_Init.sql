
-- 1. Kreiranje baze
CREATE DATABASE JavaProjectBruno;
GO

USE JavaProjectBruno;
GO

-- 2. Tablica Users
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    firstName NVARCHAR(MAX) NULL,
    lastName NVARCHAR(MAX) NULL,
    userName NVARCHAR(MAX) NULL,
    userPass NVARCHAR(10) NULL,
    email NVARCHAR(MAX) NULL,
    isAdmin BIT NOT NULL DEFAULT 0
);
GO

-- 3. Tablica Author
CREATE TABLE Author (
    AuthorID INT PRIMARY KEY IDENTITY(1,1),
    firstName NVARCHAR(MAX) NULL,
    lastName NVARCHAR(MAX) NULL
);
GO

-- 4. Tablica Category
CREATE TABLE Category (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    CategoryName NVARCHAR(MAX) NULL
);
GO

-- 5. Tablica Article
CREATE TABLE Article (
    ArtcleID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(MAX) NULL,
    Link NVARCHAR(MAX) NULL,
    AuthorID INT NOT NULL,
    pubDate DATETIMEOFFSET(7) NULL,
    CategoryID INT NOT NULL,
    ArticleDescription NVARCHAR(MAX) NULL,
    picturePath NVARCHAR(MAX) NULL,
    FOREIGN KEY (AuthorID) REFERENCES Author(AuthorID),
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);
GO

-- 6. Ubacivanje početnog admina
INSERT INTO Users (firstName, lastName, userName, userPass, email, isAdmin)
VALUES ('Admin', 'User', 'admin', 'admin123', 'admin@example.com', 1);
GO



-- =============================================
-- STORED PROCEDURES
-- =============================================

-- CREATE OR GET Article with Author and Category
CREATE PROCEDURE [dbo].[sp_CreateArticle]
    @Title               NVARCHAR(255),
    @Link                NVARCHAR(500),
    @AuthorFirstName     NVARCHAR(100),
    @AuthorLastName      NVARCHAR(100),
    @PublishedDate       DATETIMEOFFSET,
    @CategoryName        NVARCHAR(100),
    @ArticleDescription  NVARCHAR(MAX),
    @PicturePath         NVARCHAR(500),
    @NewArticleID        INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @AuthorID INT;
    SELECT @AuthorID = AuthorID 
    FROM dbo.Author 
    WHERE firstName = @AuthorFirstName AND lastName = @AuthorLastName;

    IF @AuthorID IS NULL
    BEGIN
        INSERT INTO dbo.Author (firstName, lastName)
        VALUES (@AuthorFirstName, @AuthorLastName);
        SET @AuthorID = SCOPE_IDENTITY();
    END

    DECLARE @CategoryID INT;
    SELECT @CategoryID = CategoryID
    FROM dbo.Category
    WHERE CategoryName = @CategoryName;

    IF @CategoryID IS NULL
    BEGIN
        INSERT INTO dbo.Category (CategoryName)
        VALUES (@CategoryName);
        SET @CategoryID = SCOPE_IDENTITY();
    END

    INSERT INTO dbo.Article 
        (Title, Link, AuthorID, pubDate, CategoryID, ArticleDescription, PicturePath)
    VALUES
        (@Title, @Link, @AuthorID, @PublishedDate, @CategoryID, @ArticleDescription, @PicturePath);

    SET @NewArticleID = SCOPE_IDENTITY();
END
GO

-- CREATE Author
CREATE PROCEDURE [dbo].[sp_CreateAuthor]
    @firstName NVARCHAR(50),
    @lastName NVARCHAR(50)
AS
BEGIN
    INSERT INTO Author (firstName, lastName)
    VALUES (@firstName, @lastName)
    SELECT SCOPE_IDENTITY() AS AuthorID
END
GO

-- CREATE Category
CREATE PROCEDURE [dbo].[sp_CreateCategory]
    @CategoryName NVARCHAR(100)
AS
BEGIN
    INSERT INTO Category (CategoryName)
    VALUES (@CategoryName)
    SELECT SCOPE_IDENTITY() AS CategoryID
END
GO

-- DELETE Article
CREATE PROCEDURE [dbo].[sp_DeleteArticle]
    @ArtcleID INT
AS
BEGIN
    DELETE FROM Article
    WHERE ArtcleID = @ArtcleID
    SELECT @@ROWCOUNT AS RowsAffected
END
GO

-- DELETE Author
CREATE PROCEDURE [dbo].[sp_DeleteAuthor]
    @AuthorID INT
AS
BEGIN
    DELETE FROM Author
    WHERE AuthorID = @AuthorID
    SELECT @@ROWCOUNT AS RowsAffected
END
GO

-- DELETE Category
CREATE PROCEDURE [dbo].[sp_DeleteCategory]
    @CategoryID INT
AS
BEGIN
    DELETE FROM Category
    WHERE CategoryID = @CategoryID
    SELECT @@ROWCOUNT AS RowsAffected
END
GO

-- READ All Articles
CREATE PROCEDURE [dbo].[sp_GetAllArticles]
AS
BEGIN
    SELECT a.ArtcleID as ArtcleID, a.Title as Title, a.Link as Link,
           au.firstName as AuthorFirstName, au.lastName as AuthorLastName,
           a.ArticleDescription as ArticleDescription, a.pubDate as PublishedDate,
           a.picturePath as PicturePath, a.CategoryID, c.CategoryName
    FROM Article a
    INNER JOIN Author au ON a.AuthorID = au.AuthorID
    INNER JOIN Category c ON a.CategoryID = c.CategoryID
    ORDER BY a.pubDate DESC
END
GO

-- READ All Authors
CREATE PROCEDURE [dbo].[sp_GetAllAuthors]
AS
BEGIN
    SELECT AuthorID, firstName, lastName
    FROM Author
    ORDER BY lastName, firstName
END
GO



-- READ All Categories
CREATE PROCEDURE [dbo].[sp_GetAllCategories]
AS
BEGIN
    SELECT CategoryID, CategoryName
    FROM Category
    ORDER BY CategoryName
END
GO

-- READ Article by ID
CREATE PROCEDURE [dbo].[sp_GetArticle]
    @ArtcleID INT
AS
BEGIN
    SELECT a.ArtcleID, a.Title, a.Link, a.AuthorID, a.pubDate, 
           a.CategoryID, a.ArticleDescription,
           au.firstName, au.lastName,
           c.CategoryName
    FROM Article a
    INNER JOIN Author au ON a.AuthorID = au.AuthorID
    INNER JOIN Category c ON a.CategoryID = c.CategoryID
    WHERE a.ArtcleID = @ArtcleID
END
GO

-- READ Articles by Author
CREATE PROCEDURE [dbo].[sp_GetArticlesByAuthor]
    @AuthorID INT
AS
BEGIN
    SELECT a.ArtcleID, a.Title, a.Link, a.AuthorID, a.pubDate, 
           a.CategoryID, a.ArticleDescription,
           au.firstName, au.lastName,
           c.CategoryName
    FROM Article a
    INNER JOIN Author au ON a.AuthorID = au.AuthorID
    INNER JOIN Category c ON a.CategoryID = c.CategoryID
    WHERE a.AuthorID = @AuthorID
    ORDER BY a.pubDate DESC
END
GO

-- READ Articles by Category
CREATE PROCEDURE [dbo].[sp_GetArticlesByCategory]
    @CategoryID INT
AS
BEGIN
    SELECT a.ArtcleID, a.Title, a.Link, a.AuthorID, a.pubDate, 
           a.CategoryID, a.ArticleDescription,
           au.firstName, au.lastName,
           c.CategoryName
    FROM Article a
    INNER JOIN Author au ON a.AuthorID = au.AuthorID
    INNER JOIN Category c ON a.CategoryID = c.CategoryID
    WHERE a.CategoryID = @CategoryID
    ORDER BY a.pubDate DESC
END
GO

-- READ Author by ID
CREATE PROCEDURE [dbo].[sp_GetAuthor]
    @AuthorID INT
AS
BEGIN
    SELECT AuthorID, firstName, lastName
    FROM Author
    WHERE AuthorID = @AuthorID
END
GO

-- READ Category by ID
CREATE PROCEDURE [dbo].[sp_GetCategory]
    @CategoryID INT
AS
BEGIN
    SELECT CategoryID, CategoryName
    FROM Category
    WHERE CategoryID = @CategoryID
END
GO

-- LOGIN User
CREATE PROCEDURE [dbo].[sp_LoginUser]
    @userName NVARCHAR(MAX),
    @userPass NVARCHAR(MAX)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        UserID,
        firstName,
        lastName,
        userName,
        email,
        isAdmin,
        userPass
    FROM dbo.[Users]
    WHERE userName = @userName AND userPass = @userPass;
END
GO



-- FILTERED SELECT Articles
CREATE PROCEDURE [dbo].[sp_SelectArticle]
    @ArtcleID             INT       = NULL,
    @AuthorID             INT       = NULL,
    @CategoryID           INT       = NULL,
    @PublicationDateFrom  DATE      = NULL,
    @PublicationDateTo    DATE      = NULL
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        a.ArtcleID                 AS ArtcleID,
        a.Title,
        a.Link,
        a.pubDate                  AS PublishedDate,
        a.ArticleDescription       AS ArticleDescription,
        a.PicturePath              AS PicturePath,
        a.AuthorID,
        aut.firstName              AS AuthorFirstName,
        aut.lastName               AS AuthorLastName,
        ISNULL(aut.firstName + ' ', '') + ISNULL(aut.lastName, '') AS AuthorName,
        a.CategoryID,
        c.CategoryName
    FROM dbo.Article AS a
    LEFT JOIN dbo.Author   AS aut ON a.AuthorID   = aut.AuthorID
    LEFT JOIN dbo.Category AS c   ON a.CategoryID = c.CategoryID
    WHERE
        (@ArtcleID           IS NULL OR a.ArtcleID       = @ArtcleID)
    AND (@AuthorID            IS NULL OR a.AuthorID      = @AuthorID)
    AND (@CategoryID          IS NULL OR a.CategoryID    = @CategoryID)
    AND (@PublicationDateFrom IS NULL OR a.pubDate       >= @PublicationDateFrom)
    AND (@PublicationDateTo   IS NULL OR a.pubDate       <= @PublicationDateTo)
    ORDER BY a.pubDate DESC;
END
GO

-- SELECT All Users
CREATE PROCEDURE [dbo].[sp_SelectUsers]
AS
BEGIN
    SET NOCOUNT ON;

    SELECT 
        UserID,
        firstName,
        lastName,
        userName,
        email,
        isAdmin,
        userPass
    FROM Users;
END
GO

-- UPDATE Article
CREATE PROCEDURE [dbo].[sp_UpdateArticle]
    @Title              NVARCHAR(255),
    @Link               NVARCHAR(500),
    @AuthorFirstName    NVARCHAR(100),
    @AuthorLastName     NVARCHAR(100),
    @pubDate            DATETIMEOFFSET,
    @CategoryName       NVARCHAR(100),
    @ArticleDescription NVARCHAR(MAX),
    @PicturePath        NVARCHAR(500),
    @ArtcleID           INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @AuthorID INT;
    SELECT @AuthorID = AuthorID
    FROM dbo.Author
    WHERE firstName = @AuthorFirstName AND lastName = @AuthorLastName;

    IF @AuthorID IS NULL
    BEGIN
        INSERT INTO dbo.Author (firstName, lastName)
        VALUES (@AuthorFirstName, @AuthorLastName);
        SET @AuthorID = SCOPE_IDENTITY();
    END

    DECLARE @CategoryID INT;
    SELECT @CategoryID = CategoryID
    FROM dbo.Category
    WHERE CategoryName = @CategoryName;

    IF @CategoryID IS NULL
    BEGIN
        INSERT INTO dbo.Category (CategoryName)
        VALUES (@CategoryName);
        SET @CategoryID = SCOPE_IDENTITY();
    END

    UPDATE dbo.Article
    SET Title              = @Title,
        Link               = @Link,
        AuthorID           = @AuthorID,
        pubDate            = @pubDate,
        CategoryID         = @CategoryID,
        ArticleDescription = @ArticleDescription,
        PicturePath        = @PicturePath
    WHERE ArtcleID = @ArtcleID;
END
GO

-- UPDATE Author
CREATE PROCEDURE [dbo].[sp_UpdateAuthor]
    @AuthorID INT,
    @firstName NVARCHAR(50),
    @lastName NVARCHAR(50)
AS
BEGIN
    UPDATE Author
    SET firstName = @firstName,
        lastName = @lastName
    WHERE AuthorID = @AuthorID
    SELECT @@ROWCOUNT AS RowsAffected
END
GO

-- UPDATE Category
CREATE PROCEDURE [dbo].[sp_UpdateCategory]
    @CategoryID INT,
    @CategoryName NVARCHAR(100)
AS
BEGIN
    UPDATE Category
    SET CategoryName = @CategoryName
    WHERE CategoryID = @CategoryID
    SELECT @@ROWCOUNT AS RowsAffected
END
GO
