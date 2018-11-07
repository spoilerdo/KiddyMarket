package com.kiddyMarket.DataInterfaces;

import com.kiddyMarket.Entities.Offer;
import org.springframework.data.repository.CrudRepository;

public interface IOfferRepository extends CrudRepository<Offer, Integer> {
}
