package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Tutorial;
import services.TutorialService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class TutorialServiceTest extends AbstractTest { 

@Autowired 
private TutorialService	tutorialService; 

@Test 
public void saveTutorialTest(){ 
Tutorial tutorial, saved;
Collection<Tutorial> tutorials;
tutorial = tutorialService.findAll().iterator().next();
tutorial.setVersion(57);
saved = tutorialService.save(tutorial);
tutorials = tutorialService.findAll();
Assert.isTrue(tutorials.contains(saved));
} 

@Test 
public void findAllTutorialTest() { 
Collection<Tutorial> result; 
result = tutorialService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneTutorialTest(){ 
Tutorial tutorial = tutorialService.findAll().iterator().next(); 
int tutorialId = tutorial.getId(); 
Assert.isTrue(tutorialId != 0); 
Tutorial result; 
result = tutorialService.findOne(tutorialId); 
Assert.notNull(result); 
} 

@Test 
public void deleteTutorialTest() { 
Tutorial tutorial = tutorialService.findAll().iterator().next(); 
Assert.notNull(tutorial); 
Assert.isTrue(tutorial.getId() != 0); 
Assert.isTrue(this.tutorialService.exists(tutorial.getId())); 
this.tutorialService.delete(tutorial); 
} 

} 
