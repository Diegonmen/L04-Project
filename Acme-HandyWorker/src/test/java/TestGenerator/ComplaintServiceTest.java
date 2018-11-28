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
import domain.Referee;
import domain.Report;
import services.ComplaintService;
import services.CustomerService;
import services.RefereeService;
import services.ReportService;
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
		@Autowired
		private ReportService		reportservice;

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
			this.authenticate(customerService.findAll().iterator().next().getUserAccount().getUsername());
			
			final Collection<Complaint> res = this.complaintService.findComplaintsNoAsigned();
			Assert.notEmpty(res);
		}
		
		@Test
		public void asingComplaintTest() {
			Referee referee = refereeservice.findAll().iterator().next();
			this.authenticate(referee.getUserAccount().getUsername());
			
			final Collection<Complaint> res = this.complaintService.findComplaintsNoAsigned();
			Report report = referee.getReports().iterator().next();
			
			Complaint any = res.iterator().next();
			
			complaintService.selfAssignComplaint(report, any);
			
			Report saved = reportservice.findOne(report.getId());
			
			Assert.isTrue(saved.getComplaints().contains(any));
		}

	}