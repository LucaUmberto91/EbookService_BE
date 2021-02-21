package it.ebookservice.aws.s3;

public interface S3Service {

	public String stringDownloadFile(String keyName,String bucketName);
	public void uploadFile(String keyName, String bucketName,String filePath) ;
}
