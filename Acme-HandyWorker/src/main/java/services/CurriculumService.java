
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.CurriculumRepository;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.ProfessionalRecord;

@Service
@Transactional
public class CurriculumService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CurriculumRepository	curriculumRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private PersonalRecordService	personalRecordService;


	// Simple CRUD methods ----------------------------------------------------

	public Curriculum create() {

		final Curriculum res = new Curriculum();
		final PersonalRecord personalRecord = this.personalRecordService.create();
		final Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<>();
		final Collection<EndorserRecord> endorserRecords = new ArrayList<>();
		final Collection<EducationRecord> educationRecords = new ArrayList<>();
		final Collection<ProfessionalRecord> professionalRecords = new ArrayList<>();
		res.setPersonalRecord(personalRecord);
		res.setMiscellaneousRecords(miscellaneousRecords);
		res.setEndorserRecords(endorserRecords);
		res.setEducationRecords(educationRecords);
		res.setProfessionalRecords(professionalRecords);
		return res;
	}

}
