package com.wang.verifycode.kaptcha.web;

import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author wangjubin
 */
@RestController
@RequestMapping(value = "kaptcha")
public class KaptchaController {

    private final Producer producer;

    @Autowired
    public KaptchaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/vc")
    public String getVerifyCode(HttpSession httpSession) throws IOException {
        //1.生成验证码
        String code = producer.createText();
        httpSession.setAttribute("kaptcha", code);
        BufferedImage bi = producer.createImage(code);
        //2.写入内存
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(bi, "png", fos);
        //3.生成 base64
        return Base64.encodeBase64String(fos.toByteArray());
    }
}

