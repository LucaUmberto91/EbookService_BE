package it.ebookservice.aws.s3;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import it.ebookservice.model.Book;

public interface S3Service {

	public String stringDownloadFile(String keyName,String bucketName);
	public void uploadFile(String keyName, String bucketName,String filePath);
	public File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException;
	public Book uploadFileS3(MultipartFile file,Book book) throws IllegalStateException, IOException;
}
