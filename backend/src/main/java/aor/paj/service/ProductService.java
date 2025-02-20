package aor.paj.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aor.paj.dto.ProductDto;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class ProductService {
    private final String filename = "../database/products.json";
    private Map<String, ProductDto> productMap;

    @PostConstruct
    public void init() {
        loadProductsFromFile();
    }

    public void loadProductsFromFile() {
        File file = new File(filename);
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file)) {
                Jsonb jsonb = JsonbBuilder.create();
                Type productListType = new ArrayList<ProductDto>() {
                }.getClass().getGenericSuperclass();
                List<ProductDto> products = jsonb.fromJson(fileReader,
                        productListType);
                productMap = new HashMap<>();
                for (ProductDto product : products) {
                    productMap.put(product.getId(), product);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            productMap = new HashMap<>();
        }
    }

    public ProductDto getProductById(String id) {
        return productMap.get(id);
    }

    public void addProduct(ProductDto product) {
        productMap.put(product.getId(), product);
        saveProductsToFile();
    }

    public void updateProduct(ProductDto productDto) {
        if (productMap.containsKey(productDto.getId())) {
            ProductDto existingProduct = productMap.get(productDto.getId());
            existingProduct.setTitulo(productDto.getTitulo());
            existingProduct.setCategoria(productDto.getCategoria());
            existingProduct.setPreco(productDto.getPreco());
            existingProduct.setImagem(productDto.getImagem());
            existingProduct.setLocal(productDto.getLocal());
            existingProduct.setDescricao(productDto.getDescricao());
            existingProduct.setDataDePublicacao(productDto.getDataDePublicacao());
            existingProduct.setUserAutor(productDto.getUserAutor());
            existingProduct.setAvaliacoes(productDto.getAvaliacoes());
            existingProduct.setEstado(productDto.getEstado());
            saveProductsToFile();
        } else {
            throw new IllegalArgumentException("Product not found with id: " + productDto.getId());
        }

        /*
         * productMap.put(product.getId(), product);
         * saveProductsToFile();
         */

    }

    public void deleteProduct(String id) {
        productMap.remove(id);
        saveProductsToFile();
    }

    public List<ProductDto> getAllProducts() {
        return new ArrayList<>(productMap.values());
    }

    private void saveProductsToFile() {
        File file = new File(filename);
        try (FileWriter fileWriter = new FileWriter(file)) {
            List<ProductDto> products = new ArrayList<>(productMap.values());
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(products, fileWriter);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
