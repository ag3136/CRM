package crm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3 configuration for cloud-native storage.
 *
 * Provides a Spring-managed S3Client bean using AWS SDK for Java v2.
 * Credentials are resolved automatically via the AWS Default Credential Provider Chain:
 *   1. Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
 *   2. Java system properties
 *   3. AWS credentials file (~/.aws/credentials)
 *   4. IAM role attached to the EC2 instance / ECS task / Lambda function (recommended for AWS deployments)
 *
 * The AWS region is configured via the aws.region property or the AWS_REGION environment variable.
 */
@Configuration
public class AwsS3Config {

    @Value("${aws.region:${AWS_REGION:us-east-1}}")
    private String awsRegion;

    /**
     * Creates and configures an S3Client bean.
     * Uses the default credential provider chain — no hard-coded credentials.
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
