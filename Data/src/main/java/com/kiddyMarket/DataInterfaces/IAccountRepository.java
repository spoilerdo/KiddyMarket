package com.kiddyMarket.DataInterfaces;

import com.kiddyMarket.Entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
}
