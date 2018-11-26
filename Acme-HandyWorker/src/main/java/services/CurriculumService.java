
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

		Curriculum res = new Curriculum();
		String ticker = "";
		PersonalRecord personalRecord = personalRecordService.create();
		Collection<MiscellaneousRecord> miscellaneousRecords = new ArrayList<>();
		Collection<EndorserRecord> endorserRecords = new ArrayList<>();
		Collection<EducationRecord> educationRecords = new ArrayList<>();
		Collection<ProfessionalRecord> professionalRecords = new ArrayList<>();
		res.setTicker(ticker);
		res.setPersonalRecord(personalRecord);
		res.setMiscellaneousRecords(miscellaneousRecords);
		res.setEndorserRecords(endorserRecords);
		res.setEducationRecords(educationRecords);
		res.setProfessionalRecords(professionalRecords);
		return res;
	}

}
