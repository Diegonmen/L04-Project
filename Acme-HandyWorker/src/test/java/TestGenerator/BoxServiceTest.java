package TestGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Box;
import domain.Message;
import services.ActorService;
import services.BoxServices;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml", "classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class BoxServiceTest extends AbstractTest {

	@Autowired
	private BoxServices boxService;
	@Autowired
	private ActorService actorservice;
	
	@Test
	public void newBox() {
		Actor sender = actorservice.findByUsernames(Arrays.asList("admin1")).iterator().next();
		this.authenticate(sender.getUserAccount().getUsername());
		
		Box auxbox = new Box();
		auxbox.setChildren(new LinkedList<Box>());
		auxbox.setMessages(new LinkedList<Message>());
		auxbox.setName("AUXBOX");
		auxbox.setPredefined(false);
		
		Box saved = boxService.newBox(auxbox);
		
		sender = actorservice.findByUsernames(Arrays.asList("admin1")).iterator().next();
		
		Assert.isTrue(sender.getBoxes().contains(saved));
	}

	@Test
	public void saveBoxTest() {
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
	public void findOneBoxTest() {
		Box box = boxService.findAll().iterator().next();
		int boxId = box.getId();
		Assert.isTrue(boxId != 0);
		Box result;
		result = boxService.findOne(boxId);
		Assert.notNull(result);
	}

}
