package com.aws.s3.service;

import java.io.File;
import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aws.s3.constants.CommonConstants;

public class EntryPoint {

	
	
	public static void main(String[] args) throws IOException {
		
		CommonService commonService = new CommonService();	
		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for
		// this example to work
		AWSCredentials credentials = new BasicAWSCredentials(CommonConstants.ACCESS_KEY_ID,CommonConstants.ACCESS_SEC_KEY);

		// create a client connection based on credentials
		//AmazonS3 s3client = new AmazonS3Client(credentials);
		
		AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_2)
				  .build();

		// create bucket - name must be unique for all S3 users
		String bucketName = CommonConstants.BUCKET_NAME;
		s3client.createBucket(bucketName);

		

		// create folder into bucket
		String folderName = CommonConstants.FOLDER_NAME;
		CommonService.createFolder(bucketName, folderName, s3client,CommonConstants.SUFFIX);

		// upload file to folder and set it to public
		String fileName = folderName + CommonConstants.SUFFIX + CommonConstants.FILE_NAME;
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, new File(CommonConstants.FILE_PATH))
						.withCannedAcl(CannedAccessControlList.PublicRead));

		System.out.println("Execution Completed");

		commonService.getObj(s3client);

		CommonService.deleteFolder(bucketName, folderName, s3client);

		// deletes bucket
		s3client.deleteBucket(bucketName);
	}



}