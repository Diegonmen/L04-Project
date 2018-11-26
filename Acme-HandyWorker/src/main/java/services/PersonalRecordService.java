
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.PersonalRecordRepository;
import domain.PersonalRecord;

@Service
@Transactional
public class PersonalRecordService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private PersonalRecordRepository	personalRecordRepository;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public PersonalRecord create() {

		PersonalRecord res = new PersonalRecord();
		String fullName = "";
		String photoURL = "";
		String email = "";
		String phoneNumber = "";
		String linkedInProfileURL = "";
		res.setFullName(fullName);
		res.setPhotoURL(photoURL);
		res.setEmail(email);
		res.setPhoneNumber(phoneNumber);
		res.setLinkedInProfileURL(linkedInProfileURL);
		return res;
	}
}
