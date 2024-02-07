package com.example.backend.modules.productrelation;

import com.example.backend.modules.character.Person;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRelationResponseDto {
    private Long productRelationId;
    private Person fromPerson;
    private Person toPerson;
    private String productRelationInfo;
    private String sourceHandle;
    private String targetHandle;

    public static ProductRelationResponseDto of(ProductRelation productRelation) {
        return ProductRelationResponseDto.builder()
                .productRelationId(productRelation.getId())
                .productRelationInfo(productRelation.getProductRelationInfo())
                .fromPerson(productRelation.getFromPerson())
                .toPerson(productRelation.getToPerson())
                .build();
    }
}
