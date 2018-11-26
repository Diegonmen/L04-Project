package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Phase;
import services.PhaseService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class PhaseServiceTest extends AbstractTest { 

@Autowired 
private PhaseService	phaseService; 

@Test 
public void savePhaseTest(){ 
Phase phase, saved;
Collection<Phase> phases;
phase = phaseService.findAll().iterator().next();
phase.setVersion(57);
saved = phaseService.save(phase);
phases = phaseService.findAll();
Assert.isTrue(phases.contains(saved));
} 

@Test 
public void findAllPhaseTest() { 
Collection<Phase> result; 
result = phaseService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOnePhaseTest(){ 
Phase phase = phaseService.findAll().iterator().next(); 
int phaseId = phase.getId(); 
Assert.isTrue(phaseId != 0); 
Phase result; 
result = phaseService.findOne(phaseId); 
Assert.notNull(result); 
} 

@Test 
public void deletePhaseTest() { 
Phase phase = phaseService.findAll().iterator().next(); 
Assert.notNull(phase); 
Assert.isTrue(phase.getId() != 0); 
Assert.isTrue(this.phaseService.exists(phase.getId())); 
this.phaseService.delete(phase); 
} 

} 
