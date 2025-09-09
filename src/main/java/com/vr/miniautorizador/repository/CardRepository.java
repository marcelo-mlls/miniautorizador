package com.vr.miniautorizador.repository;

import com.vr.miniautorizador.entity.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByNumeroCartao(String numeroCartao);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Card c WHERE c.numeroCartao = :numeroCartao")
    Optional<Card> findByNumeroCartaoWithLock(@Param("numeroCartao") String numeroCartao);
}

