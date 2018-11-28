package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Complaint;
import domain.Customer;
import domain.Referee;
import services.ComplaintService;
import services.CustomerService;
import services.RefereeService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {
		"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
	})
	@RunWith(SpringJUnit4ClassRunner.class)
	@Transactional
	public class ComplaintServiceTest extends AbstractTest {

		@Autowired
		private ComplaintService	complaintService;

		@Autowired
		private CustomerService		customerService;
		@Autowired
		private RefereeService		refereeservice;


		@Test
		public void saveComplaintTest() {
			Complaint complaint, saved;
			Collection<Complaint> complaints;
			this.authenticate(customerService.findAll().iterator().next().getUserAccount().getUsername());
			complaint = this.complaintService.findAll().iterator().next();
			complaint.setDescription("Description");
			saved = this.complaintService.save(complaint);
			complaints = this.complaintService.findAll();
			Assert.isTrue(complaints.contains(saved));
		}

		@Test
		public void findAllComplaintTest() {
			Collection<Complaint> result;
			result = this.complaintService.findAll();
			Assert.notNull(result);
		}

		@Test
		public void findOneComplaintTest() {
			final Complaint complaint = this.complaintService.findAll().iterator().next();
			final int complaintId = complaint.getId();
			Assert.isTrue(complaintId != 0);
			Complaint result;
			result = this.complaintService.findOne(complaintId);
			Assert.notNull(result);
		}


		@Test
		public void findAllByRefereeNoAssignedTest() {
			Referee referee = refereeservice.findAll().iterator().next();
			final Collection<Complaint> res = this.complaintService.findAllByRefereeNoAsigned(referee);
			Assert.notEmpty(res);
		}

	}