package re1kur.app.controller.car;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import re1kur.app.core.dto.CarDto;
import re1kur.app.core.dto.PageDto;
import re1kur.app.core.payload.CarPayload;
import re1kur.app.core.other.CarFilter;
import re1kur.app.service.CarService;
import re1kur.app.service.CarTypeService;
import re1kur.app.service.EngineService;
import re1kur.app.service.MakeService;

@Slf4j
@Controller
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarsController {
    private final CarService carService;
    private final MakeService makeService;
    private final CarTypeService carTypeService;
    private final EngineService engineService;

    @Value("${custom.pagination.size}")
    private Integer pageSize;

    @GetMapping
    public String redirect() {
        return "redirect:/cars/list";
    }

    @GetMapping("/")
    public String redirectSlash() {
        return "redirect:/cars/list";
    }

    @GetMapping("/list")
    public String listCars(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @ModelAttribute(name = "filter") CarFilter filter
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        PageDto<CarDto> pageDto = carService.readAll(filter, pageable);

        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("page", pageDto);
        model.addAttribute("filter", filter);

        return "cars/list.html";
    }

    @GetMapping("/create")
    public String getCreateCar(Model model) {
        model.addAttribute("makes", makeService.readAll());
        model.addAttribute("engines", engineService.readAll());
        model.addAttribute("carTypes", carTypeService.readAll());
        model.addAttribute("payload", CarPayload.builder().build());
        return "cars/create.html";
    }

    @PostMapping("/create")
    public String createCar(
            @Valid @ModelAttribute CarPayload payload,
            @RequestParam(value = "title", required = false) MultipartFile title,
            @RequestParam(value = "file", required = false) MultipartFile[] files
    ) {
        Integer id = carService.create(payload, title, files);
        return "redirect:/cars/%d" .formatted(id);
    }
}
