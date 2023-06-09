package ibf2022.miniproject.server.repository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Repository
public class ProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AmazonS3 s3;

    private static final String INSERT_NEW_PRODUCT_INTO_SQL = """
            INSERT INTO product_details(username, productName, description, price, uploadTime) values (?, ?, ?, ?, ?)
            """;

    public Integer insertProductDetailsIntoSQL(String username, String productName, String description, Double price) {
        int result = jdbcTemplate.update(INSERT_NEW_PRODUCT_INTO_SQL, username, productName, description, price, LocalDateTime.now());
        return result;
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
