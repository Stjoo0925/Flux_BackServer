package com.flux.bid.controller;

import com.flux.bid.model.Bid;
import com.flux.bid.model.BidDTO;
import com.flux.bid.model.BuyNowRequest;
import com.flux.bid.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bids")
@CrossOrigin(origins = "http://localhost:8000") // 프론트엔드 서버 주소
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    // 입찰하기
    @PostMapping("/register")
    public ResponseEntity<BidDTO> registerBid(@RequestBody BidDTO bidDTO) {
        try {
            // BidDTO를 통해 입찰 등록 로직을 처리
            Bid bid = bidService.registerBid(
                    bidDTO.getMarketId(),
                    bidDTO.getUserId(),
                    bidDTO.getBidAmount(),
                    bidDTO.getBidTime()
            );

            // Bid 엔티티를 BidDTO로 변환하여 반환합니다.
            BidDTO responseDTO = new BidDTO(
                    bid.getMarket().getMarketId(),
                    bid.getUser().getUserId(),
                    bid.getBidAmount(),
                    bid.getBidTime(),
                    bid.getStatus(),
                    bid.isSold()
            );

            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // 현재 최고 입찰 가격 가져오기
    @GetMapping("/market/{marketId}/current-bid")
    public ResponseEntity<Integer> getCurrentHighestBid(@PathVariable("marketId") Integer marketId) {
        Integer highestBidAmount = bidService.getHighestBidAmount(marketId);
        return ResponseEntity.ok(highestBidAmount);
    }


    // 즉시구매하기
    @PostMapping("/buy-now")
    public ResponseEntity<String> buyNow(@RequestBody BuyNowRequest buyNowRequest) {
        try {
            bidService.buyNow(buyNowRequest.getMarketId(), buyNowRequest.getUserId());
            return new ResponseEntity<>("상품을 성공적으로 구매하였습니다", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}