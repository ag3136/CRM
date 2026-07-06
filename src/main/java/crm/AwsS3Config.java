package crm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3 configuration bean.
 * Provides a singleton S3Client using the default AWS credential provider chain
 * (IAM role, environment variables, ~/.aws/credentials).
 * The AWS region is resolved from the environment variable AWS_REGION or
 * the application property aws.region.
 */
@Configuration
public class AwsS3Config {

    @Value("${aws.region:#{environment.AWS_REGION ?: 'us-east-1'}}")
    private String awsRegion;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }

}
