package tests;


import java.util.ArrayList;
import java.util.List;

import com.ftn.sbnz.kjar.util.KnowledgeSessionHelper;
import com.ftn.sbnz.model.examples_1.Item;
import com.ftn.sbnz.model.examples_1.Order;
import com.ftn.sbnz.model.examples_1.OrderLine;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccumulateTest {

    protected final String ksessionName = "k-session-5";

    @Test
    public void testInsertModifyAndDelete() {
        // Ovde se testira primer kako radi accumulated metoda u drolsu.
        // Za order_1 se ne zadovoljava test zato sto narudzbine ciji je salePrice atribut veci od 20 manji od 10, pogledati rule <accumulate-rules.drl>
        // Za order_2 prolazi

    	KieContainer kc = KnowledgeSessionHelper.createRuleBase();
        KieSession ksession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kc, ksessionName);
        
        //first order will have 29 items, but only 9 items with sale price over 20
        Order order1 = createCheapItemsOrder();
        // Pretpostavka: order1 je vaša instanca narudžbe (Order)
        assertTrue(order1.getTotalItems() == 29);
        //second order will have 10 items, all with sale price over 20
        Order order2 = createExpensiveItemsOrder();
        assertTrue(order2.getTotalItems() == (10));
        ksession.insert(order1);
        ksession.insert(order2);
        //so the rule should fire for only one of the orders
        int firedRules = ksession.fireAllRules();


        assertTrue(1 == firedRules);

        MatcherAssert.assertThat(order1.getDiscount(), nullValue());
        MatcherAssert.assertThat(order2.getDiscount(), notNullValue());
        MatcherAssert.assertThat(order2.getDiscount().getPercentage(), notNullValue());
        assertTrue(order2.getDiscount().getPercentage() == 0.05);
    }
    
    private Order createExpensiveItemsOrder() {
        Order order = new Order();
        OrderLine line = new OrderLine();
        line.setItem(new Item("nice phone case", 10.00, 21.00));
        line.setQuantity(10);
        List<OrderLine> orderLines = new ArrayList<OrderLine>();
        orderLines.add(line);
        order.setOrderLines(orderLines);
        return order;
    }

    private Order createCheapItemsOrder() {
        Order order = new Order();
        OrderLine line1 = new OrderLine();
        line1.setItem(new Item("pencil", 0.50, 0.80));
        line1.setQuantity(20);

        OrderLine line2 = new OrderLine();
        line2.setItem(new Item("nice phone case", 10.00, 21.00));
        line2.setQuantity(9);

        List<OrderLine> orderLines = new ArrayList<OrderLine>();
        orderLines.add(line1);
        orderLines.add(line2);
        order.setOrderLines(orderLines);
        return order;
    }

}
