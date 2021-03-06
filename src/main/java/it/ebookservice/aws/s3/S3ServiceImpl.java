package it.ebookservice.aws.s3;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

import it.ebookservice.model.Book;



@Service
public class S3ServiceImpl implements S3Service{

	@Autowired
	private AmazonS3 s3client;
	private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);
	
	@Override
	public String stringDownloadFile(String keyName,String bucketName) {
		if(bucketName!=null && keyName!=null) {
			if(s3client.doesObjectExist(bucketName, keyName)) {
	          	 GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,keyName);
	          	 logger.info("Download file :  "+keyName+" successfully!");
	          	 return s3client.generatePresignedUrl(generatePresignedUrlRequest).toString();
			}
		} 
		return "";
	}
	
	/**
	 * FUNZIONE PER FARE L'UPLOAD DI UN FILE SU AWS S3
	 */
	@Override
	public void uploadFile(String keyName, String bucketName,String filePath) {
		 int maxUploadThreads = 5;

	        TransferManager tm = TransferManagerBuilder
	                .standard()
	                .withS3Client(s3client)
	                .withMultipartUploadThreshold((long) (10 * 1024 * 1024))
	                .withExecutorFactory(() -> Executors.newFixedThreadPool(maxUploadThreads))
	                .build();
	        PutObjectRequest request = new PutObjectRequest(bucketName, keyName, new File(filePath));
	        Upload upload = tm.upload(request);

	        try {
	            upload.waitForCompletion();
	        } catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	@Override
	public  File multipartToFile(MultipartFile multipart, String fileName) throws IllegalStateException, IOException {
	    File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
	    multipart.transferTo(convFile);
	    return convFile;
	}
	
	/**
	 * METODO PER FARE L'UPLOAD DEL FILE IN S3
	 */
	@Override
	public Book uploadFileS3(MultipartFile file,Book book) throws IllegalStateException, IOException {
		File fileConv = this.multipartToFile(file, file.getOriginalFilename());
		this.uploadFile(book.getPath(),book.getBucket(),fileConv.getAbsolutePath());
		return book;
	}
	
	
}
