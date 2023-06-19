package ibf2022.miniproject.server.repository;

public class SQLQueries {

    public static final String INSERT_NEW_PRODUCT_INTO_SQL = """
                INSERT INTO product_details(username, productName, description, price, uploadTime) values (?, ?, ?, ?, ?)
            """;

    public static final String INSERT_IMAGES_INTO_SQL = """
            INSERT INTO image_details(imageName, type, imageBytes, productID) values (?, ?, ?, ?)
            """;

    public static final String GET_PRODUCT_BY_ID_FROM_SQL = """
            SELECT * FROM product_details WHERE productID = ?
            """;

    public static final String GET_ALL_PRODUCTS_BY_USERNAME_FROM_SQL = """
            SELECT * FROM product_details WHERE username = ? LIMIT ? OFFSET ?
            """;

    public static final String GET_IMAGES_BY_ID_FROM_SQL = """
            SELECT * FROM image_details WHERE productID = ?
            """;

    public static final String EDIT_PRODUCT_IN_SQL = """
            UPDATE product_details SET productName = ?, description = ?, price = ? WHERE productID = ?
            """;

    public static final String DELETE_IMAGES_IN_SQL = """
            DELETE FROM image_details WHERE productID = ?
            """;

    public static final String DELETE_PRODUCT_IN_SQL = """
            DELETE FROM product_details WHERE productID = ?
            """;

    public static final String GET_USER_BY_USERNAME = """
            SELECT * FROM user_details WHERE username = ?
            """;

    public static final String GET_USER_BY_EMAIL = """
            SELECT * FROM user_details WHERE email = ?
            """;

    public static final String SIGNUP_NEW_USER = """
            INSERT INTO user_details(username, password, email) values (?, ?, ?)
            """;
}
