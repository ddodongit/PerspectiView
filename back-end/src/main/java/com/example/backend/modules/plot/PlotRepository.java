package com.example.backend.modules.plot;

import com.example.backend.modules.product.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlotRepository extends JpaRepository<Plot, Long> {
    @EntityGraph(attributePaths = {"stories"})
    List<Plot> findWithStoryByProduct(Product product);

    List<Plot> findByProduct(Product product);

    @EntityGraph(attributePaths = {"stories"})
    Optional<Plot> findWithStoryById(Long id);

    @EntityGraph(attributePaths = {"stories.content"})
    Optional<Plot> findWithStoryAndContentById(Long id);
}
