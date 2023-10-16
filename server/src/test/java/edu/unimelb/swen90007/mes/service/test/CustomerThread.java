package edu.unimelb.swen90007.mes.service.test;

import edu.unimelb.swen90007.mes.datamapper.DBConnection;
import edu.unimelb.swen90007.mes.exceptions.TicketInsufficientException;
import edu.unimelb.swen90007.mes.exceptions.UserAlreadyExistsException;
import edu.unimelb.swen90007.mes.model.*;
import edu.unimelb.swen90007.mes.service.impl.AppUserService;
import edu.unimelb.swen90007.mes.service.impl.CustomerService;
import edu.unimelb.swen90007.mes.service.impl.PublicService;
import edu.unimelb.swen90007.mes.util.UnitOfWork;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CustomerThread extends Thread{
    private final Customer customer;
    private final PublicService publicService = new PublicService();
    private final CustomerService customerService = new CustomerService();
    private final Money money = new Money(new BigDecimal(100), "Australia");

    public CustomerThread(String email, String password, String firstName, String lastName) {
        customer = new Customer(email, password, firstName, lastName);
        try{
            AppUserService appUserService = new AppUserService();
            appUserService.register(customer);
        } catch (SQLException | UserAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

    public void placeOrder() {
        try{
            List<Event> events = publicService.viewAllEvents();
            int size = events.size();
            Random r = new Random();
            int index = r.nextInt(size);
            Event e = events.get(index);
            e = publicService.viewEventDetail(e);
            List<SubOrder> subOrders = new LinkedList<>();
            for(Section section : e.loadSections()){
                SubOrder subOrder = new SubOrder(section, 4, money);
                subOrders.add(subOrder);
            }
            Order order = new Order(e, customer, subOrders);
            customerService.placeOrder(order);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (TicketInsufficientException e) {
            System.out.println(customer.getFirstName() + " Failed to buy tickets. The tickets is insufficient");
        }
    }

    @Override
    public void run(){
        for(int i = 0; i < 50; i++){
            placeOrder();
        }
    }
}
