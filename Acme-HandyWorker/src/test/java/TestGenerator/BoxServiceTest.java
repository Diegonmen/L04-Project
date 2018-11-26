package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Box;
import services.BoxService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class BoxServiceTest extends AbstractTest { 

@Autowired 
private BoxService	boxService; 

@Test 
public void saveBoxTest(){ 
Box box, saved;
Collection<Box> boxs;
box = boxService.findAll().iterator().next();
box.setVersion(57);
saved = boxService.save(box);
boxs = boxService.findAll();
Assert.isTrue(boxs.contains(saved));
} 

@Test 
public void findAllBoxTest() { 
Collection<Box> result; 
result = boxService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneBoxTest(){ 
Box box = boxService.findAll().iterator().next(); 
int boxId = box.getId(); 
Assert.isTrue(boxId != 0); 
Box result; 
result = boxService.findOne(boxId); 
Assert.notNull(result); 
} 

@Test 
public void deleteBoxTest() { 
Box box = boxService.findAll().iterator().next(); 
Assert.notNull(box); 
Assert.isTrue(box.getId() != 0); 
Assert.isTrue(this.boxService.exists(box.getId())); 
this.boxService.delete(box); 
} 

} 
