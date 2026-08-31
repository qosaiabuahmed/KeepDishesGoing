package be.kdg.prog6.order.adapter.out;

import be.kdg.prog6.common.domain.BasketId;
import be.kdg.prog6.common.domain.CustomerId;
import be.kdg.prog6.order.domain.Basket;
import be.kdg.prog6.order.port.out.LoadBasketPort;
import be.kdg.prog6.order.port.out.SaveBasketPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BasketJpaAdapter implements LoadBasketPort, SaveBasketPort {

    private final BasketJpaRepository repository;

    public BasketJpaAdapter(BasketJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Basket> loadById(BasketId basketId) {
        return repository.findById(basketId.value())
                .map(BasketMapper::toDomain);
    }

    @Override
    public Optional<Basket> loadByCustomerId(CustomerId customerId) {
        return repository.findByCustomerId(customerId.value())
                .map(BasketMapper::toDomain);
    }

    @Override
    public Basket save(Basket basket) {
        BasketJpaEntity jpaEntity = BasketMapper.toJpaEntity(basket);
        repository.save(jpaEntity);
        return basket;
    }
}