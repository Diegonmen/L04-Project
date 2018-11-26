package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Category;
import services.CategoryService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class CategoryServiceTest extends AbstractTest { 

@Autowired 
private CategoryService	categoryService; 

@Test 
public void saveCategoryTest(){ 
Category category, saved;
Collection<Category> categorys;
category = categoryService.findAll().iterator().next();
category.setVersion(57);
saved = categoryService.save(category);
categorys = categoryService.findAll();
Assert.isTrue(categorys.contains(saved));
} 

@Test 
public void findAllCategoryTest() { 
Collection<Category> result; 
result = categoryService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneCategoryTest(){ 
Category category = categoryService.findAll().iterator().next(); 
int categoryId = category.getId(); 
Assert.isTrue(categoryId != 0); 
Category result; 
result = categoryService.findOne(categoryId); 
Assert.notNull(result); 
} 

@Test 
public void deleteCategoryTest() { 
Category category = categoryService.findAll().iterator().next(); 
Assert.notNull(category); 
Assert.isTrue(category.getId() != 0); 
Assert.isTrue(this.categoryService.exists(category.getId())); 
this.categoryService.delete(category); 
} 

} 
