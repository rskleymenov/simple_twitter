package org.simple.twitter.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.simple.twitter.dto.NoteDTO;
import org.simple.twitter.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @RequestMapping(method = GET)
    public NoteDTO getNote(@RequestParam int id) {
        return noteService.findNote(id);
    }

    @RequestMapping(path = "/all",method = GET)
    public List<NoteDTO> getNotesByUser(@RequestParam int id) {
        return noteService.getNotesByUser(id);
    }

    @RequestMapping(method = POST)
    public NoteDTO saveNoteForUser(@RequestBody NoteDTO note) {
        return noteService.saveNote(note);
    }

    @RequestMapping(method = PUT)
    public NoteDTO updateNote(@RequestBody NoteDTO note) {
        return noteService.updateNote(note);
    }

    @RequestMapping(method = DELETE, path = "/{id}")
    public void deleteNote(@PathVariable("id") int id) {
        noteService.deleteNote(id);
    }

}
