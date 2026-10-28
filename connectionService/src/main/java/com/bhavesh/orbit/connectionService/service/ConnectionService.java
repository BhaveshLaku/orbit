package com.bhavesh.orbit.connectionService.service;

import com.bhavesh.orbit.connectionService.auth.AuthContextHolder;
import com.bhavesh.orbit.connectionService.entity.Person;
import com.bhavesh.orbit.connectionService.event.ConnectionAcceptedEvent;
import com.bhavesh.orbit.connectionService.event.ConnectionRequestedEvent;
import com.bhavesh.orbit.connectionService.exception.BadRequestException;
import com.bhavesh.orbit.connectionService.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, Object> kafkaTemplate;

    public List<Person> getFirstDegreeConnectionsOfUser(Long userId) {

        log.info("Getting first degree connections of user with ID: {}", userId);

        return personRepository.getFirstDegreeConnections(userId);
    }

    public void sendConnectionRequest(Long receiverId) {
        Long senderId = AuthContextHolder.getCurrentUserId();
        log.info("Sending connection request with senderId: {}, receiverId: {} ", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("Both sender and receiver are the same");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest) {
            throw new RuntimeException("Connection request already exists, cannot send again");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new RuntimeException("Already connected users, cannot add connection request");
        }

        log.info("Successfully sent the connection request");
        personRepository.addConnectionRequest(senderId, receiverId);
        ConnectionRequestedEvent event = ConnectionRequestedEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        kafkaTemplate.send("connection_request_topic", receiverId, event);
    }

    public void acceptConnectionRequest(Long senderId) {
        Long receiverId = AuthContextHolder.getCurrentUserId();
        log.info("Accepting connection request to senderId: {}, receiverId: {} ", senderId, receiverId);


        if (receiverId.equals(senderId)) {
            throw new RuntimeException("Both sender and receiver are the same");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new RuntimeException("Already connected users, cannot accept connection request again");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (!alreadySentRequest) {
            throw new RuntimeException("No connection request exists, cannot accept without request");
        }

        personRepository.acceptConnectionRequest(senderId, receiverId);

        ConnectionAcceptedEvent event = ConnectionAcceptedEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        kafkaTemplate.send("connection_accepted_topic", senderId, event);

        log.info("Successfully accepted the connection request  with senderId: {}, receiverId: {}", senderId, receiverId);
    }

    public void rejectConnectionRequest(Long senderId) {
        Long receiverId = AuthContextHolder.getCurrentUserId();
        log.info("Rejecting a connection request with senderId: {}, receiverId: {}", senderId, receiverId);

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Both sender and receiver are the same");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (!alreadySentRequest) {
            throw new BadRequestException("No Connection request exists, cannot reject it");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);

        log.info("Successfully rejected the connection request with senderId: {}, receiverId: {}", senderId,
                receiverId);
    }
}
