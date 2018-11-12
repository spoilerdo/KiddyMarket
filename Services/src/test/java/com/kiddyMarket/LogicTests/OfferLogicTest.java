package com.kiddyMarket.LogicTests;

import com.kiddyMarket.DataInterfaces.IOfferRepository;
import com.kiddyMarket.Entities.Offer;
import com.kiddyMarket.Logic.OfferLogic;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OfferLogicTest {

    //Add exception dependency
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IOfferRepository offerRepository;

    //The logic you want to test injected with the repo mocks
    @InjectMocks
    private OfferLogic _logic;

    @Before //sets up the mock
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOfferValid(){
        Offer dummyOffer = new Offer(1, 1, 10f);

        when(offerRepository.save(dummyOffer)).thenReturn(dummyOffer);

        _logic.createOffer(dummyOffer);

        verify(offerRepository, times(1)).save(dummyOffer);
    }

    @Test
    public void testCreateOfferUnvalid(){
        Offer dummyOffer = new Offer(1, 0, 0f);

        exception.expect(IllegalArgumentException.class);

        _logic.createOffer(dummyOffer);
    }

    @Test
    public void testDeleteOfferValid(){
        Offer dummyOffer = new Offer(1, 1, 10f);

        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.of(dummyOffer));

        _logic.deleteOffer(dummyOffer.getOfferId());

        verify(offerRepository, times(1)).delete(dummyOffer);
    }

    @Test
    public void testDeleteOfferUnvalid(){
        Offer dummyOffer = new Offer(1, 1, 10f);

        when(offerRepository.findById(dummyOffer.getOfferId())).thenReturn(Optional.empty());

        exception.expect(IllegalArgumentException.class);

        _logic.deleteOffer(dummyOffer.getOfferId());
    }


    //TODO finish this!!
    /*@Test
    public void testUpdateOfferValid(){
        Offer dummyOldOffer = new Offer(1, 1, 10f);
        Offer dummyNewOffer = new Offer(1, 1, 15f);

        when(offerRepository.findById())
    }*/
}