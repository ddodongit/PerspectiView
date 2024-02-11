package com.example.backend.modules.productrelation;

import com.example.backend.modules.character.Character;
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
    private Character fromCharacter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_character_id")
    private Character toCharacter;

    @Column(nullable = false)
    private String productRelationInfo;

    @Column(nullable = true)
    private Long sourceId;

    @Column(nullable = true)
    private Long tagetId;

    @Column(nullable = true)
    private String sourceHandle;

    @Column(nullable = true)
    private String targetHandle;

    @Builder
    public ProductRelation(Long id, Product product, Character fromCharacter, Character toCharacter,
                           String productRelationInfo, Long sourceId, Long tagetId, String sourceHandle, String targetHandle) {
        this.id = id;
        this.product = product;
        this.fromCharacter = fromCharacter;
        this.toCharacter = toCharacter;
        this.productRelationInfo = productRelationInfo;
        this.sourceId = sourceId;
        this.tagetId = tagetId;
        this.sourceHandle = sourceHandle;
        this.targetHandle = targetHandle;
    }

    public void updateProductRelation(ProductRelation productRelation) {
        this.fromCharacter = productRelation.fromCharacter;
        this.toCharacter = productRelation.toCharacter;
        this.productRelationInfo = productRelation.getProductRelationInfo();
        this.sourceId = productRelation.getSourceId();
        this.tagetId = productRelation.getTagetId();
        this.sourceHandle = productRelation.sourceHandle;
        this.targetHandle = productRelation.targetHandle;
    }

    public void updateProduct(Product product) {
        this.product = product;
    }
}
