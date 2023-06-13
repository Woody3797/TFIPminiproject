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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.server.model.Image;
import ibf2022.miniproject.server.model.Product;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    @SuppressWarnings("null")
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
