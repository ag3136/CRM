package crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Spring configuration that exposes an Amazon S3 client as a managed bean.
 *
 * <p>The AWS region is resolved from the {@code AWS_REGION} environment
 * variable (12-factor app principle III – Config).  When running on AWS
 * infrastructure (EC2, ECS, Lambda, etc.) the SDK will automatically pick up
 * credentials from the instance / task IAM role via the default credential
 * provider chain, so no hard-coded secrets are required.
 *
 * <p>Required environment variables:
 * <ul>
 *   <li>{@code AWS_REGION} – AWS region code, e.g. {@code us-east-1}.
 *       Defaults to {@code us-east-1} when not set.</li>
 * </ul>
 */
@Configuration
public class S3ClientConfig {

    @Bean
    public S3Client s3Client() {
        String regionName = System.getenv("AWS_REGION");
        if (regionName == null || regionName.isEmpty()) {
            regionName = "us-east-1";
        }
        return S3Client.builder()
                .region(Region.of(regionName))
                .build();
    }
}
