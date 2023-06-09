package ibf2022.miniproject.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
    
    @Value("${ACCESS_KEY}")
    private String accessKey;

    @Value("${SECRET_KEY}")
    private String secretKey;

    @Bean
    public AmazonS3 createS3Client() {
        // Create credentials with access and secret keys
        BasicAWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);

        // Configure endpoint (HTTP access) to our bucket
        EndpointConfiguration epConfig = new EndpointConfiguration("sgp1.digitaloceanspaces.com", "sgp1");

        AmazonS3 client = AmazonS3ClientBuilder.standard().withEndpointConfiguration(epConfig).withCredentials(new AWSStaticCredentialsProvider(cred)).build();
        
        return client;
    }
    
}