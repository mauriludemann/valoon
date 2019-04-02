package valoon.core;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import valoon.dao.*;
import valoon.dto.CreateRateDto;
import valoon.dto.OrderRequestDto;
import valoon.dto.TakeOrderDto;
import valoon.exceptions.BadRequestException;
import valoon.exceptions.InvalidOrderStatusException;
import valoon.exceptions.NotFoundException;
import valoon.lock.LockService;
import valoon.statics.OrderStatus;
import valoon.statics.ValoonUserType;
import valoon.wrappers.ClientWrapper;
import valoon.wrappers.DealerWrapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValoonService {

    private static final Double DELIVERY_PRICE = 40d;

    private ClientDao clientDao;
    private DealerDao dealerDao;
    private ProductDao productDao;
    private OrderDao orderDao;
    private ProductOrderDao productOrderDao;
    private RateDao rateDao;
    private LockService lockService;

    public ValoonService(ClientDao clientDao, DealerDao dealerDao, ProductDao productDao, OrderDao orderDao, ProductOrderDao productOrderDao, RateDao rateDao, LockService lockService) {
        this.clientDao = clientDao;
        this.dealerDao = dealerDao;
        this.productDao = productDao;
        this.orderDao = orderDao;
        this.productOrderDao = productOrderDao;
        this.lockService = lockService;
        this.rateDao = rateDao;
    }

    @Transactional
    public Order createOrder(OrderRequestDto request) {
        Client client = getClient(request.getClientId());
        Set<ProductOrder> productOrderSet = request.getProductsList().stream().map(productDto -> {
            return productOrderDao.createProduct(new ProductOrder(
                    productDao.getProduct(productDto.getProductId()),
                    productDto.getQuantity()
            ));
        }).collect(Collectors.toSet());
        if (productOrderSet.stream().anyMatch(productOrder -> {return Objects.isNull(productOrder.getProduct());})) {
            throw new NotFoundException("Could not create. One or more Products do not exist");
        }
        Order order = orderDao.createOrder(new Order(client, productOrderSet, DELIVERY_PRICE));
        productOrderSet.forEach(productOrder -> { productOrder.setValoonOrder(order);});
        return order;
    }

    @Transactional
    public Order acceptOrder(Long orderId) {
        Order order = orderDao.getOrder(orderId);
        if (order == null) {
            throw new NotFoundException("Could not accept. Order does not exist");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException(String.format("Could not accept. Order is in a invalid status: %s", order.getStatus()));
        }
        if (! order.getClient().hasEnoughCredit(order.getTotalAmount())) {
            order.setStatus(OrderStatus.CANCELLED);
            return order;
        }
        order.setStatus(OrderStatus.ACCEPTED);
        return order;
    }

    @Transactional
    public Order takeOrder(TakeOrderDto takeOrderDto) {
        lockService.lock(takeOrderDto.getOrderId().toString());
        Order order = orderDao.getOrder(takeOrderDto.getOrderId());
        Dealer dealer = dealerDao.getDealer(takeOrderDto.getDealerId());
        if (order == null || dealer == null) {
            lockService.unlock(takeOrderDto.getOrderId().toString());
            throw new NotFoundException("Could not take. Order or Dealer does not exist");
        }
        if (order.getStatus() != OrderStatus.ACCEPTED) {
            lockService.unlock(takeOrderDto.getOrderId().toString());
            throw new InvalidOrderStatusException(String.format("Could not take. Order is in a invalid status: %s", order.getStatus()));
        }
        if (! order.getClient().hasEnoughCredit(order.getTotalAmount())) {
            order.setStatus(OrderStatus.CANCELLED);
            lockService.unlock(takeOrderDto.getOrderId().toString());
            return order;
        }
        Double newCredit = order.getClient().getCredit() - order.getTotalAmount();
        order.getClient().setCredit(newCredit);
        order.setDealer(dealer);
        order.setStatus(OrderStatus.CONFIRMED);
        lockService.unlock(takeOrderDto.getOrderId().toString());
        return order;
    }

    @Transactional
    public Order deliverOrder(Long orderId) {
        Order order = orderDao.getOrder(orderId);
        if (order == null) {
            throw new NotFoundException("Could not deliver. Order does not exist");
        }
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidOrderStatusException(String.format("Could not deliver. Order is in a invalid status: %s", order.getStatus()));
        }
        order.setStatus(OrderStatus.DELIVERED);
        return order;
    }

    // Dealer
    @Transactional
    public Dealer createDealer(DealerWrapper dealerWrapper) {
        if (dealerWrapper.getName() == null || dealerWrapper.getEmail() == null) {
            throw new BadRequestException("Dealer name or email cannot be null");
        }
        Dealer delaer = new Dealer();
        delaer.setName(dealerWrapper.getName());
        delaer.setEmail(dealerWrapper.getEmail());
        delaer.setRegistrationDate(new Date());
        dealerDao.createDealer(delaer);
        return delaer;
    }

    // Client
    @Transactional
    public Client createClient(ClientWrapper clientWrapper) {
        if (clientWrapper.getName() == null || clientWrapper.getEmail() == null) {
            throw new BadRequestException("Client name or email cannot be null");
        }
        Client client = new Client();
        client.setName(clientWrapper.getName());
        client.setEmail(clientWrapper.getEmail());
        client.setCredit(clientWrapper.getCredit() != null ? clientWrapper.getCredit() : 0d);
        client.setRegistrationDate(new Date());
        clientDao.createClient(client);
        return client;
    }

    @Transactional
    public Client addCreditToClient(Long clientId, Double amount) {
        Client client = getClient(clientId);
        client.setCredit(client.getCredit() + amount);
        return client;
    }

    @Transactional
    public Client setCreditToClient(Long clientId, Double amount) {
        Client client = getClient(clientId);
        client.setCredit(amount);
        return client;
    }

    //Rate
    @Transactional
    public Rate rateDealer(CreateRateDto createRateDto) {
        if (createRateDto.getComment() == null || createRateDto.getScore() == null) {
            throw new BadRequestException("Please, include a score and comment to rate a dealer");
        }
        if (createRateDto.getScore() < 1 || createRateDto.getScore() > 10) {
            throw new BadRequestException("Please, the score must be between 1 and 10");
        }
        Order order = getOrder(createRateDto.getOrderId());
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException(String.format("Cannot rate. The order is not DELIVERED. The current status is: %s", order.getStatus()));
        }
        Rate rate = new Rate(createRateDto.getScore(), createRateDto.getComment(), order.getDealer());
        rateDao.createRate(rate);
        order.setStatus(OrderStatus.RATED);
        return rate;
    }

    // Getters
    @Transactional
    public Order getOrder(Long orderId) {
        Order order = orderDao.getOrder(orderId);
        if (order == null) {
            throw new NotFoundException(String.format("OrderId %s was not found", orderId));
        }
        return order;
    }

    @Transactional
    public Dealer getDealer(Long dealerId) {
        Dealer dealer = dealerDao.getDealer(dealerId);
        if (dealer == null) {
            throw new NotFoundException(String.format("DealerId %s was not found", dealerId));
        }
        return dealer;
    }

    @Transactional
    public Client getClient(Long clientId) {
        Client client = clientDao.getClient(clientId);
        if (client == null) {
            throw new NotFoundException(String.format("ClientId %s was not found", clientId));
        }
        return client;
    }

    @Transactional
    public List<Order> getAssociatedOrdersToValoonUser(Long valoonUserId, ValoonUserType userType) {
        ValoonUser valoonUser = userType == ValoonUserType.CLIENT ? getClient(valoonUserId) : getDealer(valoonUserId);
        return orderDao.getAssociatedOrdersToValoonUser(userType, valoonUser);
    }

    @Transactional
    public List<Rate> getAssociatedRatesToDealer(Long dealerId) {
        Dealer dealer = getDealer(dealerId);
        return rateDao.getAssociatedRates(dealer);
    }
}