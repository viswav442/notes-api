package com.viswa.notesapi.controller;

import com.viswa.notesapi.dto.NotePageResponse;
import com.viswa.notesapi.dto.NoteRequestDTO;
import com.viswa.notesapi.dto.NoteResponseDTO;
import com.viswa.notesapi.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.viswa.notesapi.dto.NotePageResponse;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponseDTO> createNote(@Valid @RequestBody NoteRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.createNote(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponseDTO>> getAllNotes() {
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> getNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> updateNote(@PathVariable Long id,
                                                      @Valid @RequestBody NoteRequestDTO requestDTO) {
        return ResponseEntity.ok(noteService.updateNote(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResponseDTO>> searchNotes(@RequestParam String keyword) {
        return ResponseEntity.ok(noteService.searchNotes(keyword));
    }

    @GetMapping("/paginated")
    public ResponseEntity<NotePageResponse> getAllNotesPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir){
            return ResponseEntity.ok(noteService.getAllNotesPaginated(page,size,sortBy,sortDir));
        }
    
}
