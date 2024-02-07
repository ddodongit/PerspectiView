package com.example.backend.modules.productrelation;

import com.example.backend.modules.character.Person;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRelationRequestDto {
    private Long productRelationId;
    private Person fromPerson;
    private Person toPerson;
    private String productRelationInfo;
    private String sourceHandle;
    private String targetHandle;


    public ProductRelation from(ProductRelationRequestDto productRelationRequestDto) {
        return ProductRelation.builder()
                .sourceHandle(productRelationRequestDto.getSourceHandle())
                .targetHandle(productRelationRequestDto.getTargetHandle())
                .id(productRelationRequestDto.getProductRelationId())
                .productRelationInfo(productRelationRequestDto.getProductRelationInfo())
                .fromPerson(productRelationRequestDto.getFromPerson())
                .toPerson(productRelationRequestDto.getToPerson())
                .build();
    }

}
