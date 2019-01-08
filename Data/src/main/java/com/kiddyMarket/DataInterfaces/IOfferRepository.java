package com.kiddyMarket.DataInterfaces;

import com.kiddyMarket.Entities.Offer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IOfferRepository extends CrudRepository<Offer, Integer> {
    Iterable<Offer> findTop10ByOrderByOfferId();
    void deleteOfferBySenderIdAndSold(int id, boolean sold);
}
