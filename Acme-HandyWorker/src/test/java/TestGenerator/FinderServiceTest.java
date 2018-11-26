package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Finder;
import services.FinderService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class FinderServiceTest extends AbstractTest { 

@Autowired 
private FinderService	finderService; 

@Test 
public void saveFinderTest(){ 
Finder finder, saved;
Collection<Finder> finders;
finder = finderService.findAll().iterator().next();
finder.setVersion(57);
saved = finderService.save(finder);
finders = finderService.findAll();
Assert.isTrue(finders.contains(saved));
} 

@Test 
public void findAllFinderTest() { 
Collection<Finder> result; 
result = finderService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneFinderTest(){ 
Finder finder = finderService.findAll().iterator().next(); 
int finderId = finder.getId(); 
Assert.isTrue(finderId != 0); 
Finder result; 
result = finderService.findOne(finderId); 
Assert.notNull(result); 
} 

@Test 
public void deleteFinderTest() { 
Finder finder = finderService.findAll().iterator().next(); 
Assert.notNull(finder); 
Assert.isTrue(finder.getId() != 0); 
Assert.isTrue(this.finderService.exists(finder.getId())); 
this.finderService.delete(finder); 
} 

} 
