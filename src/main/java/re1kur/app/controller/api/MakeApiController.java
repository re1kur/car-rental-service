package re1kur.app.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import re1kur.app.mapper.ApiMapper;
import re1kur.app.dto.response.MakeResponse;
import re1kur.app.service.make.MakeService;

import java.util.List;

@Tag(name = "Makes", description = "Car makes (brands)")
@RestController
@RequestMapping("/api/v1/makes")
@RequiredArgsConstructor
public class MakeApiController {

    private final MakeService makeService;
    private final ApiMapper mapper;

    @Operation(summary = "List all makes")
    @GetMapping
    public List<MakeResponse> makes() {
        return makeService.readAll().stream().map(mapper::makeSummary).toList();
    }

    @Operation(summary = "Get a single make by id")
    @GetMapping("/{id}")
    public MakeResponse make(@PathVariable Integer id) {
        return mapper.makeDetail(makeService.read(id, null));
    }

    // (cars of a make are served by GET /cars?makeId=..)
}
