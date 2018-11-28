
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Complaint;
import domain.Customer;
import domain.Referee;
import domain.Report;
import repositories.ComplaintRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ComplaintService {

	//Managed Repository
	@Autowired
	private ComplaintRepository	complaintRepository;


	//Constructor
	public ComplaintService() {
		super();
	}

	//Simple CRUD methods
	public Complaint create() {
		Complaint result;

		result = new Complaint();
		result.setAttachments(new ArrayList<String>());

		return result;
	}

	public Collection<Complaint> findAll() {
		Collection<Complaint> result;
		Assert.notNull(this.complaintRepository);
		result = this.complaintRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Complaint findOne(final Integer id) {
		Complaint res;
		res = this.complaintRepository.findOne(id);
		return res;
	}

	public Complaint save(final Complaint c) {
		Complaint res;
		UserAccount logedUserAccount;
		Authority authority = new Authority();
		authority.setAuthority("CUSTOMER");
		logedUserAccount = LoginService.getPrincipal();
		Assert.isTrue(logedUserAccount.getAuthorities().contains(authority));
		res = this.complaintRepository.save(c);
		return res;
	}
	
	public boolean exists(final int id) {
		return this.complaintRepository.exists(id);
	}


	public void delete(final Iterable<Complaint> complaints) {
		Assert.notNull(complaints);
		for (final Complaint c : complaints)
			this.complaintRepository.delete(c);
	}

	//Other Business

	public Collection<Complaint> findAllByRefereeNoAsigned(final Referee r) {
		Collection<Complaint> complaints;
		Report report = this.complaintRepository.findReportWithComplaintsNoAsigned();
		complaints = report.getComplaints();
		Assert.isTrue(!(complaints.isEmpty()));

		return complaints;
	}

	public Complaint selfAssignComplaint(final Referee r) {
		final Collection<Complaint> complaints = this.findAllByRefereeNoAsigned(r);
		final Report report = new Report();
		final Collection<Report> reps = new ArrayList<Report>();

		final Complaint elegido = (Complaint) complaints.toArray()[0];
		complaints.add(elegido);
		report.setComplaints(complaints);
		reps.add(report);

		r.setReports(reps);

		return elegido;
	}

	public Collection<Complaint> findByReferee(final Referee r) {
		Collection<Complaint> res;
		res = this.complaintRepository.findComplaintByReferee(r.getId());
		return res;
	}

}
