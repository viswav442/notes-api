package com.viswa.notesapi.service;

import com.viswa.notesapi.dto.NoteRequestDTO;
import com.viswa.notesapi.dto.NoteResponseDTO;
import com.viswa.notesapi.entity.Note;
import com.viswa.notesapi.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteResponseDTO createNote(NoteRequestDTO requestDTO) {
        Note note  = new Note();
        note.setTitle(requestDTO.getTitle());
        note.setTitle(requestDTO.getContent());
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        return toResponseDTO(noteRepository.save(note));
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
    }

    public Note updateNote(Long id,Note updatedNote){
        Note existingNote = getNoteById(id);
        existingNote.setTitle(updatedNote.getTitle());
        existingNote.setContent(updatedNote.getContent());
        existingNote.setUpdatedAt(LocalDateTime.now());
        return noteRepository.save(existingNote);
    }

    public void deleteNote(Long id){
        Note existingNote = getNoteById(id);
        noteRepository.delete(existingNote);
    }

    public List<Note> searchNotes(String keyword){
        return noteRepository.findByTitleContainingIgnoreCase(keyword);
    }

    private NoteResponseDTO toResponseDTO(Note note){
        return new NoteResponseDTO(
                                    note.getId(),
                                    note.getTitle(),
                                    note.getContent(),
                                    note.getCreatedAt(),
                                    note.getUpdatedAt()
        );
    }
}
