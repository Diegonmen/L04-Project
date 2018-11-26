package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Curriculum;
import services.CurriculumService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class CurriculumServiceTest extends AbstractTest { 

@Autowired 
private CurriculumService	curriculumService; 

@Test 
public void saveCurriculumTest(){ 
Curriculum curriculum, saved;
Collection<Curriculum> curriculums;
curriculum = curriculumService.findAll().iterator().next();
curriculum.setVersion(57);
saved = curriculumService.save(curriculum);
curriculums = curriculumService.findAll();
Assert.isTrue(curriculums.contains(saved));
} 

@Test 
public void findAllCurriculumTest() { 
Collection<Curriculum> result; 
result = curriculumService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneCurriculumTest(){ 
Curriculum curriculum = curriculumService.findAll().iterator().next(); 
int curriculumId = curriculum.getId(); 
Assert.isTrue(curriculumId != 0); 
Curriculum result; 
result = curriculumService.findOne(curriculumId); 
Assert.notNull(result); 
} 

@Test 
public void deleteCurriculumTest() { 
Curriculum curriculum = curriculumService.findAll().iterator().next(); 
Assert.notNull(curriculum); 
Assert.isTrue(curriculum.getId() != 0); 
Assert.isTrue(this.curriculumService.exists(curriculum.getId())); 
this.curriculumService.delete(curriculum); 
} 

} 
