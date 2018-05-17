package com.ppmall.controller.backend;

import com.ppmall.common.Const;
import com.ppmall.common.ResponseCode;
import com.ppmall.common.ServerResponse;
import com.ppmall.pojo.Product;
import com.ppmall.pojo.User;
import com.ppmall.service.IFileService;
import com.ppmall.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> getProductList(HttpSession session, int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Integer productId, String productName) {
        Map paramMap = new HashMap();
        paramMap.put("productName", productName);
        paramMap.put("productId", productId);
        return iProductService.getProductList(pageNum, pageSize, paramMap);

    }

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> saveProduct(HttpSession session, Product product) {
        return iProductService.saveProduct(product);
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> getDetail(HttpSession session, int productId) {
        return iProductService.getDetailById(productId);
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> uploadFile(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        String path = session.getServletContext().getRealPath("upload");
        try {
            return iFileService.uploadFile(file, path);
        } catch (IOException e) {
            e.printStackTrace();
            return ServerResponse.createErrorMessage("上传失败");
        }

    }

    @RequestMapping(value = "set_sale_status", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setProductStatus(Product product, HttpSession session) {
        return iProductService.setStatus(product);
    }
}
