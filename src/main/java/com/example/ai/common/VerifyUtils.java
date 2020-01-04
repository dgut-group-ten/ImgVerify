package com.example.ai.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

import org.json.JSONObject;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONException;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageInputStream;

public class VerifyUtils {
    //身份证识别
    public static JSONObject IDCardRecogize(String filePath) {
        AipOcr client = new AipOcr(BaiduConfig.APP_ID, BaiduConfig.API_KEY, BaiduConfig.SECRET_KEY);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");
        //背面照
        //String idCardSide = "back";

        //前面照
        String idCardSide = "front";

        // 参数为本地图片路径
        //String image = "D:\\back.jpg";
        //String image = "D:\\front.jpg";
        String image = filePath;//身份证照片路径
        JSONObject res = client.idcard(image, idCardSide, options);
        System.out.println(res.toString(2));

        // 参数为本地图片二进制数组
        byte[] file = readImageFile(image);
        res = client.idcard(file, idCardSide, options);
        System.out.println(res.toString(2));

        return res;
    }

    //车牌识别
    public static JSONObject plateLicense(String filePath) {
        String image = filePath;//照片路径
        byte[] img = readImageFile(image);
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(BaiduConfig.APP_ID, BaiduConfig.API_KEY, BaiduConfig.SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        // client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理
        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", ExportWordUtil.class.getResource("/").getPath() +"/log4j.properties");
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("multi_detect", "false");
        // 调用接口
        JSONObject res = client.plateLicense(img, options);
        try {
            System.out.println(res.toString(2));
            if (res.get("words_result") != null) {
                String number = res.getJSONObject("words_result").getString("number");
                //System.out.println(number);
                return res;
            } else {
                //失败打印错误信息
                System.out.println(res.getString("error_msg"));
            }
        } catch (JSONException e) {
            //异常信息
            System.out.println(e.getMessage());
        }
        return null;
    }

    //发票识别
    public static JSONObject receipt(String filePath) {
        //api返回的json对象
        JSONObject jsonObject = new JSONObject();
        //以token信息创建api调用对象
        AipOcr aipOcr = new AipOcr(BaiduConfig.APP_ID, BaiduConfig.API_KEY, BaiduConfig.SECRET_KEY);
        //定义装图片信息的byte数组
//        byte[] img=null;
        //图片本地地址
        String path = filePath;
        //转换为byte数组
//        img=image2byte(path);
        byte[] img = readImageFile(path);
        //调用Api可选参数，把返回文字外接多边形顶点位置设为true
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("vertexes_location", "true");

        //请求百度接口识别图片
        jsonObject = aipOcr.general(img, null);
//      int logid=jsonObject.getInt("log_id");
//      System.out.println("log_id"+logid);
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = (JSONObject) jsonArray.get(i);
            String words = object.getString("words");
            JSONObject location = (JSONObject) object.get("location");
            System.out.println("words  " + words);
            System.out.println("location" + location);
        }

        return jsonObject;
    }

    //文字卡片识别
    public static JSONObject result(String filePath) {
        String path = filePath;
        byte[] img = readImageFile(path);
        // 传入可选参数调用接口
        AipOcr client = new AipOcr(BaiduConfig.APP_ID, BaiduConfig.API_KEY, BaiduConfig.SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true"); // 检测图片朝上
        options.put("vertexes_location", "true");
        options.put("recognize_granularity", "big");
        options.put("detect_language", "true");  // 检测语言,默认是不检查
        options.put("probability", "true");   //是否返回识别结果中每一行的置信度
        JSONObject res = client.basicAccurateGeneral(img, options);
        System.out.println(res.toString(2));
        return res;
    }

    /**
     * 将图像转为二进制数组
     *
     * @param path
     * @return
     */
    public static byte[] readImageFile(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }


    public static InetAddress getLANAddressOnWindows() {
        try {
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                if (nif.getName().startsWith("wlan")) {
                    Enumeration<InetAddress> addresses = nif.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        if (addr.getAddress().length == 4) { // 速度快于 instanceof
                            return addr;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }

    public static String getTargetFile(String filePath, String absolutePath){
        String[] list = new File(absolutePath).list();
        String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
        String src = null;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(fileName) || list[i].contains(fileName)){
                src = absolutePath + list[i];
                return src;
            }
        }

        return null;
    }
}
