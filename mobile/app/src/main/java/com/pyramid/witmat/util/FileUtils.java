package com.pyramid.witmat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;


/**
 * Create by lxl 2019/05/24.
 */

public class FileUtils {
    private String SDPATH;

    public FileUtils() {

    }


    public String getSDPATH() {
        return SDPATH;
    }
    public static File getDefaultFileDir(String childPath, Context context) {
        try {
            File fileDir = new File(Environment.getExternalStorageDirectory().toString() + File.separator +"表格存放");//仅创建路径的File对象
            if (!fileDir.exists()) {
                fileDir.mkdir();//如果路径不存在就先创建路径
            }

//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            File file = new File(filePath);
//            Uri uri = Uri.fromFile(file);
//            intent.setData(uri);
//            context.sendBroadcast(intent);
            if (!childPath.trim().isEmpty()) {
                fileDir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + EnvironmentUtil.fileNameFromAppPage(context) + File.separator + childPath);
                if (!fileDir.exists()) {
                    fileDir.mkdir();//如果路径不存在就先创建路径
                }
            }

            return fileDir;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length >= 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                try {
                    //substr.trim();
                    substr = new String(substr.getBytes("8859_1"), "GB2312");

                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                ret = new File(ret, substr);

            }
            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            try {
                //substr.trim();
                substr = new String(substr.getBytes("8859_1"), "GB2312");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            ret = new File(ret, substr);
            return ret;
        }
        return ret;
    }
    public static String getDefaultFilePath(String childPath, String fileName) {
        try {
            File fileDir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + childPath);//仅创建路径的File对象

            if (!fileDir.exists()) {
                fileDir.mkdir();//如果路径不存在就先创建路径
            }

            if (!childPath.trim().isEmpty()) {
                fileDir = new File(Environment.getExternalStorageDirectory().toString() + File.separator + childPath);
                if (!fileDir.exists()) {
                    fileDir.mkdir();//如果路径不存在就先创建路径
                }
            }

//			File file = new File(fileDir,fileName);//然后再创建路径和文件的File对象
            File file = null;
            try {
                file = new File(Environment.getExternalStorageDirectory().toString() + File.separator + childPath + File.separator + fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                return file.getPath();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 获取默认的文件路径
     *
     * @return
     */
    public static String getDefaultFilePath(String fileName) {
        return getDefaultFilePath("", fileName);
    }
    /**
     * 删除文件或文件夹
     */
    public static void deleteFile(File file, boolean isDeleteDir) {

        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File[] files = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i], true); // 把每个文件 用这个方法进行迭代
                }

                if (isDeleteDir)
                    file.delete();
            } else
                file.delete();

        } else {
            //
        }
    }
    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }
    public FileUtils(String SDPATH){
        //得到外部存储设备的目录（/SDCARD）
        SDPATH = Environment.getExternalStorageDirectory() + "/" ;
    }

    /**
     * 在SD卡上创建文件
     * @param fileName
     * @return

     */
    public static File createSDFile(Context context, String fileName)  {
        File file = new File(context.getExternalCacheDir() +"/"+ fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    public static File createSDFile(String filePath, String filename)  {
        File fileFull = new File(filePath, filename);
        if (fileFull.exists()) {
            fileFull.delete();
        } else {
            File fileDir = new File(filePath);
            if (!fileDir.exists())
                fileDir.mkdirs();
        }
        try {
            fileFull.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileFull;
    }

    /**
     * 在SD卡上创建目录
     * @param dirName 目录名字
     * @return 文件目录
     */
    public File createDir(String dirName){
        File dir = new File(SDPATH + dirName);
        if(!dir.exists())
        dir.mkdir();
        return dir;
    }

    /**
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName){
        File file = new File(SDPATH + fileName);
        return file.exists();
    }
    private File rootFile = Environment.getExternalStorageDirectory();
    private final static String DOT = ".";

    /**
     * 新建目录
     *
     * @param folderPath
     */
    public void newFolder(String folderPath) {
        String filePath = folderPath;
        File myFilePath = new File(filePath);
        if (!myFilePath.exists()) {
            myFilePath.mkdir();
        }
    }

    /**
     * 新建文件
     *
     * @param path
     * @param fileContent
     */
    public static void newFile(String path, String fileContent) {
        String filePath = path;
        File myFilePath = new File(filePath);
        BufferedWriter bw;
        try {
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(myFilePath, true));
            bw.write(fileContent);
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean writeFile(String filePath, String content) {

        newFile(filePath, content);
        return true;

    }

    /**
     * 以字节为单位读取文件，如图片，声音，音频文件
     *
     * @param fileName
     */
    public static void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = is.read()) != -1) {

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @param filename
     * @param fileContent
     */
    public static void write2File(String filePath, String filename, String fileContent) {
        fileContent=fileContent.replace("\",\"","\n");
        try {
            File fileFull = new File(filePath, filename);
            if (fileFull.exists()) {
                fileFull.delete();
            } else {
                File fileDir = new File(filePath);
                if (!fileDir.exists())
                    fileDir.mkdirs();
            }
            fileFull.createNewFile();

//			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(fileFull), PreferHelper
// .NOVEL_CONTENT_ENCODING);
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(fileFull), "UTF-8");
            BufferedWriter writer = new BufferedWriter(write);
            // writer.write(fileContent);
            writer.append(fileContent);
            writer.close();
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 保存内容到文件中
     *
     * @param filePath 路径
     * @param fileName 文件名
     * @param content  内容
     * @return
     */
    public static boolean saveToFile(String filePath, String fileName, String content) {

        RandomAccessFile randomFile;

        try {
            File file1 = new File(filePath, fileName);
            if (!file1.exists()) {
                File file2 = new File(filePath);
                if (!file2.exists())
                    file2.mkdirs();
            }
            file1.createNewFile();
            randomFile = new RandomAccessFile(file1, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            // 写入乱码处理
            // String h = new String(content.getBytes("iso-8859-1"),"GB2312");
            randomFile.write(content.getBytes("GB2312"));
            randomFile.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 在文件末尾追加内容
     *
     * @param fileName
     * @param content
     * @return true 追加成功，反之失败
     */
    public static boolean saveToFile2(String filePath, String fileName, String content) {
        try {
            File file1 = new File(filePath, fileName);
            if (!file1.exists()) {
                File file2 = new File(filePath);
                if (!file2.exists())
                    file2.mkdirs();
            }
            file1.createNewFile();

            FileWriter writer = new FileWriter(file1, true);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 计算文件夹的总大小
     *
     * @param file
     * @return
     */
    public static long getDirSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if(children==null)
                    return 0;
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {// 如果是文件则直接返回其大小,以“兆”为单位
                long size = file.length();
                return size;
            }
        } else {
            return 0;
        }
    }

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileLen(long size) {
        if (size >= 1024 * 1024 * 1024) {
            return String.format("%.1fG", (size * 1.0) / (1024 * 1024 * 1024));
        }
        if (size > 1024 * 1024) {
            return String.format("%.1fM", (size * 1.0) / (1024 * 1024));
        }
        if (size > 1024) {
            return String.format("%.1fK", (size * 1.0) / 1024);
        }
        return size + "B";
    }

    /**
     * 获取扩展名
     *
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        if (-1 == fileName.indexOf(DOT))
            return "";
        String ext = fileName.substring(fileName.lastIndexOf(DOT) + 1);
        return ext.trim();
    }

    /**
     * @param myContext
     * @param ASSETS_NAME
     * @param savePath
     * @param saveName
     * @Func copy Assets 目录下的文件
     * @date 2016-7-28 下午7:41:51
     */
    public static void copy(Context myContext, String ASSETS_NAME, String savePath, String saveName) {
        String filename = savePath + "/" + saveName;
        File dir = new File(savePath);
        // 如果目录不中存在，创建这个目录
        if (!dir.exists())
            dir.mkdirs();
        try {
            if (!(new File(filename)).exists()) {
                InputStream is = myContext.getResources().getAssets().open(ASSETS_NAME);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param bitmap
     * @param name
     * @return
     * @Func 保存图片
     * @date 2016-8-10 下午4:26:02
     */
    public static boolean storeImageToFile(Bitmap bitmap, String path, String name) {
        if (bitmap == null) {
            return false;
        }
        File file = null;
        RandomAccessFile accessFile = null;
        ByteArrayOutputStream steam = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 72, steam);
        byte[] buffer = steam.toByteArray();
        try {
            File pFile = new File(path);
            if (!pFile.exists()) {
                pFile.mkdirs();
            }
            path = path + "/" + name + ".jpg";
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            accessFile = new RandomAccessFile(file, "rw");
            accessFile.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            steam.close();
            accessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param file
     * @Func 删除文件
     * @date 2016-8-10 下午4:28:57
     */
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
        }
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param delpath String
     * @return boolean
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean deleteFolder(String delpath) throws Exception {
        try {
            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "/" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
//                        if(!filelist[i].startsWith("native"))
                        deleteFolder(delpath + "/" + filelist[i]);
                    }
                }
                file.delete();
            }

        } catch (FileNotFoundException e) {
        }
        return true;
    }
}