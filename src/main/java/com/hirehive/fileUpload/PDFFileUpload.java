package com.hirehive.fileUpload;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class PDFFileUpload {
    public final String UPLOAD_DIR="D:\\HireHive\\Backend\\HireHive-Backend\\src\\main\\resources\\static\\Resumes";

    public boolean uploadFile(MultipartFile file){
        boolean f = false;

        try{
            //   2 ways of Doing it

            // 1:  =====>  by using stream API  io.package
//            InputStream inputStream = file.getInputStream();
//            byte[] data = new byte[inputStream.available()];
//            inputStream.read(data);
//            //write
//            FileOutputStream fos = new FileOutputStream(UPLOAD_DIR+"\\"+file.getOriginalFilename());
//            fos.write(data);
//            fos.flush();
//            fos.close();
//            f=true;


            // 2:  =====>  by using nio package
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR+"\\"+file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);


            f = true;

        }catch (Exception e){
            e.printStackTrace();
        }


        return f;
    }
}
