package com.ppmall.test.sevice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.ppmall.common.ServerResponse;
import com.ppmall.service.ICategoryService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class CategoryTest {
	@Autowired
	private ICategoryService iCategoryService;
	
	@Test
	public void test() {
		ServerResponse response = iCategoryService.getAllCategoryList();
		System.out.println(response);
	}
}
