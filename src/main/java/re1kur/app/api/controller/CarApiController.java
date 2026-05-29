package re1kur.app.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import re1kur.app.api.ApiMapper;
import re1kur.app.api.dto.CarResponse;
import re1kur.app.api.dto.CarTypeResponse;
import re1kur.app.api.dto.EngineResponse;
import re1kur.app.core.other.CarFilter;
import re1kur.app.entity.CarType;
import re1kur.app.entity.Engine;
import re1kur.app.service.CarService;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Cars", description = "Cars, their types and engines")
@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarApiController {

    private static final int MAX = 1000;

    private final CarService carService;
    private final ApiMapper mapper;

    @Operation(summary = "List all available cars, optionally filtered")
    @GetMapping
    public List<CarResponse> cars(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String engine,
            @RequestParam(required = false) Integer makeId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String model
    ) {
        CarFilter filter = CarFilter.builder()
                .carType(type != null ? parseType(type) : null)
                .engine(engine != null ? parseEngine(engine) : null)
                .makeId(makeId)
                .year(year)
                .model(model)
                .build();
        return list(filter);
    }

    // ----- car types -----

    @Operation(summary = "List all car types (body types)")
    @GetMapping("/types")
    public List<CarTypeResponse> types() {
        return Arrays.stream(CarType.values()).map(mapper::carType).toList();
    }

    // ----- engines -----

    @Operation(summary = "List all engine types")
    @GetMapping("/engines")
    public List<EngineResponse> engines() {
        return Arrays.stream(Engine.values()).map(mapper::engine).toList();
    }

    // (cars filtered by type/engine are served by GET /cars?type=..&engine=..)

    // ----- single car (kept after literal sub-paths so /types and /engines win) -----

    @Operation(summary = "Get a single car by id")
    @GetMapping("/{id}")
    public CarResponse car(@PathVariable Integer id) {
        return mapper.carDetail(carService.readFull(id, null));
    }

    private List<CarResponse> list(CarFilter filter) {
        return carService.readAll(filter, PageRequest.of(0, MAX), null)
                .content().stream()
                .map(mapper::carSummary)
                .toList();
    }

    private CarType parseType(String value) {
        try {
            return CarType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car type [%s] not found.".formatted(value));
        }
    }

    private Engine parseEngine(String value) {
        try {
            return Engine.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Engine [%s] not found.".formatted(value));
        }
    }
}
