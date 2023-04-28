package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class OrderService {
    @Autowired
    OrderRepository orderRepository;



    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        orderRepository.addPartner(partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        Optional<Order> orderOpt = orderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> partnerOpt = orderRepository.getPartnerById(partnerId);
        if(orderOpt.isPresent() && partnerOpt.isPresent()) {
            DeliveryPartner p = partnerOpt.get();
            Integer initialOrder = p.getNumberOfOrders();
            initialOrder++;
            p.setNumberOfOrders(initialOrder);
            orderRepository.addPartner(p);

            orderRepository.addOrderPartnerPair(orderId, partnerId);
        }
    }

    public Order getOrderById(String orderId) {
        Optional<Order> orderOpt = orderRepository.getOrderById(orderId);
        if(orderOpt.isPresent()){
            return orderOpt.get();
        }
        throw new RuntimeException("Order not present");
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        Optional<DeliveryPartner> partnerOpt = orderRepository.getPartnerById(partnerId);
        if(partnerOpt.isPresent()){
            return partnerOpt.get();
        }
        throw new RuntimeException("Order not present");
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        Optional<DeliveryPartner> p = orderRepository.getPartnerById(partnerId);
        if(p.isPresent()){
            return p.get().getNumberOfOrders();
        }
        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orderIds = new ArrayList<>();
        Map<String,String> orderPartnerMap = orderRepository.getAllOrderPartnerMappings();
        for(var entry : orderPartnerMap.entrySet()){
            if(entry.getValue().equals(partnerId)){
                orderIds.add(entry.getKey());
            }
        }
        return orderIds;
    }

    public List<String> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Integer getUnassignedOrders() {
        return orderRepository.getAllOrders().size() - orderRepository.getAssignOrder().size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        List<String> orderIds = getOrdersByPartnerId(partnerId);
        int currTime = TimeUtil.convertStringToInt(time);
        int orderLeft = 0;
        for(String orderId : orderIds){
            int deliveryTime = orderRepository.getOrderById(orderId).get().getDeliveryTime();
            if(currTime < deliveryTime){
                orderLeft++;
            }
        }
        return orderLeft;
    }

    public String getLstDeliveryForPartner(String partnerId) {
        List<String> orderIds = getOrdersByPartnerId(partnerId);
        int max = 0;
        for(String orderId : orderIds){
            int deliveryTime = orderRepository.getOrderById(orderId).get().getDeliveryTime();
            if(deliveryTime > max){
                max = deliveryTime;
            }
        }
        return TimeUtil.convertIntToString(max);
    }

    public void deletePartner(String partnerId) {
        List<String> orders = getOrdersByPartnerId(partnerId);
        orderRepository.deletePartner(partnerId);
        for(String orderId : orders) {
            orderRepository.removeOrderPartnerMapping(orderId);
        }
    }

    public void deleteOrder(String orderId) {
        String partnerId = orderRepository.deleteForOrder(orderId);
        orderRepository.deleteOrder(orderId);
        if(Objects.nonNull(partnerId)){
            DeliveryPartner p = orderRepository.getPartnerById(partnerId).get();
            Integer initialOrder = p.getNumberOfOrders();
            initialOrder--;
            p.setNumberOfOrders(initialOrder);
            orderRepository.addPartner(p);
            orderRepository.removeOrderForPartner(partnerId, orderId);
        }
    }
}
