package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Note;
import services.NoteService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class NoteServiceTest extends AbstractTest { 

@Autowired 
private NoteService	noteService; 

@Test 
public void saveNoteTest(){ 
Note note, saved;
Collection<Note> notes;
note = noteService.findAll().iterator().next();
note.setVersion(57);
saved = noteService.save(note);
notes = noteService.findAll();
Assert.isTrue(notes.contains(saved));
} 

@Test 
public void findAllNoteTest() { 
Collection<Note> result; 
result = noteService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneNoteTest(){ 
Note note = noteService.findAll().iterator().next(); 
int noteId = note.getId(); 
Assert.isTrue(noteId != 0); 
Note result; 
result = noteService.findOne(noteId); 
Assert.notNull(result); 
} 

@Test 
public void deleteNoteTest() { 
Note note = noteService.findAll().iterator().next(); 
Assert.notNull(note); 
Assert.isTrue(note.getId() != 0); 
Assert.isTrue(this.noteService.exists(note.getId())); 
this.noteService.delete(note); 
} 

} 
