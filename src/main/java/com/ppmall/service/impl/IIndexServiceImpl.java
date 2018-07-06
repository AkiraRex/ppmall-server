package com.ppmall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ppmall.service.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppmall.common.Const;
import com.ppmall.common.ServerResponse;
import com.ppmall.dao.AdvertMapper;
import com.ppmall.dao.CategoryMapper;
import com.ppmall.dao.ProductMapper;
import com.ppmall.pojo.Advert;
import com.ppmall.pojo.Category;
import com.ppmall.util.PropertiesUtil;
import com.ppmall.vo.HotProductVo;

@Service("iIndexService")
public class IIndexServiceImpl implements IIndexService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private AdvertMapper advertMapper;

    @Override
    public ServerResponse getIndexData() {
        // TODO Auto-generated method stub
        List<HotProductVo> hotProductList = productMapper.selectHotProduct(3);
        List categoryProductList = new ArrayList<>();

        for (int i = 0; i < hotProductList.size(); i++) {
            int categoryId = hotProductList.get(i).getCategoryId();
            String categoryName = categoryMapper.selectByPrimaryKey(categoryId).getName();

            Map paramMap = new HashMap<>();
            paramMap.put("categoryId", categoryId);
            List hotCategoryProducts = productMapper.selectAll(paramMap);
            if (hotCategoryProducts != null && hotCategoryProducts.size() > 3)
                hotCategoryProducts = hotProductList.subList(0, 3);

            Map dataMap = new HashMap<>();
            dataMap.put("name", categoryName);
            dataMap.put("id", categoryId);
            dataMap.put("productList", hotCategoryProducts);
            categoryProductList.add(dataMap);
        }

        for (int i = 0; i < categoryProductList.size() - 1; i++) {
            for (int j = categoryProductList.size() - 1; j > i; j--) {
                int iid = hotProductList.get(i).getCategoryId();
                int jid = hotProductList.get(j).getCategoryId();
                if (iid == jid)
                    categoryProductList.remove(j);
            }
        }


//		List<Category> hotCategory = categoryMapper.selectCategoryByIds(categoryIds);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("beginTime", new Date(new Date().getTime() - Const.ExpiredType.ONE_DAY * 7));
        paramMap.put("endTime", new Date());
        List newProductList = productMapper.selectAll(paramMap);

        List categoryList = categoryMapper.selectCategoryByParentId(0);

        Advert advert = new Advert();
        advert.setPosition(Const.AdvertPosition.INDEX_BANNER);
        advert.setStatus(Const.ValidStatus.VALID);
        List bannerList = advertMapper.selectBySelective(advert);

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("hotProductList", hotProductList);
        returnMap.put("newProductList", newProductList);
        returnMap.put("categoryList", categoryList);
        returnMap.put("bannerList", bannerList);
        returnMap.put("categoryProductList", categoryProductList);
        returnMap.put("imageHost", PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return ServerResponse.createSuccess(returnMap);
    }

}
