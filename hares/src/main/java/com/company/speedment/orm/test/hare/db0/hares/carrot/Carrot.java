package com.company.speedment.orm.test.hare.db0.hares.carrot;

import com.company.speedment.orm.test.hare.db0.hares.hare.Hare;
import com.company.speedment.orm.test.hare.db0.hares.hare.HareManager;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Generated;

/**
 * An interface representing an entity (for example, a row) in the Table 'hare.db0.hares.carrot'.
 * <p>
 * This Class or Interface has been automatically generated by Speedment.
 * Any changes made to this Class or Interface will be overwritten.
 * 
 * @author Speedment 
 */
@Generated("Speedment")
public interface Carrot {
    
    Integer getId();
    
    String getName();
    
    Integer getOwner();
    
    Integer getRival();
    
    default Hare findOwner() {
        return HareManager.get()
                .stream().filter(hare -> Objects.equals(this.getOwner(), hare.getId())).findAny().get();
    }
    
    default Optional<Hare> findRival() {
        return HareManager.get()
                .stream().filter(hare -> Objects.equals(this.getRival(), hare.getId())).findAny();
    }
    
    static CarrotBuilder builder() {
        return CarrotManager.get().builder();
    }
    
    default CarrotBuilder toBuilder() {
        return CarrotManager.get().toBuilder(this);
    }
    
    static Stream<Carrot> stream() {
        return CarrotManager.get().stream();
    }
    
    default Optional<Carrot> persist() {
        return CarrotManager.get().persist(this);
    }
    
    default Optional<Carrot> update() {
        return CarrotManager.get().update(this);
    }
    
    default Optional<Carrot> remove() {
        return CarrotManager.get().remove(this);
    }
}