package org.simple.twitter.repository;

import org.simple.twitter.model.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Integer> {
    List<Note> findByUserId(int userId);
}
