package com.crispy.crispyapi.service;

import com.crispy.crispyapi.dto.CardDto;
import com.crispy.crispyapi.dto.CreateCardRequest;
import com.crispy.crispyapi.model.Card;
import com.crispy.crispyapi.repository.CardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CardService implements ServiceInterface<Card> {
    private final CardRepository cardRepository;
    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public boolean create(Card card) {
        cardRepository.save(card);
        return true;
    }

    public boolean create(Card card, CreateCardRequest request) {
        card.setName(request.getName());
        card.setDescription(request.getDescription());
        card.setCreationTime(request.getCreationTime());
        card.setDeadLineTime(request.getDeadLineTime());
        card.setStoryPoints(request.getStoryPoints());
        cardRepository.save(card);
        return true;
    }

    @Override
    public Card read(Long id) throws Exception {
        return cardRepository.findCardById(id).orElseThrow(() -> new Exception("Card not found"));
    }

    @Override
    public boolean update(Card card) {
        try {
            cardRepository.save(card);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws Exception {
        cardRepository.deleteCardById(id).orElseThrow(() -> new Exception("Card not found"));
        return true;
    }

    public CardDto convertToDto(Card card) {
        return new CardDto(card.getId(), card.getName(), card.getDescription(), card.getUser().getName(), card.getUser().getId(), card.getCreationTime(), card.getDeadLineTime(), card.getStoryPoints());
    }
}
