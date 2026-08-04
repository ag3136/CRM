# Google Cloud Storage Configuration Guide

## Overview
This application has been migrated from local file system dependencies to Google Cloud Storage (GCS) for cloud-native file operations.

## Changes Made

### 1. Replaced Hard-coded File Paths
- **Old Approach**: Used `JFileChooser` (Swing GUI) to select files from local file system
- **New Approach**: Reads files from Google Cloud Storage using GCS Java SDK
- **File Modified**: `src/main/java/crm/utils/ReadDataUtils.java`

### 2. Added GCS Dependencies
- Added `google-cloud-storage` SDK to `pom.xml`
- Version: 2.17.2

### 3. Configuration Properties
Added GCS configuration to `application.properties`:
```properties
gcs.bucket.name=${GCS_BUCKET_NAME:crm-data-bucket}
gcs.project.id=${GCP_PROJECT_ID:}
gcs.file.path=${GCS_FILE_PATH:}
```

## Environment Variables

Set these environment variables for GCS integration:

### Required
- **GCS_BUCKET_NAME**: Name of your GCS bucket (default: `crm-data-bucket`)
- **GCP_PROJECT_ID**: Your Google Cloud Project ID

### Optional
- **GCS_FILE_PATH**: Default file path/blob name in GCS bucket for CSV imports

### Example
```bash
export GCS_BUCKET_NAME=my-crm-bucket
export GCP_PROJECT_ID=my-gcp-project-id
export GCS_FILE_PATH=imports/customers.csv
```

## GCP Authentication

The application uses Google Cloud default credentials. Choose one of these methods:

### Option 1: Service Account Key (Development)
```bash
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/service-account-key.json
```

### Option 2: Workload Identity (GKE - Recommended for Production)
Configure Workload Identity for your GKE cluster and bind it to your Kubernetes service account.

### Option 3: Application Default Credentials (Cloud Run, App Engine)
These services automatically provide credentials when deployed.

## Usage

### Reading Files from GCS

**New Method (Recommended)**:
```java
File csvFile = ReadDataUtils.readFileFromGCS("imports/customers.csv", "csv");
```

**Legacy Method (Backward Compatible)**:
```java
// Set GCS_FILE_PATH environment variable first
File csvFile = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");
```

### Uploading Files to GCS

```java
File localFile = new File("/tmp/data.csv");
boolean success = ReadDataUtils.uploadFileToGCS(localFile, "exports/data.csv");
```

## GCS Bucket Setup

1. **Create a GCS Bucket**:
   ```bash
   gsutil mb -p YOUR_PROJECT_ID -l us-central1 gs://crm-data-bucket
   ```

2. **Set Bucket Permissions**:
   ```bash
   # Grant service account access
   gsutil iam ch serviceAccount:YOUR_SERVICE_ACCOUNT@YOUR_PROJECT.iam.gserviceaccount.com:objectAdmin gs://crm-data-bucket
   ```

3. **Upload Sample Files**:
   ```bash
   gsutil cp local-file.csv gs://crm-data-bucket/imports/customers.csv
   ```

## Migration Notes

### For CSV Import Functionality
The commented-out CSV import endpoint in `CSVController.java` previously used:
```java
File document = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");
```

To enable this in cloud environment:
1. Set `GCS_FILE_PATH` environment variable to the blob name
2. Or refactor to accept blob name as request parameter
3. Or implement a file upload endpoint that stores to GCS

### Recommended Approach for Web Applications
Instead of reading from GCS in response to user action, consider:
1. **File Upload Endpoint**: Accept file uploads via HTTP multipart, store to GCS
2. **Scheduled Jobs**: Process files from GCS on a schedule
3. **Event-Driven**: Use GCS notifications to trigger processing when files are uploaded

## Testing

### Local Testing with GCS Emulator
```bash
# Start GCS emulator
gcloud beta emulators storage start

# Set environment variable
export STORAGE_EMULATOR_HOST=http://localhost:9023
```

### Integration Testing
Ensure your test environment has:
- Valid GCP credentials
- Access to test GCS bucket
- Proper IAM permissions

## Troubleshooting

### Error: "GCS_FILE_PATH environment variable not set"
**Solution**: Set the `GCS_FILE_PATH` environment variable or use the new `readFileFromGCS()` method directly.

### Error: "File not found in GCS bucket"
**Solution**: 
- Verify the blob name/path is correct
- Check bucket name is correct
- Ensure file exists in GCS: `gsutil ls gs://YOUR_BUCKET/path/to/file`

### Error: "Access Denied"
**Solution**:
- Verify service account has `storage.objects.get` permission
- Check IAM roles: `roles/storage.objectViewer` or `roles/storage.objectAdmin`

### Error: "Could not find default credentials"
**Solution**:
- Set `GOOGLE_APPLICATION_CREDENTIALS` environment variable
- Or run `gcloud auth application-default login` for local development

## Cloud Deployment Checklist

- [ ] GCS bucket created
- [ ] Service account created with appropriate permissions
- [ ] Environment variables configured in deployment
- [ ] Authentication method configured (Workload Identity/Service Account)
- [ ] Test files uploaded to GCS bucket
- [ ] Application tested with GCS integration

## Security Best Practices

1. **Use Workload Identity** in GKE instead of service account keys
2. **Principle of Least Privilege**: Grant only necessary GCS permissions
3. **Bucket Policies**: Configure bucket-level IAM policies
4. **Encryption**: Enable encryption at rest (default in GCS)
5. **Audit Logging**: Enable Cloud Audit Logs for GCS access
6. **Private Buckets**: Don't make buckets publicly accessible unless required

## Performance Considerations

1. **Caching**: Consider caching frequently accessed files
2. **Regional Buckets**: Use regional buckets close to your compute resources
3. **Parallel Downloads**: For large files, use parallel composite downloads
4. **Temporary Files**: Clean up temporary files after processing

## Cost Optimization

1. **Lifecycle Policies**: Set up lifecycle rules to delete old files
2. **Storage Classes**: Use appropriate storage class (Standard, Nearline, Coldline)
3. **Egress Costs**: Minimize cross-region data transfer
4. **Monitoring**: Set up billing alerts for GCS usage
