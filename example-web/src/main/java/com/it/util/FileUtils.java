package com.it.util;


import com.google.common.collect.Lists;
import com.it.common.util.ConvertUtils;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

/**
 * @author zhaowq
 * @description 文件工具类
 * @create 2017-12-05 上午 11:02
 **/
public final class FileUtils {
    public static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final String DIGEST_TYPE = "MD5";
    private static final int BUF_SIZE = 8192;

    public static String getDigest(MultipartFile file) {
        String digest = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_TYPE);
            InputStream fis = file.getInputStream();
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = fis.read(buf)) != -1) {
                messageDigest.update(buf, 0, len);
            }
            fis.close();
            digest = ConvertUtils.bytesToHexString(messageDigest.digest());
            messageDigest.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;
    }

    /**
     * 返回斜杠拼接的字符串
     *
     * @param args
     * @return
     */
    public static String mergeStringWithSeparator(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
            sb.append(File.separator);
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String getImageInfo(String urlPath) {
        String file = "";
        try {
            byte[] byteBuffer;
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            InputStream is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                byteBuffer = readInputStream(is);
            } else {
                byteBuffer = null;
            }
            // 图片文件的字节码 转字符串
            Base64 base64 = new Base64();
            file = base64.encodeToString(byteBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 读取InputStream数据，转为byte[]数据类型
     *
     * @param is InputStream数据
     * @return 返回byte[]数据
     */
    private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        try {
            is.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 文件下载
     *
     * @param filePath
     * @param request
     * @param response
     * @throws Exception
     */
    public static void downFile(String filePath, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //1.得到要下载的文件
        File file = new File(filePath);
        //2.获取要下载的文件名
        String fileName = file.getName();
        String userAgent = request.getHeader("User-Agent");
        byte[] bytes = userAgent.contains("MSIE") ? fileName.getBytes()
                : fileName.getBytes("UTF-8"); // fileName.getBytes("UTF-8")处理乱码问题
        fileName = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码
        //3.设置content-disposition响应头控制浏览器以下载的形式打开文件
        response.setHeader("Content-disposition",
                String.format("attachment; filename=\"%s\"", fileName));
        //4.获取要下载的文件输入流
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {//获取要下载的文件的绝对路径
            in = new BufferedInputStream(new FileInputStream(filePath));
            //5.创建数据缓冲区
            byte[] buffer = new byte[1024];
            //6.通过response对象获取OutputStream流
            out = new BufferedOutputStream(response.getOutputStream());
            //7.将FileInputStream流写入到buffer缓冲区
            int len = 0;
            //in.read(byte[] b)最多读入b.length个字节 在碰到流的结尾时 返回-1
            while ((len = in.read(buffer)) > 0) {
                //8.使用OutputStream将缓冲区的数据输出到客户端浏览器
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取请求文件
     *
     * @param request
     * @param name
     * @return
     * @throws IOException
     */
    public static List<MultipartFile> getMultipartFileList(HttpServletRequest request, String name) throws IOException {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (CollectionUtils.isEmpty(fileMap)) {
                return null;
            }
            List<MultipartFile> list = Lists.newArrayList();
            for (int i = 0; i < fileMap.size(); ++i) {
                String currentFileName = String.format("%s%d", name, i + 1);
                if (fileMap.containsKey(currentFileName)) {
                    MultipartFile file = fileMap.get(currentFileName);
                    if (!file.isEmpty()) {
                        list.add(file);
                    }
                }
            }
            return list;
        }
        return null;
    }
}
