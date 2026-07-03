package crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration for Amazon S3 client (AWS SDK for Java v2).
 *
 * The S3Client is a singleton bean shared across the application.
 * Credentials are resolved automatically via the AWS Default Credential Provider Chain:
 *   1. Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
 *   2. Java system properties
 *   3. AWS credentials file (~/.aws/credentials)
 *   4. IAM role attached to the EC2 instance / ECS task / Lambda function
 *
 * The AWS region is read from the environment variable AWS_REGION or the
 * application property aws.region (default: us-east-1).
 */
@Configuration
public class S3Config {

    @Value("${aws.region:#{environment.AWS_REGION ?: 'us-east-1'}}")
    private String awsRegion;

    /**
     * Creates and exposes an AWS SDK v2 S3Client as a Spring bean.
     * Credentials are resolved via the default credential provider chain,
     * which supports IAM roles in cloud environments without hard-coded secrets.
     *
     * @return a configured S3Client instance
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
