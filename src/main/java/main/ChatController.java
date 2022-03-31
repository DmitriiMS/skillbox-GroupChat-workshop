package main;

import main.dto.MessageDTO;
import main.dto.MessageMapper;
import main.dto.UserDTO;
import main.dto.UserMapper;
import main.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/init")
    public Map<String, Boolean> init() {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = userRepository.findBySessionId(sessionId).orElse(null);
        return Map.of("result", user != null);
    }

    @PostMapping("/auth")
    public Map<String, Boolean> auth(@RequestParam String name) {
        if(name.isBlank()) {
            return Map.of("result", false);
        }
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = new User(sessionId, name);
        userRepository.save(user);
        return Map.of("result", true);
    }

    @PostMapping("/messages")
    public  Map<String, Boolean> sendMessage(@RequestParam String message) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = userRepository.findBySessionId(sessionId).orElse(null);
        if(user == null || message.isBlank()){
            return Map.of("result", false);
        }
        messageRepository.save(new Message(user, LocalDateTime.now(), message));
        return Map.of("result", true);
    }

    @GetMapping("/messages")
    public List<MessageDTO> getMessagesList() {
        return messageRepository.findAll(Sort.by("dateTime")).stream()
                .map(MessageMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/users")
    public List<UserDTO> getUsersList(){
        return userRepository.findAll(Sort.by("name")).stream()
                .map(UserMapper::map)
                .collect(Collectors.toList());
    }
}
