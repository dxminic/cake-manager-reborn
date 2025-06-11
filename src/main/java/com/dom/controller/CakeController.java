package com.dom.controller;

import com.dom.model.CakeEntity;
import com.dom.repository.CakeRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import java.util.List;
import java.util.Map;

@Controller("/cakes")
public class CakeController {

    private final CakeRepository cakeRepository;

    public CakeController(CakeRepository cakeRepository) {
        this.cakeRepository = cakeRepository;
    }

    @Get("/")
    public HttpResponse<List<CakeEntity>> getAllCakes() {
        return HttpResponse.ok(cakeRepository.findAll());
    }

    @Post("/")
    public HttpResponse<CakeEntity> createCake(@Body CakeEntity cake) {
        return HttpResponse.ok(cakeRepository.save(cake));
    }

    @Put("/{id}")
    public HttpResponse<CakeEntity> updateCake(@PathVariable Long id, @Body CakeEntity updatedCake) {
        return cakeRepository.findById(id)
                .map(existingCake -> {
                    existingCake.setTitle(updatedCake.getTitle());
                    existingCake.setDesc(updatedCake.getDesc());
                    existingCake.setImage(updatedCake.getImage());
                    CakeEntity saved = cakeRepository.update(existingCake);
                    return HttpResponse.ok(saved);
                })
                .orElse(HttpResponse.notFound());
    }

    @Delete("/{id}")
    public HttpResponse<?> deleteCake(@PathVariable Long id) {
        cakeRepository.deleteById(id);
        return HttpResponse.ok(Map.of("message", "Cake deleted successfully", "id", id.toString()));
    }
}
