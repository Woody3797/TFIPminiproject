package ibf2022.miniproject.server.repository;

public class SQLQueries {

    public static final String INSERT_NEW_PRODUCT_INTO_SQL = """
                INSERT INTO product_details(email, productName, description, price, uploadTime) values (?, ?, ?, ?, ?)
            """;

    public static final String INSERT_IMAGES_INTO_SQL = """
            INSERT INTO image_details(imageName, type, imageBytes, productID) values (?, ?, ?, ?)
            """;

    public static final String GET_PRODUCT_BY_ID_FROM_SQL = """
            SELECT * FROM product_details WHERE productID = ?
            """;

    public static final String GET_ALL_PRODUCTS_BY_EMAIL_FROM_SQL = """
            SELECT * FROM product_details WHERE email = ? ORDER BY productStatus, productID ASC LIMIT ? OFFSET ?
            """;

    public static final String GET_ALL_OTHER_PRODUCTS_FROM_SQL = """
            SELECT * FROM product_details WHERE email NOT LIKE ? AND productStatus NOT LIKE 'sold' ORDER BY productID ASC LIMIT ? OFFSET ?
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

    public static final String GET_USER_BY_EMAIL = """
            SELECT * FROM user_details WHERE email = ?
            """;

    public static final String SIGNUP_NEW_USER = """
            INSERT INTO user_details(email, password) values (?, ?)
            """;

    public static final String UPDATE_PRODUCT_STATUS_IN_SQL = """
            UPDATE product_details SET productStatus = ? WHERE productID = ?
            """;
}
