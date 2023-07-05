package ibf2022.miniproject.server.repository;

import static ibf2022.miniproject.server.repository.SQLQueries.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.result.UpdateResult;

import ibf2022.miniproject.server.model.Image;
import ibf2022.miniproject.server.model.OrderDetails;
import ibf2022.miniproject.server.model.Product;
import ibf2022.miniproject.server.model.ProductTags;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @SuppressWarnings("null")
    public Integer insertProductDetailsIntoSQL(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement statement = conn.prepareStatement(INSERT_NEW_PRODUCT_INTO_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getEmail());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setDouble(4, product.getPrice());
            statement.setObject(5, LocalDateTime.now());
            statement.setString(6, "selling");
            return statement;
        }, keyHolder);
        product.setProductID(keyHolder.getKey().intValue());

        return product.getProductID();
    }

    public Integer insertImageDetailsIntoSQL(MultipartFile[] imageFiles, Integer productID) throws DataAccessException, IOException {
        int rows = 0;
        for (MultipartFile f : imageFiles) {
            rows += jdbcTemplate.update(INSERT_IMAGES_INTO_SQL, f.getOriginalFilename(), f.getContentType(), f.getBytes(), productID);
        }

        return rows;
    }

    @SuppressWarnings("null")
    public Optional<Product> getProductByID(Integer productID) {
        List<Image> images = new ArrayList<>();
        SqlRowSet prs = jdbcTemplate.queryForRowSet(GET_PRODUCT_BY_ID_FROM_SQL, productID);
        Optional<List<Image>> opt = jdbcTemplate.query(GET_IMAGES_BY_ID_FROM_SQL, rs -> {
            while (rs.next()) {
                Image i = new Image(rs.getInt("imageID"), rs.getString("imageName"), rs.getString("type"), rs.getBytes("imageBytes"));
                images.add(i);
            }
            return Optional.of(images);
        }, productID);
        
        if (opt.isPresent() && prs.next()) {
            Product product = new Product(prs.getInt("productID"), prs.getString("productName"), prs.getString("description"), prs.getDouble("price"), prs.getString("email"), LocalDateTime.parse(prs.getString("uploadTime")), opt.get(), prs.getString("productStatus"));
            return Optional.of(product);
        } else {
            return Optional.empty();
        }
    }

    public Optional<List<Product>> getAllProducts(String email, Integer pageSize, Integer pageIndex) {
        List<Product> products = new ArrayList<>();

        jdbcTemplate.query(GET_ALL_PRODUCTS_BY_EMAIL_FROM_SQL, rs -> {
            while(rs.next()) {
                Product p = new Product(rs.getInt("productID"), rs.getString("productName"), rs.getString("description"), rs.getDouble("price"), rs.getString("email"), rs.getTimestamp("uploadTime").toLocalDateTime(), rs.getString("productStatus"));
                
                List<Image> images = getImagesByID(p.getProductID());

                p.setImages(images);
                products.add(p);
            }
            return Optional.of(products);
        }, email, pageSize, pageIndex*pageSize);

        return Optional.of(products);
    }

    public Optional<List<Product>> getAllOtherProducts(String email, Integer pageSize, Integer pageIndex) {
        List<Product> products = new ArrayList<>();

        jdbcTemplate.query(GET_ALL_OTHER_PRODUCTS_FROM_SQL, rs -> {
            while(rs.next()) {
                Product p = new Product(rs.getInt("productID"), rs.getString("productName"), rs.getString("description"), rs.getDouble("price"), rs.getString("email"), rs.getTimestamp("uploadTime").toLocalDateTime(), rs.getString("productStatus"));
                
                List<Image> images = getImagesByID(p.getProductID());

                p.setImages(images);
                products.add(p);
            }
            return Optional.of(products);
        }, email, pageSize, pageIndex*pageSize);

        return Optional.of(products);
    }

    public Integer editProductDetailsInSQL(Product product) {
        jdbcTemplate.update(EDIT_PRODUCT_IN_SQL, product.getProductName(), product.getDescription(), product.getPrice(), product.getProductID());

        return product.getProductID();
    }

    public Integer editImageDetailsInSQL(MultipartFile[] imageFiles, Integer productID) throws DataAccessException, IOException {
        int rows = 0;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(DELETE_IMAGES_IN_SQL, productID);
        
        for (MultipartFile f : imageFiles) {
            rows += jdbcTemplate.update(conn -> {
            PreparedStatement statement = conn.prepareStatement(INSERT_IMAGES_INTO_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, f.getOriginalFilename());
            statement.setString(2, f.getContentType());
            try {
                statement.setBytes(3, f.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            statement.setInt(4, productID);
            return statement;
            }, keyHolder);
        }

        return rows;
    }

    public boolean deleteProductByID(Integer productID) {
        int result = jdbcTemplate.update(DELETE_PRODUCT_IN_SQL, productID);

        return result > 0;
    }

    public boolean deleteImagesByProductID(Integer productID) {
        int result = jdbcTemplate.update(DELETE_IMAGES_IN_SQL, productID);

        return result > 0;
    }

    public List<Image> getImagesByID(Integer productID) {
        List<Image> images = new ArrayList<>();
        jdbcTemplate.query(GET_IMAGES_BY_ID_FROM_SQL, rs -> {
            while (rs.next()) {
                Image i = new Image(rs.getInt("imageID"), rs.getString("imageName"), rs.getString("type"), rs.getBytes("imageBytes"));
                images.add(i);
            }
            return images;
        }, productID);

        return images;
    }

    @Transactional(rollbackFor = DataAccessException.class)
    public boolean buyProduct(Integer productID) {
        int result = jdbcTemplate.update(UPDATE_PRODUCT_STATUS_IN_SQL, "pending", productID);
        
        return result > 0;
    }

    @Transactional(rollbackFor = DataAccessException.class)
    public boolean cancelBuyProduct(Integer productID) {
        int result = jdbcTemplate.update(UPDATE_PRODUCT_STATUS_IN_SQL, "selling", productID);

        return result > 0;
    }

    @Transactional(rollbackFor = DataAccessException.class)
    public boolean acceptOrder(Integer productID) {
        int result = jdbcTemplate.update(UPDATE_PRODUCT_STATUS_IN_SQL, "sold", productID);

        return result > 0;
    }

    public boolean upsertOrderDetails(Integer productID, String buyer, String seller, String action) {
        Query query = Query.query(Criteria.where("productID").is(productID).and("seller").is(seller));
        Update update = new Update().set("status", "selling");
        if (action.equals("buy")) {
            update.push("buyers").each(buyer);
        } else if (action.equals("cancel")) {
            update.pull("buyers", buyer);
        } else {
            update.set("buyers", null);
        }
        UpdateResult res = mongoTemplate.upsert(query, update, "order_details");

        return res.wasAcknowledged();
    }
    
    public boolean upsertOrderDetails(Integer productID, String buyer, String action) {
        Query query = Query.query(Criteria.where("productID").is(productID));
        Update update = new Update().set("status", action);
        update.set("buyers", buyer);
        UpdateResult res = mongoTemplate.upsert(query, update, "order_details");

        return res.wasAcknowledged();
    }
    
    public OrderDetails getOrderDetails(Integer productID) {
        Query query = Query.query(Criteria.where("productID").is(productID));
        OrderDetails order = mongoTemplate.findOne(query, OrderDetails.class, "order_details");
        return order;
    }

    public boolean upsertProductTags(ProductTags productTags, String action) {
        Query query = Query.query(Criteria.where("productID").is(productTags.getProductID()));
        Update update = new Update().set("tags", productTags.getTags()).set("status", action);
        UpdateResult res = mongoTemplate.upsert(query, update, "product_tags");

        return res.wasAcknowledged();
    }

    public Optional<ProductTags> getProductTags(Integer productID) {
        Query query = Query.query(Criteria.where("productID").is(productID));
        ProductTags productTags = mongoTemplate.findOne(query, ProductTags.class, "product_tags");

        return Optional.ofNullable(productTags);
    }




    // public URL uploadImageIntoS3(MultipartFile imageFile, String username) throws IOException {
    //     // Add custom metadata
    //     Map<String, String> userData = new HashMap<>();
    //     userData.put("filename", imageFile.getOriginalFilename());
    //     userData.put("upload-date", LocalDateTime.now().toString());

    //     // Add object's metadata
    //     ObjectMetadata metadata = new ObjectMetadata();
    //     metadata.setContentType(imageFile.getContentType());
    //     metadata.setContentLength(imageFile.getSize());
    //     metadata.setUserMetadata(userData);

    //     // woodybucket - bucket name
    //     // key - imagefile path
    //     // file.getInputStream() - actual bytes
    //     // metadata
    //     String key = username + "/" + imageFile.getOriginalFilename();
    //     PutObjectRequest putReq = new PutObjectRequest("woodybucket", key, imageFile.getInputStream(), metadata);

    //     // Make the file publically accessible
    //     putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

    //     PutObjectResult result = s3.putObject(putReq);
    //     System.out.println(">> result: " + result);

    //     return s3.getUrl("woodybucket", key);
    // }


}
