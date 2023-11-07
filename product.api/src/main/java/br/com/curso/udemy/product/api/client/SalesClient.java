package br.com.curso.udemy.product.api.client;

import br.com.curso.udemy.product.api.dto.ProductSalesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
    name = "salesClient",
    contextId = "salesClient",
    url = "${app-config.services.sales}"
)
public interface SalesClient {

    @GetMapping("/api/orders/product/{productId}")
    Optional<ProductSalesResponse> findSalesByProductId(@PathVariable Integer productId);
}
