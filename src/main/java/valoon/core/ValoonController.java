package valoon.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import valoon.dto.CreateRateDto;
import valoon.dto.OrderRequestDto;
import valoon.dto.TakeOrderDto;
import valoon.exceptions.BaseExceptionHandler;
import valoon.exceptions.InvalidOrderStatusException;
import valoon.exceptions.BadRequestException;
import valoon.exceptions.NotFoundException;
import valoon.statics.ValoonUserType;
import valoon.utils.JacksonJsonMapper;
import valoon.wrappers.ClientWrapper;
import valoon.wrappers.DealerWrapper;

import java.util.List;

@RestController
public class ValoonController extends BaseExceptionHandler {

    private JacksonJsonMapper jacksonJsonMapper;
    private ValoonService valoonService;

    public ValoonController(JacksonJsonMapper jacksonJsonMapper, ValoonService valoonService) {
        this.jacksonJsonMapper = jacksonJsonMapper;
        this.valoonService = valoonService;
    }

    /**
     * Gets the Order if exists
     * @param orderId the desired Order's id
     * @return the Order
     * @throws NotFoundException when the Order doesn't exist
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/valoon/order/{orderId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getOrder(
        @PathVariable Long orderId
     ) {
        Order order = valoonService.getOrder(orderId);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(order), HttpStatus.OK);
    }

    /**
     * Gets the Dealer if exists
     * @param dealerId the desired Dealer's id
     * @return the Dealer
     * @throws NotFoundException when the Dealer doesn't exist
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/valoon/dealer/{dealerId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getDealer(
            @PathVariable Long dealerId
    ) {
        Dealer dealer = valoonService.getDealer(dealerId);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(dealer), HttpStatus.OK);
    }

    /**
     * Gets the Client if exists
     * @param clientId the desired Client's id
     * @return the Client
     * @throws NotFoundException when the Client doesn't exist
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/valoon/client/{clientId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getClient(
            @PathVariable Long clientId
    ) {
        Client client = valoonService.getClient(clientId);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(client), HttpStatus.OK);
    }

    // Order

    /**
     * Creates an Order with the given parameters
     * @param body the object with all information needed to create an order
     * @return the created Order
     * @throws NotFoundException when the Client doesn't exist or any product don't exist
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/createOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> createOrder(
            @RequestBody String body
    ) {
        OrderRequestDto request = jacksonJsonMapper.deserialize(body, OrderRequestDto.class);
        Order order = valoonService.createOrder(request);

        return new ResponseEntity<>(jacksonJsonMapper.serialize(order), HttpStatus.CREATED);
    }

    /**
     * A Client accepts an existent Order if has enough credit
     * @param orderId the Order's id
     * @return the Order
     * @throws NotFoundException when the Order doesn't exist
     * @throws InvalidOrderStatusException when the Order's status is Invalid to be accepted
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/acceptOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> acceptOrder(
            @RequestParam(value = "order_id") Long orderId
    ) {
        Order order = valoonService.acceptOrder(orderId);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(order), HttpStatus.OK);
    }

    /**
     * A Dealer takes an existent and accepted Order
     * @param body the object with all information needed to create an order
     * @return the Order
     * @throws NotFoundException when the Order or the Dealer don't exist
     * @throws InvalidOrderStatusException when the Order's status is Invalid to be taken
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/takeOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> takeOrder(
            @RequestBody String body
    ) {
        TakeOrderDto request = jacksonJsonMapper.deserialize(body, TakeOrderDto.class);
        Order order = valoonService.takeOrder(request);

        return new ResponseEntity<>(jacksonJsonMapper.serialize(order), HttpStatus.OK);
    }

    /**
     * A Dealer delivers an Order
     * @param orderId the Order's id to be delivered
     * @return the Order
     * @throws NotFoundException when the Order doesn't exist
     * @throws InvalidOrderStatusException when the Order's status is Invalid to be delivered
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/deliverOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> deliverOrder(
            @RequestParam(value = "order_id") Long orderId
    ) {
        Order order = valoonService.deliverOrder(orderId);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(order), HttpStatus.OK);
    }

    // Dealer
    /**
     * Creates a Dealer with the given information
     * @param body the object with all information needed to create a Dealer
     * @return the created Dealer
     * @throws BadRequestException when some Dealer's information is empty
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/dealer/create",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> createDealer(
            @RequestBody String body
    ) {
        DealerWrapper dealerWrapper = jacksonJsonMapper.deserialize(body, DealerWrapper.class);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(valoonService.createDealer(dealerWrapper)), HttpStatus.OK);
    }

    /**
     * Collects all associated Orders to the given Dealer
     * @param dealerId the desired Dealer's id
     * @return a list of the associated Orders
     * @throws NotFoundException when the Dealer doesn't exist
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/valoon/dealer/{dealerId}/orders",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> getDealerOrders(
            @PathVariable Long dealerId
    ) {
        List<Order> orderList = valoonService.getAssociatedOrdersToValoonUser(dealerId, ValoonUserType.DEALER);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(orderList), HttpStatus.OK);
    }

    // Client
    /**
     * Creates a Client with the given information
     * @param body the object with all information needed to create a Client
     * @return the created Client
     * @throws BadRequestException when some Client's information is empty
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/client/create",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> createClient(
            @RequestBody String body
    ) {
        ClientWrapper clientWrapper = jacksonJsonMapper.deserialize(body, ClientWrapper.class);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(valoonService.createClient(clientWrapper)), HttpStatus.OK);
    }

    /**
     * Collects all associated Orders to the given Client
     * @param clientId the desired Client's id
     * @return a list of the associated Orders
     * @throws NotFoundException when the Client doesn't exist
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/valoon/client/{clientId}/orders",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> getClientOrders(
            @PathVariable Long clientId
    ) {
        List<Order> orderList = valoonService.getAssociatedOrdersToValoonUser(clientId, ValoonUserType.CLIENT);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(orderList), HttpStatus.OK);
    }

    /**
     * Adds the given amount of credit to the given Client
     * @param clientId the desired Client's id
     * @param amount the amount to be added
     * @return the Client
     * @throws NotFoundException when the Client doesn't exist
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/client/add",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> addCredit(
            @RequestParam(value = "client_id") Long clientId,
            @RequestParam(value = "amount") Double amount
    ) {
        Client client = valoonService.addCreditToClient(clientId, amount);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(client), HttpStatus.OK);
    }

    /**
     * Sets the given amount of credit to the given Client
     * @param clientId the desired Client's id
     * @param amount the amount to be set
     * @return the Client
     * @throws NotFoundException when the Client doesn't exist
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/client/set",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> setCredit(
            @RequestParam(value = "client_id") Long clientId,
            @RequestParam(value = "amount") Double amount
    ) {
        Client client = valoonService.setCreditToClient(clientId, amount);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(client), HttpStatus.OK);
    }

    //Rate

    /**
     * Rates a Dealer with the associated Order
     * @param body the object with all information needed to rate a Dealer
     * @return the Rate created
     * @throws BadRequestException when the given information is invalid
     * @throws InvalidOrderStatusException when the Order's status is invalid to be rated
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/valoon/dealer/rate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> rateDealer(
            @RequestBody String body
    ) {
        CreateRateDto createRateDto = jacksonJsonMapper.deserialize(body, CreateRateDto.class);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(valoonService.rateDealer(createRateDto)), HttpStatus.CREATED);
    }

    /**
     * Gets all rates of the given Dealer
     * @param dealerId the desired Dealer's id
     * @return a list with all dealer's rates
     * @throws NotFoundException when the Dealer doesn't exist
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/valoon/dealer/rate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<String> getDealerRate(
            @RequestParam(value = "dealer_id") Long dealerId
    ) {
        List<Rate> rateList = valoonService.getAssociatedRatesToDealer(dealerId);
        return new ResponseEntity<>(jacksonJsonMapper.serialize(rateList),  HttpStatus.OK);
    }
}
