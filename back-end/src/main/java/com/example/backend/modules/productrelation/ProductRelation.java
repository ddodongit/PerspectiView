package com.example.backend.modules.productrelation;

import com.example.backend.modules.character.Person;
import com.example.backend.modules.product.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_character_id")
    private Person fromPerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_character_id")
    private Person toPerson;

    @Column(nullable = false)
    private String productRelationInfo;

    @Column(nullable = true)
    private String sourceHandle;

    @Column(nullable = true)
    private String targetHandle;

    @Builder
    public ProductRelation(Long id, Product product, Person fromPerson, Person toPerson, String productRelationInfo, String sourceHandle, String targetHandle) {
        this.id = id;
        this.product = product;
        this.fromPerson = fromPerson;
        this.toPerson = toPerson;
        this.productRelationInfo = productRelationInfo;
        this.sourceHandle = sourceHandle;
        this.targetHandle = targetHandle;
    }

    public void updateProductRelation(ProductRelation productRelation) {
        this.fromPerson = productRelation.fromPerson;
        this.toPerson = productRelation.toPerson;
        this.productRelationInfo = productRelation.getProductRelationInfo();
        this.sourceHandle = productRelation.sourceHandle;
        this.targetHandle = productRelation.targetHandle;
    }
}
