package com.flux.fluxDomainMarket.repository;

import com.flux.fluxDomainMarket.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {
}
