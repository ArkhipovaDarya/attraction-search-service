package ssau.graduatework.attractionsearchservice.review.dto;

import lombok.Getter;

@Getter
public class ReviewDto {
    private final Long id;
    private final Integer rate;
    private final String review;

    public ReviewDto(Long id, Integer rate, String review) {
        this.id = id;
        this.rate = rate;
        this.review = review;
    }
}
