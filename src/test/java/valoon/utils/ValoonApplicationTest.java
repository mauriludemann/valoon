package valoon.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import valoon.core.Client;
import valoon.core.ProductOrder;
import valoon.dto.CreateOrderDto;
import valoon.dto.TakeOrderDto;
import valoon.dto.response.OrderResponseDto;
import valoon.statics.OrderStatus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Valoon App - External Tests With Restassured.
 *
 */

public class ValoonApplicationTest {

    private static ObjectMapper mapper=new ObjectMapper();

    private static final Double DELIVERY_PRICE = 40D;
    private final String url = "http://localhost:8080/valoon";
    private String createOrderUrl;
    private String acceptOrderUrl;
    private String takeOrderUrl;
    private String deliverOrderUrl;
    private String setCreditUrl;
    private OrderResponseDto orderResponse;


    @Before
    public void setUp() {
        createOrderUrl = url + "/createOrder";
        acceptOrderUrl = url + "/acceptOrder/?order_id=";
        takeOrderUrl = url + "/takeOrder";
        deliverOrderUrl = url + "/deliverOrder/?order_id=";
        setCreditUrl = url + "/client/set/?client_id=";

    }

    @Test
    public void completeFlowPurchaseTest() {
        /**
         * Flujo Completo de Compra
         */

        long clientId = 1L;
        Client clientDto = RequestHandler.post(setCreditUrl+ Long.toString(clientId) +"&amount=15000", 200).as(Client.class);

        CreateOrderDto createOrderDTO = this.getCreateOrderDto();

        orderResponse = RequestHandler.postWithCreateOrderBody(createOrderUrl, createOrderDTO).as(OrderResponseDto.class);

        Assert.assertNotNull(orderResponse);
        Assert.assertNotNull(orderResponse.getClient());
        Assert.assertFalse(orderResponse.getProductOrders().isEmpty());
        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.PENDING);
        List<Long> productOrderIds = orderResponse.getProductOrders().stream().map(productOrder -> {return productOrder.getProduct().getProductId();}).collect(Collectors.toList());
        Assert.assertTrue(productOrderIds.contains(1L));
        Assert.assertTrue(productOrderIds.contains(2L));
        Assert.assertEquals(
                Double.valueOf(orderResponse.getProductOrders().stream().mapToDouble(ProductOrder::getTotalAmount).sum() + DELIVERY_PRICE),
                orderResponse.getTotalAmount()
        );

        Double previousCredit = orderResponse.getClient().getCredit();
        Long order_id = orderResponse.getOrderId();

        orderResponse = RequestHandler.post(acceptOrderUrl + order_id, 200).as(OrderResponseDto.class);

        Assert.assertNotNull(orderResponse);
        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.ACCEPTED);

        TakeOrderDto takeOrderDto = new TakeOrderDto();
        takeOrderDto.setOrderId(order_id);
        takeOrderDto.setDealerId(4L);

        orderResponse = RequestHandler.postWithTakeOrderBody(takeOrderUrl, 200, takeOrderDto).as(OrderResponseDto.class);

        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.CONFIRMED);
        Assert.assertEquals(orderResponse.getClient().getCredit(), Double.valueOf(previousCredit -orderResponse.getTotalAmount()));

        orderResponse = RequestHandler.post(deliverOrderUrl + order_id, 200).as(OrderResponseDto.class);

        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.DELIVERED);
    }

    @Test
    public void testSetCredit() {

        Long clientId = 1L;
        Double credit = 10.00;
        Client clientDto = RequestHandler.post(setCreditUrl+clientId.toString()+"&amount="+credit, 200).as(Client.class);

        Assert.assertNotNull(clientDto);
        Assert.assertEquals("1", clientDto.getId().toString());
        Assert.assertEquals(credit, clientDto.getCredit());
    }

    @Test
    public void testFlowPurchase_withNotEnoughCreditWhenClientAcceptsOrder_shouldCancelOrder() {
        long clientId = 1L;
        Client clientDto = RequestHandler.post(setCreditUrl+ Long.toString(clientId) +"&amount=0", 200).as(Client.class);

        CreateOrderDto createOrderDTO = this.getCreateOrderDto();

        orderResponse = RequestHandler.postWithCreateOrderBody(createOrderUrl, createOrderDTO).as(OrderResponseDto.class);

        Assert.assertNotNull(orderResponse);
        Assert.assertNotNull(orderResponse.getClient());
        Assert.assertFalse(orderResponse.getProductOrders().isEmpty());
        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.PENDING);
        List<Long> productOrderIds = orderResponse.getProductOrders().stream().map(productOrder -> {return productOrder.getProduct().getProductId();}).collect(Collectors.toList());
        Assert.assertTrue(productOrderIds.contains(1L));
        Assert.assertTrue(productOrderIds.contains(2L));
        Assert.assertEquals(
                Double.valueOf(orderResponse.getProductOrders().stream().mapToDouble(ProductOrder::getTotalAmount).sum() + DELIVERY_PRICE),
                orderResponse.getTotalAmount()
        );

        Long order_id = orderResponse.getOrderId();

        orderResponse = RequestHandler.post(acceptOrderUrl + order_id, 200).as(OrderResponseDto.class);

        Assert.assertNotNull(orderResponse);
        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.CANCELLED);
    }

    @Test
    public void testFlowPurchase_withNotEnoughCreditWhenDealerTakesOrder_shouldCancelOrder() {

        long clientId = 1L;
        Client clientDto = RequestHandler.post(setCreditUrl+ Long.toString(clientId) +"&amount=15000", 200).as(Client.class);

        CreateOrderDto createOrderDTO = this.getCreateOrderDto();

        orderResponse = RequestHandler.postWithCreateOrderBody(createOrderUrl, createOrderDTO).as(OrderResponseDto.class);

        Assert.assertNotNull(orderResponse);
        Assert.assertNotNull(orderResponse.getClient());
        Assert.assertFalse(orderResponse.getProductOrders().isEmpty());
        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.PENDING);
        List<Long> productOrderIds = orderResponse.getProductOrders().stream().map(productOrder -> {return productOrder.getProduct().getProductId();}).collect(Collectors.toList());
        Assert.assertTrue(productOrderIds.contains(1L));
        Assert.assertTrue(productOrderIds.contains(2L));
        Assert.assertEquals(
                Double.valueOf(orderResponse.getProductOrders().stream().mapToDouble(ProductOrder::getTotalAmount).sum() + DELIVERY_PRICE),
                orderResponse.getTotalAmount()
        );

        Long order_id = orderResponse.getOrderId();

        orderResponse = RequestHandler.post(acceptOrderUrl + order_id, 200).as(OrderResponseDto.class);

        Assert.assertNotNull(orderResponse);
        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.ACCEPTED);

        TakeOrderDto takeOrderDto = new TakeOrderDto();
        takeOrderDto.setOrderId(order_id);
        takeOrderDto.setDealerId(4L);

        //Setting Client Credit to 0

        clientId = 1L;
        clientDto = RequestHandler.post(setCreditUrl+ Long.toString(clientId) +"&amount=0", 200).as(Client.class);

        orderResponse = RequestHandler.postWithTakeOrderBody(takeOrderUrl,200, takeOrderDto).as(OrderResponseDto.class);

        Assert.assertEquals(orderResponse.getStatus(), OrderStatus.CANCELLED);
    }

    private CreateOrderDto getCreateOrderDto() {
        try {
            return mapper.readValue(new File("./src/main/resources/createOrder.json"), new TypeReference<CreateOrderDto>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("There was an error parsing createOrder");
        }
    }


}