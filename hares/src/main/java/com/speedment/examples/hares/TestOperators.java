package com.speedment.examples.hares;

import com.company.speedment.orm.test.hare.HareApplication;
import com.company.speedment.orm.test.hare.db0.hares.hare.Hare;
import com.company.speedment.orm.test.hare.db0.hares.hare.HareField;

/**
 *
 * @author pemi
 */
public class TestOperators {
    
    public static void main(String[] args) {
        new HareApplication().start();
        
        Hare.stream().filter(HareField.NAME.contains("er")).forEach(System.out::println);
        Hare.stream().filter(HareField.NAME.contains("eR")).forEach(System.out::println);
        
        Hare.stream().filter(HareField.NAME.endsWith("rra")).forEach(System.out::println);
        
        Hare.stream().filter(HareField.NAME.equalIgnoreCase("PerRA")).forEach(System.out::println); // Fel
        
        Hare.stream().filter(HareField.NAME.notEqualIgnoreCase("PerRA")).forEach(System.out::println);
        
        Hare.stream().filter(HareField.NAME.startsWith("H")).forEach(System.out::println);
        
    }
    
}
