package com.kiddyMarket.DataInterfaces;

import com.kiddyMarket.Entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
}
