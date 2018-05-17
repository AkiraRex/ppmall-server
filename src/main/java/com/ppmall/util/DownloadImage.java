package com.ppmall.util;

import com.github.pagehelper.PageInfo;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.ProductMapper;
import com.ppmall.pojo.Product;
import com.ppmall.service.IProductService;
import com.ppmall.service.impl.ProductServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DownloadImage {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("开始下载");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        ProductMapper productMapper = (ProductMapper) context.getBean("productMapper");
        List<Product> productList = productMapper.selectAll(null);

//        src="http://img.happymmall.com/00bce8d4-e9af-4c8d-b205-e6c75c7e252b.jpg"
//        String regex = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        String regex = "src\\s*=\\s*\"?(.*?)(\"|>|\\s+)";
        Pattern pattern = Pattern.compile(regex);

        List<String> imgs = new ArrayList<>();

        for (Product product : productList) {
//            String subImg = product.getSubImages();
//            String subImgArrays[] = subImg.split(",");

            String detail = product.getDetail();
            Matcher matcher = pattern.matcher(detail);
            while (matcher.find()) {
                String content = matcher.group();
                content = content.substring(content.lastIndexOf("/"), content.length() - 1);
                imgs.add(content);
            }

        }
        for (int i = 0; i < imgs.size(); i++) {
            download("http://img.happymmall.com/" + imgs.get(i), imgs.get(i), "g:\\imgextra\\");
        }
        System.out.println("下載完成");
    }

    public static void download(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

}