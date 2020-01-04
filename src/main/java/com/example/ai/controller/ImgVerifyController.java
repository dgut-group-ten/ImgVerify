package com.example.ai.controller;

import com.example.ai.common.ResponseEntity;
import com.example.ai.common.VerifyUtils;
import com.example.ai.dao.DBDao;
import com.example.ai.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping
public class ImgVerifyController {
    public static final String absolutePath = "F:\\workplace\\workspace-sts-3.9.8.RELEASE\\TranceImg\\src\\main\\resources\\";
    @Autowired
    DBDao dbDao;

    /**
     * 获取文件列表
     *
     * @return 识别结果
     */
    @RequestMapping(value = "/getFiles", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity getFilePath() throws UnknownHostException {
        //String[] list = new File(absolutePath).list();
        Iterable<Image> images = dbDao.findAll();
        List<Image> list = new ArrayList<>();
//        HashMap<Long, String> list = new HashMap<>();
        String localIp = VerifyUtils.getLANAddressOnWindows().getHostAddress();
//        for (int i = 0; i < list.length; i++) {
//            System.out.println(list[i]);
//            list[i] = "http://" + localIp + ":8080/img/" + list[i];
//        }
        for (Image image : images) {
            image.setUrl("http://" + localIp + ":8080/img/" + image.getUrl().substring(image.getUrl().lastIndexOf("\\") + 1));
            list.add(image);
        }
        return new ResponseEntity().message("图片列表获取成功").data(list);
    }

    /**
     * 身份证识别
     *
     * @param id 文件路径
     * @return 识别结果
     */
    @RequestMapping(value = "/IDCardRecognize", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String IDCardRecognize(@RequestParam Long id) {
        if (id == null) return "图片不存在！";
        Image img = dbDao.findById(id).orElse(null);
        //String src = VerifyUtils.getTargetFile(filePath, absolutePath);
        //if (src == null) return "无目标图片！！！";
//        HashMap<String, Object> result = new HashMap<>();

        return VerifyUtils.IDCardRecogize(img.getUrl()).toString();
    }

    /**
     * 车牌识别
     *
     * @param id 文件路径
     * @return 识别结果
     */
    @RequestMapping(value = "/plateLicense", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String plateLicense(@RequestParam Long id) {
        if (id == null) return "图片不存在！";
        Image img = dbDao.findById(id).orElse(null);
        //String src = VerifyUtils.getTargetFile(filePath, absolutePath);
        return VerifyUtils.plateLicense(img.getUrl()).toString();
    }

    /**
     * 发票识别
     *
     * @param id 文件路径
     * @return 识别结果
     */
    @RequestMapping(value = "/receipt", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String receipt(@RequestParam Long id) {
        if (id == null) return "图片不存在！";
        Image img = dbDao.findById(id).orElse(null);
        //String src = VerifyUtils.getTargetFile(filePath, absolutePath);
        return VerifyUtils.receipt(img.getUrl()).toString();
    }

    /**
     * 图片文字识别
     *
     * @param id 文件路径
     * @return 识别结果
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String result(@RequestParam Long id) {
        if (id == null) return "图片不存在！";
        Image img = dbDao.findById(id).orElse(null);
        //String src = VerifyUtils.getTargetFile(filePath, absolutePath);
        return VerifyUtils.result(img.getUrl()).toString();
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public String saveFile() {
        String[] list = new File(absolutePath).list();
        for (int i = 0; i < list.length; i++) {
            String filePath = list[i];
            String src = VerifyUtils.getTargetFile(filePath, absolutePath);
            dbDao.save(new Image(src));
        }

        return "success";
    }

    @RequestMapping(value = "/saveOne", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public String saveOneFile(@RequestParam String path) {
        System.out.println(path);
        return "success";
    }
}
