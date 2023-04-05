package com.online_shopping_rest_api.unitTest;

import com.online_shopping_rest_api.models.OrderItem;
import com.online_shopping_rest_api.repositories.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderItemRepositoryTest {
    @Autowired
    OrderItemRepository orderItemRepository;

    // creates a OrderItem entry, then insert it into the database inside the
    // OrderItem table
    @Test
    public void addOrderItem() {

        final Integer productId = 12;
        final LocalDateTime date = LocalDateTime.now();
        final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        final OrderItem orderItem = new OrderItem(null, productId, date, date);
        final OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        final OrderItem retrievedOrderItem = orderItemRepository.findById(savedOrderItem.getId()).get();

        assertThat(productId).isEqualTo(retrievedOrderItem.getProductId());
        assertThat(date.format(format)).isEqualTo(retrievedOrderItem.getCreatedAt().format(format));
        assertThat(date.format(format)).isEqualTo(retrievedOrderItem.getModifiedAt().format(format));
    }
}
