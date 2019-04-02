package valoon.dto;

import valoon.exceptions.BadRequestException;

public class CreateRateDto {

    private String comment;
    private Integer score;
    private Long orderId;

    public CreateRateDto() { }

    public String getComment() {
        return comment;
    }

    public Integer getScore() {
        return score;
    }

    public Long getOrderId() {
        return orderId;
    }
}
