package prax2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import prax2.PaymentPlan;
import prax2.orderdao.OrderDao;
import prax2.orderpojo.Installment;
import prax2.orderpojo.Order;
import prax2.orderpojo.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;


@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringController {

    @Autowired
    private OrderDao dao;

    private static final String URL_ORDER_END = "api/";

    @PostMapping(URL_ORDER_END + "orders")
    public Order addOrderToDataBase(@RequestBody @Valid Order o) {
        return dao.saveOrderAndReturnOrder(o);
    }

    @GetMapping(URL_ORDER_END + "orders/{id}")
    public Order getOrder(@PathVariable Long id) {
        return dao.getOrder(id);
    }

    @GetMapping(URL_ORDER_END + "orders")
    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }

    @DeleteMapping(URL_ORDER_END + "orders/{id}")
    public void deleteOrder(@PathVariable Long id) {
        dao.deleteOrder(id);
    }

    @PostMapping(URL_ORDER_END + "parse")
    public List<AbstractMap.SimpleEntry<String, String>> jsonReverse(
            @RequestBody List<AbstractMap.SimpleEntry<String, String>> val) {
        for (int i = 0; i < val.size(); i++) {
            String key = new StringBuilder(val.get(i).getKey()).reverse().toString();
            String value = new StringBuilder(val.get(i).getValue()).reverse().toString();
            val.set(i, new AbstractMap.SimpleEntry<>(key, value));
        }
        return val;
    }

    @PostMapping("orders/form")
    public Order saveOrderForm(@ModelAttribute @Valid Order o) {
        return dao.saveOrderAndReturnOrder(o);
    }

    @GetMapping(URL_ORDER_END + "orders/{id}/installments")
    public List<Installment> makeInstalmentPlan(@PathVariable Long id
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start
            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        Order o = dao.getOrder(id);
        @Valid PaymentPlan plan = new PaymentPlan(o, start, end);
        List<Installment> installments = new ArrayList<>();
        while (plan.next()) {
            // System.out.println(plan.getDate());
            installments.add(new Installment(plan.getPayment(), plan.getDate()));
        }
        return installments;
    }
    @GetMapping(URL_ORDER_END + "users")
    public List<User> getUserList() {
        return dao.getAllUsers();
    }

    @GetMapping(URL_ORDER_END + "users/{username}")
    @PreAuthorize("#username == authentication.name or hasRole('ROLE_ADMIN')")
    public User getUser(@PathVariable String username) {
      return dao.getUser(username);
    }


    @GetMapping(URL_ORDER_END + "version")
    public String getVersion() {
        return "1.0";
    }
    /*
    public static void main(String[] args) {
        Validator v  = Validation.buildDefaultValidatorFactory().getValidator();
        var errors = v.validate(new Order(1L, "Li", List.of(new OrderRow(-1L
                ,"s",0,0))));
        for (var e: errors) {
            System.out.println(e.getMessage());
        }
        System.out.println(errors.size());
    }
*/
}
