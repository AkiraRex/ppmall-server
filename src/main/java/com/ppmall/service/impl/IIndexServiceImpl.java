package com.ppmall.service.impl;

import java.util.*;

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
        Set categorySet = new HashSet<>();

        for (int i = 0; i < hotProductList.size(); i++) {
            int categoryId = hotProductList.get(i).getCategoryId();

            if (categorySet.contains(categoryId))
                continue;

            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            categorySet.add(categoryId);

            Map paramMap = new HashMap<>();
            paramMap.put("categoryId", categoryId);
            List hotCategoryProducts = productMapper.selectAll(paramMap);
            if (hotCategoryProducts != null && hotCategoryProducts.size() > 3)
                hotCategoryProducts = hotCategoryProducts.subList(0, 3);

            Map dataMap = new HashMap<>();
            dataMap.put("name", category.getName());
            dataMap.put("id", categoryId);
            dataMap.put("parentId", category.getParentId());
            dataMap.put("productList", hotCategoryProducts);
            categoryProductList.add(dataMap);
        }


//		List<Category> hotCategory = categoryMapper.selectCategoryByIds(categoryIds);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("beginTime", new Date(new Date().getTime() - Const.ExpiredType.ONE_DAY * 7));
        paramMap.put("endTime", new Date());
        List newProductList = productMapper.selectAll(paramMap);
        newProductList = newProductList.subList(0, 10);
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
