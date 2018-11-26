package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Configuration;
import services.ConfigurationService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class ConfigurationServiceTest extends AbstractTest { 

@Autowired 
private ConfigurationService	configurationService; 

@Test 
public void saveConfigurationTest(){ 
Configuration configuration, saved;
Collection<Configuration> configurations;
configuration = configurationService.findAll().iterator().next();
configuration.setVersion(57);
saved = configurationService.save(configuration);
configurations = configurationService.findAll();
Assert.isTrue(configurations.contains(saved));
} 

@Test 
public void findAllConfigurationTest() { 
Collection<Configuration> result; 
result = configurationService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneConfigurationTest(){ 
Configuration configuration = configurationService.findAll().iterator().next(); 
int configurationId = configuration.getId(); 
Assert.isTrue(configurationId != 0); 
Configuration result; 
result = configurationService.findOne(configurationId); 
Assert.notNull(result); 
} 

@Test 
public void deleteConfigurationTest() { 
Configuration configuration = configurationService.findAll().iterator().next(); 
Assert.notNull(configuration); 
Assert.isTrue(configuration.getId() != 0); 
Assert.isTrue(this.configurationService.exists(configuration.getId())); 
this.configurationService.delete(configuration); 
} 

} 
