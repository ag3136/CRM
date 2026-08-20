# Azure Blob Storage Configuration Guide

## Overview
This application has been migrated from local file system operations to Azure Blob Storage for cloud-native file handling. The `ReadDataUtils` class now downloads files from Azure Blob Storage instead of using GUI-based file choosers.

## Configuration Options

### Option 1: Connection String (Development/Testing)
Set the following environment variables:
```bash
export AZURE_STORAGE_CONNECTION_STRING="DefaultEndpointsProtocol=https;AccountName=<account-name>;AccountKey=<account-key>;EndpointSuffix=core.windows.net"
export AZURE_STORAGE_CONTAINER_NAME="crm-files"
export AZURE_BLOB_NAME="customers.csv"
```

### Option 2: Managed Identity (Production - Recommended)
Set the following environment variable:
```bash
export AZURE_STORAGE_ACCOUNT_NAME="<your-storage-account-name>"
export AZURE_STORAGE_CONTAINER_NAME="crm-files"
export AZURE_BLOB_NAME="customers.csv"
```

The application will automatically use Azure Managed Identity for authentication when deployed to:
- Azure App Service
- Azure Container Apps
- Azure Kubernetes Service (AKS)
- Azure Virtual Machines with managed identity enabled

## Required Environment Variables

| Variable | Required | Description | Example |
|----------|----------|-------------|---------|
| `AZURE_STORAGE_CONNECTION_STRING` | Dev/Test | Full connection string to Azure Storage | See Option 1 above |
| `AZURE_STORAGE_ACCOUNT_NAME` | Production | Storage account name for managed identity | `mystorageaccount` |
| `AZURE_STORAGE_CONTAINER_NAME` | Optional | Container name (defaults to `crm-files`) | `crm-files` |
| `AZURE_BLOB_NAME` | Yes | Name of the blob/file to download | `customers.csv` |

## Azure Blob Storage Setup

### 1. Create Storage Account
```bash
az storage account create \
  --name <storage-account-name> \
  --resource-group <resource-group> \
  --location <location> \
  --sku Standard_LRS
```

### 2. Create Container
```bash
az storage container create \
  --name crm-files \
  --account-name <storage-account-name>
```

### 3. Upload Files
```bash
az storage blob upload \
  --account-name <storage-account-name> \
  --container-name crm-files \
  --name customers.csv \
  --file /path/to/local/customers.csv
```

### 4. Configure Managed Identity (Production)
```bash
# Enable managed identity on App Service
az webapp identity assign \
  --name <app-name> \
  --resource-group <resource-group>

# Grant Storage Blob Data Contributor role
az role assignment create \
  --assignee <managed-identity-principal-id> \
  --role "Storage Blob Data Contributor" \
  --scope /subscriptions/<subscription-id>/resourceGroups/<resource-group>/providers/Microsoft.Storage/storageAccounts/<storage-account-name>
```

## Code Changes Summary

### Before (Local File System)
```java
File document = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");
```
- Used JFileChooser (GUI component)
- Required local file system access
- Not cloud-compatible

### After (Azure Blob Storage)
```java
File document = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");
```
- Same method signature (backward compatible)
- Downloads from Azure Blob Storage
- Blob name specified via `AZURE_BLOB_NAME` environment variable
- Fully cloud-native

## Testing Locally

1. Set up Azure Storage Emulator or use a real Azure Storage account
2. Set environment variables:
   ```bash
   export AZURE_STORAGE_CONNECTION_STRING="<your-connection-string>"
   export AZURE_STORAGE_CONTAINER_NAME="crm-files"
   export AZURE_BLOB_NAME="test.csv"
   ```
3. Upload test files to the container
4. Run the application

## Troubleshooting

### Error: "AZURE_BLOB_NAME environment variable not set"
**Solution**: Set the `AZURE_BLOB_NAME` environment variable with the name of the file to download.

### Error: "Azure Storage configuration missing"
**Solution**: Set either:
- `AZURE_STORAGE_CONNECTION_STRING` (for dev/test), OR
- `AZURE_STORAGE_ACCOUNT_NAME` (for production with managed identity)

### Error: "BlobStorageException: The specified container does not exist"
**Solution**: Create the container in Azure Storage or verify the `AZURE_STORAGE_CONTAINER_NAME` value.

### Error: "Authentication failed"
**Solution**: 
- For connection string: Verify the connection string is correct
- For managed identity: Ensure the managed identity has "Storage Blob Data Contributor" role

## Security Best Practices

1. **Never commit connection strings** to source control
2. **Use managed identity** in production environments
3. **Use Azure Key Vault** to store connection strings in non-production environments
4. **Enable HTTPS only** for storage account access
5. **Use SAS tokens** for temporary access if needed
6. **Enable storage account firewall** to restrict access

## Migration Notes

- The `ReadDataUtils.ReadFile()` method signature remains unchanged for backward compatibility
- The `parent` and `fileExtensionDescription` parameters are now ignored (legacy from JFileChooser)
- Files are downloaded to temporary storage and automatically cleaned up
- The blob name must be specified via the `AZURE_BLOB_NAME` environment variable

## Additional Features

### List Available Files
```java
String[] csvFiles = ReadDataUtils.listBlobs("csv");
for (String fileName : csvFiles) {
    System.out.println("Available file: " + fileName);
}
```

### Download Specific File
```java
File file = ReadDataUtils.ReadFile("myfile.csv", "csv");
if (file != null) {
    // Process the file
}
```
