package titan.common.mybatis.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import titan.lightbatis.annotations.Lightbatis;
import titan.lightbatis.web.TitanDalApplication;
import titan.lightbatis.web.entity.propertyset.IPropertySet;
import titan.lightbatis.web.service.PropertySetManager;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TitanDalApplication.class})

public class PropertySetTest {
    public static final String FirstLimitKey = "exchange.first.limit.amount";

    @Test
    public void testPropertySet() {
        System.out.println("===== test property set");
        IPropertySet propertySet = PropertySetManager.getInstance().getPropertySetByGlobal(1L);
        System.out.println("property set =" + propertySet);
        Double amount = 110D;
        propertySet.setDouble(FirstLimitKey, amount);
    }
}
