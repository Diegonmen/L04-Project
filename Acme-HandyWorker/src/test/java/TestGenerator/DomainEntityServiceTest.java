package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.DomainEntity;
import services.DomainEntityService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class DomainEntityServiceTest extends AbstractTest { 

@Autowired 
private DomainEntityService	domainentityService; 

@Test 
public void saveDomainEntityTest(){ 
DomainEntity domainentity, saved;
Collection<DomainEntity> domainentitys;
domainentity = domainentityService.findAll().iterator().next();
domainentity.setVersion(57);
saved = domainentityService.save(domainentity);
domainentitys = domainentityService.findAll();
Assert.isTrue(domainentitys.contains(saved));
} 

@Test 
public void findAllDomainEntityTest() { 
Collection<DomainEntity> result; 
result = domainentityService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneDomainEntityTest(){ 
DomainEntity domainentity = domainentityService.findAll().iterator().next(); 
int domainentityId = domainentity.getId(); 
Assert.isTrue(domainentityId != 0); 
DomainEntity result; 
result = domainentityService.findOne(domainentityId); 
Assert.notNull(result); 
} 

@Test 
public void deleteDomainEntityTest() { 
DomainEntity domainentity = domainentityService.findAll().iterator().next(); 
Assert.notNull(domainentity); 
Assert.isTrue(domainentity.getId() != 0); 
Assert.isTrue(this.domainentityService.exists(domainentity.getId())); 
this.domainentityService.delete(domainentity); 
} 

} 
