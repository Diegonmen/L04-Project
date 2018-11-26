package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Section;
import services.SectionService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class SectionServiceTest extends AbstractTest { 

@Autowired 
private SectionService	sectionService; 

@Test 
public void saveSectionTest(){ 
Section section, saved;
Collection<Section> sections;
section = sectionService.findAll().iterator().next();
section.setVersion(57);
saved = sectionService.save(section);
sections = sectionService.findAll();
Assert.isTrue(sections.contains(saved));
} 

@Test 
public void findAllSectionTest() { 
Collection<Section> result; 
result = sectionService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneSectionTest(){ 
Section section = sectionService.findAll().iterator().next(); 
int sectionId = section.getId(); 
Assert.isTrue(sectionId != 0); 
Section result; 
result = sectionService.findOne(sectionId); 
Assert.notNull(result); 
} 

@Test 
public void deleteSectionTest() { 
Section section = sectionService.findAll().iterator().next(); 
Assert.notNull(section); 
Assert.isTrue(section.getId() != 0); 
Assert.isTrue(this.sectionService.exists(section.getId())); 
this.sectionService.delete(section); 
} 

} 
