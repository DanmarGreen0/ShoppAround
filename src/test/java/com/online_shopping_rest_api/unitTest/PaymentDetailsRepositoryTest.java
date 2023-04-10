package com.online_shopping_rest_api.unitTest;

import com.online_shopping_rest_api.models.PaymentDetails;
import com.online_shopping_rest_api.repositories.PaymentDetailsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PaymentDetailsRepositoryTest {

    @Autowired
    PaymentDetailsRepository paymentDetailsRepository;

    //creates a paymentDetails entry, then insert it into the database inside the PaymentDetails table
    @Test
    public void addPaymentDetails(){


        final Integer orderId = 12;
        final Double amount = 50.00;
        final String provider = "Amazon";
        final String status = "completed";
        final LocalDateTime date = LocalDateTime.now();
        final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        final PaymentDetails paymentDetails = new PaymentDetails(null,orderId, amount, provider, status, date, date);
        final PaymentDetails savedPaymentDetails = paymentDetailsRepository.save(paymentDetails);
        final PaymentDetails retrievedPaymentDetails = paymentDetailsRepository.findById(savedPaymentDetails.getId()).get();

        assertThat(orderId).isEqualTo(retrievedPaymentDetails.getOrderId());
        assertThat(amount).isEqualTo(retrievedPaymentDetails.getAmount());
        assertThat(provider).isEqualTo(retrievedPaymentDetails.getProvider());
        assertThat(status).isEqualTo(retrievedPaymentDetails.getStatus());
        assertThat(date.format(format)).isEqualTo(retrievedPaymentDetails.getCreatedAt().format(format));
        assertThat(date.format(format)).isEqualTo(retrievedPaymentDetails.getModifiedAt().format(format));
    }




}
