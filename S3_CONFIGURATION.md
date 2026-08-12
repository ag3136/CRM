# AWS S3 Configuration for CRM Application

## Overview
The application has been migrated from local file system access to Amazon S3 object storage for cloud compatibility.

## Configuration

### Environment Variables
Set the following environment variables in your AWS cloud environment:

```bash
S3_BUCKET_NAME=your-crm-data-bucket
AWS_REGION=us-east-1  # or your preferred region
```

### AWS Credentials
The application uses AWS SDK's `DefaultCredentialsProvider` which automatically discovers credentials in the following order:

1. **Environment Variables**: `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`
2. **System Properties**: `aws.accessKeyId` and `aws.secretAccessKey`
3. **IAM Role** (Recommended for EC2, ECS, Lambda): Automatically provided by AWS
4. **AWS Credentials File**: `~/.aws/credentials`

### Recommended Setup for AWS Cloud Deployment

#### For EC2/ECS/EKS:
1. Create an IAM role with S3 read permissions
2. Attach the role to your EC2 instance or ECS task
3. No need to configure credentials explicitly

#### For Lambda:
1. Add S3 permissions to the Lambda execution role
2. Set environment variables in Lambda configuration

#### IAM Policy Example:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:ListBucket"
      ],
      "Resource": [
        "arn:aws:s3:::your-crm-data-bucket",
        "arn:aws:s3:::your-crm-data-bucket/*"
      ]
    }
  ]
}
```

## Usage Changes

### Before (Local File System):
```java
File document = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");
```

### After (S3 Object Storage):
```java
// Download from default bucket configured via S3_BUCKET_NAME environment variable
File document = ReadDataUtils.ReadFile("data/customers.csv");

// Or specify bucket explicitly
File document = ReadDataUtils.ReadFile("data/customers.csv", "my-custom-bucket");
```

## S3 Bucket Structure
Organize your files in S3 with a clear structure:

```
your-crm-data-bucket/
├── data/
│   ├── customers.csv
│   ├── imports/
│   │   └── customer-import-2024.csv
│   └── exports/
│       └── customer-export-2024.csv
└── documents/
    └── contracts/
```

## Migration Steps

1. **Create S3 Bucket**: Create an S3 bucket in your AWS account
2. **Upload Files**: Upload existing CSV and data files to the S3 bucket
3. **Configure IAM**: Set up IAM roles/policies for S3 access
4. **Set Environment Variables**: Configure `S3_BUCKET_NAME` and `AWS_REGION`
5. **Update Code**: Replace file dialog calls with S3 key paths
6. **Test**: Verify file downloads work correctly

## Security Best Practices

1. **Use IAM Roles**: Never hardcode AWS credentials in code
2. **Least Privilege**: Grant only necessary S3 permissions
3. **Encryption**: Enable S3 bucket encryption at rest
4. **VPC Endpoints**: Use VPC endpoints for S3 access from private subnets
5. **Logging**: Enable S3 access logging for audit trails

## Troubleshooting

### Common Issues:

1. **Access Denied**: Check IAM permissions and bucket policies
2. **Region Mismatch**: Ensure AWS_REGION matches your bucket region
3. **Credentials Not Found**: Verify IAM role is attached or credentials are configured
4. **File Not Found**: Verify the S3 key path is correct

### Debug Logging:
Enable AWS SDK logging by adding to application.properties:
```properties
logging.level.software.amazon.awssdk=DEBUG
```

## Cost Optimization

- Use S3 lifecycle policies to move old files to cheaper storage classes
- Enable S3 Intelligent-Tiering for automatic cost optimization
- Monitor S3 usage with AWS Cost Explorer
