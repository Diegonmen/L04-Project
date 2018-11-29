package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Note;
import repositories.NoteRepository;

@Service
@Transactional
public class NoteService {

	@Autowired
	private NoteRepository noteRepository;

	public Note save(Note entity) {
		return noteRepository.save(entity);
	}

	public List<Note> findAll() {
		return noteRepository.findAll();
	}

	public Note findOne(Integer id) {
		return noteRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return noteRepository.exists(id);
	}

	public void delete(Note entity) {
		noteRepository.delete(entity);
	}
	
	
}
