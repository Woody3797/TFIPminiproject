package ibf2022.miniproject.server.repository;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import ibf2022.miniproject.server.model.Image;
import ibf2022.miniproject.server.model.Product;
import static ibf2022.miniproject.server.repository.SQLQueries.*;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AmazonS3 s3;

    @SuppressWarnings("null")
    public Integer insertProductDetailsIntoSQL(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement statement = conn.prepareStatement(INSERT_NEW_PRODUCT_INTO_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "admin");
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getDescription());
            statement.setDouble(4, product.getPrice());
            statement.setObject(5, LocalDateTime.now());
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

    public Optional<Product> getProductByID(Integer productID) {
        List<Image> images = new ArrayList<>();
        SqlRowSet prs = jdbcTemplate.queryForRowSet(GET_PRODUCT_BY_ID_FROM_SQL, productID);
        Optional<List<Image>> opt = jdbcTemplate.query(GET_IMAGES_BY_ID_FROM_SQL, rs -> {
            // if (!rs.first()) {
            //     return Optional.empty();
            // }
            while (rs.next()) {
                Image i = new Image(rs.getInt("image_id"), rs.getString("imageName"), rs.getString("type"), rs.getBytes("imageBytes"));
                images.add(i);
            }
            return Optional.of(images);
        }, productID);
        
        if (opt.isPresent() && prs.next()) {
            Product product = new Product(prs.getInt("productID"), prs.getString("productName"), prs.getString("description"), prs.getDouble("price"), prs.getString("username"), LocalDateTime.parse(prs.getString("uploadTime")), opt.get());
            System.out.println(product);
            return Optional.of(product);
        } else {
            return Optional.empty();
        }
    }












    public URL uploadImageIntoS3(MultipartFile imageFile, String username) throws IOException {
        // Add custom metadata
        Map<String, String> userData = new HashMap<>();
        userData.put("filename", imageFile.getOriginalFilename());
        userData.put("upload-date", LocalDateTime.now().toString());

        // Add object's metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(imageFile.getContentType());
        metadata.setContentLength(imageFile.getSize());
        metadata.setUserMetadata(userData);

        // woodybucket - bucket name
        // key - imagefile path
        // file.getInputStream() - actual bytes
        // metadata
        String key = username + "/" + imageFile.getOriginalFilename();
        PutObjectRequest putReq = new PutObjectRequest("woodybucket", key, imageFile.getInputStream(), metadata);

        // Make the file publically accessible
        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult result = s3.putObject(putReq);
        System.out.println(">> result: " + result);

        return s3.getUrl("woodybucket", key);
    }


    private static final String GET_PRODUCT_DETAILS_FROM_SQL = """
            SELECT * FROM product_details WHERE username = ?
            """;

    public SqlRowSet getProductDetailsFromSQL(String username, String productName) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(GET_PRODUCT_DETAILS_FROM_SQL, username, productName);
        
        return rs;
    }
    
}
