package com.bgi.edims.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.bgi.edims.model.EdiException;

public class FTPChannel {
        //ftp服务器地址
        public String hostname = "59.38.196.150";
        //ftp服务器端口号默认为21
        public Integer port = 21 ;
        //ftp登录账号
        public String username = "EDI-FTPUser";
        //ftp登录密码
        public String password = "Trcf20191231";
        
        public FTPClient ftpClient = null;
        
        public FTPChannel() {
        	
        	try {
				initFtpClient();
			} catch (EdiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
//        ftp.host=59.38.196.150
//        		ftp.port=21
//        		ftp.username=EDI-FTPUser
//        		ftp.password=123
//        		ftp.passiveMode=2
//        		ftp.encoding=utf-8
//        		ftp.clientTimeout=30000
//        		ftp.threadNum=10
//        		ftp.renameUploaded=false
//        		ftp.retryTimes=10
        /**
         * 初始化ftp服务器
         * @throws EdiException 
         */
        public void initFtpClient() throws EdiException {
            ftpClient = new FTPClient();
//            ftpClient.setControlEncoding("utf-8");
            try {
            	ftpClient.setConnectTimeout(1200*1000);
                System.out.println("connecting...ftp服务器:"+this.hostname+":"+this.port); 
                ftpClient.connect(hostname, port); //连接ftp服务器

                int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
                if(!FTPReply.isPositiveCompletion(replyCode)){
                    System.out.println("connect failed...ftp服务器:"+this.hostname+":"+this.port); 
                }
                ftpClient.login(username, password); //登录ftp服务器
                boolean result = ftpClient.login(username, password);
                if (!result) {
                     throw new EdiException("ftpClient登陆失败! userName:" +username + " ; password:" + password);
                }
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("GBK");
                System.out.println("connect successfu...ftp服务器:"+this.hostname+":"+this.port); 
            }catch (MalformedURLException e) { 
               e.printStackTrace(); 
            }catch (IOException e) { 
               e.printStackTrace(); 
            } 
        }

        /**
        * 上传文件
        * @param pathname ftp服务保存地址
        * @param fileName 上传到ftp的文件名
        *  @param originfilename 待上传文件的名称（绝对地址） * 
        * @return
        */
        public boolean uploadFile( String pathname, String fileName,String originfilename){
//            boolean flag = false;
            InputStream inputStream = null;
            try{
                System.out.println("开始上传文件");
                inputStream = new FileInputStream(new File(originfilename));
//                initFtpClient();
//                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
                createDirecroty(pathname);
//                ftpClient.makeDirectory(pathname);
                ftpClient.changeWorkingDirectory(pathname);
                ftpClient.enterLocalPassiveMode();
                ftpClient.storeFile(fileName, inputStream);
                inputStream.close();
//                ftpClient.logout();
//                flag = true;
                System.out.println("上传文件成功");
            }catch (Exception e) {
                System.out.println("上传文件失败");
                e.printStackTrace();
                return false;
            }finally{
//                if(ftpClient.isConnected()){ 
//                    try{
//                        ftpClient.disconnect();
//                    }catch(IOException e){
//                        e.printStackTrace();
//                    }
//                } 
                if(null != inputStream){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                } 
            }
            return true;
        }
        /**
         * 上传文件
         * @param pathname ftp服务保存地址
         * @param fileName 上传到ftp的文件名
         * @param inputStream 输入文件流 
         * @return
         */
        public boolean uploadFile( String pathname, String fileName,InputStream inputStream){
            boolean flag = false;
            try{
                System.out.println("开始上传文件");
//                initFtpClient();
                ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
                createDirecroty(pathname);
                ftpClient.makeDirectory(pathname);
                ftpClient.changeWorkingDirectory(pathname);
                ftpClient.storeFile(fileName, inputStream);
                inputStream.close();
//                ftpClient.logout();
                flag = true;
                System.out.println("上传文件成功");
            }catch (Exception e) {
                System.out.println("上传文件失败");
                e.printStackTrace();
            }finally{
//                if(ftpClient.isConnected()){ 
//                    try{
//                        ftpClient.disconnect();
//                    }catch(IOException e){
//                        e.printStackTrace();
//                    }
//                } 
                if(null != inputStream){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                } 
            }
            return true;
        }
        //改变目录路径
         public boolean changeWorkingDirectory(String directory) {
                boolean flag = true;
                try {
                    flag = ftpClient.changeWorkingDirectory(directory);
                    if (flag) {
                      System.out.println("进入文件夹" + directory + " 成功！");

                    } else {
                        System.out.println("进入文件夹" + directory + " 失败！开始创建文件夹");
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return flag;
            }

        //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
        public boolean createDirecroty(String remote) throws IOException {
            boolean success = true;
            String directory = remote + "/";
            // 如果远程目录不存在，则递归创建远程服务器目录
            if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
                int start = 0;
                int end = 0;
                if (directory.startsWith("/")) {
                    start = 1;
                } else {
                    start = 0;
                }
                end = directory.indexOf("/", start);
                String path = "";
                String paths = "";
                while (true) {
                    String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                    path = path + "/" + subDirectory;
                    if (!existFile(path)) {
                        if (makeDirectory(subDirectory)) {
                            changeWorkingDirectory(subDirectory);
                        } else {
                            System.out.println("创建目录[" + subDirectory + "]失败");
                            changeWorkingDirectory(subDirectory);
                        }
                    } else {
                        changeWorkingDirectory(subDirectory);
                    }

                    paths = paths + "/" + subDirectory;
                    start = end + 1;
                    end = directory.indexOf("/", start);
                    // 检查所有目录是否创建完毕
                    if (end <= start) {
                        break;
                    }
                }
            }
            return success;
        }

        public boolean renameFile(String path,String oldFileName,String fileName) {
        	boolean flag = false;
        	try {
                changeWorkingDirectory(path); 
                ftpClient.enterLocalPassiveMode();
                String ftpPath1 = new String(oldFileName.getBytes("GBK"),"iso-8859-1");
                String ftpPath2 = new String(fileName.getBytes("GBK"),"iso-8859-1");
        		flag =ftpClient.rename(ftpPath1, ftpPath2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return flag;
        }
      //判断ftp服务器文件是否存在    
        public boolean existFile(String path) throws IOException {
                boolean flag = false;
                FTPFile[] ftpFileArr = ftpClient.listFiles(path);
                if (ftpFileArr.length > 0) {
                    flag = true;
                }
                return flag;
            }
        //创建目录
        public boolean makeDirectory(String dir) {
            boolean flag = true;
            try {
                flag = ftpClient.makeDirectory(dir);
                if (flag) {
                    System.out.println("创建文件夹" + dir + " 成功！");

                } else {
                    System.out.println("创建文件夹" + dir + " 失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return flag;
        }
        
        public String[] listFileNames(String pathname) throws IOException {
            ftpClient.changeWorkingDirectory(pathname); 
            ftpClient.enterLocalPassiveMode();
            FTPFile[] ftpFiles = ftpClient.listFiles(); 
            String[] filenames=new String[ftpFiles.length];
            for(int i=0;i<ftpFiles.length;i++) {
            	filenames[i]=ftpFiles[i].getName();
            }
            return filenames;
            
        }
        /** * 下载文件 * 
        * @param pathname FTP服务器文件目录 * 
        * @param filename 文件名称 * 
        * @param localpath 下载后的文件路径 * 
        * @return */
        public  boolean downloadFile(String pathname, String filename, String localpath){ 
            boolean flag = false; 
            OutputStream os=null;
            try { 
                System.out.println("开始下载文件");
//                initFtpClient();
                //切换FTP目录 
                ftpClient.changeWorkingDirectory(pathname); 
                ftpClient.enterLocalPassiveMode();
                FTPFile[] ftpFiles = ftpClient.listFiles(); 
                for(FTPFile file : ftpFiles){ 
                    if(filename.equalsIgnoreCase(file.getName())){ 
                        File localFile = new File(localpath + "/" + file.getName()); 
                        os = new FileOutputStream(localFile); 
                        ftpClient.retrieveFile(file.getName(), os); 
                        os.close(); 
                    } 
                } 
//                ftpClient.logout(); 
                flag = true; 
                System.out.println("下载文件成功");
            } catch (Exception e) { 
                System.out.println("下载文件失败");
                e.printStackTrace(); 
            } finally{ 
//                if(ftpClient.isConnected()){ 
//                    try{
//                        ftpClient.disconnect();
//                    }catch(IOException e){
//                        e.printStackTrace();
//                    }
//                } 
                if(null != os){
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                } 
            } 
            return flag; 
        }
        
        /** * 删除文件 * 
        * @param pathname FTP服务器保存目录 * 
        * @param filename 要删除的文件名称 * 
        * @return */ 
        public boolean deleteFile(String pathname, String filename){ 
            boolean flag = false; 
            try { 
                System.out.println("开始删除文件");
//                initFtpClient();
                //切换FTP目录 
                ftpClient.changeWorkingDirectory(pathname); 
                ftpClient.enterLocalPassiveMode();
                int code=ftpClient.dele(filename); 
//                ftpClient.logout();
                flag = true; 
                System.out.println("删除文件成功"+code);
            } catch (Exception e) { 
                System.out.println("删除文件失败");
                e.printStackTrace(); 
//            } finally {
//                if(ftpClient.isConnected()){ 
//                    try{
//                        ftpClient.disconnect();
//                    }catch(IOException e){
//                        e.printStackTrace();
//                    }
//                } 
            }
            return flag; 
        }
        
        public static void main(String[] args) {
            FTPChannel ftp =new FTPChannel(); 
            ftp.deleteFile("temp2","75K.csv");
//            ftp.renameFile("temp2","75K.csv","75K1.csv");
            System.out.println("ok");
        }
}