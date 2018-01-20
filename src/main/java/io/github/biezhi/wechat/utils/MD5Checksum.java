package io.github.biezhi.wechat.utils;

import java.io.*;
import java.security.MessageDigest;

public class MD5Checksum {

   public static byte[] createChecksum(String filename) throws Exception {
       InputStream fis =  new FileInputStream(filename);

       byte[] buffer = new byte[1024];
       MessageDigest complete = MessageDigest.getInstance("MD5");
       int numRead;

       do {
           numRead = fis.read(buffer);
           if (numRead > 0) {
               complete.update(buffer, 0, numRead);
           }
       } while (numRead != -1);

       fis.close();
       return complete.digest();
   }

   // see this How-to for a faster way to convert
   // a byte array to a HEX string
   public static String getMD5Checksum(String filename) {
       try {
           byte[] b = createChecksum(filename);
           String result = "";

           for (int i=0; i < b.length; i++) {
               result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
           }
           return result;
       } catch (Exception e){
           return "";
       }
   }

   public static void main(String args[]) {
       try {
           System.out.println(getMD5Checksum("apache-tomcat-5.5.17.exe"));
           // output :
           //  0bb2827c5eacf570b6064e24e0e6653b
           // ref :
           //  http://www.apache.org/dist/
           //          tomcat/tomcat-5/v5.5.17/bin
           //              /apache-tomcat-5.5.17.exe.MD5
           //  0bb2827c5eacf570b6064e24e0e6653b *apache-tomcat-5.5.17.exe
       }
       catch (Exception e) {
           e.printStackTrace();
       }
   }
}