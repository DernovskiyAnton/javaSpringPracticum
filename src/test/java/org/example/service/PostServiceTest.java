package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.config.TestConfig;
import org.example.mapper.PostMapper;
import org.example.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.reset;

@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor
@ContextConfiguration(classes = TestConfig.class)
public class PostServiceTest {

}
