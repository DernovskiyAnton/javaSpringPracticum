package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CommentDto;
import org.example.dto.CommentRequest;
import org.example.mapper.CommentMapper;
import org.example.model.Comment;
import org.example.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public List<CommentDto> getAllByPostId(long postId) {
        return commentRepository.findAllByPostId(postId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public CommentDto getById(long id) {
        return commentMapper.toDto(commentRepository.findById(id));
    }

    @Transactional
    public CommentDto create(CommentRequest request) {
        Comment comment = new Comment(0L, request.text(), request.postId());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto update(long id, CommentRequest request) {
        return commentMapper.toDto(commentRepository.update(id, request));
    }

    @Transactional
    public void delete(long id) {
        commentRepository.deleteById(id);
    }

}
