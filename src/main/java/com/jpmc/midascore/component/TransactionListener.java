package com.jpmc.midascore.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class TransactionListener {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public TransactionListener(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "test-topic")
    public void listen(Transaction transaction) {
        System.out.println("Received transaction: " + transaction);
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

            if (sender == null || recipient == null) {
            System.out.println(" Invalid sender or recipient. Skipping transaction.");
            return;
        }

            if (sender.getBalance() < transaction.getAmount()) {
            System.out.println(" Insufficient balance for sender. Skipping transaction.");
            return;
        }

        // call incentive api
        String url = "http://localhost:8080/incentive";
        Incentive response = restTemplate.postForObject(url, transaction, Incentive.class);
        float incentiveAmount = response!= null ? response.getAmount(): 0.0f;

        // apply transcation and incentive
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        System.out.println(" Transaction processed. Incentive: " + incentiveAmount +
                " | Sender balance: " + sender.getBalance() +
                " | Recipient balance: " + recipient.getBalance());


    }

}

