package it.ebookservice.aws.s3;

import java.io.File;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;


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
}
