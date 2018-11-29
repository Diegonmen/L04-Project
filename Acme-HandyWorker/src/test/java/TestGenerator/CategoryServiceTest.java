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
import repositories.AdministratorRepository;
import services.CategoryService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class CategoryServiceTest extends AbstractTest { 

@Autowired 
private CategoryService	categoryService; 

@Autowired
private AdministratorRepository administratorRepository;

@Test 
public void saveCategoryTest(){ 
Category category, saved;
Collection<Category> categorys;
this.authenticate(this.administratorRepository.findAll().iterator().next().getUserAccount().getUsername());
category = categoryService.findAll().iterator().next();
category.setName("Test Name");;
saved = categoryService.save(category);
categorys = categoryService.findAll();
Assert.isTrue(categorys.contains(saved));
} 

@Test 
public void findAllCategoryTest() { 
Collection<Category> result; 
this.authenticate(this.administratorRepository.findAll().iterator().next().getUserAccount().getUsername());
result = categoryService.findAll(); 
Assert.notEmpty(result); 
} 

@Test 
public void findOneCategoryTest(){ 
this.authenticate(this.administratorRepository.findAll().iterator().next().getUserAccount().getUsername());
Category category = categoryService.findAll().iterator().next(); 
Assert.isTrue(categoryService.exists(category.getId()));
Category result; 
result = categoryService.findOne(category.getId()); 
Assert.notNull(result); 
} 

@Test 
public void deleteCategoryTest() { 
this.authenticate(this.administratorRepository.findAll().iterator().next().getUserAccount().getUsername());
Category category = categoryService.findAll().iterator().next(); 
Assert.notNull(category); 
Assert.isTrue(this.categoryService.exists(category.getId())); 
this.categoryService.delete(category); 
} 

} 
