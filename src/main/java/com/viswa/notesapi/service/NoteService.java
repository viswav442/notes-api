package com.viswa.notesapi.service;

import com.viswa.notesapi.dto.NotePageResponse;
import com.viswa.notesapi.dto.NoteRequestDTO;
import com.viswa.notesapi.dto.NoteResponseDTO;
import com.viswa.notesapi.entity.Note;
import com.viswa.notesapi.repository.NoteRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Note note = new Note();
        note.setTitle(requestDTO.getTitle());
        note.setContent(requestDTO.getContent());
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        return toResponseDTO(noteRepository.save(note));
    }

    public List<NoteResponseDTO> getAllNotes() {
        return noteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public NoteResponseDTO getNoteById(Long id) {
        return toResponseDTO(findNoteById(id));
    }

    public NoteResponseDTO updateNote(Long id, NoteRequestDTO requestDTO) {
        Note note = findNoteById(id);
        note.setTitle(requestDTO.getTitle());
        note.setContent(requestDTO.getContent());
        note.setUpdatedAt(LocalDateTime.now());
        return toResponseDTO(noteRepository.save(note));
    }

    public void deleteNote(Long id) {
        noteRepository.delete(findNoteById(id));
    }

    public List<NoteResponseDTO> searchNotes(String keyword) {
        return noteRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Note findNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
    }

    private NoteResponseDTO toResponseDTO(Note note) {
        return new NoteResponseDTO(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }

    public NotePageResponse getAllNotesPaginated(int page,int size,String sortBy,String sortDir){
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
        Sort.by(sortBy).descending() : 
        Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page,size,sort);
        Page<Note> notepage = noteRepository.findAll(pageable);

        List<NoteResponseDTO> content = notepage.getContent().stream().map(this::toResponseDTO).collect(Collectors.toList());
        return new NotePageResponse(
            content,
            notepage.getNumber(),
            notepage.getSize(),
            notepage.getTotalElements(),
            notepage.getTotalPages(),
            notepage.isLast()
        );
    }
}
