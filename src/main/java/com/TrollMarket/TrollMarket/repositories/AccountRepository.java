package com.TrollMarket.TrollMarket.repositories;

import com.TrollMarket.TrollMarket.models.Account;
import com.TrollMarket.TrollMarket.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,String> {


}
