package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    private Map<String, Order> orderMap = new HashMap<>();
    private Map<String, DeliveryPartner> partnerMap = new HashMap<>();
    private Map<String, String> orderPartnerPair = new HashMap<>();
    private Map<String, List<String>> partnerOrderMap = new HashMap<>();
    public void addOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

    public void addPartner(DeliveryPartner partner) {
        partnerMap.put(partner.getId(), partner);
    }


    public Optional<Order> getOrderById(String orderId) {
        if(orderMap.containsKey(orderId)){
            return Optional.of(orderMap.get(orderId));
        }
        return Optional.empty();
    }

    public Optional<DeliveryPartner> getPartnerById(String partnerId) {
        if(partnerMap.containsKey(partnerId)){
            return Optional.of(partnerMap.get(partnerId));
        }
        return Optional.empty();
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderPartnerPair.put(orderId,partnerId);
    }

    public Map<String, String> getAllOrderPartnerMappings() {
        return orderPartnerPair;
    }

    public List<String> getAllOrders() {
        return  new ArrayList<>(orderMap.keySet());
    }

    public List<String> getAssignOrder() {
        return new ArrayList<>(orderPartnerPair.keySet());
    }

    public void deletePartner(String partnerId) {
        partnerMap.remove(partnerId);
        partnerOrderMap.remove(partnerId);
    }

    public void removeOrderPartnerMapping(String orderId) {
        orderPartnerPair.remove(orderId);
    }

    public void deleteOrder(String orderId) {
        orderMap.remove(orderId);
        orderPartnerPair.remove(orderId);
    }

    public String deleteForOrder(String orderId) {
        return orderPartnerPair.get(orderId);
    }

    public void removeOrderForPartner(String partnerId, String orderId) {
        List<String> orderIds = partnerOrderMap.get(partnerId);
        orderIds.remove(orderId);
        partnerOrderMap.put(partnerId, orderIds);
    }
}
