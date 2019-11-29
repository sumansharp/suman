/**
 * 
 */
package controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

import service.PatientService;
import utils.PatientFacade;
import utils.PatientWrapper;
 

/**
 * @author Efficenz
 *
 */
@RestController
@RequestMapping("/patients")


public class PatientController {
@Autowired
PatientService ps;
 
/*
@CrossOrigin(origins = "*")
@RequestMapping(value="/createpatient",method=RequestMethod.POST)
public String createpatient (@RequestParam final String firstname,
		@RequestParam final String lastname,
		@RequestParam final String email,
		@RequestParam (value="relationship", defaultValue = "self") final String relationship,
		@RequestParam final String deviceId,
		@RequestParam final String osType
 ) {
	PatientWrapper wrapperP =  ps.userLoginSignup(firstname,lastname,email,relationship,deviceId,osType);
	return wrapperP;
	}
 
*/


@CrossOrigin(origins = "*")
@RequestMapping(value="/userLoginSignup",method=RequestMethod.POST)
public PatientWrapper userLoginSignup (@RequestParam final String firstname,
		@RequestParam final String lastname,
		@RequestParam final String email,
		@RequestParam (value="relationship", defaultValue = "self") final String relationship,
		@RequestParam final String deviceId,
		@RequestParam final String osType,
		@RequestParam final String userImgUrl
		
		
		) {
	
	PatientWrapper wrapperP =  ps.userLoginSignup(firstname,lastname,email,relationship,deviceId,osType,userImgUrl);
//	String result = pushEMR(firstname,lastname,dateofbirth,gender,mongoId);
	return wrapperP;
	}


private String pushEMR(String fname, String lname, String dob, String sex, String mongoId) {
	// TODO Auto-generated method stub
	boolean form_create = true;
	String mname="";
	String url = "http://localhost/openemr/interface/new/new_patient_save.php";
	
	try {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("POST");
		 
		String urlParameters = "fname="+fname+"&lname="+lname
								+"&mname="+mname
								+"&DOB="+dob
								+"&sex="+sex
								+"&linker="+mongoId;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr;
		wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return null;
}
/*
@CrossOrigin(origins = "*")
@RequestMapping(value="/updatepatient",method=RequestMethod.POST)
public String updatepatient (@RequestParam final String firstname, 
							 @RequestParam final String lastname, 
							 @RequestParam final String dateofbirth,
							 @RequestParam final String gender, 
							 @RequestParam final String deviceid,
							 @RequestParam final String phonenumber, 
							 @RequestParam final String ostype,
							 @RequestParam final String password
							 ,@RequestHeader( value="user")  String user,@RequestHeader( value="passkey"
							  *  )  String passkey ) {
 
	return ps.updatePatient(firstname,lastname,dateofbirth,gender,
			deviceid,phonenumber,ostype,password);
	
	}
*/

@CrossOrigin(origins = "*")
@RequestMapping(value="/updateUser",method=RequestMethod.POST)
// address, pincode, emergencyContactNumber, 
public String updatepatient (@RequestParam (value="firstName", defaultValue = "NA") final String firstName, 
							 @RequestParam (value="lastName", defaultValue = "NA") final String lastName, 
							 @RequestParam (value="dateofbirth", defaultValue = "NA") final String dateofbirth,
							 @RequestParam (value="gender", defaultValue = "NA") final String gender, 
							 @RequestParam (value="deviceid", defaultValue = "NA") final String deviceid,
							 @RequestParam (value="phonenumber", defaultValue = "NA") final String phonenumber, 
							 @RequestParam (value="ostype", defaultValue = "NA") final String ostype,
							 @RequestParam (value="address", defaultValue = "NA") final String address,
							 @RequestParam (value="pincode", defaultValue = "NA") final String pincode,
							 @RequestParam (value="emcontact", defaultValue = "NA") final String emcontact,
							 @RequestParam (value="email", defaultValue = "NA") final String email,
							 @RequestParam (value="heightFt", defaultValue = "NA") final String heightFt,
							 @RequestParam (value="heightInches", defaultValue = "NA") final String heightInches,
							 @RequestParam (value="weight", defaultValue = "NA") final String weight,
							 @RequestParam final String patientId
							  ) {
 
	return ps.updatePatient(firstName,
							lastName,
							dateofbirth,
							gender,
							deviceid,
							phonenumber,
							ostype,
							address,
							pincode,
							emcontact,
							email,
							patientId,
							heightFt,
							heightInches,
							weight);
	
	}


/*public String login(@RequestParam final String phonenumber, @RequestParam final String password ) 
{
	
	
	return ps.checkUser(phonenumber, password);
}
//Version Check for Terms And Conditions
@CrossOrigin(origins = "*")
@RequestMapping(value="/vcheck")

public String versionCheck(){
	return ps.versionCheck();
}
*/
//Forgot Password
    // Check phone number. Scramble his password. Send through push notification

// Terms and Conditions page to reside at server. Every time the user logs in
// Aditya needs to check if the versionString of T&C changes in MongoDB [
//tnc collection , field vStr ].  If it does not matches what he has in 
// local Storage , he should pull new TnC page and upon Acceptance
// update versionString in local Storage


//addFamilyMember 

//Book an appointment  -- Will be done offline via Admin / Junior doctors




// This service is to be used for creating record for a dependent record

//@CrossOrigin(origins = "*")
//@RequestMapping(value="/createchild",method=RequestMethod.POST)
//public String createchild (@RequestParam final String firstname, @RequestParam final String lastname, @RequestParam final String dateofbirth,@RequestParam final String gender, @RequestParam final String deviceid,@RequestParam final String phonenumber, @RequestParam final String ostype, @RequestParam final String password,@RequestParam final String relationship,@RequestParam final String parentid /*,@RequestHeader( value="user")  String user,@RequestHeader( value="passkey" )  String passkey */) {
	
//	String mongoId =  ps.addChild(firstname,lastname,dateofbirth,gender,deviceid,
	//		phonenumber,ostype,password,relationship,parentid);
	//String result = pushEMR(firstname,lastname,dateofbirth,gender,mongoId);
	//return mongoId;
//	}


@CrossOrigin(origins = "*")
@RequestMapping(value="/detailsp/{patientId}")
public PatientFacade getPatient(@PathVariable  ("patientId") String patientId /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {

	
	
	return ps.getPatient(patientId);
 
}



@CrossOrigin(origins="*")
@RequestMapping(value="/uploadReports",method=RequestMethod.POST)
public String uploadReports(@RequestParam(name="files", required=false) MultipartFile[] Files,@RequestParam final String patientId, @RequestParam(name="reportName", defaultValue="NA") String reportName ) {
	
	LocalDateTime today = LocalDateTime.now();
//	String pdfName = "/var/www/html/patientreports/"+patientId+"_"+today.toString()+".pdf";
	String pdfName = "/tmp/"+patientId+"_"+today.toString()+".pdf";
//https://s3-us-west-2.amazonaws.com/eff-store/58da13e9a6031ab74185d628_2017-03-30T22%3A29%3A24.424.pdf
	
	String pdfURL = "https://s3-us-west-2.amazonaws.com/eff-store/"+patientId+"_"+today.toString()+".pdf";
	
	com.itextpdf.text.Document document = new com.itextpdf.text.Document();
	
     try {
		PdfWriter.getInstance(document, new FileOutputStream(pdfName));
	} catch (FileNotFoundException | DocumentException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
     document.open();
	for(MultipartFile uploadedFile : Files) {
        File file = new File("/tmp/" + uploadedFile.getOriginalFilename());
        try {
			uploadedFile.transferTo(file);
			Image img = Image.getInstance("/tmp/" + uploadedFile.getOriginalFilename()); 
			document.setPageSize(img);
            document.newPage();
            img.setAbsolutePosition(0, 0);
            document.add(img);
		} catch (IllegalStateException | IOException |  DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	document.close();
	
	if(reportName.equals("NA"))
	{
		reportName = LocalDate.now(ZoneId.of("Asia/Kolkata")).toString();
	}
	
	 String bucket_name = "eff-store";
     String file_path = pdfName;
     String key_name = Paths.get(file_path).getFileName().toString();

     System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucket_name);
     
     BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIV4EFEUMYNDBQSUA",
    		 "P4hDSg8pjXmZwMUD1IGi6qWYodlPraaitC/nJ3Go"); 
     AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
    		 .withRegion(Regions.US_WEST_2).	
    		 build();

  // Create a list of UploadPartResponse objects. You get one of these
     // for each part upload.
     List<PartETag> partETags = new ArrayList<PartETag>();

     // Step 1: Initialize.
     InitiateMultipartUploadRequest initRequest = new 
          InitiateMultipartUploadRequest(bucket_name, key_name);
     InitiateMultipartUploadResult initResponse = 
     	                   s3.initiateMultipartUpload(initRequest);

     File file = new File(file_path);
     long contentLength = file.length();
     System.out.println(contentLength);
     long partSize = 5242880; // Set part size to 5 MB.

     try {
         // Step 2: Upload parts.
         long filePosition = 0;
         for (int i = 1; filePosition < contentLength; i++) {
             // Last part can be less than 5 MB. Adjust part size.
         	partSize = Math.min(partSize, (contentLength - filePosition));
         	
             // Create request to upload a part.
             UploadPartRequest uploadRequest = new UploadPartRequest()
                 .withBucketName(bucket_name).withKey(key_name)
                 .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                 .withFileOffset(filePosition)
                 .withFile(file)
                 .withPartSize(partSize);

             // Upload part and add response to our list.
             partETags.add(
             		s3.uploadPart(uploadRequest).getPartETag());

             filePosition += partSize;
         }

         // Step 3: Complete.
         CompleteMultipartUploadRequest compRequest = new 
                      CompleteMultipartUploadRequest(
                                 bucket_name, 
                                 key_name, 
                                 initResponse.getUploadId(), 
                                 partETags);

         s3.completeMultipartUpload(compRequest);
         
     } catch (Exception e) {
         s3.abortMultipartUpload(new AbortMultipartUploadRequest(
                 bucket_name, key_name, initResponse.getUploadId()));
     }
	
	
	
		return ps.AddReport(patientId,reportName,pdfURL);
	
	
//	return "{\"responseCode\":\"success\",\"responseMessage\":\"\"}";

}

@CrossOrigin(origins = "*")
@RequestMapping(value="/getReports/{patientId}")
public HashMap<String,Object> getReports(@PathVariable  ("patientId") String patientId /*@RequestHeader( value="user")  String user,@RequestHeader( value="passkey")  String passkey */) {

	
	
	return ps.getReports(patientId);
 
}


@RequestMapping(value="/uploadImages",method = RequestMethod.POST)
public String uploadImages(@RequestParam("patientId") String patientId,
		@RequestParam(name="reportName", defaultValue="NA") String reportName,
		@RequestParam("images") ArrayList<String> encodedStringsList
		){
	
	

	LocalDateTime today = LocalDateTime.now();

	String pdfName = "/tmp/"+patientId+"_"+today.toString()+".pdf";
	
	String pdfURL = "https://s3-us-west-2.amazonaws.com/eff-store/"+patientId+"_"+today.toString()+".pdf";
	
	com.itextpdf.text.Document document = new com.itextpdf.text.Document();
	
     try {
		PdfWriter.getInstance(document, new FileOutputStream(pdfName));
	} catch (FileNotFoundException | DocumentException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
     document.open();
	
	
	if(encodedStringsList != null && encodedStringsList.size()>0){
		for(int i = 0 ; i < encodedStringsList.size();i++){
			try{
				byte[] byte_arr = Base64.decode(encodedStringsList.get(i), 0);
				String outputFileName = "/tmp/"+patientId+System.currentTimeMillis()+".jpg";
				System.out.println(outputFileName);
				File outputFile = new File(outputFileName);
				FileOutputStream outputStream = new FileOutputStream(outputFile );
				outputStream.write(byte_arr);
				outputStream.close();
				
				Image img = Image.getInstance(outputFileName); 
				document.setPageSize(img);
	            document.newPage();
	            img.setAbsolutePosition(0, 0);
	            document.add(img);
				

			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		}
	}
	
document.close();
	
	if(reportName.equals("NA"))
	{
		reportName = LocalDate.now(ZoneId.of("Asia/Kolkata")).toString();
	}
	
	 String bucket_name = "eff-store";
     String file_path = pdfName;
     String key_name = Paths.get(file_path).getFileName().toString();

     System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucket_name);
     
     BasicAWSCredentials creds = new BasicAWSCredentials("AKIAIV4EFEUMYNDBQSUA",
    		 "P4hDSg8pjXmZwMUD1IGi6qWYodlPraaitC/nJ3Go"); 
     AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds))
    		 .withRegion(Regions.US_WEST_2).	
    		 build();

  // Create a list of UploadPartResponse objects. You get one of these
     // for each part upload.
     List<PartETag> partETags = new ArrayList<PartETag>();

     // Step 1: Initialize.
     InitiateMultipartUploadRequest initRequest = new 
          InitiateMultipartUploadRequest(bucket_name, key_name);
     InitiateMultipartUploadResult initResponse = 
     	                   s3.initiateMultipartUpload(initRequest);

     File file = new File(file_path);
     long contentLength = file.length();
     System.out.println(contentLength);
     long partSize = 5242880; // Set part size to 5 MB.

     try {
         // Step 2: Upload parts.
         long filePosition = 0;
         for (int i = 1; filePosition < contentLength; i++) {
             // Last part can be less than 5 MB. Adjust part size.
         	partSize = Math.min(partSize, (contentLength - filePosition));
         	
             // Create request to upload a part.
             UploadPartRequest uploadRequest = new UploadPartRequest()
                 .withBucketName(bucket_name).withKey(key_name)
                 .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                 .withFileOffset(filePosition)
                 .withFile(file)
                 .withPartSize(partSize);

             // Upload part and add response to our list.
             partETags.add(
             		s3.uploadPart(uploadRequest).getPartETag());

             filePosition += partSize;
         }

         // Step 3: Complete.
         CompleteMultipartUploadRequest compRequest = new 
                      CompleteMultipartUploadRequest(
                                 bucket_name, 
                                 key_name, 
                                 initResponse.getUploadId(), 
                                 partETags);

         s3.completeMultipartUpload(compRequest);
         
     } catch (Exception e) {
         s3.abortMultipartUpload(new AbortMultipartUploadRequest(
                 bucket_name, key_name, initResponse.getUploadId()));
     }
	
	
	
		return ps.AddReport(patientId,reportName,pdfURL);

	
//	return "";
}




}
